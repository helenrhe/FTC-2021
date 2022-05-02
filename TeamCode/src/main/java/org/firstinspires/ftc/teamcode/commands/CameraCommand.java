package org.firstinspires.ftc.teamcode.commands;

import android.content.res.Resources;
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
import org.firstinspires.ftc.teamcode.subsystems.CameraSubsystem;
import org.firstinspires.ftc.teamcode.vision.GripPipeline;
import org.firstinspires.ftc.teamcode.vision.TeamObjectPipeline;
import org.opencv.android.Utils;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvInternalCamera2;
import org.openftc.easyopencv.PipelineRecordingParameters;

import java.util.concurrent.TimeUnit;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class CameraCommand implements SubCommand {


    private OpMode opMode;

    private Point out = null;

    CameraSubsystem subsystem;

    @Override
    public void onInit(OpMode opMode) {
        subsystem = new CameraSubsystem(opMode.hardwareMap);
        this.opMode = opMode;
        Log.i("cam", "\n\n\nopMode.hardwareMap.appContext: " + opMode.hardwareMap.appContext.getPackageName() + "\n\n\n" );

        Resources res = opMode.hardwareMap.appContext.getResources();

        int cameraMonitorViewId = res.getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        WebcamName webcamName = opMode.hardwareMap.get(WebcamName.class, "Webcam 1");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
                camera.startStreaming(640, 480);
                FtcDashboard.getInstance().startCameraStream(camera, 0);
                camera.setPipeline(new GripPipeline(new GripPipeline.KeyPointConsumer() {
                    @Override
                    public void onUpdatedData(Point point) {
                        subsystem.setPoint(point);
                        out = point;
                    }
                }));
            }

            @Override
            public void onError(int errorCode) {
                opMode.telemetry.addData("camera_status", "failed @" + errorCode);
            }
        });
    }

    @Override
    public void onExecute(OpMode opMode) {
        if(out == null) return;
        opMode.telemetry.addData("Point Rounded: ", "{" + Math.floor(out.x) + ", " + Math.floor(out.y) + "}");
    }

    @Override
    public void onEnd(OpMode opMode) {
    }

    public Point getPoint() {
        return out;
    }
}
