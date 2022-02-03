package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.structure.SubCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrainSubsystem;

public class DriveTrainCommand implements SubCommand {
    DriveTrainSubsystem driveTrain;

    @Override
    public void onInit(OpMode opMode) {
        driveTrain = new DriveTrainSubsystem(opMode.hardwareMap);
    }

    @Override
    public void onExecute(OpMode opMode) {
        driveTrain.logData(opMode.telemetry);
        driveTrain.arcadeDrive(opMode.gamepad1.right_stick_x, opMode.gamepad1.left_stick_y);
        //driveTrain.tankDrive(opMode.gamepad1.left_stick_y, opMode.gamepad1.left_stick_y);
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
