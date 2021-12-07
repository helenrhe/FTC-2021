package org.firstinspires.ftc.structure;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrainSubsystem;

import java.util.ArrayList;
import java.util.List;

public abstract class Command extends LinearOpMode implements SubCommand {

    private ElapsedTime runtime = new ElapsedTime();
    private List<SubCommand> subCommands = new ArrayList<>();

    @Override
    public void runOpMode() {
        //Initialize the opMode
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        runtime.reset();

        onInit(this);
        for(SubCommand command : subCommands) {
            command.onInit(this);
        }
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Wait for the play button on the driver station to be pressed.
        waitForStart();
        runtime.reset();

        //Run the opmode
        while(opModeIsActive()) {
            onExecute(this);
            for(SubCommand command : subCommands) {
                command.onExecute(this);
            }
            telemetry.addData("Status", "Running...");
            telemetry.update();
        }

        //End the opmode.
        onEnd(this);
        for(SubCommand command : subCommands) {
            command.onEnd(this);
        }
        telemetry.addData("Status", "Shut down.");
        telemetry.update();
    }

    protected void addSubCommand(SubCommand command) {
        if(subCommands.contains(command)) {
            throw new IllegalArgumentException("This subcommand has already been added to this command.");
        }
        if(command instanceof Command) {
            throw new IllegalArgumentException("A command cannot be added as a subcommand!");
        }

        subCommands.add(command);
    }
}
