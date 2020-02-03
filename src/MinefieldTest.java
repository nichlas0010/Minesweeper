import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;
import java.util.stream.IntStream;

public class MinefieldTest {

    // Simple existence check for the Minefield constructor, checks that the minefields are the right size.
    @Test
    public void checkMinefieldSize() {
        for(int i = 1; i <= 10; i++) {
            for(int j = 1; j <= 10; j++) {
            Minefield M = new Minefield(i, j, 0);
            boolean[][] mineField = M.getMinefield();
            assertEquals(i*j, mineField.length*mineField[0].length);
            int[][] minedNeighbours = M.getMinedNeighbours();
            assertEquals(i*j, minedNeighbours.length*minedNeighbours[0].length);
            }
        }

    }

    // Check for populate() and toString()
    @Test
    public void checkAmountOfMines() {
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
    
    // Check for mineTile()
    @Test
    public void checkMineTile() {
        Minefield M = new Minefield(2, 2, 2);
        assertEquals(true, M.mineTile(1, 0));
        assertEquals(false, M.mineTile(1, 0));
        assertEquals(true, M.mineTile(1, 1));
        assertEquals(false, M.mineTile(0, 1));
    }
}