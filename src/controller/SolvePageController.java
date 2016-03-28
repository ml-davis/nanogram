package controller;

//import javafx.animation.AnimationTimer;
//import javafx.application.Platform;
//import javafx.scene.Node;
//import java.util.ArrayList;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
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
        PageLoader.launchPromptWindow("Under construction");
    }

    public void columnButtonClicked(int columnNumber) {

        PageLoader.launchPromptWindow("Under construction");

//        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        int numberOfRows = Main.getBoard().getNumberOfRows();
//        ArrayList<ArrayList<Boolean>> validCombos = Main.getBoard().getValidRowCombos(columnNumber);
//
//        scheduler.scheduleAtFixedRate(new Runnable() {
//            int counter = 0;
//            boolean clear = true;
//
//            @Override
//            public void run() {
//                if (counter < validCombos.size() && clear) {
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (counter < validCombos.size()) {
//                                ArrayList<Boolean> combos = validCombos.get(counter++);
//                                for (int j = 0; j < Main.getBoard().getNumberOfColumns(); j++) {
//                                    if (combos.get(j) && !Main.getBoard().isBlack(columnNumber, j) && !Main.getBoard().isUserSelected(columnNumber, j)) {
//                                        Main.getBoard().setStyle(columnNumber, j, Enums.SquareColor.LIGHT_GREY);
//                                    }
//                                }
//                                Main.getBoard().notifyObservers();
//                            }
//                            clear = false;
//                        }
//                    });
//                } else if (counter <= validCombos.size() && !clear) {
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int j = 0; j < Main.getBoard().getNumberOfColumns(); j++) {
//                                if (!Main.getBoard().isBlack(columnNumber, j) && !Main.getBoard().isUserSelected(columnNumber, j)) {
//                                    Main.getBoard().setStyle(columnNumber, j, Enums.SquareColor.WHITE);
//                                }
//                            }
//                            Main.getBoard().notifyObservers();
//                            clear = true;
//                        }
//                    });
//                } else {
//                    scheduler.shutdown();
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("Ending animation");
//                        }
//                    });
//                }
//            }
//        }, 1, 500, TimeUnit.MILLISECONDS);
    }
}
