package controller;

import helpers.Enums;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Main;

import java.util.ArrayList;

public class CreatorOneController extends Observer {

    public CreatorOneController(int numberOfRows, int numberOfColumns) {
        super(numberOfRows, numberOfColumns);
    }

    @Override
    public void toggleCell(int row, int column) {
        // update backend
        Main.getBoard().toggleBlack(row, column);
        Main.getBoard().toggleFlag(row, column);
        if (Main.getBoard().isFlagged(row, column)) {
            Main.getBoard().setStyle(row, column, Enums.SquareColor.BLACK);
        } else {
            Main.getBoard().setStyle(row, column, Enums.SquareColor.WHITE);
        }

        // print some stuff for debugging purposes
        System.out.println("Row " + (column+1) + ", Column " + (row+1));
        System.out.println(Main.getBoard().getSquare(row, column).getStateString());

        // notify everyone subscribed to Observer of changes
        Main.getBoard().notifyObservers();
    }
}
