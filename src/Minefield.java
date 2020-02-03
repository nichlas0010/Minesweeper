import java.util.Random;

public class Minefield {
    // The actual minefield, true is mined, false is not mined
    private boolean[][] minefield;
    // Amount of neighbours that are mined. Name is slightly misleading, since the number includes the tile itself
    // It doesn't really matter though, since it will be represented as a * not the number if there's a mine
    private int[][] minedNeighbours;
    // Maximum amount of mines, mineTile() will not exceed this value
    private int maxMines;
    // Current amount of mines, incremented by mineTile()
    private int currentMines;

    /**
     * Instantiates all instance variables.
     *
     * @param rows amount of rows in the minefield, should be more than 0
     * @param columns amount of columns in the minefield, should be more than 0
     * @param maxMines maximum amount of mines, should not exceed (rows * colums - 1)
     */
    public Minefield(int rows, int columns, int maxMines) {
        if(rows < 1 || columns < 1) {
            System.out.println("ERROR: Minefield must be at least 1x1.");
            return;
        }
        if(maxMines > (rows*columns)-1) {
            System.out.println("ERROR: maxMines cannot be greater than the available amount of squares.");
            return;
        }
        minefield = new boolean[rows][columns];
        minedNeighbours = new int[rows][columns];
        this.maxMines = maxMines;
        currentMines = 0;
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
        // These two if-statements have been split for readibility purposes
        // This check makes sure we're not exceeding the bouns of the minefield, in either direction
        if(row < 0 || row >= minefield.length || column < 0 || column >= minefield[0].length) {
            return false;
        }
        // This check makes sure the tile has not aready been mined, and that we have spare mines.
        if(minefield[row][column] || currentMines >= maxMines) {
            return false;
        }
        minefield[row][column] = true;
        // Iterate through the neighbouring tiles (including our own), and increment each
        for(int i = row-1; i <= row+1; i++) {
            for(int j = column-1; j <= column+1; j++) {
                if(i >= 0 && i < minefield.length && j >= 0 && j < minefield[0].length) {
                    minedNeighbours[i][j]++;
                }
            }
        }
        // Increment currentMines
        currentMines++;
        return true;

    }

    /**
     * Populates the minefield, up to the maxMines passed to the constructor. Doesn't mine (0, 0).
     */
    public void populate() {
        Random random = new Random();
        // Determine the amount of free tiles, excluding (0,0)
        int freeTiles = -1;
        for(int i = 0; i < minefield.length; i++) {
            for(int j = 0; j < minefield.length; j++) {
                if(!minefield[i][j]) {
                    freeTiles++;
                }
            }
        }
        main: while(currentMines < maxMines) {
            // Pick a random position between 0 and our amount of free tiles, then increment by one to avoid (0,0)
            int randomPosition = random.nextInt(freeTiles) + 1;
            freeTiles--;
            int currentPosition = 0;
            // Iterate through the minefield to find the randomPositionth free tile, then mine it
            for(int i = 0; i < minefield.length; i++) {
                for(int j = 0; j < minefield[0].length; j++) {
                    // We only care about free tiles
                    if(!minefield[i][j]) {
                        if(currentPosition < randomPosition) {
                            h++;
                        } else {
                            if(mineTile(i, j)) {
                                // Continue the while-loop
                                continue main;
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
     *
     * @return String consisting of the entire minefield.
     */
    public String toString() {
        String output = "";
        for(int i = 0; i < minefield.length; i++) {
            for(int j = 0; j < minefield[0].length; j++) {
                if(minefield[i][j]) {
                    output += "*";
                }
                else {
                    output += minedNeighbours[i][j];
                }
            }
            output += "\n";
        }
        return output;
    }

    /**
     * @return the 2d boolean array of mines
     */
    public boolean[][] getMinefield() {
        return minefield;
    }

    /**
     * @return the 2d int array of mined neighbours
     */
    public int[][] getMinedNeighbours() {
        return minedNeighbours;
    }

    public int getCurrentMines() {
        return currentMines;
    }

    public int getMaxMines() {
        return maxMines;
    }
}
