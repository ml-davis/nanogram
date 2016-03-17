package controller;

import helpers.Enums;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Node;
import main.Main;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        System.out.println("Row " + (column + 1) + ", Column " + (row + 1));
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

    public void columnButtonClicked(int columnNumber) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        int numberOfRows = Main.getBoard().getNumberOfRows();
        ArrayList<ArrayList<Boolean>> validCombos = Main.getBoard().getValidRowCombos(columnNumber);

        scheduler.scheduleAtFixedRate(new Runnable() {
            int counter = 0;
            boolean clear = true;

            @Override
            public void run() {
                if (counter < validCombos.size() && clear) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (counter < validCombos.size()) {
                                ArrayList<Boolean> combos = validCombos.get(counter++);
                                for (int j = 0; j < Main.getBoard().getNumberOfColumns(); j++) {
                                    if (combos.get(j) && !Main.getBoard().isBlack(columnNumber, j) && !Main.getBoard().isUserSelected(columnNumber, j)) {
                                        Main.getBoard().setStyle(columnNumber, j, Enums.SquareColor.LIGHT_GREY);
                                    }
                                }
                                Main.getBoard().notifyObservers();
                            }
                            clear = false;
                        }
                    });
                } else if (counter <= numberOfRows && !clear) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int j = 0; j < Main.getBoard().getNumberOfColumns(); j++) {
                                if (!Main.getBoard().isBlack(columnNumber, j) && !Main.getBoard().isUserSelected(columnNumber, j)) {
                                    Main.getBoard().setStyle(columnNumber, j, Enums.SquareColor.WHITE);
                                }
                            }
                            Main.getBoard().notifyObservers();
                            clear = true;
                        }
                    });
                } else {
                    scheduler.shutdown();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Ending animation");
                            Main.getBoard().resetColors();
                        }
                    });
                }
            }
        }, 1, 500, TimeUnit.MILLISECONDS);
    }
}
