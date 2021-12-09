package org.firstinspires.ftc.structure;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public interface SubCommand {
    void onInit(OpMode opMode);
    void onExecute(OpMode opMode);
    void onEnd(OpMode opMode);
}
