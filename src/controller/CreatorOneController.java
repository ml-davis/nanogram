package controller;

import helpers.Enums;
import main.Main;
import model.Board;

public class CreatorOneController extends Observer {

    public CreatorOneController(int numberOfRows, int numberOfColumns) {
        super(numberOfRows, numberOfColumns);
    }

    @Override
    public void toggleCell(int column, int row) {

        Board board = Main.getBoard();

        if (board.isFlagged(column, row) && !board.isBlack(column, row)) {
            board.toggleFlag(column, row);
        } else {
            board.toggleFlag(column, row);
            board.toggleBlack(column, row);
        }

        if (board.isFlagged(column, row)) {
            board.setStyle(column, row, Enums.SquareColor.BLACK);
        } else {
            board.setStyle(column, row, Enums.SquareColor.WHITE);
        }

        // print some stuff for debugging purposes
        System.out.println("Column " + (column + 1) + ", Row " + (row + 1));
        System.out.println(board.getSquare(column, row).getStateString());

        // notify everyone subscribed to Observer of changes
        board.notifyObservers();
    }
}
