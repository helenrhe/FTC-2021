package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.structure.Subsystem;

public class DriveTrainSubsystem extends Subsystem {

    public DcMotor leftMotor, rightMotor;

    public DriveTrainSubsystem(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
    }

    public void tankDrive(double left, double right) {
        leftMotor.setPower(left);
        rightMotor.setPower(right);
    }

    public void arcadeDrive(double x, double y) {
        leftMotor.setPower(Util.Clamp(-x + y, -1, 1));
        rightMotor.setPower(Util.Clamp(-x - y, -1, 1));
    }
}
