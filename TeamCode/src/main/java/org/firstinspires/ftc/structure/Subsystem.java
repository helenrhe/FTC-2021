package org.firstinspires.ftc.structure;

public abstract class Subsystem {

    public Subsystem() {

    }

    protected static class Util {
        public static double Clamp(double value, double min, double max) {
            return Math.max(min, Math.min(max, value));
        }
    }
}
