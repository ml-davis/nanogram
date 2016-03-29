package main;

import helpers.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Board;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application {

    private static BorderPane root = new BorderPane();
    private static Board board;
    private static MenuBar menuBar;
    private static Menu savedPuzzlesMenu;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL menuBarURL = getClass().getResource("../view/MenuBar.fxml");
        menuBar = FXMLLoader.load(menuBarURL);
        loadSavedPuzzles();
        root.setTop(menuBar);

        URL frontPageURL = getClass().getResource("../view/FrontPage.fxml");
        AnchorPane frontPage = FXMLLoader.load(frontPageURL);
        root.setCenter(frontPage);

        Scene myScene = new Scene(root, 1366, 722);
        myScene.getStylesheets().add(getClass().getResource("../style/Style.css").toExternalForm());
        primaryStage.setScene(myScene);

        primaryStage.setTitle("Nanogram");

        primaryStage.show();
    }

    private static void loadSavedPuzzles() {
        savedPuzzlesMenu = new Menu("Solve Saved Puzzles");
        savedPuzzlesMenu.getItems().addAll(getSavedPuzzles());
        menuBar.getMenus().get(1).getItems().add(savedPuzzlesMenu);
    }

    public static void addSavedPuzzle(MenuItem item) {
        savedPuzzlesMenu.getItems().add(item);
    }

    private static ArrayList<MenuItem> getSavedPuzzles() {
        ArrayList<MenuItem> items = new ArrayList<>();
        ArrayList<File> puzzles = FileManager.getSavedPuzzles();
        if (puzzles != null) {
            Collections.sort(puzzles);
            for (File file : puzzles) {
                items.add(new MenuItem(file.getName()));
            }
        }

        return items;
    }

    public static BorderPane getRoot() {
        return root;
    }

    public static Board getBoard() {
        return board;
    }

    public static void setBoard(Board b) {
        board = b;
    }
}
