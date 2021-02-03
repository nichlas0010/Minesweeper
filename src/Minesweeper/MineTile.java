package Minesweeper;

public class MineTile {
    private boolean hasMine;
    private boolean isRevealed;
    private boolean hasFlag;
    private int neighbours;
    private int row;
    private int column;

    public MineTile(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void mine() {
        hasMine = true;
    }

    public void reveal() {
        isRevealed = true;
    }

    public boolean mark() {
        hasFlag = !hasFlag;
        return hasFlag;
    }

    public void increaseNeighbours() {
        neighbours++;
    }

    public boolean isMined() {
        return hasMine;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public String toString() {
        if(!isRevealed) {
            if(hasFlag) {
                return "!";
            }
            return "?";
        }
        if(hasMine) {
            return "X";
        }
        if(neighbours == 0) {
            return " ";
        }
        return Integer.toString(neighbours);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
