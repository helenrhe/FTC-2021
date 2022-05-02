package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.structure.Command;
import org.firstinspires.ftc.teamcode.commands.CameraCommand;
import org.firstinspires.ftc.teamcode.subsystems.CameraSubsystem;

import java.io.IOException;

@TeleOp(name="Camera Test", group="Test - DON'T USE IN COMP OR I KILL YOU.")
public class CameraTestCommand extends Command {

    BNO055IMU imu;

    @Override
    public void onInit(OpMode opMode) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

    }

    @Override
    public void onExecute(OpMode opMode) {
        telemetry.addData("running", "true");
        telemetry.addData("angle 1", imu.getAngularOrientation().firstAngle);
        telemetry.addData("angle 2", imu.getAngularOrientation().secondAngle);
        telemetry.addData("angle 3", imu.getAngularOrientation().thirdAngle);
    }

    @Override
    public void onEnd(OpMode opMode) {
    }
}
