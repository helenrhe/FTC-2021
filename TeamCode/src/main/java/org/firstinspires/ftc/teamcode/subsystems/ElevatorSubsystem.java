package org.firstinspires.ftc.teamcode.subsystems;

import android.content.Context;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ReadWriteFile;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.structure.Subsystem;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ElevatorSubsystem extends Subsystem {

    private static double speed = 1;

    private static int[] storedHeights = {
            0,      // Ground
            -2293,  // Low
            -5482,  // Mid
            -8004   // High
    };

    private byte currentPosition = 0;

    private ExpansionHubMotor elevatorMotor;

    private RevTouchSensor touchSensor;
    private boolean wasPressed = false;

    private int lastPosition = 0;

    private static final double MAX_CURRENT_DRAW = 1000;

    public ElevatorSubsystem(HardwareMap hardwareMap) {
        //Initialize Hardware.
        elevatorMotor = (ExpansionHubMotor) hardwareMap.get(DcMotor.class, "elevator");
    }

    /**
     * Log relevant debug data to the console.
     * @param telemetry The telemetry system to log the data to
     */
    public void logData(Telemetry telemetry) {
        telemetry.addData("elevatorPosition", elevatorMotor.getCurrentPosition());
        telemetry.addData("stepPosition", currentPosition);

        telemetry.addData("Motor Current", elevatorMotor.getCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));
    }

    /**
     * Raise the elevator one preset level. If it is at the max level already, nothing will happen
     */
    public void raise() {
        if(currentPosition < storedHeights.length - 1) {
            currentPosition++;
        }

        elevatorMotor.setTargetPosition(storedHeights[currentPosition]);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorMotor.setPower(speed);
    }

    /**
     * Lower the elevator one preset level. If it is at the min level already, nothing will happen
     */
    public void lower() {
        if(currentPosition > 0) {
            currentPosition--;
        }

        elevatorMotor.setTargetPosition(storedHeights[currentPosition]);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorMotor.setPower(speed);
    }

    /**
     * Manually raise the elevator. It will continue to move until the {@link #manualStop()} is called
     */
    public void manualUp() {
        elevatorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elevatorMotor.setPower(-speed);
    }

    /**
     * Manually lower the elevator. It will continue to move until the {@link #manualStop()} is called
     */
    public void manualDown() {
        elevatorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elevatorMotor.setPower(speed);
    }

    /**
     * Stops any elevator movement - manual or automatic
     */
    public void manualStop() {
        elevatorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elevatorMotor.setPower(0);
    }

    /**
     * Get the change in the elevator since this function was last called.
     */
    public int getMovementDelta() {
        int change = elevatorMotor.getCurrentPosition() - lastPosition;
        lastPosition = elevatorMotor.getCurrentPosition();
        return change;
    }

    /**
     * Resets the elevator. Should old be called when the elevator is at the lowest level.
     */
    public void reset() {
        elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        currentPosition = 0;
    }
}
