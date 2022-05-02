package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.structure.AutoCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;

@Autonomous(name = "Reset", group = "Auto")
public class Reset extends AutoCommand {

    private ElevatorSubsystem elevator;
    private ClawSubsystem claw;

    @Override
    public void onInit(OpMode opMode) {
        elevator = new ElevatorSubsystem(opMode.hardwareMap);
        claw = new ClawSubsystem(opMode.hardwareMap);
    }

    @Override
    public void onExecute(OpMode opMode) {
        super.onExecute(opMode);


        if(elevator.autoReset()) {
            claw.open();
        } else {
            claw.close();
        }
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
