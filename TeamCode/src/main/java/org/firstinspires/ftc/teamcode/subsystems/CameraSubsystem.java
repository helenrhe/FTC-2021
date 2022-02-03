package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.structure.Subsystem;

public class CameraSubsystem extends Subsystem {
    private Servo servo;

    public CameraSubsystem(HardwareMap map) {
        servo = map.servo.get("camera_servo");
    }

    public void setServoPosition(double position, Telemetry tel) {
        servo.setPosition(position);
        tel.addData("camera", position);
    }
}
