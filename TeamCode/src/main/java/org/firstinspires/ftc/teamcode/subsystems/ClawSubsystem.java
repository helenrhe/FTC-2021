package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.structure.Subsystem;

public class ClawSubsystem extends Subsystem {

    private Servo left, right;

    public ClawSubsystem(HardwareMap map){
        left = map.servo.get("claw_left");
        right = map.servo.get("claw_right");
    }

    public void log(Telemetry tem){
        tem.addData("servo_left", left.getPosition());
        tem.addData("servo_right", right.getPosition());
    }

    public void open(){
        left.setPosition(0.3);
        right.setPosition(0.6);
    }

    public void close(){
        left.setPosition(0.6);
        right.setPosition(0.2);
    }
}
