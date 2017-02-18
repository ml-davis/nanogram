package main;

import controller.PageLoader;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Board;

import java.net.URL;

public class Main extends Application {

    private static BorderPane root = new BorderPane();
    private static Board board;
    private static MenuBar menuBar;
    private static Menu savedPuzzlesMenu;

    private static int width;
    private static int height;

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

        // Determine users screen size
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        width = (int) (primaryScreenBounds.getMaxX() * 0.95);
        height = (int) (primaryScreenBounds.getMaxY() * 0.95);

        Scene myScene = new Scene(root, width, height);
        myScene.getStylesheets().add(getClass().getResource("../style/Style.css").toExternalForm());
        primaryStage.setScene(myScene);

        primaryStage.setTitle("Nanogram");
        primaryStage.getIcons().add(new Image("file:images/puzzle_icon.png"));

        primaryStage.show();
    }

    public static void addSavedPuzzle(MenuItem item) {
        savedPuzzlesMenu.getItems().add(item);
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

    public static MenuBar getMenuBar() {
        return menuBar;
    }

    public static Menu getSavedPuzzlesMenu() {
        return savedPuzzlesMenu;
    }

    public static void setSavedPuzzlesMenu(Menu menu) {
        savedPuzzlesMenu = menu;
    }

    private static void loadSavedPuzzles() {
        PageLoader loader = new PageLoader();
        loader.addSavedPuzzlesToMenuBar();
    }

    public static int getWidth() {
    	return width;
	}

	public static int getHeight() {
    	return height;
	}
}
