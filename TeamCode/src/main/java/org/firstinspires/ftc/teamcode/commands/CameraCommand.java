package org.firstinspires.ftc.teamcode.commands;

import android.hardware.camera2.CameraCaptureSession;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.structure.SubCommand;

import java.util.concurrent.TimeUnit;

public class CameraCommand implements SubCommand {

    private CameraManager manager;
    private WebcamName cameraName;
    private Camera camera;
    private CameraCaptureSession cameraSession;

    @Override
    public void onInit(OpMode opMode) {
        manager = ClassFactory.getInstance().getCameraManager();
        cameraName = opMode.hardwareMap.get(WebcamName.class, "Webcam 1");

        camera = manager.requestPermissionAndOpenCamera(new Deadline(30, TimeUnit.SECONDS), cameraName, null);
        if(camera == null) {
            throw new IllegalStateException("Camera permission not granted!");
        }

    }

    @Override
    public void onExecute(OpMode opMode) {

    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
