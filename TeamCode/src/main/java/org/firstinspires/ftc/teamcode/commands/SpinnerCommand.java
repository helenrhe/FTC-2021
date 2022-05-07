package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.structure.SubCommand;
import org.firstinspires.ftc.teamcode.subsystems.SpinnerSubsystem;

public class SpinnerCommand implements SubCommand {
    private static double speed = -1;
    SpinnerSubsystem spinner;

    @Override
    public void onInit(OpMode opMode) {
        spinner = new SpinnerSubsystem(opMode.hardwareMap);
    }

    @Override
    public void onExecute(OpMode opMode) {
        if (opMode.gamepad1.y) {
            spinner.spin(speed);
        } else if (opMode.gamepad1.x) {
            spinner.spin(-speed);
        } else {
            spinner.spin(0);
        }
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
