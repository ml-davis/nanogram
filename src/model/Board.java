package model;

import helpers.Enums;
import helpers.LetterMapper;
import main.Main;
import java.io.Serializable;
import java.util.*;

public class Board extends Observable implements Serializable {
    private int numberOfRows;
    private int numberOfColumns;
    private Square[][] board;
    private HashMap<Integer, ArrayList<Integer>> rowIndicator;
    private HashMap<Integer, ArrayList<Integer>> columnIndicator;
    private boolean[] rowSolved;
    private boolean[] columnSolved;

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

		rowSolved = new boolean[numberOfRows];
		columnSolved = new boolean[numberOfRows];
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

    public ArrayList<ArrayList<Boolean>> getValidSolutions(int lineNumber, boolean isRow) {
		ArrayList<ArrayList<Boolean>> validSolutions = new ArrayList<>();

		Square[] r;
		boolean[] line;
		ArrayList<Integer> indicator;

		if (isRow){
			r = getRow(lineNumber);
			indicator = getRowIndicator(lineNumber);
			line = new boolean[numberOfColumns];
		} else {
			r = getColumn(lineNumber);
			indicator = getColumnIndicator(lineNumber);
			line = new boolean[numberOfRows];
		}

		int index = 0;
		for (Square square : r) {
			line[index++] = square.isBlack();
		}

		ArrayList<ArrayList<Boolean>> possibilities =
				PossibilityGenerator.getPossibleCombinations(indicator, line);

		System.out.println("\n*** POSSIBLE ***");
		for (ArrayList<Boolean> possible : possibilities) {
			for (Boolean item : possible) {
				System.out.print((item) ? "X " : "- ");
			}
			System.out.println();
		}

		for (ArrayList<Boolean> possibility : possibilities) {

			boolean[] temp = makeCopy(line);

			for (int i = 0; i < temp.length; i++) {
				if (possibility.get(i)) {
					temp[i] = true;
				}
			}

			if (isValid(temp, indicator)) {
				validSolutions.add(possibility);
			}

		}

		System.out.println("\n*** VALID ***");
		for (ArrayList<Boolean> possible : validSolutions) {
			for (Boolean item : possible) {
				System.out.print((item) ? "X " : "- ");
			}
			System.out.println();
		}

		return validSolutions;
	}

	private boolean isValid(boolean[] temp, ArrayList<Integer> indicator) {
		ArrayList<Integer> tempIndicator = new ArrayList<>();
		int count = 0;

		for (boolean item: temp) {
			if (item) {
				count++;
			} else if (count > 0) {
				tempIndicator.add(count);
				count = 0;
			}
		}
		if (count > 0) {
			tempIndicator.add(count);
		}

		return indicator.equals(tempIndicator);
	}

	private boolean[] makeCopy(boolean[] r) {
		boolean[] temp = new boolean[r.length];
		for (int i = 0; i < r.length; i++) {
			temp[i] = r[i];
		}
		return temp;
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

        if (userIndicator.equals(solutionIndicator)) {
        	rowSolved[row] = true;
		} else {
        	rowSolved[row] = false;
		}

        return rowSolved[row];
    }

    public boolean isColumnSolved(int column) {
        ArrayList<Integer> userIndicator = createUserIndicatorList(getColumn(column));
        ArrayList<Integer> solutionIndicator = Main.getBoard().getColumnIndicator(column);

        if (userIndicator.equals(solutionIndicator)) {
			columnSolved[column] = true;
		} else {
			columnSolved[column] = false;
		}

		return columnSolved[column];
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

	public Enums.SquareColor getColor(int column, int row) {
		return board[row][column].getColor();
	}
}
