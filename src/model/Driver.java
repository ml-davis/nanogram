package model;

public class Driver {
    public static void main(String[] args) {
        Board board = new Board(5, 5);

        board.toggleFlag(0, 0);
        board.toggleFlag(0, 1);
        board.toggleFlag(0, 4);

        board.toggleBlack(0, 0);

        board.getValidLineCombinations(board.getColumn(0));
    }


}
