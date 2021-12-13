package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.structure.SubCommand;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;

public class ElevatorCommand implements SubCommand {

    ElevatorSubsystem elevator;
    boolean held = false;
    boolean manualUsed = false;

    @Override
    public void onInit(OpMode opMode) {
        elevator = new ElevatorSubsystem(opMode.hardwareMap);
    }

    @Override
    public void onExecute(OpMode opMode) {
        //Reset code - press back and b at the same time to set the location of the elevator to the bottom.
        if((opMode.gamepad1.back && opMode.gamepad1.b)) {
            elevator.reset();
        }

        //Choose between either manual or automatic control based on currently pressed buttons
        //Automatic movement
        if(opMode.gamepad1.dpad_down || opMode.gamepad1.dpad_up) {
            //Stop any manual movement
            if(manualUsed) {
                elevator.manualStop();
                manualUsed = false;
            }
            //If the d-pad down button was just pressed
            if(opMode.gamepad1.dpad_down && !held) {
                elevator.lower();
                held = true;
            //If the d-pad up button was just pressed
            } else if(opMode.gamepad1.dpad_up && !held) {
                elevator.raise();
                held = true;
            }
        //Manual Movement
        } else {
            //Auto-move buttons have been released - allow them to be pressed again.
            held = false;

            //Manually raise the elevator.
            if(opMode.gamepad1.right_bumper) {
                elevator.manualUp();
                manualUsed = true;
            //Manually lower the elevator
            } else if(opMode.gamepad1.left_bumper) {
                elevator.manualDown();
                manualUsed = true;
            //Stop movement when button released.
            } else if(manualUsed){
                elevator.manualStop();
            }
        }

        elevator.logData(opMode.telemetry);
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
