package org.firstinspires.ftc.teamcode.commands;

import android.content.res.Resources;
import android.util.Log;
import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.structure.SubCommand;
import org.firstinspires.ftc.teamcode.subsystems.CameraSubsystem;
import org.firstinspires.ftc.teamcode.vision.GripPipeline;
import org.opencv.core.Point;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.ArrayList;

public class SimpleCameraCommand implements SubCommand {

    private Point out = null;

    CameraSubsystem subsystem;

    @Override
    public void onInit(OpMode opMode) {
        subsystem = new CameraSubsystem(opMode.hardwareMap);

        Resources res = opMode.hardwareMap.appContext.getResources();

        int cameraMonitorViewId = res.getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        WebcamName webcamName = opMode.hardwareMap.get(WebcamName.class, "Webcam 1");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
                camera.startStreaming(160, 120);
                FtcDashboard.getInstance().startCameraStream(camera, 0);
            }

            @Override
            public void onError(int errorCode) {
                opMode.telemetry.addData("camera_status", "failed @" + errorCode);
            }
        });
    }

    @Override
    public void onExecute(OpMode opMode) {
    }

    @Override
    public void onEnd(OpMode opMode) {
    }
}
