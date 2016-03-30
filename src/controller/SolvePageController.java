package controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import helpers.Enums;
import main.Main;
import model.Board;


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

    public void getHint() {
        String hint = Main.getBoard().getHint();
        System.out.println(hint + "\n");
        PageLoader.launchPromptWindow(hint);
    }

    public void rowButtonClicked(int rowNumber) {
        Board board = Main.getBoard();
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
                                board.setStyle(j, rowNumber, Enums.SquareColor.WHITE);
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
                                board.setStyle(columnNumber, j, Enums.SquareColor.WHITE);
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
}
