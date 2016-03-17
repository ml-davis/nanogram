package model;

public class Driver {
    public static void main(String[] args) {
        Board board = new Board(13, 13);

        board.toggleFlag(0, 0);
        board.toggleFlag(0, 1);

        board.toggleFlag(0, 5);
        board.toggleFlag(0, 6);
        board.toggleFlag(0, 7);

        board.getRowCombos(0);

    }
}
