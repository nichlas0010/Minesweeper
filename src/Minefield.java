import java.util.Random;

public class Minefield {
    boolean[][] minefield;
    int[][] minedNeighbours;
    int maxMines;
    int currentMines;

    public void Minefield(int rows, int columns, int maxMines) {
        minefield = new boolean[rows][columns];
        minedNeighbours = new int[rows][columns];
        this.maxMines = maxMines;
        currentMines = 0;
        populate();
    }

    public boolean mineTile(int row, int column) {
        if(minefield[row][column] || currentMines >= maxMines) {
            return false;
        }
        minefield[row][column] = true;
        for(int i = row-1; i <= row+1; i++) {
            for(int j = column-1; j <= column+1; j++) {
                if(i >= 0 && i < minefield.length && j >= 0 && j < minefield[0].length) {
                    minedNeighbours[i][j]++;
                }
            }
        }

    }

    public void populate() {
        Random random = new Random();
        while(currentMines < maxMines) {
            int randomRow = random.nextInt(minefield.length);
            int randomColumn = random.nextInt(minefield[0].length); // Taking the value from the 1st array, since all arrays have the same length
            // This test is here, rather than in mineTile, since the design document stated populate should exempt (0,0), but not that mineTile should.
            if(randomRow == 0 && randomColumn == 0) {
                continue;
            }
            mineTile(randomRow, randomColumn);
        }
    }

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
}
