package controller;

import helpers.Enums;
import helpers.LetterMapper;
import helpers.PageMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;

import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.Board;

public class PageLoader {
    @FXML
    public GridPane boardPane;
    @FXML
    private MenuItem createFive, createThirteen, createTwenty, solveFive, solveThirteen, solveTwenty;
    @FXML
    private MenuItem createCustom; // TODO implement this

    // navigates to page and returns it's object
    public AnchorPane navigateToPage(Enums.Page page) {
        PageMapper mapper = new PageMapper();
        AnchorPane loadPage = null;
        try {
            URL pageUrl = getClass().getResource(mapper.getPage(page));
            loadPage = FXMLLoader.load(pageUrl);
            BorderPane root = Main.getRoot();
            root.setCenter(loadPage);

        } catch (IOException e) {
            System.out.println("Error navigating to " + page.toString());
        }
        return loadPage;
    }

    @FXML
    public void loadFrontPage() {
        AnchorPane frontPage = navigateToPage(Enums.Page.FRONT_PAGE);
    }

    @FXML
    public void loadCreatorPageOne(ActionEvent event) {
        AnchorPane creatorPageOne = navigateToPage(Enums.Page.CREATOR_PAGE_ONE);
        int boardSize = getBoardSize(event);
        CreatorOneController boardPane = new CreatorOneController(boardSize, boardSize);
        if (boardSize > 0) {
            Board board = new Board(boardSize, boardSize);
            createBoard(creatorPageOne, board, boardPane);
        }
    }

    @FXML
    public void loadCreatorPageTwo() {
        AnchorPane creatorPageTwo = navigateToPage(Enums.Page.CREATOR_PAGE_TWO);
        int numberOfRows = Main.getBoard().getNumberOfRows();
        int numberOfColumns = Main.getBoard().getNumberOfColumns();
        CreatorTwoController boardPane = new CreatorTwoController(numberOfRows, numberOfColumns);
        createBoard(creatorPageTwo, Main.getBoard(), boardPane);
        Main.getBoard().notifyObservers();
    }

    @FXML
    public void loadSolvePage(ActionEvent event) {
        AnchorPane solvePage = navigateToPage(Enums.Page.SOLVE_PAGE);
        int boardSize = getBoardSize(event);
        SolvePageController controller = new SolvePageController(boardSize, boardSize);
        if (boardSize > 0) {
            Board board = new Board(boardSize, boardSize);
            board = board.randomFillBoard();
            createBoard(solvePage, board, controller);
            Main.getBoard().notifyObservers();
            int cellSize = getCellSize(max(board.getNumberOfRows(), board.getNumberOfColumns()));
            GridPane grid = (GridPane) solvePage.lookup("#boardPane");
            addRowLabels(boardSize, grid, cellSize);
            addColumnLabels(boardSize, grid, cellSize);
        }

    }

    public void addRowLabels(int numberOfRows, GridPane grid, int cellSize) {
        for (int i = 0; i < numberOfRows; i++) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            char rowLetter = LetterMapper.mapToLetter(i + 1);
            Button rowButton = new Button(Character.toString(rowLetter));
            rowButton.setId("lineLabel");
            rowButton.setPrefSize(cellSize * .6, cellSize);
            hBox.getChildren().add(rowButton);
            grid.add(hBox, numberOfRows + 1, i + 1);
        }
    }

    public void addColumnLabels(int numberOfColumns, GridPane grid, int cellSize) {
        SolvePageController controller = new SolvePageController(numberOfColumns, numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            char rowLetter = LetterMapper.mapToLetter(i + 1);
            Button columnButton = new Button(Character.toString(rowLetter));
            columnButton.setId("lineLabel");
            columnButton.setPrefSize(cellSize, cellSize * .6);
            final int finalI = i;
            columnButton.setOnAction(e -> {
                controller.columnButtonClicked(finalI);
            });
            vBox.getChildren().add(columnButton);
            grid.add(vBox, i + 1, numberOfColumns + 1);
        }
    }

    @FXML
    public void verifyPuzzle() {
        SolvePageController controller = new SolvePageController(Main.getBoard().getNumberOfRows(),
                Main.getBoard().getNumberOfColumns());
        controller.verifyPuzzle();
    }

    @FXML
    public void getHint() {
        SolvePageController controller = new SolvePageController(Main.getBoard().getNumberOfRows(),
                Main.getBoard().getNumberOfColumns());
        controller.getHint();
    }

    public static void launchPromptWindow(String message) {
        Stage stage = new Stage();
        VBox window = new VBox();
        window.setPadding(new Insets(25, 25, 25, 25));
        window.setSpacing(25);
        window.setAlignment(Pos.CENTER);

        Label label = new Label(message);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> stage.close());

        window.getChildren().addAll(label, okButton);

        int width = 400, height = 200;
        Scene scene = new Scene(window, width, height);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void createBoard(AnchorPane page, Board board, Observer boardPane) {
        board.attach(boardPane);
        GridPane grid = (GridPane) page.lookup("#boardPane");

        int numberOfRows = board.getNumberOfRows();
        int numberOfColumns = board.getNumberOfColumns();
        int cellSize = getCellSize(max(numberOfRows, numberOfColumns));

        // add squares
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                final int finalI = i, finalJ = j;
                Button square = new Button("");
                square.setStyle(board.getStyle(i, j));
                square.setPadding(new Insets(0, 0, 0, 0));
                square.setPrefSize(cellSize, cellSize);
                square.setId(i + "" + j);
                square.setOnAction(e -> boardPane.toggleCell(finalI, finalJ));
                grid.add(square, i+1, j+1);
                boardPane.setSquare(square, i, j);
            }
        }

        addColumnIndicators(numberOfColumns, grid, boardPane);
        addRowIndicators(numberOfRows, grid, boardPane);

        Main.setBoard(board);
    }

    private void addRowIndicators(int numberOfRows, GridPane grid, Observer boardPane) {
        HBox[] hBox = new HBox[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            hBox[i] = new HBox();
            hBox[i].setAlignment(Pos.CENTER_RIGHT);
            hBox[i].setPrefWidth(130);
            grid.add(hBox[i], 0, i + 1);
        }
        boardPane.setRowIndicators(hBox);
    }

    private void addColumnIndicators(int numberOfColumns, GridPane grid, Observer boardPane) {
        VBox[] vBox = new VBox[numberOfColumns];
        for (int i = 0; i < numberOfColumns; i++) {
            vBox[i] = new VBox();
            vBox[i].setAlignment(Pos.BOTTOM_CENTER);
            vBox[i].setPrefHeight(130);
            grid.add(vBox[i], i+1, 0);
        }
        boardPane.setColumnIndicators(vBox);
    }

    private int getCellSize(int largestLine) {
        return (int) ((-.7)*largestLine + 30);
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private int getBoardSize(ActionEvent event) {
        if (event.getSource() == createFive || event.getSource() == solveFive) {
            System.out.println("Creating 5x5\n");
            return 5;
        } else if (event.getSource() == createThirteen || event.getSource() == solveThirteen) {
            System.out.println("Creating 13x13\n");
            return 13;
        } else if (event.getSource() == createTwenty || event.getSource() == solveTwenty) {
            System.out.println("Creating 20x20\n");
            return 20;
        } else if (event.getSource() == createCustom) {
            System.out.println("Opening custom board window\n");
            // TODO implement custom board window
            return -1;
        } else {
            System.out.println("Error determining board size.");
            System.out.println("Event source = " + event.getSource());
            System.out.println("Check controller.PageLoader.loadCreatorPageOne()");
            return -1;
        }
    }
}
