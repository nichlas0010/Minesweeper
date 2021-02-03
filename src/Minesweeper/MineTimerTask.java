package Minesweeper;

import javafx.application.Platform;

import java.util.TimerTask;

public class MineTimerTask extends TimerTask {

    private MineTimer timer;

    public MineTimerTask(MineTimer timer) {
        this.timer = timer;
    }

    public void run() {
        Platform.runLater(() -> timer.checkTime());

    }
}
