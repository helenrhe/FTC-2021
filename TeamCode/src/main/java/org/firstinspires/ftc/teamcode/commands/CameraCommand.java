package org.firstinspires.ftc.teamcode.commands;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.*;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.system.ContinuationSynchronizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.structure.SubCommand;
import org.firstinspires.ftc.teamcode.vision.GripPipeline;
import org.firstinspires.ftc.teamcode.vision.TeamObjectPipeline;
import org.opencv.android.Utils;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;

import java.util.concurrent.TimeUnit;

public class CameraCommand implements SubCommand {

    private CameraManager cameraManager;
    private WebcamName cameraName;
    private Camera camera;
    private CameraCaptureSession cameraCaptureSession;

    private long updateTime;

    private Handler callbackHandler;

    private volatile Bitmap currentFrame;
    private OpMode opMode;
    private GripPipeline pipeline;

    @Override
    public void onInit(OpMode opMode) {
        this.opMode = opMode;
        callbackHandler = CallbackLooper.getDefault().getHandler();

        cameraManager = ClassFactory.getInstance().getCameraManager();
        cameraName = opMode.hardwareMap.get(WebcamName.class, "Webcam 1");

        camera = cameraManager.requestPermissionAndOpenCamera(new Deadline(30, TimeUnit.SECONDS), cameraName, null);
        if(camera == null) {
            throw new IllegalStateException("Camera permission not granted!");
        }

        openCamera();
        startCamera();
        opMode.telemetry.update();

        Log.i("cam", "Initalizing Pipeline");

        pipeline = new GripPipeline();
        Log.i("cam", "Initalized Pipeline");
    }

    @Override
    public void onExecute(OpMode opMode) {
        if(cameraCaptureSession == null) {
            return;
        }
        opMode.telemetry.addData("camera", "updated at " + updateTime);

        if(currentFrame == null) return;

        Mat frame = new Mat();
        Bitmap currentFrame32 = currentFrame.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(currentFrame32, frame);
        KeyPoint[] points = pipeline.process(frame, true);
        String pointData = "";
        for(int i = 0 ; i < points.length; i++) {
            pointData += "\nPt: " + points[i].pt + "Size: " + points[i].size;
        }
        opMode.telemetry.addData("points", pointData);
    }

    @Override
    public void onEnd(OpMode opMode) {
        stopCamera();
        closeCamera();
    }

    private void openCamera() {
        if (camera != null) return; // be idempotent

        Deadline deadline = new Deadline(10000, TimeUnit.SECONDS);
        camera = cameraManager.requestPermissionAndOpenCamera(deadline, cameraName, null);
        if (camera == null) {
            opMode.telemetry.addData("error: ", "camera not found or permission to use not granted: + cameraName");
        }

        opMode.telemetry.addData("camera", "open");
    }

    private void startCamera() {

        /** YUY2 is supported by all Webcams, per the USB Webcam standard: See "USB Device Class Definition
         * for Video Devices: Uncompressed Payload, Table 2-1". Further, often this is the *only*
         * image format supported by a camera */
        final int imageFormat = ImageFormat.YUY2;

        /** Verify that the image is supported, and fetch size and desired frame rate if so */
        CameraCharacteristics cameraCharacteristics = cameraName.getCameraCharacteristics();
        final Size size = cameraCharacteristics.getDefaultSize(imageFormat);
        final int fps = cameraCharacteristics.getMaxFramesPerSecond(imageFormat, size);

        /** Some of the logic below runs asynchronously on other threads. Use of the synchronizer
         * here allows us to wait in this method until all that asynchrony completes before returning. */
        final ContinuationSynchronizer<CameraCaptureSession> synchronizer = new ContinuationSynchronizer<>();
        try {
            /** Create a session in which requests to capture frames can be made */
            camera.createCaptureSession(Continuation.create(callbackHandler, new CameraCaptureSession.StateCallbackDefault() {
                @Override public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        /** The session is ready to go. Start requesting frames */
                        final CameraCaptureRequest captureRequest = camera.createCaptureRequest(imageFormat, size, fps);
                        session.startCapture(captureRequest,
                                new CameraCaptureSession.CaptureCallback() {
                                    @Override public void onNewFrame(@NonNull CameraCaptureSession session, @NonNull CameraCaptureRequest request, @NonNull CameraFrame cameraFrame) {
                                        /** A new frame is available. The frame data has <em>not</em> been copied for us, and we can only access it
                                         * for the duration of the callback. So we copy here manually. */
                                        Bitmap bmp = captureRequest.createEmptyBitmap();
                                        cameraFrame.copyToBitmap(bmp);

                                        currentFrame = bmp;

                                        updateTime = System.currentTimeMillis();
                                    }
                                },
                                Continuation.create(callbackHandler, new CameraCaptureSession.StatusCallback() {
                                    @Override public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, CameraCaptureSequenceId cameraCaptureSequenceId, long lastFrameNumber) {
                                    }
                                })
                        );
                        synchronizer.finish(session);
                    } catch (CameraException|RuntimeException e) {
                        opMode.telemetry.addData("camera", "failed to start camera session.");
                        session.close();
                        synchronizer.finish(null);
                    }
                }
            }));
        } catch (CameraException|RuntimeException e) {
            opMode.telemetry.addData("camera", "failed to open camera");
            synchronizer.finish(null);
        }

        /** Wait for all the asynchrony to complete */
        try {
            synchronizer.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /** Retrieve the created session. This will be null on error. */
        cameraCaptureSession = synchronizer.getValue();
        opMode.telemetry.addData("camera", "started");
    }

    private void stopCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.stopCapture();
            cameraCaptureSession.close();
            cameraCaptureSession = null;
            opMode.telemetry.addData("camera", "stopped");
        }
    }

    private void closeCamera() {
        stopCamera();
        if (camera != null) {
            camera.close();
            camera = null;
            opMode.telemetry.addData("camera", "closed");
        }
    }
}
