package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.structure.SubCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;

public class ClawCommand implements SubCommand {
    private ClawSubsystem clawSubsystem;
    @Override
    public void onInit(OpMode opMode) {
         clawSubsystem = new ClawSubsystem(opMode.hardwareMap);
    }

    @Override
    public void onExecute(OpMode opMode) {
        clawSubsystem.log(opMode.telemetry);
        if(opMode.gamepad1.a) {
            clawSubsystem.open();
        } else if(opMode.gamepad1.b) {
            clawSubsystem.close();
        }
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
