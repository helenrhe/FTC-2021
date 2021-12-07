package org.firstinspires.ftc.teamcode.commands;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.*;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.structure.SubCommand;

import java.util.concurrent.TimeUnit;

public class CameraCommand implements SubCommand {

    private CameraManager manager;
    private WebcamName cameraName;
    private Camera camera;
    private CameraCaptureSession cameraSession;

    private Handler callbackHandler;

    @Override
    public void onInit(OpMode opMode) {
        callbackHandler = CallbackLooper.getDefault().getHandler();

        manager = ClassFactory.getInstance().getCameraManager();
        cameraName = opMode.hardwareMap.get(WebcamName.class, "Webcam 1");

        camera = manager.requestPermissionAndOpenCamera(new Deadline(30, TimeUnit.SECONDS), cameraName, null);
        if(camera == null) {
            throw new IllegalStateException("Camera permission not granted!");
        }

        int format = ImageFormat.YUY2;

        CameraCharacteristics cameraCharacteristics = cameraName.getCameraCharacteristics();

        Size size = cameraCharacteristics.getDefaultSize(format);
        int fps = cameraCharacteristics.getMaxFramesPerSecond(format, size);

        try {
            camera.createCaptureSession(Continuation.create(callbackHandler, new org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession.StateCallbackDefault() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        CameraCaptureRequest request = camera.createCaptureRequest(format, size, fps);
                        session.startCapture(request, (CameraCaptureSession.CaptureCallback) (cameraCaptureSession, cameraCaptureRequest, cameraFrame) -> {
                            Bitmap bmp = request.createEmptyBitmap();
                            cameraFrame.copyToBitmap(bmp);
                        }, Continuation.create(callbackHandler, (session1, cameraCaptureSequenceId, lastFrameNumber) -> {}));
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        } catch (CameraException e) {

        }
    }

    @Override
    public void onExecute(OpMode opMode) {

    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
