package model;

import helpers.Enums;
import helpers.LetterMapper;
import main.Main;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class Board extends Observable implements Serializable {
    private int numberOfRows;
    private int numberOfColumns;
    private Square[][] board;
    private HashMap<Integer, ArrayList<Integer>> rowIndicator;
    private HashMap<Integer, ArrayList<Integer>> columnIndicator;

    public Board(int numberOfColumns, int numberOfRows) {
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
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

    public void toggleFlag(int column, int row) {
        board[row][column].toggleFlag();
        this.rowIndicator.put(row, createIndicatorList(getRow(row)));
        this.columnIndicator.put(column, createIndicatorList(getColumn(column)));
    }

    public void toggleBlack(int column, int row) {
        board[row][column].toggleBlack();
    }

    public void toggleUserSelected(int column, int row) {
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
                this.toggleBlack(column, row);
                this.setStyle(column, row, Enums.SquareColor.BLACK);
            }
            System.out.println("toggle flag");
            this.toggleFlag(column, row);
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
            ArrayList<Integer> userIndicator = createUserIndicatorList(getColumn(i));
            ArrayList<Integer> solutionIndicator = Main.getBoard().getColumnIndicator(i);
            System.out.println("u: " + userIndicator);
            System.out.println("s: " + solutionIndicator);
            System.out.println();
            if (!userIndicator.equals(solutionIndicator)) {
                return false;
            }
        }
        return true;
    }

    private boolean areRowsSolved(Board board) {
        for (int i = 0; i < board.getNumberOfRows(); i++) {
            ArrayList<Integer> userIndicator = createUserIndicatorList(getRow(i));
            ArrayList<Integer> solutionIndicator = Main.getBoard().getRowIndicator(i);
            System.out.println("u: " + userIndicator);
            System.out.println("s: " + solutionIndicator);
            System.out.println();
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

        if (errorColumns.size() > 0) {
            errorMessage += "Columns: \t";
        }
        for (Integer errorColumn : errorColumns) {
            errorMessage += LetterMapper.mapToLetter(errorColumn + 1) + " ";
        }
        errorMessage += "\n";

        if (errorRows.size() > 0) {
            errorMessage += "Rows:\t";
        }
        for (Integer errorRow : errorRows) {
            errorMessage += LetterMapper.mapToLetter(errorRow + 1) + " ";
        }
        errorMessage += "\n";

        return errorMessage;
    }

    private ArrayList<Integer> getErrorColumns(Board board) {
        ArrayList<Integer> errorColumns = new ArrayList<>(board.getNumberOfColumns());
        for (int i = 0; i < board.getNumberOfColumns(); i++) {
            ArrayList<Integer> userIndicator = createUserIndicatorList(getColumn(i));
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
            ArrayList<Integer> userIndicator = createUserIndicatorList(getRow(i));
            ArrayList<Integer> solutionIndicator = Main.getBoard().getRowIndicator(i);
            if (!userIndicator.equals(solutionIndicator)) {
                errorRows.add(i);
            }
        }
        return errorRows;
    }

    public String getHint() {
        HashMap<String, Double> percentageFilled = new HashMap<>();
        int sumOfIndicator;
        int sumOfBlack;

        for (int i = 0; i < this.getNumberOfColumns(); i++) {
            sumOfIndicator = getSumOfColumnIndicator(i);
            sumOfBlack = getSumOfBlackOrSelectedInColumn(i);
            percentageFilled.put("Column " + LetterMapper.mapToLetter(i + 1), (double) sumOfBlack / sumOfIndicator);
        }
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            sumOfIndicator = getSumOfRowIndicator(i);
            sumOfBlack = getSumOfBlackOrSelectedInRow(i);
            percentageFilled.put("Row " + LetterMapper.mapToLetter(i + 1), (double) sumOfBlack / sumOfIndicator);
        }

        System.out.println(percentageFilled);

        return getBestLine(percentageFilled);
    }

    private String getBestLine(HashMap<String, Double> map) {
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
        if (highestValue == 0) {
            return "The solution is complete!!";
        } else {
            return "Look in " + highestLine;
        }
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

    private int getSumOfRowIndicator(ArrayList<Integer> row) {
        int sum = 0;
        for (int indicator : row) {
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

    private ArrayList<Integer> createIndicatorList(Square[] line) {
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

    private ArrayList<Integer> createUserIndicatorList(Square[] line) {
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

    private ArrayList<Integer> createPossibleIndicatorList(Square[] line) {
        ArrayList<Integer> list = new ArrayList<>();
        int count = 0;
        for (Square square : line) {
            if (square.isBlack() || square.isUserSelected() || square.isPossible()) {
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

    public boolean isFlagged(int column, int row) {
        return board[row][column].isFlagged();
    }

    public boolean isBlack(int column, int row) {
        return board[row][column].isBlack();
    }

    public boolean isUserSelected(int column, int row) {
        return board[row][column].isUserSelected();
    }

    public void setStyle(int column, int row, Enums.SquareColor color) {
        board[row][column].setStyle(color);
    }

    public String getStyle(int column, int row) {
        return board[row][column].getStyle();
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
        Square[] col = new Square[this.numberOfRows];
        for (int i = 0; i < this.numberOfRows; i++) {
            col[i] = board[i][column];
        }
        return col;
    }

    public Square getSquare(int column, int row) {
        return board[row][column];
    }

    public ArrayList<ArrayList<Boolean>> getValidLineCombinations(Square[] line) {
        ArrayList<ArrayList<Boolean>> validCombos = new ArrayList<>();
        ArrayList<Integer> indicator = createIndicatorList(line);
        ArrayList<ArrayList<Boolean>> possibleCombos = getPossibleLineCombinations(line, indicator);
        System.out.println("********* VALID *********");

        for (int i = 0; i < possibleCombos.size(); i++) {
            for (int j = 0; j < line.length; j++) {
                if (possibleCombos.get(i).get(j)) {
                    line[j].setPossible(true);
                } else {
                    line[j].setPossible(false);
                }
            }
            ArrayList<Integer> possibleIndicator = createPossibleIndicatorList(line);
            if (possibleIndicator.equals(indicator)) {
                validCombos.add(possibleCombos.get(i));
            }
        }
        printCombos(validCombos);

        return validCombos;
    }

    public ArrayList<ArrayList<Boolean>> getPossibleLineCombinations(Square[] line, ArrayList<Integer> indicator) {
        ArrayList<ArrayList<Boolean>> combos = new ArrayList<>();
        ArrayList<Integer> positions = getStartPositions(indicator);
        System.out.println(positions);
        System.out.println("Indicator:\t\t" + indicator + "\n******** POSSIBLE ********");

        clearPieces(line);
        line = placePieces(indicator, positions, line);
        combos.add(getCombo(line));
        while ((positions = incrementPositions(indicator, positions, line.length)) != null) {
            line = clearPieces(line);
            line = placePieces(indicator, positions, line);
            combos.add(getCombo(line));
        }

        printCombos(combos);

        return combos;
    }

    private void printLine(Square[] line) {
        System.out.print("$ ");
        for (Square s : line) {
            if (s.isPossible()) {
                System.out.print("X ");
            } else {
                System.out.print("- ");
            }
        }
        System.out.println("\n");
    }

    private void printCombos(ArrayList<ArrayList<Boolean>> combos) {
        for (ArrayList<Boolean> combo : combos) {
            for (Boolean possible : combo) {
                if (possible) {
                    System.out.print("0 ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    private ArrayList<Boolean> getCombo(Square[] line) {
        ArrayList<Boolean> combo = new ArrayList<>();
        for (Square square : line) {
            if (square.isPossible()) {
                combo.add(true);
            } else {
                combo.add(false);
            }
        }
        return combo;
    }

    private ArrayList<Integer> getStartPositions(ArrayList<Integer> indicator) {
        ArrayList<Integer> startPositions = new ArrayList<>(indicator.size());
        int sum = 0;
        for (int i = 0; i < indicator.size(); i++) {
            startPositions.add(sum + i);
            sum += indicator.get(i);
        }
        return startPositions;
    }

    private int getStopPosition(int focusPiece, ArrayList<Integer> indicator, int lineLength) {
        ArrayList<Integer> endPositions = new ArrayList<>(indicator.size());
        int index = indicator.size() - 1;
        for (int i = 0; i < indicator.size(); i++) {
            endPositions.add(lineLength - i);
            lineLength -= (indicator.get(index--));
        }

        return endPositions.get(focusPiece);
    }

    private ArrayList<Integer> incrementPositions(ArrayList<Integer> indicator, ArrayList<Integer> positions, int lineLength) {
        int count = 0;
        for (int i = positions.size() - 1; i >= 0; i--) {
            if (positions.get(i) + indicator.get(i) < getStopPosition(count++, indicator, lineLength)) {
                positions.set(i, positions.get(i) + 1);
                return positions;
            } else {
                int a = indicator.size();
                // todo turn this into a loop
                if (indicator.size() > 1) {
                    if (count == 1 && indicator.size() > 1) {
                        positions.set(a - 2, positions.get(a - 2));
                        positions.set(a - 1, positions.get(a - 2) + indicator.get(a-2) + 2);
                    } else if (count == 2 && indicator.size() > 2) {
                        positions.set(a - 3, positions.get(a - 3));
                        positions.set(a - 2, positions.get(a - 3) + indicator.get(a - 3) + 2);
                        positions.set(a - 1, positions.get(a - 2) + indicator.get(a - 2) + 1);
                    } else if (count == 3 && indicator.size() > 3) {
                        positions.set(a - 4, positions.get(a - 4));
                        positions.set(a - 3, positions.get(a - 4) + indicator.get(a - 4) + 2);
                        positions.set(a - 2, positions.get(a - 3) + indicator.get(a - 3) + 1);
                        positions.set(a - 1, positions.get(a - 2) + indicator.get(a - 2) + 1);
                    } else if (count == 4  && indicator.size() > 4) {
                        positions.set(a - 5, positions.get(a - 5));
                        positions.set(a - 4, positions.get(a - 5) + indicator.get(a - 5) + 2);
                        positions.set(a - 3, positions.get(a - 4) + indicator.get(a - 4) + 1);
                        positions.set(a - 2, positions.get(a - 3) + indicator.get(a - 3) + 1);
                        positions.set(a - 1, positions.get(a - 2) + indicator.get(a - 2) + 1);
                    } else if (count == 5 && indicator.size() > 5) {
                        positions.set(a - 6, positions.get(a - 6));
                        positions.set(a - 5, positions.get(a - 6) + indicator.get(a - 6) + 2);
                        positions.set(a - 4, positions.get(a - 5) + indicator.get(a - 5) + 1);
                        positions.set(a - 3, positions.get(a - 4) + indicator.get(a - 4) + 1);
                        positions.set(a - 2, positions.get(a - 3) + indicator.get(a - 3) + 1);
                        positions.set(a - 1, positions.get(a - 2) + indicator.get(a - 2) + 1);
                    } else if (count == 6 && indicator.size() > 6) {
                        positions.set(a - 7, positions.get(a - 7));
                        positions.set(a - 6, positions.get(a - 7) + indicator.get(a - 7) + 2);
                        positions.set(a - 5, positions.get(a - 6) + indicator.get(a - 6) + 1);
                        positions.set(a - 4, positions.get(a - 5) + indicator.get(a - 5) + 1);
                        positions.set(a - 3, positions.get(a - 4) + indicator.get(a - 4) + 1);
                        positions.set(a - 2, positions.get(a - 3) + indicator.get(a - 3) + 1);
                        positions.set(a - 1, positions.get(a - 2) + indicator.get(a - 2) + 1);
                    } else if (count == 7 && indicator.size() > 7) {
                        positions.set(a - 8, positions.get(a - 8));
                        positions.set(a - 7, positions.get(a - 8) + indicator.get(a - 8) + 2);
                        positions.set(a - 6, positions.get(a - 7) + indicator.get(a - 7) + 1);
                        positions.set(a - 5, positions.get(a - 6) + indicator.get(a - 6) + 1);
                        positions.set(a - 4, positions.get(a - 5) + indicator.get(a - 5) + 1);
                        positions.set(a - 3, positions.get(a - 4) + indicator.get(a - 4) + 1);
                        positions.set(a - 2, positions.get(a - 3) + indicator.get(a - 3) + 1);
                        positions.set(a - 1, positions.get(a - 2) + indicator.get(a - 2) + 1);
                    }
                }
            }
        }
        return null;
    }

    private Square[] placePieces(ArrayList<Integer> indicator, ArrayList<Integer> positions, Square[] line) {
        for (int i = 0; i < indicator.size(); i++) {
            int position = positions.get(i);
            for (int j = 0; j < indicator.get(i); j++) {
                line[position++].setPossible(true);
            }
        }
        return line;
    }

    private Square[] clearPieces(Square[] line) {
        for (Square square : line) {
            square.setPossible(false);
        }
        return line;
    }

}
