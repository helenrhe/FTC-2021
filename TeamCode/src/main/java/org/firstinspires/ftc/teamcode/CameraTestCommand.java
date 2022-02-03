package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.structure.Command;
import org.firstinspires.ftc.teamcode.commands.CameraCommand;

import java.io.IOException;

@TeleOp(name="Camera Test", group="Test - DON'T USE IN COMP OR I KILL YOU.")
public class CameraTestCommand extends Command {

    @Override
    public void onInit(OpMode opMode) {
        addSubCommand(new CameraCommand());
    }

    @Override
    public void onExecute(OpMode opMode) {
        telemetry.addData("running", "true");
    }

    @Override
    public void onEnd(OpMode opMode) {
    }
}
