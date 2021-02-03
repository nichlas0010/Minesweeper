package Minesweeper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static java.lang.System.exit;

public class MinesweeperActionHandler implements EventHandler<ActionEvent> {

    private MinesweeperGUI mGUI;
    private Button cancelSecondWindow;
    private Button acceptSecondWindow;
    private Button newGame;
    private Button quit;
    private Button hint;
    private MineTimer timer;
    private TextField rows;
    private TextField columns;
    private TextField mines;

    public MinesweeperActionHandler(MinesweeperGUI mGUI) {
        this.mGUI = mGUI;
    }

    @Override
    public void handle(ActionEvent T) {
        if(T.getSource() == quit) {
            exit(0);
        } else if(T.getSource() == newGame) {
            mGUI.newGameDialogue();
            return;
        } else if(T.getSource() == cancelSecondWindow) {
            mGUI.closeDialogue();
        } else if(T.getSource() == acceptSecondWindow) {
            int row;
            int column;
            int mine;
            try {
                row = Integer.parseInt(rows.getText());
                column = Integer.parseInt(columns.getText());
                mine = Integer.parseInt(mines.getText());
            } catch(NumberFormatException e) {
                mGUI.updateDialogue("Unable to parse integers from fields!");
                return;
            }
            if (row <= 0 || column <= 0) {
                mGUI.updateDialogue("Minefield too small");
                return;
            }
            if (row >= 32 || column >= 32) {
                mGUI.updateDialogue("Minefield too large");
                return;
            }
            if (mine <= 0) {
                mGUI.updateDialogue("Too few mines");
                return;
            }
            if (mine > row * column - 1) {
                mGUI.updateDialogue("Too many mines");
                return;
            }
            mGUI.newMinefield(row, column, mine);
        } else if(T.getSource() == hint) {
            if(mGUI.getLost()) {
                return;
            }
            mGUI.getHint();
        } else if(T.getSource() == timer) {
            timer.playOrPause();
        }
    }

    public void setCancelSecondWindow(Button cancelSecondWindow) {
        this.cancelSecondWindow = cancelSecondWindow;
        cancelSecondWindow.setOnAction(this);
    }

    public void setAcceptSecondWindow(Button acceptSecondWindow) {
        this.acceptSecondWindow = acceptSecondWindow;
        acceptSecondWindow.setOnAction(this);
    }

    public void setNewGame(Button newGame) {
        this.newGame = newGame;
        newGame.setOnAction(this);
    }

    public void setQuit(Button quit) {
        this.quit = quit;
        quit.setOnAction(this);
    }

    public void setHint(Button hint) {
        this.hint = hint;
        hint.setOnAction(this);
    }

    public void setTimer(MineTimer timer) {
        this.timer = timer;
        timer.setOnAction(this);
    }

    public void setRows(TextField rows) {
        this.rows = rows;
    }

    public void setColumns(TextField columns) {
        this.columns = columns;
    }

    public void setMines(TextField mines) {
        this.mines = mines;
    }
}
