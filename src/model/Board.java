package model;

import helpers.Enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Board extends Observable implements Serializable {
    private int numberOfRows;
    private int numberOfColumns;
    private Square[][] board;
    private HashMap<Integer, ArrayList<Integer>> rowIndicator;
    private HashMap<Integer, ArrayList<Integer>> columnIndicator;

    public Board(int numberOfRows, int numberOfColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        board = new Square[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                board[i][j] = new Square();
            }
        }

        rowIndicator = new HashMap<>();
        for (int i = 0; i < numberOfRows; i++) {
            rowIndicator.put(i, new ArrayList<>(numberOfColumns));
        }
        columnIndicator = new HashMap<>();
        for (int i = 0; i < numberOfColumns; i++) {
            columnIndicator.put(i, new ArrayList<>(numberOfRows));
        }
    }

    public void toggleFlag(int row, int column) {
        board[row][column].toggleFlag();
        this.rowIndicator.put(column, createIndicatorList(getColumn(column)));
        this.columnIndicator.put(row, createIndicatorList(getRow(row)));
    }

    public void toggleBlack(int row, int column) {
        board[row][column].toggleBlack();
    }

    public void toggleUserSelected(int row, int column) { board[row][column].toggleUserSelected(); }

    public ArrayList<Integer> createIndicatorList(Square[] line) {
        ArrayList<Integer> list = new ArrayList<>();
        int count = 0;
        for (Square square : line) {
            if (square.isFlagged()) {
                count++;
            } else if (count > 0){
                if (count > 0) {
                    list.add(count);
                }
                count = 0;
            }
        }
        if (count > 0)
            list.add(count);
        return list;
    }

    public ArrayList<Integer> createUserIndicatorList(Square[] line) {
        ArrayList<Integer> list = new ArrayList<>();
        int count = 0;
        for (Square square : line) {
            if (square.isBlack() || square.isUserSelected()) {
                count++;
            } else if (count > 0){
                if (count > 0) {
                    list.add(count);
                }
                count = 0;
            }
        }
        if (count > 0)
            list.add(count);
        return list;
    }

    public int getNumberSelectedInColumn(int row) {
        int sum = 0;
        for (int i = 0; i < numberOfColumns; i++) {
            if (isBlack(i, row) || isUserSelected(i, row)) {
                sum++;
            }
        }
        return sum;
    }

    public int getNumberSelectedInRow(int column) {
        int sum = 0;
        for (int i = 0; i < numberOfRows; i++) {
            if (isBlack(column, i) || isUserSelected(column, i)) {
                sum++;
            }
        }
        return sum;
    }

    public int getSumOfRowIndicator(int row) {
        int sum = 0;
        ArrayList<Integer> indicator = getRowIndicator(row);
        for (int i : indicator) {
            sum += i;
        }
        return sum;
    }

    public int getSumOfColumnIndicator(int column) {
        int sum = 0;
        ArrayList<Integer> indicator = getColumnIndicator(column);
        for (int i : indicator) {
            sum += i;
        }
        return sum;
    }

    public boolean isFlagged(int row, int column) {
        return board[row][column].isFlagged();
    }

    public boolean isBlack(int row, int column) {
        return board[row][column].isBlack();
    }

    public boolean isUserSelected(int row, int column) { return board[row][column].isUserSelected(); }

    public void setStyle(int row, int column, Enums.SquareColor color) {
        board[row][column].setStyle(color);
    }

    public String getStyle(int row, int column) { return board[row][column].getStyle(); }

    public HashMap<Integer, ArrayList<Integer>> getRowIndicatorMap() {
        return rowIndicator;
    }

    public HashMap<Integer, ArrayList<Integer>> getColumnIndicatorMap() {
        return columnIndicator;
    }

    public ArrayList<Integer> getRowIndicator(int row) {
        return rowIndicator.get(row);
    }

    public ArrayList<Integer> getColumnIndicator(int column) {
        return columnIndicator.get(column);
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public Square[] getRow(int row) {
        return board[row];
    }

    public Square[] getColumn(int column) {
        Square[] col = new Square[board.length];
        for (int i = 0; i < board.length; i++) {
            col[i] = board[i][column];
        }
        return col;
    }

    public Square getSquare(int row, int column) {
        return board[row][column];
    }

}
