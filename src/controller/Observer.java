package controller;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Main;

import java.util.ArrayList;

public abstract class Observer {
    protected Button[][] squares;
    protected VBox[] columnIndicators;
    protected HBox[] rowIndicators;

    public Observer(int numberOfRows, int numberOfColumns) {
        squares = new Button[numberOfRows][numberOfColumns];
        columnIndicators = new VBox[numberOfColumns];
        rowIndicators = new HBox[numberOfRows];
    }

    public void update() {
        updateSquares();
        updateColumnIndicators();
        updateRowIndicators();
    }

    public abstract void toggleCell(int row, int column);

    public void setSquare(Button square, int row, int column) {
        squares[row][column] = square;
    }

    public void setColumnIndicators(VBox[] vbox) {
        columnIndicators = vbox;
    }

    public void setRowIndicators(HBox[] hbox) {
        rowIndicators = hbox;
    }

    private void updateSquares() {
        for (int i = 0; i < Main.getBoard().getNumberOfRows(); i++) {
            for (int j = 0; j < Main.getBoard().getNumberOfColumns(); j++) {
                squares[i][j].setStyle(Main.getBoard().getStyle(i, j));
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
