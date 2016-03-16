package model;

import helpers.Enums;
import helpers.LetterMapper;
import main.Main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public void toggleUserSelected(int row, int column) {
        board[row][column].toggleUserSelected();
    }

    public Board randomFillBoard() {
        Random random = new Random();
        int numberOfRows = this.getNumberOfRows();
        int numberOfColumns = this.getNumberOfColumns();

        System.out.println("rows = " + numberOfRows);
        System.out.println("cols = " + numberOfColumns);

        // get list of numbers which will map to spaces on board
        ArrayList<Integer> list = new ArrayList<>(numberOfRows * numberOfColumns);
        for (int i = 0; i < numberOfRows * numberOfColumns; i++) {
            list.add(i);
        }

        // randomly toggle 20% of board black and flagged, toggle additional 20% only flagged
        int amountOfSpacesBlack = (int) (Math.floor(numberOfRows * numberOfColumns) * 0.2);
        int amountOfSpacesFlagged = (int) (Math.floor(numberOfRows * numberOfColumns) * 0.4);

        int piece, row, column;
        for (int i = 0; i < amountOfSpacesFlagged; i++) {
            piece = list.remove(random.nextInt(list.size())); // remove a random number from list and store it in piece
            System.out.println("piece = " + piece);
            row = (int) Math.floor(piece/numberOfColumns); // map the piece to its corresponding row
            System.out.println("row = " + row);
            column = piece % this.getNumberOfColumns(); // map the piece to its corresponding column
            System.out.println("col = " + column);
            if (i < amountOfSpacesBlack) {
                System.out.println("toggle black");
                this.toggleBlack(row, column);
                this.setStyle(row, column, Enums.SquareColor.BLACK);
            }
            System.out.println("toggle flag");
            this.toggleFlag(row, column);
            System.out.println();
        }

        notifyObservers();

        return this;
    }

    public boolean isSolved() {
        return areColumnsSolved(this) && areRowsSolved(this);
    }

    private boolean areColumnsSolved(Board board) {
        for (int i = 0; i < board.getNumberOfColumns(); i++) {
            ArrayList<Integer> userIndicator = createUserIndicatorList(getRow(i));
            ArrayList<Integer> solutionIndicator = Main.getBoard().getColumnIndicator(i);
            if (!userIndicator.equals(solutionIndicator)) {
                return false;
            }
        }
        return true;
    }

    private boolean areRowsSolved(Board board) {
        for (int i = 0; i < board.getNumberOfRows(); i++) {
            ArrayList<Integer> userIndicator = createUserIndicatorList(getColumn(i));
            ArrayList<Integer> solutionIndicator = Main.getBoard().getRowIndicator(i);
            if (!userIndicator.equals(solutionIndicator)) {
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage() {
        ArrayList<Integer> errorColumns = getErrorColumns(this);
        ArrayList<Integer> errorRows = getErrorRows(this);
        String errorMessage = "There was an error in your solution. Check in\n";

        if (errorRows.size() > 0) {
            errorMessage += "Rows:\t";
        }
        for (Integer errorRow : errorRows) {
            errorMessage += LetterMapper.mapToLetter(errorRow) + " ";
        }
        errorMessage += "\n";

        if (errorColumns.size() > 0) {
            errorMessage += "Columns: \t";
        }
        for (Integer errorColumn : errorColumns) {
            errorMessage += LetterMapper.mapToLetter(errorColumn) + " ";
        }
        errorMessage += "\n";

        return errorMessage;
    }

    private ArrayList<Integer> getErrorColumns(Board board) {
        ArrayList<Integer> errorColumns = new ArrayList<>(board.getNumberOfColumns());
        for (int i = 0; i < board.getNumberOfColumns(); i++) {
            ArrayList<Integer> userIndicator = createUserIndicatorList(getRow(i));
            ArrayList<Integer> solutionIndicator = Main.getBoard().getColumnIndicator(i);
            if (!userIndicator.equals(solutionIndicator)) {
                errorColumns.add(i);
            }
        }
        return errorColumns;
    }

    private ArrayList<Integer> getErrorRows(Board board) {
        ArrayList<Integer> errorRows = new ArrayList<>(board.getNumberOfRows());
        for (int i = 0; i < board.getNumberOfRows(); i++) {
            ArrayList<Integer> userIndicator = createUserIndicatorList(getColumn(i));
            ArrayList<Integer> solutionIndicator = Main.getBoard().getRowIndicator(i);
            if (!userIndicator.equals(solutionIndicator)) {
                errorRows.add(i);
            }
        }
        return errorRows;
    }

    public String getHint() {
        HashMap<String, Double> percentageFilled = new HashMap<>();
        for (int i = 0; i < this.getNumberOfColumns(); i++) {
            int sumOfIndicator = getSumOfColumnIndicator(i);
            int sumOfBlack = getSumOfBlackOrSelectedInRow(i);
            percentageFilled.put("Column " + LetterMapper.mapToLetter(i + 1), (double) sumOfBlack / sumOfIndicator);
        }
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            int sumOfIndicator = getSumOfRowIndicator(i);
            int sumOfBlack = getSumOfBlackOrSelectedInColumn(i);
            percentageFilled.put("Row " + LetterMapper.mapToLetter(i + 1), (double) sumOfBlack / sumOfIndicator);
        }

        System.out.println(percentageFilled);

        return "Look in " +  getHighestLine(percentageFilled);
    }

    private String getHighestLine(HashMap<String, Double> map) {
        String highestLine = "";
        double highestValue = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            if (value > highestValue && value != 1) {
                highestLine = key;
                highestValue = value;
            }
        }
        return highestLine;
    }

    private int getSumOfColumnIndicator(int column) {
        ArrayList<Integer> columnIndicator = getColumnIndicator(column);
        int sum = 0;
        for (int indicator : columnIndicator) {
            sum += indicator;
        }
        return sum;
    }

    private int getSumOfBlackOrSelectedInColumn(int column) {
        int sum = 0;
        Square[] line = this.getColumn(column);
        for (Square square: line) {
            if (square.isBlack() || square.isUserSelected()) {
                sum++;
            }
        }
        return sum;
    }

    private int getSumOfRowIndicator(int row) {
        ArrayList<Integer> rowIndicator = getRowIndicator(row);
        int sum = 0;
        for (int indicator : rowIndicator) {
            sum += indicator;
        }
        return sum;
    }

    private int getSumOfBlackOrSelectedInRow(int row) {
        int sum = 0;
        Square[] line = this.getRow(row);
        for (Square square : line) {
            if (square.isBlack() || square.isUserSelected()) {
                sum++;
            }
        }
        return sum;
    }

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
