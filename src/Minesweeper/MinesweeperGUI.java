package Minesweeper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class MinesweeperGUI extends Application {
    private Minefield minefield;
    private GridPane mineGrid;
    private GridPane containerGrid;
    private Stage stage;
    private MinesweeperActionHandler actionHandler;
    private MinesweeperMouseHandler mouseHandler;
    private Stage newStage;
    private MineTimer timer;

    public static void main(String[] args) {
        if(args.length > 0 && args[0].equals("-t")) {
            Terminal.main(args);
        } else {
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        actionHandler = new MinesweeperActionHandler(this);
        mouseHandler = new MinesweeperMouseHandler(this);

        primaryStage.setTitle("Minesweeper");

        minefield = new Minefield(10, 15, 20, this);
        minefield.populate();
        containerGrid = new GridPane();
        containerGrid.setAlignment(Pos.CENTER);

        setup_buttons();


        Scene scene = new Scene(containerGrid, 200, 200);

        primaryStage.setScene(scene);
        construct_gui();
        primaryStage.show();

    }

    public void setup_buttons() {
        Button quit = new Button("Quit Game");
        Button newGame = new Button("New Game");
        Button hint = new Button("Hint");
        timer = new MineTimer(0);

        actionHandler.setQuit(quit);
        actionHandler.setNewGame(newGame);
        actionHandler.setHint(hint);
        actionHandler.setTimer(timer);

        containerGrid.add(quit, 0, 0);
        containerGrid.add(newGame, 0, 0);
        containerGrid.add(hint, 0, 0);
        containerGrid.add(timer, 0, 0);
        newGame.setTranslateX(75);
        hint.setTranslateX(150);
        timer.setTranslateX(190);
    }

    public void construct_gui() {
        if(mineGrid != null) {
            containerGrid.getChildren().remove(mineGrid);
        }
        mineGrid = new GridPane();
        mineGrid.setAlignment(Pos.CENTER);
        mineGrid.setHgap(5);
        mineGrid.setVgap(5);
        mineGrid.setPadding(new Insets(25, 25, 25, 25));
        mineGrid.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");

        MineTile[][] mines = minefield.getMinefield();
        for(int row = 0; row < mines.length; row++) {
            for(int column = 0; column < mines[0].length; column++) {
                RadialGradient RG = new RadialGradient(0, 0.1, 25, 25, 25, false, CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTBLUE.brighter()), new Stop(1, Color.LIGHTBLUE));
                Rectangle R =  new Rectangle(50, 50, RG);
                mineGrid.add(R, column, row);
                R.setOnMousePressed(mouseHandler);
            }
        }

        containerGrid.add(mineGrid, 0, 1);
        stage.setHeight(50*mines.length+200);
        stage.setWidth(50*mines[0].length+200);

        mouseHandler.setFieldAndGrid(minefield, mineGrid);

    }

    public void newGameDialogue() {
        if(newStage == null) {
            newStage = new Stage();
            GridPane newGrid = new GridPane();
            newGrid.add(new Text("Rows"), 0, 1);
            newGrid.add(new Text("Columns"), 0, 2);
            newGrid.add(new Text("Mines"), 0, 3);

            TextField rows = new TextField("10");
            actionHandler.setRows(rows);
            newGrid.add(rows, 1, 1);
            TextField columns = new TextField("15");
            actionHandler.setColumns(columns);
            newGrid.add(columns, 1, 2);
            TextField mines = new TextField("20");
            actionHandler.setMines(mines);
            newGrid.add(mines, 1, 3);

            Button cancelSecondWindow = new Button("Cancel");
            cancelSecondWindow.setCancelButton(true);
            actionHandler.setCancelSecondWindow(cancelSecondWindow);
            newGrid.add(cancelSecondWindow, 0, 4);

            Button acceptSecondWindow = new Button("Accept");
            acceptSecondWindow.setDefaultButton(true);
            actionHandler.setAcceptSecondWindow(acceptSecondWindow);
            newGrid.add(acceptSecondWindow, 1, 4);

            Scene newScene = new Scene(newGrid, 201, 100); // If this is only 200, the cancel button gets cut off. So 201 it is
            newStage.setScene(newScene);
        }
        newStage.show();
    }

    public void closeDialogue(){
        newStage.close();
    }

    public GridPane getMineGrid() {
        return mineGrid;
    }

    public void newMinefield(int rows, int columns, int mines) {
        minefield = new Minefield(rows, columns, mines, this);
        minefield.populate();
        construct_gui();
        timer.stopTimer();
        timer.setTimer(0);
        timer.resetTimer();
        timer.checkTime();
        closeDialogue();
    }

    public void updateDialogue(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text);
        alert.setTitle("Minesweeper");
        alert.show();
    }

    public boolean getLost() {
        return minefield.getLost();
    }

    public void getHint() {
        timer.addToTimer(10);
        MineTile[][] mineField = minefield.getMinefield();
        ArrayList<MineTile> mineTiles = new ArrayList();
        boolean hasRevealedNeighbour = false;
        for(MineTile[] m1 : mineField) {
            for(MineTile m2 : m1) {
                if(m2.isRevealed() || (m2.isMined() && m2.hasFlag())) {
                    continue;
                }
                boolean hasFreeNeighbour = false;
                int row = m2.getRow();
                int column = m2.getColumn();
                loop: for(int i = row - 1; i <= row + 1; i++) {
                    for(int j = column - 1; j <= column + 1; j++) {
                        if(i >= mineField.length || i < 0 || j >= mineField[0].length || j < 0) {
                            continue;
                        }
                        MineTile mt = mineField[i][j];
                        if(mt == m2 || !mt.isRevealed()) {
                            continue;
                        }
                        hasFreeNeighbour = true;
                        break loop;
                    }
                }
                if(hasFreeNeighbour) {
                    mineTiles.add(m2);
                }
            }
        }
        MineTile mt;
        if(mineTiles.size() < 1) {
            mt = mineField[0][0];
        } else if(mineTiles.size() < 2) {
            mt = mineTiles.get(0);
        } else {
            Random random = new Random();
            mt = mineTiles.get(random.nextInt(mineTiles.size()));
        }

        int row = mt.getRow();
        int column = mt.getColumn();
        for(Node n : mineGrid.getChildren()) {
            if(GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == column) {
                Rectangle r = (Rectangle) n;
                if(mt.isMined()) {
                    RadialGradient RG = new RadialGradient(0, 0.1, 25, 25, 25, false, CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.RED));
                    r.setFill(RG);
                } else {
                    if(mt.hasFlag()) {
                        RadialGradient RG = new RadialGradient(0, 0.1, 25, 25, 25, false, CycleMethod.NO_CYCLE, new Stop(0, Color.RED), new Stop(1, Color.LIGHTGREEN));
                        r.setFill(RG);
                    } else {
                        RadialGradient RG = new RadialGradient(0, 0.1, 25, 25, 25, false, CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.LIGHTGREEN));
                        r.setFill(RG);
                    }

                }
                return;
            }
        }

    }

    public void checkNeighbours(MineTile mt) {
        int row = mt.getRow();
        int column = mt.getColumn();
        int minesSoFar = 0;
        int expectedMines = mt.getNeighbours();
        MineTile[][] mineField = minefield.getMinefield();
        for(int i = row - 1; i <= row + 1; i++) {
            for(int j = column - 1; j <= column + 1; j++) {
                if(i >= mineField.length || i < 0 || j >= mineField[0].length || j < 0) {
                    continue;
                }
                MineTile mt2 = mineField[i][j];
                if(mt2.hasFlag()) {
                    minesSoFar++;
                }
            }
        }
        if(minesSoFar == expectedMines) {
            for(int i = row - 1; i <= row + 1; i++) {
                for(int j = column - 1; j <= column + 1; j++) {
                    if(i >= mineField.length || i < 0 || j >= mineField[0].length || j < 0) {
                        continue;
                    }
                    MineTile mt2 = mineField[i][j];
                    if(!mt2.hasFlag()) {
                        minefield.step(i, j);
                    }
                }
            }
        }
    }

    public void startTimer() {
        timer.startTimer();
    }

    public void stopTimer() {
        timer.stopTimer();
    }
}
