package Minesweeper;

import uk.ac.sussex.ianw.minesweeper.part2.*;

public class Terminal {
    Parser parser = new Parser();
    Minefield minefield;

    public Terminal() {}

    private void execute(Command c) {

        switch(c.getCommand()) {
            case NEW:
                int row = c.getRow();
                int column = c.getColumn();
                if(row < 1 || column < 1) {
                    break;
                }
                int mines = (row*column)/5;
                minefield = new Minefield(c.getRow() , c.getColumn(), mines);
                minefield.populate();
                minefield.step(0, 0, true);
                minefield.update("New game started");
                break;
            case STEP:
                if(minefield == null || minefield.getLost()) {
                    break;
                }
                minefield.step(c.getRow(), c.getColumn(), false);
                break;
            case MARK:
                if(minefield == null || minefield.getLost()) {
                    break;
                }
                minefield.mark(c.getRow(), c.getColumn());
                break;
            case QUIT:
                System.exit(0);
                break;
            default:
                if(minefield == null) {
                    break;
                }
                minefield.update("Command " + c.getCommand().getWord() + " not recognized");
        }
        printPrompt(c.getMsg());
    }

    private void commandLine() {
        String st = "Commands:\n";
        st += "Quit: quits the game\n";
        st += "Mark [row] [column]: (un)marks the tile at the given co-ordinates\n";
        st += "Step [row] [column]: step onto the tile at the given co-ordinates, triggering or revealing it\n";
        st += "New [row] [column]: start a new game with the specified amount of rows and columns";
        printPrompt(st);
        Command c = parser.getCommand();
        while(c.getCommand() != CommandWord.QUIT) {
            execute(c);
            c = parser.getCommand();
        }
    }
    private void printPrompt(String msg) {
        System.out.println(msg);
        System.out.print(">");
    }

    public static void main(String args[]) {
        Terminal t = new Terminal();
        t.commandLine();
    }
}
