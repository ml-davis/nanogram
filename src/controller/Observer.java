package controller;

import helpers.Enums;
import helpers.LetterMapper;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Main;
import model.Board;
import model.Square;

import java.util.ArrayList;

public abstract class Observer {
    protected Button[][] squares;
    protected VBox[] columnIndicators;
    protected HBox[] rowIndicators;

    public Observer(int numberOfRows, int numberOfColumns) {
        squares = new Button[numberOfRows][numberOfColumns];
        columnIndicators = new VBox[numberOfRows];
        rowIndicators = new HBox[numberOfColumns];
    }

    public void update() {
        updateColumnsSolved();
        updateRowsSolved();
        updateSquares();
        updateColumnIndicators();
        updateRowIndicators();
        System.out.println();
    }

    public void update2() {
        updateSquares();
        updateColumnIndicators();
        updateRowIndicators();
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

    public abstract void toggleCell(int column, int row);

    public void setSquare(Button square, int column, int row) {
        squares[row][column] = square;
    }

    public void setColumnIndicators(VBox[] vBox) {
        columnIndicators = vBox;
    }

    public void setRowIndicators(HBox[] hBox) {
        rowIndicators = hBox;
    }

    private void updateSquares() {
        Board board = Main.getBoard();
        for (int i = 0; i < board.getNumberOfRows(); i++) {
            for (int j = 0; j < board.getNumberOfColumns(); j++) {
                squares[i][j].setStyle(board.getStyle(i, j));
            }
        }
    }

    private void updateColumnIndicators() {
        for (int i = 0; i < columnIndicators.length; i++) {
            columnIndicators[i].getChildren().clear();
            ArrayList<Integer> indicator = Main.getBoard().getColumnIndicator(i);
            for (int j : indicator) {
                Text text = new Text(Integer.toString(j));
                text.setId("indicator");
                columnIndicators[i].getChildren().add(text);
            }
        }
    }

    private void updateRowIndicators() {
        for (int i = 0; i < rowIndicators.length; i++) {
            rowIndicators[i].getChildren().clear();
            ArrayList<Integer> indicator = Main.getBoard().getRowIndicator(i);
            for (int j : indicator) {
                Text text = new Text(Integer.toString(j));
                text.setId("indicator");
                rowIndicators[i].getChildren().add(text);
            }
        }
    }
}
