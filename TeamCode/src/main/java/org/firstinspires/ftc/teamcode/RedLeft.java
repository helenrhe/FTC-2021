package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import org.firstinspires.ftc.structure.AutoCommand;
import org.firstinspires.ftc.teamcode.commands.CameraCommand;
import org.firstinspires.ftc.teamcode.subsystems.*;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Autonomous(name = "Red Left", group = "Auto")
@Config
public class RedLeft extends AutoCommand {

    private int stage = 0;

    private DriveTrainSubsystem driveTrain;
    private ElevatorSubsystem elevator;
    private ClawSubsystem claw;
    private SpinnerSubsystem spinner;
    private CameraCommand camera;

    private BNO055IMU imu;

    public static int CAMERA_READ_TIMEOUT_MAX = 2000;

    public static double TO_TEAM_ELEMENT = 700;

    public static double TURN_TO_SPINNER = 67;
    public static double TO_SPINNER = 750;

    public static int SPIN_TIME = 4000;

    public static double TURN_TO_MID = -70;
    public static double TO_MID = 4200;

    public static double TURN_TO_SHIPPING_HUB = -20;
    public static double TO_SHIPPING_HUB = 1200;

    public static double TURN_TO_WAREHOUSE = -73;
    public static double TO_WAREHOUSE = 7000;

    public static double SPEED = 0.25;

    private double teamShippingElementLocation = 0;
    private int count = 0;


    @Override
    public void onInit(OpMode opMode) {
        opMode.telemetry = new MultipleTelemetry(opMode.telemetry, FtcDashboard.getInstance().getTelemetry());
        driveTrain = new DriveTrainSubsystem(hardwareMap);
        elevator = new ElevatorSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        spinner = new SpinnerSubsystem(hardwareMap);
        camera = new CameraCommand();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        addSubCommand(camera);

        addInstantStage(driveTrain::resetDistance);
        addInstantStage(elevator::reset);
        addInstantStage(claw::close);
        AtomicLong startTime = new AtomicLong();
        addInstantStage(() -> {
            startTime.set(System.currentTimeMillis());
        });
        addStage(() -> {
            if(System.currentTimeMillis() - startTime.get() >= CAMERA_READ_TIMEOUT_MAX) {
                if(count > 0) teamShippingElementLocation /= (double) count;
                nextStage();
            } else if(camera.getPoint() != null && !Double.isNaN(camera.getPoint().y)) {
                teamShippingElementLocation += camera.getPoint().y;
                count++;
            }
        });

        addInstantStage(elevator::raise);

        //Move to Spinner
        addDriveForwardStage(TO_TEAM_ELEMENT, SPEED);
        addTurnLeftStage(TURN_TO_SPINNER, SPEED);
        addDriveForwardStage(TO_SPINNER, SPEED);

        //Spin Spinner
        addInstantStage(() -> spinner.spin(-1));
        addDelay(SPIN_TIME);
        addInstantStage(() -> spinner.spin(0));

        //Go to the shipping hub
        addDriveBackwardStage(350, SPEED);
        addTurnRightStage(TURN_TO_MID, SPEED);
        addInstantConditionalStage(() -> teamShippingElementLocation >= 300 || true, elevator::raise);
        addDriveForwardStage(TO_MID, SPEED);
        addTurnLeftStage(TURN_TO_SHIPPING_HUB, SPEED);
        addDriveForwardStage(TO_SHIPPING_HUB, SPEED);

        //Drop Box
        addInstantStage(claw::open);
        addDelay(1000);

        //Go to warehouse
        addDriveBackwardStage(TO_SHIPPING_HUB / 4, SPEED);
        addTurnRightStage(TURN_TO_WAREHOUSE, SPEED);
        addInstantStage(claw::close);
        addInstantConditionalStage(() -> teamShippingElementLocation >= 300 || true, elevator::lower);
        addDriveForwardStage(TO_WAREHOUSE, 0.8);
    }

    public void addDriveForwardStage(double distance, double speed) {
        addStage(() -> {
            driveTrain.tankDrive(-speed, -speed);

            if(driveTrain.getDisplacement().first >= distance) {
                driveTrain.tankDrive(0, 0);
                nextStage();
            }
        });
        addInstantStage(driveTrain::resetDistance);
    }

    public void addDriveBackwardStage(double distance, double speed) {
        addStage(() -> {
            driveTrain.tankDrive(speed, speed);
            if(driveTrain.getDisplacement().first <= -distance) {
                driveTrain.tankDrive(0, 0);
                nextStage();
            }
        });
        addInstantStage(driveTrain::resetDistance);
    }

    public void addTurnLeftStage(double distance, double speed) {
        addStage(() -> {
            driveTrain.tankDrive(speed, -speed);
            if(imu.getAngularOrientation().firstAngle >= distance) {
                driveTrain.tankDrive(0, 0);
                nextStage();
            }
        });
        addInstantStage(driveTrain::resetDistance);
    }

    public void addTurnRightStage(double distance, double speed) {
        addStage(() -> {
            driveTrain.tankDrive(-speed, speed);
            if(imu.getAngularOrientation().firstAngle <= distance) {
                driveTrain.tankDrive(0, 0);
                nextStage();
            }
        });
        addInstantStage(driveTrain::resetDistance);
    }

    @Override
    public void onExecute(OpMode opMode) {
        super.onExecute(opMode);

        driveTrain.logData(opMode.telemetry);

        telemetry.addData("target", teamShippingElementLocation > 300 ? "mid" : "low");
    }

    @Override
    public void onEnd(OpMode opMode) {

    }
}