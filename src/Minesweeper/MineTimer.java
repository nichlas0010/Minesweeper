package Minesweeper;

import javafx.scene.control.Button;

import java.util.Timer;

public class MineTimer extends Button {

    private long startTime;
    private int currentTime; // In seconds
    private Timer timer;
    private boolean running;
    private MineTimerTask timertask;

    public MineTimer(int time) {
        setTimer(time);
        resetTimer();
        checkTime();
        timer = new Timer();
    }

    public void resetTimer() {
        startTime = System.currentTimeMillis() - currentTime * 1000;
    }

    public void startTimer() {
        if(running) {
            return;
        }
        running = true;
        resetTimer();
        timertask = new MineTimerTask(this);
        timer.schedule(timertask, 1000, 1000);
    }

    public void stopTimer() {
        timertask.cancel();
        timer.purge();
        running = false;
    }

    public void setTimer(int i) {
        currentTime = i;
    }

    public void checkTime() {
        try {
            currentTime = Math.toIntExact(System.currentTimeMillis() - startTime) / 1000;
        } catch (Exception e) {
            currentTime = Integer.MAX_VALUE;
        } finally {
            int minutes = Math.floorDiv(currentTime, 60);

            int seconds = currentTime % 60;
            setText((minutes > 9 ? minutes  : "0" + minutes) + ":" + (seconds > 9 ? seconds : "0" + seconds));
        }

    }

    public void addToTimer(int seconds) {
        currentTime += seconds;
        resetTimer();
    }

    public void playOrPause() {
        if(running) {
            stopTimer();
        } else {
            startTimer();
        }
    }
}
