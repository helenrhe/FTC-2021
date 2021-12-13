package org.firstinspires.ftc.structure;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AutoCommand extends Command {
    private ElapsedTime delay = new ElapsedTime();

    private int stage = 0;
    private LinkedList<Runnable> stages = new LinkedList<>();

    protected void addStage(Runnable callback) {
        stages.add(callback);
    }

    protected void addInstantStage(Runnable callback) {
        addStage(() -> {
            callback.run();
            nextStage();
        });
    }

    protected void addDelay(long time) {
        AtomicBoolean started = new AtomicBoolean(false);
        AtomicLong startTime = new AtomicLong();
        addStage(() -> {
            if(!started.get()) {
                startTime.set(System.currentTimeMillis());
                started.set(true);
            }
            if(System.currentTimeMillis() >= startTime.get() + time) nextStage();
        });
    }

    protected void nextStage() {
        stage++;
    }

    @Override
    public void onExecute(OpMode opMode) {
        if(stage < stages.size()) {
            stages.get(stage).run();
        }
    }
}
