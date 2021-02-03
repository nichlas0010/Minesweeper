package Tests;
import Minesweeper.MineTile;
import Minesweeper.Minefield;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class MinefieldTests {

    @Test
    public void testOutOfBounds() {
        Minefield M = new Minefield(1, 1, 0);
        assertFalse(M.step(-1, -1));
        assertFalse(M.step(1, 1));
    }

    @Test
    public void testAlreadyRevealed() {
        Minefield M = new Minefield(2, 2, 2);
        M.populate();
        assertTrue(M.step(0, 0));
        assertFalse(M.step(0, 0));
    }

    @Test
    public void testAlreadyMarked() {
        Minefield M = new Minefield(1, 1, 0);
        M.mark(0, 0);
        assertFalse(M.step(0, 0));
    }

    @Test
    public void testMined() {
        Minefield M = new Minefield(2, 2, 2);
        M.mineTile(0, 0);
        assertFalse(M.step(0, 0));
        assertTrue(M.getLost());
    }

    @Test
    public void revealEmptyBoard() {
        Minefield M = new Minefield(2, 2, 1);
        assertTrue(M.step(0, 0));
        assertTrue(M.getLost());
        MineTile[][] field = M.getMinefield();
        for(MineTile[] row : field) {
            for(MineTile tile : row) {
                assertTrue(tile.isRevealed());
            }
        }
    }

    @Test
    public void testFullGame() {
        Minefield M = new Minefield(4, 4, 6);
        long seed = 123456789L;
        M.setSeed(seed);
        M.populate();
        assertTrue(M.step(0, 0));
        assertTrue(M.step(3, 3));
        assertTrue(M.step(1, 1));
        assertTrue(M.step(2, 2));
        M.mark(3, 2);
        M.mark(2, 3);
        assertTrue(M.step(0, 3));
        assertTrue(M.step(3, 0));
        assertTrue(M.step(3, 1));
        assertTrue(M.step(2, 1));
        M.mark(2, 0);
        M.mark(1, 2);
        M.mark(1, 3);
        assertTrue(M.step(0, 2));
        assertTrue(M.step(1, 0));
        M.mark(0, 1);
        assertTrue(M.getLost());
        assertTrue(M.areAllMinesRevealed());
    }

}
