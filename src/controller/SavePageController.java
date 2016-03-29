package controller;

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
            if (fileManager.savePuzzle(board, name)) {
                PageLoader.launchPromptWindow("Puzzle saved successfully");
                PageLoader pageLoader = new PageLoader();
                pageLoader.loadFrontPage();
                Main.addSavedPuzzle(new MenuItem(name));
            } else {
                PageLoader.launchPromptWindow("Sorry, we could not save your puzzle.");
            }
        }
    }
}
