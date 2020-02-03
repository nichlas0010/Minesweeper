import java.util.Random;

public class Minefield {
    private boolean[][] minefield;
    private int[][] minedNeighbours;
    private int maxMines;
    private int currentMines;

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
        currentMines++;
        return true;

    }

    public void populate() {
        Random random = new Random();
        int minefieldSize = minefield.length*minefield[0].length - 1;
        while(currentMines < maxMines) {
            int randomPosition = random.nextInt(minefieldSize) + 1;
            minefieldSize--;
            int h = 0;
            main: for(int i = 0; i < minefield.length; i++) {
                for(int j = 0; j < minefield[0].length; j++) {
                    if(!minefield[i][j]) {
                        if(h < randomPosition) {
                            h++;
                        } else {
                            if(mineTile(i, j)) {
                                break main;
                            }
                        }
                    }
                }
            }
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

    public boolean[][] getMinefield() {
        return minefield;
    }

    public int[][] getMinedNeighbours() {
        return minedNeighbours;
    }
}
