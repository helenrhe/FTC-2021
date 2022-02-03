package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.structure.SubCommand;
import org.firstinspires.ftc.teamcode.subsystems.CameraSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;

public class ClawCommand implements SubCommand {
    private ClawSubsystem clawSubsystem;
    private CameraSubsystem cameraSubsystem;
    @Override
    public void onInit(OpMode opMode) {
         clawSubsystem = new ClawSubsystem(opMode.hardwareMap);
         cameraSubsystem = new CameraSubsystem(opMode.hardwareMap);
    }

    @Override
    public void onExecute(OpMode opMode) {
        clawSubsystem.log(opMode.telemetry);
        if(opMode.gamepad2.a) {
            clawSubsystem.open();
        } else if(opMode.gamepad2.b) {
            clawSubsystem.close();
        }

        cameraSubsystem.setServoPosition((opMode.gamepad2.left_stick_y + 1) / 2, opMode.telemetry);
        opMode.telemetry.addData("camera_g", opMode.gamepad2.left_stick_y);
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
