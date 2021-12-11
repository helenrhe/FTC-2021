package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.structure.Subsystem;

public class SpinnerSubsystem extends Subsystem{

    public DcMotor spinnerMotor;

    public SpinnerSubsystem(HardwareMap hardwareMap) {
        spinnerMotor = hardwareMap.dcMotor.get("spinner_motor");
    }

    public void spin(double speed) {
        spinnerMotor.setPower(speed);
    }
}
