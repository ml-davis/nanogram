package controller;

import helpers.LetterMapper;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import helpers.Enums;
import main.Main;
import model.Board;
import model.Square;


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

        updateColumnsSolved();
        updateRowsSolved();
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

    public void getHint() {
        String hint = Main.getBoard().getHint();
        System.out.println(hint + "\n");
        PageLoader.launchPromptWindow(hint);
    }

    public void rowButtonClicked(int rowNumber) {
        Board board = Main.getBoard();

        if (board.isRowSolved(rowNumber)) {
            PageLoader.launchPromptWindow("That row is solved");
            return;
        }

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ArrayList<ArrayList<Boolean>> validCombos = board.getValidLineCombinations(board.getRow(rowNumber));

        scheduler.scheduleAtFixedRate(new Runnable() {
            int counter = 0;
            boolean clear = true;

            @Override
            public void run() {
                if (counter < validCombos.size() && clear) {
                    Platform.runLater(() -> {
                        if (counter < validCombos.size()) {
                            ArrayList<Boolean> combos = validCombos.get(counter++);
                            for (int j = 0; j < board.getNumberOfRows(); j++) {
                                if (combos.get(j) && !board.isBlack(j, rowNumber) && !board.isUserSelected(j, rowNumber)) {
                                    board.setStyle(j, rowNumber, Enums.SquareColor.LIGHT_GREY);
                                }
                            }
                            board.notifyObservers();
                        }
                        clear = false;
                    });
                } else if (counter <= validCombos.size() && !clear) {
                    Platform.runLater(() -> {
                        for (int j = 0; j < board.getNumberOfRows(); j++) {
                            if (!board.isBlack(j, rowNumber) && !board.isUserSelected(j, rowNumber)) {
                                if (!board.isColumnSolved(j)) {
                                    board.setStyle(j, rowNumber, Enums.SquareColor.WHITE);
                                } else {
                                    board.setStyle(j, rowNumber, Enums.SquareColor.LIGHT_GREEN);
                                }

                            }
                        }
                        board.notifyObservers();
                        clear = true;
                    });
                } else {
                    scheduler.shutdown();
                    Platform.runLater(() -> System.out.println("Ending animation"));
                }
            }
        }, 1, 350, TimeUnit.MILLISECONDS);
    }

    public void columnButtonClicked(int columnNumber) {
        Board board = Main.getBoard();

        if (board.isColumnSolved(columnNumber)) {
            PageLoader.launchPromptWindow("That column is solved");
            return;
        }

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ArrayList<ArrayList<Boolean>> validCombos = board.getValidLineCombinations(board.getColumn(columnNumber));

        scheduler.scheduleAtFixedRate(new Runnable() {
            int counter = 0;
            boolean clear = true;

            @Override
            public void run() {
                if (counter < validCombos.size() && clear) {
                    Platform.runLater(() -> {
                        if (counter < validCombos.size()) {
                            ArrayList<Boolean> combos = validCombos.get(counter++);
                            for (int j = 0; j < board.getNumberOfColumns(); j++) {
                                if (combos.get(j) && !board.isBlack(columnNumber, j) && !board.isUserSelected(columnNumber, j)) {
                                    board.setStyle(columnNumber, j, Enums.SquareColor.LIGHT_GREY);
                                }
                            }
                            board.notifyObservers();
                        }
                        clear = false;
                    });
                } else if (counter <= validCombos.size() && !clear) {
                    Platform.runLater(() -> {
                        for (int j = 0; j < board.getNumberOfColumns(); j++) {
                            if (!board.isBlack(columnNumber, j) && !board.isUserSelected(columnNumber, j)) {
                                if (!board.isRowSolved(j)) {
                                    board.setStyle(columnNumber, j, Enums.SquareColor.WHITE);
                                } else {
                                    board.setStyle(columnNumber, j, Enums.SquareColor.LIGHT_GREEN);
                                }
                            }
                        }
                        board.notifyObservers();
                        clear = true;
                    });
                } else {
                    scheduler.shutdown();
                    Platform.runLater(() -> System.out.println("Ending animation"));
                }
            }
        }, 1, 350, TimeUnit.MILLISECONDS);
    }

    public void updateRowsSolved() {
        Board board = Main.getBoard();
        for (int i = 0; i < board.getNumberOfRows(); i++) {
            if (board.isRowSolved(i)) {
                System.out.println("ROW " + LetterMapper.mapToLetter(i + 1) + " IS SOLVED");
                colorRowGreen(i);
            } else {
                colorRow(i);
            }
        }
    }

    public void updateColumnsSolved() {
        Board board = Main.getBoard();
        for (int i = 0; i < board.getNumberOfColumns(); i++) {
            if (board.isColumnSolved(i)) {
                System.out.println("COLUMN " + LetterMapper.mapToLetter(i + 1) + " IS SOLVED");
                colorColumnGreen(i);
            } else {
                colorColumn(i);
            }
        }
    }

    private void colorRowGreen(int row) {
        Board board = Main.getBoard();
        Square[] squares = board.getRow(row);
        colorLineGreen(squares);
    }

    private void colorColumnGreen(int column) {
        Board board = Main.getBoard();
        Square[] squares = board.getColumn(column);
        colorLineGreen(squares);
    }

    private void colorLineGreen(Square[] squares) {
        for (Square square : squares) {
            square.setGreen(true);
            if (square.isBlack() || square.isUserSelected()) {
                square.setStyle(Enums.SquareColor.DARK_GREEN);
            } else {
                square.setStyle(Enums.SquareColor.LIGHT_GREEN);
            }
        }
    }

    private void colorRow(int row) {
        Board board = Main.getBoard();
        for (int i = 0; i < board.getNumberOfColumns(); i++) {
            Square square = board.getSquare(i, row);
            if (!board.isColumnSolved(i)) {
                colorSquare(square);
            }
        }
    }

    private void colorColumn(int column) {
        Board board = Main.getBoard();
        for (int i = 0; i < board.getNumberOfRows(); i++) {
            Square square = board.getSquare(column, i);
            if (!board.isRowSolved(i)) {
                colorSquare(square);
            }
        }
    }

    private void colorSquare(Square square) {
        square.setGreen(false);
        if (square.isBlack()) {
            square.setStyle(Enums.SquareColor.BLACK);
        } else if (square.isUserSelected()) {
            square.setStyle(Enums.SquareColor.DARK_GREY);
        } else {
            square.setStyle(Enums.SquareColor.WHITE);
        }
    }
}
