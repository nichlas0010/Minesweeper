package Minesweeper;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Random;

public class Minefield {
    // The minefield
    private MineTile[][] minefield;
    // Maximum amount of mines, mineTile() will not exceed this value
    private int maxMines;
    // Current amount of mines, incremented by mineTile()
    private int currentMines;
    // Current amount of marked tiles
    private int markedTiles;
    // Whether or not we've lost.
    private boolean hasLost = false;
    // Whether or not the game is over
    private boolean gameOver = false;
    // Whether or not we've won
    private boolean hasWon = false;
    // The random generator
    private Random random;
    // Whether we're using the terminal or the gui
    private boolean terminal = true;
    // The gui, if we have it
    private MinesweeperGUI gui;


    /**
     * Instantiates all instance variables.
     *
     * @param rows amount of rows in the minefield, should be more than 0
     * @param columns amount of columns in the minefield, should be more than 0
     * @param maxMines maximum amount of mines, should not exceed (rows * colums - 1)
     */
    public Minefield(int rows, int columns, int maxMines) {
        if(rows < 1 || columns < 1) {
            throw new NegativeArraySizeException("Rows: " + rows + ", columns: " + columns);
        }
        if(maxMines > (rows*columns)-1) {
            throw new IllegalArgumentException("maxMines is larger than is allowed. Rows: " + rows + ", columns: " + columns + ", maxMines: " + maxMines);
        }
        minefield = new MineTile[rows][columns];
        this.maxMines = maxMines;
        for(int i = 0; i < minefield.length; i++) {
            for(int j = 0; j < minefield[0].length; j++) {
                minefield[i][j] = new MineTile(i, j);
            }
        }
        random = new Random();
    }
    /**
     * Instantiates all instance variables.
     *
     * @param rows amount of rows in the minefield, should be more than 0
     * @param columns amount of columns in the minefield, should be more than 0
     * @param maxMines maximum amount of mines, should not exceed (rows * colums - 1)
     * @param mGUI the MinesweeperGUI that we're attached to.
     */
    public Minefield(int rows, int columns, int maxMines, MinesweeperGUI mGUI) {
        this(rows, columns, maxMines);
        gui = mGUI;
        terminal = false;
    }


    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    /**
     * Method to mine a tile at given co-ordinate (row, column), and increase minedNeighbours of neighbouring tiles.
     * Will fail if row/column is larger/smaller than the grid's dimensions, if the maxMines limit has been reached,
     * or if the tile has already been mined.
     *
     * @param row row number to insert mine at
     * @param column column number to insert mine at
     * @return true if tile was mined successfully, false if mining failed
     */
    public boolean mineTile(int row, int column) {
        // These two if-statements have been split for readability purposes
        // This check makes sure we're not exceeding the bounds of the minefield, in either direction
        if(row < 0 || row >= minefield.length || column < 0 || column >= minefield[0].length) {
            throw new ArrayIndexOutOfBoundsException("row: " + row + ", column: " + column);
        }
        // This check makes sure the tile has not already been mined, and that we have spare mines.
        if(minefield[row][column].isMined() || currentMines >= maxMines) {
            return false;
        }
        minefield[row][column].mine();
        // Iterate through the neighbouring tiles (including our own), and increment each
        for(int i = row-1; i <= row+1; i++) {
            for(int j = column-1; j <= column+1; j++) {
                if(i >= 0 && i < minefield.length && j >= 0 && j < minefield[0].length && !(i == row && j == column)) {
                    minefield[i][j].increaseNeighbours();
                }
            }
        }
        // Increment currentMines
        currentMines++;
        return true;

    }

    public void mark(int row, int column) {
        if(row < 0 || row >= minefield.length || column < 0 || column >= minefield[0].length) {
            update("Tile at row " + row + ", column " + column + " is out of grid bounds");
            return;
        }
        if(minefield[row][column].isRevealed()) {
            update("Tile at row " + row + ", column " + column + " has already been revealed, cannot mark");
        }
        boolean marked = minefield[row][column].mark();
        if(marked) {
            markedTiles++;
        } else {
            markedTiles--;
        }
        // one-indexing the grid for the user.
        if(!areAllMinesRevealed()) {
            update("Tile at row " + (row + 1) + ", column " + (column + 1) + " has been " + (marked ? "marked" : "unmarked"));
        }

    }

    public boolean step(int row, int column) {
        return step(row, column, false);
    }

    public boolean step(int row, int column, boolean isRecursive) {
        if(row < 0 || row >= minefield.length || column < 0 || column >= minefield[0].length) {
            if(!isRecursive) {
                update("Tile at row " + row + ", column " + column + " is out of grid bounds");
            }
            return false;
        }
        MineTile tile = minefield[row][column];
        if(tile.isRevealed()) {
            if(!isRecursive) {
                update("Tile at row " + row + ", column " + column + " has already been revealed");
            }
            return false;
        }
        if(tile.hasFlag()) {
            if(!isRecursive) {
                update("Tile at row " + row + ", column " + column + " is marked, and cannot be stepped on.");
            }
            return false;
        }
        tile.reveal();
        if(tile.isMined()) {
            hasLost = true;
            gameOver = true;
            update("Tile at row " + row + ", column " + column + " was mined, and has exploded");
            if(gui != null) {
                gui.stopTimer();
                GridPane grid = gui.getMineGrid();
                for(Node n : grid.getChildrenUnmodifiable()) {
                    if(grid.getRowIndex(n) == row && grid.getColumnIndex(n) == column) {
                        Rectangle R = (Rectangle) n;
                        RadialGradient RG = new RadialGradient(0, 0.1, 25, 25, 25, false, CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTGRAY.brighter()), new Stop(1, Color.LIGHTGRAY));
                        R.setFill(RG);
                        break;
                    }
                }
                Text text = new Text("X");
                text.setFill(Color.RED);
                text.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
                grid.add(text, column, row);
                grid.setHalignment(text, HPos.CENTER);
            }
            return false;
        }
        if(tile.getNeighbours() == 0) {
            for(int i = row - 1; i <= row + 1; i++) {
                for(int j = column - 1; j <= column + 1; j++) {
                    if(i >= 0 && i < minefield.length && j >= 0 && j < minefield[0].length) {
                        step(i, j, true);
                    }
                }
            }
        }
        if(gui != null) {
            GridPane grid = gui.getMineGrid();
            for(Node n : grid.getChildrenUnmodifiable()) {
                if(grid.getRowIndex(n) == row && grid.getColumnIndex(n) == column) {
                    Rectangle R = (Rectangle) n;
                    RadialGradient RG = new RadialGradient(0, 0.1, 25, 25, 25, false, CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTGRAY.brighter()), new Stop(1, Color.LIGHTGRAY));
                    R.setFill(RG);
                    break;
                }
            }
            Text text = new Text("" + tile.getNeighbours());
            grid.add(text, column, row);
            grid.setHalignment(text, HPos.CENTER);
        }
        if(!areAllMinesRevealed() && !isRecursive) {
            update("Tile at row " + row + ", column " + column + " has been revealed");
        }
        return true;

    }

    public boolean areAllMinesRevealed() {
        for(MineTile[] m1 : minefield) {
            for(MineTile m : m1) {
                if((m.hasFlag() && !m.isMined()) || (!m.hasFlag() && m.isMined()) || (!m.isRevealed() && !m.hasFlag())) {
                    return false;
                }
            }
        }
        update("Game has been won!");
        // Ends the game
        gameOver= true;
        hasWon = true;
        if(gui != null) {
            gui.stopTimer();
            Alert alert = new Alert(Alert.AlertType.NONE, "Congratulations, you won!");
            alert.setTitle("Minesweeper");
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.show();
        }
        return true;
    }

    public void update(String s) {
        if(!terminal) {
            return;
        }
        System.out.flush();
        String st = "You have marked " + markedTiles + " tiles out of " + currentMines + " mined tiles.\n";
        st += s + "\n\n";
        st += toString();

        st += "\n\nCommands:\n";
        st += "Quit: quits the game\n";
        st += "Mark [row] [column]: (un)marks the tile at the given co-ordinates\n";
        st += "Step [row] [column]: step onto the tile at the given co-ordinates, triggering or revealing it\n";
        st += "New [row] [column]: start a new game with the specified amount of rows and columns\n\n";
        st += "Legend:\n";
        st += "Unrevealed: ?\n";
        st += "Marked: !\n";
        st += "Exploded: X\n";
        System.out.print(st);
    }

    /**
     * Populates the minefield, up to the maxMines passed to the constructor. Doesn't mine (0, 0).
     */
    public void populate() {

        // Determine the amount of free tiles, excluding (0,0)
        int freeTiles = -1;
        for(int i = 0; i < minefield.length; i++) {
            for(int j = 0; j < minefield[0].length; j++) {
                if(!minefield[i][j].isMined()) {
                    freeTiles++;
                }
            }
        }
        while(currentMines < maxMines && freeTiles > 0) {
            // Pick a random position between 0 and our amount of free tiles, then increment by one to avoid (0,0)
            int randomPosition = random.nextInt(freeTiles) + 1;
            freeTiles--;
            int currentPosition = 0;
            // Iterate through the minefield to find the randomPositionth free tile, then mine it
            main: for(int i = 0; i < minefield.length; i++) {
                for(int j = 0; j < minefield[0].length; j++) {
                    // We only care about free tiles
                    if(!minefield[i][j].isMined()) {
                        if(currentPosition < randomPosition) {
                            currentPosition++;
                        } else {
                            if(mineTile(i, j)) {
                                // Continue the while-loop
                                break main;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns a string consisting of the entire minefield. Mined tiles will be represented by "*", free tiles will be
     * represented by the amount of neighbouring tiles that are mined. Rows are seperated by a newline.
     * See: https://github.com/Aracthor/ascii-minesweeper for the visual inspiration for this output. Note that I didn't
     * Look at any of the code, only the screenshot in the README.md file.
     *
     * @return String consisting of the entire minefield.
     */
    @Override
    public String toString() {
        String output = "#";
        for(int i = 0; i < minefield[0].length * 2 - 1; i++) {
            output += "-";
        }
        output += "#\n";
        for(int i = 0; i < minefield.length; i++) {
            output += "|";
            for(int j = 0; j < minefield[0].length; j++) {
                // If there's a mine, output "*"
                output += minefield[i][j].toString();
                output += "|";
            }
            output += "\n";
            if(i < minefield.length - 1) {
                output += "|";
                for(int j = 0; j < minefield[0].length - 1; j++) {
                    output += "-+";
                }
                output += "-|\n";
            }
        }
        output += "#";
        for(int i = 0; i < minefield[0].length * 2 - 1; i++) {
            output += "-";
        }
        output += "#";
        return output;
    }

    /**
     * @return the 2d boolean array of mines
     */
    public MineTile[][] getMinefield() {
        return minefield;
    }

    /**
     *
     * @return the int representing the current amount of mines
     */
    public int getCurrentMines() {
        return currentMines;
    }

    /**
     *
     * @return the int representing the maximum amount of mines passed by the constructor
     */
    public int getMaxMines() {
        return maxMines;
    }

    public boolean getLost() {
        return gameOver;
    }
}
