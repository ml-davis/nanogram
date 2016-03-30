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
            if (!isColumnSolved(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean areRowsSolved(Board board) {
        for (int i = 0; i < board.getNumberOfRows(); i++) {
            if (!isRowSolved(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isRowSolved(int row) {
        ArrayList<Integer> userIndicator = createUserIndicatorList(getRow(row));
        ArrayList<Integer> solutionIndicator = Main.getBoard().getRowIndicator(row);

        return userIndicator.equals(solutionIndicator);
    }

    public boolean isColumnSolved(int column) {
        ArrayList<Integer> userIndicator = createUserIndicatorList(getColumn(column));
        ArrayList<Integer> solutionIndicator = Main.getBoard().getColumnIndicator(column);

        return userIndicator.equals(solutionIndicator);
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
        HashMap<String, Integer> lines = new HashMap<>();
        ArrayList<ArrayList<Boolean>> combos;

        for (int i = 0; i < this.getNumberOfColumns(); i++) {
            if (!isColumnSolved(i)) {
                combos = getValidLineCombinations(getColumn(i));
                lines.put("Column " + LetterMapper.mapToLetter(i + 1), combos.size());
            }
            if (!isRowSolved(i)) {
                combos = getValidLineCombinations(getRow(i));
                lines.put("Row " + LetterMapper.mapToLetter(i + 1), combos.size());
            }
        }

        return getBestLine(lines);
    }

    private String getBestLine(HashMap<String, Integer> map) {
        if (map.size() == 0) {
            return "The solution is complete!";
        }

        String bestLine = "";
        double lowestValue = 100;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("lowest value\t" + lowestValue);
            System.out.println("new value\t\t" + value);
            System.out.println("line\t\t\t " + key);
            System.out.println();
            if (value < lowestValue) {
                bestLine = key;
                lowestValue = value;
            }
        }

        return "Look in " + bestLine;
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
        System.out.println("\n********* VALID *********");

        for (int i = 0; i < possibleCombos.size(); i++) {
            for (int j = 0; j < line.length; j++) {
                if (possibleCombos.get(i).get(j)) {
                    line[j].setPossible(true);
                } else {
                    line[j].setPossible(false);
                }
            }

            ArrayList<Integer> possibleIndicator = createPossibleIndicatorList(line);
            if (possibleIndicator.equals(indicator) && !overlapsGreen(line)) {
                validCombos.add(possibleCombos.get(i));
            }
        }
        printCombos(validCombos);

        return validCombos;
    }

    private boolean overlapsGreen(Square[] line) {
        for (Square square : line) {
            if (square.isPossible()) {
                if (square.isGreen() && !(square.isBlack() || square.isUserSelected())) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<ArrayList<Boolean>> getPossibleLineCombinations(Square[] line, ArrayList<Integer> indicator) {
        ArrayList<ArrayList<Boolean>> combos = new ArrayList<>();
        ArrayList<Integer> positions = getStartPositions(indicator);
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
