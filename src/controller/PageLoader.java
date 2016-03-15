package controller;

import helpers.Enums;
import helpers.PageMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;

import main.Main;
import model.Board;

public class PageLoader {
    @FXML
    private MenuItem five;
    @FXML
    private MenuItem thirteen;
    @FXML
    private MenuItem twenty;
    @FXML
    private MenuItem custom; // TODO implement this

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
    public void loadFrontPage(ActionEvent event) {
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
    public void loadSolvePageOne(ActionEvent event) {
        AnchorPane solvePage = navigateToPage(Enums.Page.SOLVE_PAGE);
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

    private void addColumnIndicators(int numberOfColumns, GridPane grid, Observer boardPane) {
        VBox[] vbox = new VBox[numberOfColumns];
        for (int i = 0; i < numberOfColumns; i++) {
            vbox[i] = new VBox();
            vbox[i].setAlignment(Pos.BOTTOM_CENTER);
            vbox[i].setPrefHeight(110);
            grid.add(vbox[i], i+1, 0);
        }
        boardPane.setColumnIndicators(vbox);
    }

    private void addRowIndicators(int numberOfRows, GridPane grid, Observer boardPane) {
        HBox[] hbox = new HBox[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            hbox[i] = new HBox();
            hbox[i].setAlignment(Pos.CENTER_RIGHT);
            hbox[i].setPrefWidth(110);
            grid.add(hbox[i], 0, i + 1);
        }
        boardPane.setRowIndicators(hbox);
    }

    private int getCellSize(int largestLine) {
        return (int) ((-.7)*largestLine + 30);
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private int getBoardSize(ActionEvent event) {
        if (event.getSource() == five) {
            System.out.println("Creating 5x5\n");
            return 5;
        } else if (event.getSource() == thirteen) {
            System.out.println("Creating 13x13\n");
            return 13;
        } else if (event.getSource() == twenty) {
            System.out.println("Creating 20x20\n");
            return 20;
        } else if (event.getSource() == custom) {
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
