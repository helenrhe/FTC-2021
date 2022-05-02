package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.structure.Subsystem;
import org.opencv.core.Point;

public class CameraSubsystem extends Subsystem {
    private Servo servo;

    private Point pt;

    public CameraSubsystem(HardwareMap map) {
        servo = map.servo.get("camera_servo");
    }

    public void setServoPosition(double position, Telemetry tel) {
        servo.setPosition(position);
        tel.addData("camera", position);
    }

    public void setPoint(Point pt) {
        this.pt = pt;
    }

    public Point getPoint() {
        return pt;
    }
}
