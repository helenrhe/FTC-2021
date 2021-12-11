package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.structure.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;

@Autonomous(name = "Main Auto", group = "Auto")
public class AutonomousCommand extends Command {

    private int stage = 0;

    private DriveTrainSubsystem driveTrain;
    private ElevatorSubsystem elevator;

    private static int DISTANCE_TO_HUB = 2000;
    private static int BACKUP_DISTANCE = 1750;
    private static int TURN_DISTANCE = 1000;

    @Override
    public void onInit(OpMode opMode) {
        driveTrain = new DriveTrainSubsystem(hardwareMap);
        elevator = new ElevatorSubsystem(hardwareMap);
    }

    @Override
    public void onExecute(OpMode opMode) {
        telemetry.addData("Stage", stage);
        driveTrain.logData(opMode.telemetry);
        switch(stage) {
            case 0:
                driveTrain.resetDistance();
                stage++;
                return;
            case 1:
                driveTrain.tankDrive(-0.6, -0.6);
                if(driveTrain.getDisplacement().first >= DISTANCE_TO_HUB) {
                    stage++;
                }
                return;
            case 2:
                //TODO: Open Claw
                stage++;
                return;
            case 3:
                driveTrain.tankDrive(0.2, 0.2);
                if(driveTrain.getDisplacement().first <= BACKUP_DISTANCE) {
                    stage++;
                }
                return;
            case 4:
                driveTrain.resetDistance();
                stage++;
                return;
            case 5:
                driveTrain.tankDrive(-0.3, 0.3);
                if(driveTrain.getDisplacement().first >= TURN_DISTANCE) {
                    stage++;
                }
                return;
            case 6:
                driveTrain.resetDistance();
                stage++;
        }
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}
