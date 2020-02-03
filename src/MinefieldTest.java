import static org.junit.Assert.*;
import org.junit.Test;

public class MinefieldTest {

    // Simple existence check for the Minefield constructor, checks that the minefields are the right size.
    @Test
    public void checkMinefieldSize() {
        for(int i = 1; i <= 10; i++) {
            for(int j = 1; j <= 10; j++) {
            Minefield M = new Minefield(i, j, 5);
            boolean[][] mineField = M.getMinefield();
            assertEquals(i*j, mineField.length*mineField[0].length);
            int[][] minedNeighbours = M.getMinedNeighbours();
            assertEquals(i*j, minedNeighbours.length*minedNeighbours[0].length);
            assertEquals(5, M.getMaxMines());
            }
        }

    }

    // Check for populate()
    @Test
    public void checkpopulate() {
        for(int i = 1; i <= 10; i++) {
            for(int j = 1; j <= 10; j++) {
                for(int k = 1; k <= i*j-1; k++) {
                    Minefield M = new Minefield(i, j, k);
                    M.populate();
                    assertEquals(k, M.getCurrentMines());
                    boolean[][] minefield = M.getMinefield();
                    int[][] minedNeighbours = M.getMinedNeighbours();
                    for(int l = 0; l <= minefield.length; l++) {
                        for(int m = 0; m <= minefield[0].length; m++) {
                            int q = 0;
                            for(int o = l-1; o <= l+1; o++) {
                                for(int p = m-1; p <= m+1; p++) {
                                    if(i >= 0 && i < minefield.length && j >= 0 && j < minefield[0].length) {
                                        if(minefield[o][p]) {
                                            q++;
                                        }
                                    }
                                }
                            }
                            assertEquals(minedNeighbours[l][m], q);
                        }
                    }
                }
            }
        }
    }
    
    // Check for mineTile()
    @Test
    public void checkMineTile() {
        Minefield M = new Minefield(2, 2, 2);
        assertEquals(true, M.mineTile(1, 0));
        assertEquals(false, M.mineTile(1, 0));
        assertEquals(true, M.mineTile(1, 1));
        assertEquals(false, M.mineTile(0, 1));
    }

    // Check for toString()
    @Test
    public void checkpopulate() {
        for(int i = 1; i <= 10; i++) {
            for(int j = 1; j <= 10; j++) {
                for(int k = 1; k <= i*j-1; k++) {
                    Minefield M = new Minefield(i, j, k);
                    M.populate();
                    String toStringOutput = M.toString();
                    int l = 0;
                    for(int m = 0; m < toStringOutput.length(); m++) {
                        if(toStringOutput.charAt(m) == '*') {
                            l++;
                        }
                    }
                    assertEquals(k, l);
                }
            }
        }
    }
}