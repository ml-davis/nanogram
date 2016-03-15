package controller;

import helpers.Enums;
import main.Main;

public class CreatorTwoController extends Observer {

    public CreatorTwoController(int numberOfRows, int numberOfColumns) {
        super(numberOfRows, numberOfColumns);
    }

    @Override
    public void toggleCell(int row, int column) {
        // update backend
        if (Main.getBoard().isBlack(row, column)) {
            Main.getBoard().toggleBlack(row, column);
            Main.getBoard().setStyle(row, column, Enums.SquareColor.LIGHT_GREY);
        } else if (Main.getBoard().isFlagged(row, column) && !Main.getBoard().isBlack(row, column)) {
            Main.getBoard().toggleBlack(row, column);
            Main.getBoard().setStyle(row, column, Enums.SquareColor.BLACK);
        }

        // print some stuff for debugging purposes
        System.out.println("Row " + (column+1) + ", Column " + (row+1));
        System.out.println(Main.getBoard().getSquare(row, column).getStateString());

        // tell backend to notify all observers
        Main.getBoard().notifyObservers();
    }
}
