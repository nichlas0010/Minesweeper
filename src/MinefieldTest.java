import static org.junit.Assert.*;
import org.junit.Test;

public class MinefieldTest {

    // Simple existence check for the Minefield constructor, checks that the minefields are the right size.
    @Test
    public void checkConstructor() {
        // Iterate through every combination between 1x1 and 10x10
        for(int rows = 1; rows <= 10; rows++) {
            for(int columns = 1; columns <= 10; columns++) {
                // Generate a minefield of the designated size. Mines set to 0, since that's not what we're checking
                Minefield M = new Minefield(rows, columns, 0);
                boolean[][] mineField = M.getMinefield();
                int[][] minedNeighbours = M.getMinedNeighbours();
                // Then make sure they're the correct size
                assertEquals(rows*columns, mineField.length*mineField[0].length);
                assertEquals(rows*columns, minedNeighbours.length*minedNeighbours[0].length);
            }
        }

    }

    // Check for populate()
    @Test
    public void checkpopulate() {
        // Iterate through every combination between 1x1, and 10x10, and give them rows*columns-1 mines
        // This is so we keep (0,0) clear.
        for(int rows = 1; rows <= 10; rows++) {
            for(int columns = 1; columns <= 10; columns++) {
                for(int mines = 0; mines <= rows*columns-1; mines++) {
                    // Create our minefield
                    Minefield M = new Minefield(rows, columns, mines);
                    // Check that we have the correct maximum of mines
                    assertEquals(mines, M.getMaxMines());
                    // Populate the minefield
                    M.populate();
                    // Check that we have the correct amount of mines
                    assertEquals(mines, M.getCurrentMines());
                    boolean[][] minefield = M.getMinefield();
                    int[][] minedNeighbours = M.getMinedNeighbours();
                    // Loop through the entire minefield
                    for(int row = 0; row < minefield.length; row++) {
                        for(int column = 0; column < minefield[0].length; column++) {
                            int mineCount = 0;
                            // For every tile, loop through the 9 neighbouring tiles (including itself)
                            for(int neighbourRow = row-1; neighbourRow <= row+1; neighbourRow++) {
                                for(int neighbourColumn = column-1; neighbourColumn <= column+1; neighbourColumn++) {
                                    // If it's within bounds, check if there's a mine
                                    if(neighbourRow >= 0 && neighbourRow < minefield.length && neighbourColumn >= 0 && neighbourColumn < minefield[0].length) {
                                        if(minefield[neighbourRow][neighbourColumn]) {
                                            // If there is, iterate mineCount
                                            mineCount++;
                                        }
                                    }
                                }
                            }
                            // Check that the minedNeighbours array has the correct count of neighbouring mines
                            assertEquals(minedNeighbours[row][column], mineCount);
                        }
                    }
                }
            }
        }
    }
    
    // Check for mineTile()
    @Test
    public void checkMineTile() {
        // Create our minefield, a 2x2 minefield with a max of 2 mines
        Minefield M = new Minefield(2, 2, 2);
        // Place a mine in an empty space, should return true
        assertTrue(M.mineTile(1, 0));
        // Place a mine in a full space, should return false since it's already filled
        assertFalse(M.mineTile(1, 0));
        // Place a second mine in an empty space, should return true
        assertTrue(M.mineTile(1, 1));
        // Place a third mine in an empty space, should return false since we're exceeding the maximum mines
        assertFalse(M.mineTile(0, 1));
    }

    // Check for toString()
    // This check is two-pronged.
    // First we do a general check, checking that it outputs the correct amount of mines for every instance of 10x10.
    // Second, we do a more specific check, checking that specific instances are correct.
    @Test
    public void checktoString() {
        // First the general check
        // Iterate through all the combinations between 1x1, and 10x10, with a maximum of rows*columns-1 mines.
        for(int rows = 1; rows <= 10; rows++) {
            for(int columns = 1; columns <= 10; columns++) {
                for(int mines = 1; mines <= rows*columns-1; mines++) {
                    // Create the minefield
                    Minefield M = new Minefield(rows, columns, mines);
                    // Populate it
                    M.populate();
                    // And get the output
                    String toStringOutput = M.toString();
                    // If you want to see the output in the terminal, uncomment the following line
                    //System.out.println(toStringOutput);
                    int minesFound = 0;
                    // Loop through all the characters of the output
                    for(int m = 0; m < toStringOutput.length(); m++) {
                        // Check if we found a mine
                        if(toStringOutput.charAt(m) == '*') {
                            // If we did, increment minesFound
                            minesFound++;
                        }
                    }
                    // Make sure we found the right amount of mines
                    assertEquals(mines, minesFound);
                }
            }
        }
        // Then some more specific checks
        // 3x3, bomb in bottom right
        Minefield M = new Minefield(3, 3, 1);
        assertTrue(M.mineTile(2, 2));
        assertEquals("000\n011\n01*\n", M.toString());

        // 2x2, bombs in top right and bottom left
        M = new Minefield(2, 2, 2);
        assertTrue(M.mineTile(1, 1));
        assertTrue( M.mineTile(0, 0));
        assertEquals("*2\n2*\n", M.toString());

        //3x1, bomb in the middle
        M = new Minefield(3, 1, 1);
        assertTrue(M.mineTile(1, 0));
        assertEquals("1\n*\n1\n", M.toString());
    }
}