package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.structure.AutoCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;

@Autonomous(name = "Blue Left", group = "Auto")
public class BlueLeft extends AutoCommand {

    private int stage = 0;

    private DriveTrainSubsystem driveTrain;
    private ElevatorSubsystem elevator;
    private ClawSubsystem claw;

    private static int DISTANCE_TO_HUB = 2000;
    private static int BACKUP_DISTANCE = 1900;
    private static int TURN_DISTANCE = 100;
    private static int SPINNER_DISTANCE = 7000;

    @Override
    public void onInit(OpMode opMode) {
        driveTrain = new DriveTrainSubsystem(hardwareMap);
        elevator = new ElevatorSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);

        addInstantStage(driveTrain::resetDistance);
        addInstantStage(elevator::reset);
        addInstantStage(claw::close);
        addDelay(750);
        addInstantStage(elevator::raise);
        addInstantStage(elevator::raise);
        addDelay(2000);
        addStage(() -> {
            if(driveTrain.getDisplacement().first < DISTANCE_TO_HUB) {
                driveTrain.tankDrive(-0.6, -0.6);
            } else if(elevator.getMovementDelta() != 0) {
                driveTrain.tankDrive(0, 0);
            } else {
                nextStage();
            }
        });
        addInstantStage(claw::open);
        addDelay(2000);
        addStage(() -> {
            if(driveTrain.getDisplacement().first > BACKUP_DISTANCE) {
                driveTrain.tankDrive(0.6, 0.6);
            } else {
                nextStage();
            }
        });
        addInstantStage(driveTrain::resetDistance);
        addDelay(500);
        addStage(() -> {
            if(driveTrain.getDisplacement().second < TURN_DISTANCE) {
                driveTrain.tankDrive(0.3, -0.3);
            } else {
                nextStage();
            }
        });
        addDelay(500);
        addInstantStage(driveTrain::resetDistance);
        addStage(() -> {
            if(driveTrain.getDisplacement().first < SPINNER_DISTANCE) {
                driveTrain.tankDrive(-1, -1);
            } else {
                nextStage();
            }
        });
        addInstantStage(driveTrain::resetDistance);
    }

    @Override
    public void onExecute(OpMode opMode) {
        super.onExecute(opMode);
        driveTrain.logData(opMode.telemetry);
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
