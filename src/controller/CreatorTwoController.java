package controller;

import helpers.Enums;
import main.Main;
import model.Board;

public class CreatorTwoController extends Observer {

    public CreatorTwoController(int numberOfRows, int numberOfColumns) {
        super(numberOfRows, numberOfColumns);
    }

    @Override
    public void toggleCell(int column, int row) {

        // update backend
        Board board = Main.getBoard();
        if (board.isFlagged(column, row) && !board.isBlack(column, row)) {
            board.toggleBlack(column, row);
            board.setStyle(column, row, Enums.SquareColor.BLACK);
        } else if (board.isFlagged(column, row) && board.isBlack(column, row)) {
            board.toggleBlack(column, row);
            board.setStyle(column, row, Enums.SquareColor.LIGHT_GREY);
        }

        // print some stuff for debugging purposes
        System.out.println("Column " + (column + 1) + ", Row " + (row + 1));
        System.out.println(board.getSquare(column, row).getStateString());

        // tell backend to notify all observers
        board.notifyObservers();
    }
}
