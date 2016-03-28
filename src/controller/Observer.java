package controller;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Main;
import model.Board;

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
        updateSquares();
        updateColumnIndicators();
        updateRowIndicators();
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
