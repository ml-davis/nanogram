package controller;

import helpers.Enums;
import main.Main;
import model.Board;

import java.util.ArrayList;

public class SolvePageController extends Observer {

    public SolvePageController(int numberOfRows, int numberOfColumns) {
        super(numberOfRows, numberOfColumns);
	}

    @Override
    public void toggleCell(int column, int row) {

        // update back end
        Board board = Main.getBoard();
        if (!board.isBlack(column, row) && !board.isUserSelected(column, row)) {
            board.toggleUserSelected(column, row);
            board.setStyle(column, row, Enums.SquareColor.DARK_GREY);

        } else if (!board.isBlack(column, row) && board.isUserSelected(column, row)) {
            board.toggleUserSelected(column, row);
            board.setStyle(column, row, Enums.SquareColor.WHITE);
        }

        // print some stuff for debugging purposes
        System.out.println("Column " + (column + 1) + ", Row " + (row + 1));
        System.out.println(board.getSquare(column, row).getStateString());

        board.notifyObservers();
    }

    public void verifyPuzzle() {
        Board board = Main.getBoard();
        String message;
        if (board.isSolved()) {
            message = "Congratulations! You solved the puzzle!";
        } else {
          message = board.getErrorMessage();
        }
        PageLoader.launchPromptWindow(message);
    }

    public void rowButtonClicked(int rowNumber) {
		Board board = Main.getBoard();
		ArrayList<ArrayList<Boolean>> possibleSolutions = board.getValidSolutions(rowNumber, true);
		lineButtonMessage(board.isRowSolved(rowNumber), possibleSolutions);
	}

	public void columnButtonClicked(int columnNumber) {
		Board board = Main.getBoard();
		ArrayList<ArrayList<Boolean>> possibleSolutions = board.getValidSolutions(columnNumber, false);
		lineButtonMessage(board.isColumnSolved(columnNumber), possibleSolutions);
    }

	private void lineButtonMessage(boolean isSolved, ArrayList<ArrayList<Boolean>> solutions) {

		if (isSolved) {
			PageLoader.launchPromptWindow("This line is solved");
		} else if (solutions.size() == 1) {
			PageLoader.launchPromptWindow("There is only one possible solution for this line");
		} else {
			String message1 = "There are " + solutions.size() + " possible solutions for this line\n";
			String message2 = "";
			for (ArrayList<Boolean> solution : solutions) {
				for (Boolean item : solution) {
					message2 += (item) ? "X" : "-";
				}
				message2 += "\n";
			}
			PageLoader.launchLineWindow(message1, message2);
		}

	}

	public void getHint() {
    	Board board = Main.getBoard();
	}
}
