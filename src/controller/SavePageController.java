package controller;

import helpers.Enums;
import helpers.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Board;

public class SavePageController {

    @FXML
    private TextField nameField;

    @FXML
    public void cancelButtonClicked() {
        Stage s = (Stage) nameField.getScene().getWindow();
        s.close();
    }

    @FXML
    public void saveButtonClicked() {
        Board board = Main.getBoard();
        String name = nameField.getText();
        if (name.equals("")) {
            PageLoader.launchPromptWindow("Please enter a name for your puzzle");
        } else {
            name += " (" + board.getNumberOfRows() + " x " + board.getNumberOfColumns() + ")";

            FileManager fileManager = new FileManager();
			board = hideSquares(board);

            if (fileManager.savePuzzle(board, name)) {
                PageLoader.launchPromptWindow("Puzzle saved successfully");
                PageLoader pageLoader = new PageLoader();
                pageLoader.loadFrontPage();
                MenuItem item = new MenuItem(name);
                final String finalName = name;
                item.setOnAction(e -> pageLoader.loadPuzzle(finalName));
                Main.addSavedPuzzle(item);
            } else {
                PageLoader.launchPromptWindow("Sorry, we could not save your puzzle.");
            }

            Stage s = (Stage) nameField.getScene().getWindow();
            s.close();
        }
    }

    private Board hideSquares(Board board) {
		for (int i = 0; i < board.getNumberOfRows(); i++) {
			for (int j = 0; j < board.getNumberOfColumns(); j++) {
				if (board.getColor(i, j) == Enums.SquareColor.LIGHT_GREY) {
					board.setStyle(i, j, Enums.SquareColor.WHITE);
				}
			}
		}
		return board;
	}
}
