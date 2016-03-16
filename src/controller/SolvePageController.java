package controller;

import helpers.Enums;
import main.Main;

public class SolvePageController extends Observer {

    public SolvePageController(int numberOfRows, int numberOfColumns) {
        super(numberOfRows, numberOfColumns);
    }

    @Override
    public void toggleCell(int row, int column) {
        // update back end
        if (!Main.getBoard().isBlack(row, column) && !Main.getBoard().isUserSelected(row, column)) {
            Main.getBoard().toggleUserSelected(row, column);
            Main.getBoard().setStyle(row, column, Enums.SquareColor.DARK_GREY);
        } else if (!Main.getBoard().isBlack(row, column) && Main.getBoard().isUserSelected(row, column)) {
            Main.getBoard().toggleUserSelected(row, column);
            Main.getBoard().setStyle(row, column, Enums.SquareColor.WHITE);
        }

        // print some stuff for debugging purposes
        System.out.println("Row " + (column+1) + ", Column " + (row+1));
        System.out.println(Main.getBoard().getSquare(row, column).getStateString());

        Main.getBoard().notifyObservers();
    }


    public void verifyPuzzle() {
        String message;
        if (Main.getBoard().isSolved()) {
            message = "Congratulations! You solved the puzzle!";
        } else {
          message = Main.getBoard().getErrorMessage();
        }
        PageLoader.launchPromptWindow(message);
    }

    public void getHint() {
        String hint = Main.getBoard().getHint();
        System.out.println(hint + "\n");
        PageLoader.launchPromptWindow(hint);
    }
}
