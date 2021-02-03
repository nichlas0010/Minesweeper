package Minesweeper;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class MinesweeperMouseHandler implements EventHandler<MouseEvent> {

    private Minefield minefield;
    private GridPane mineGrid;
    private MinesweeperGUI mGUI;

    public MinesweeperMouseHandler(MinesweeperGUI mGUI) {
        this.mGUI = mGUI;
    }

    public void setFieldAndGrid(Minefield minefield, GridPane mineGrid) {
        this.minefield = minefield;
        this.mineGrid = mineGrid;
    }

    @Override
    public void handle(MouseEvent e) {
        if(minefield.getLost()) {
            return;
        }
        // This is ugly, but we only ever add setOnMousePressed to Rectangles, so we know they're rectangles.
        Rectangle R = (Rectangle) e.getSource();
        int row = GridPane.getRowIndex(R);
        int column = GridPane.getColumnIndex(R);
        MineTile[][] mines = minefield.getMinefield();
        MineTile tile = mines[row][column];
        mGUI.startTimer();
        if(!tile.isRevealed()) {
            if(e.isPrimaryButtonDown() && !tile.hasFlag()) {
                minefield.step(row, column);
            } else if(e.isSecondaryButtonDown()) {
                minefield.mark(row, column);
                if(tile.hasFlag()) {
                    RadialGradient RG = new RadialGradient(0, 0.1, 25, 25, 25, false, CycleMethod.NO_CYCLE, new Stop(0, Color.RED), new Stop(1, Color.LIGHTBLUE));
                    R.setFill(RG);
                } else {
                    RadialGradient RG = new RadialGradient(0, 0.1, 25, 25, 25, false, CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTBLUE.brighter()), new Stop(1, Color.LIGHTBLUE));
                    R.setFill(RG);
                }
            }
        } else {
            if(e.isMiddleButtonDown() || (e.isShiftDown() && e.isPrimaryButtonDown()) || (e.isPrimaryButtonDown() && e.isSecondaryButtonDown())) {
                mGUI.checkNeighbours(tile);
            }
        }

    }
}
