package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Pair;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.structure.Subsystem;

public class DriveTrainSubsystem extends Subsystem {

    public DcMotor leftMotor, rightMotor;

    public void logData(Telemetry telemetry) {
        telemetry.addData("displacement", getDisplacement());
    }

    public DriveTrainSubsystem(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");

        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void tankDrive(double left, double right) {
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotor.setPower(left);
        rightMotor.setPower(right);
    }

    public void arcadeDrive(double x, double y) {
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotor.setPower(Util.Clamp(x - y, -1, 1));
        rightMotor.setPower(Util.Clamp(-x - y, -1, 1));
    }

    public void resetDistance() {
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public Pair<Integer, Integer> getDisplacement() {
        return new Pair<>(-leftMotor.getCurrentPosition(), -rightMotor.getCurrentPosition());
    }
}
