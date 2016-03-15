package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Board;

import java.net.URL;

public class Main extends Application {

    private static BorderPane root = new BorderPane();
    private static Board board;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        URL menuBarURL = getClass().getResource("../view/MenuBar.fxml");
        MenuBar menuBar = FXMLLoader.load(menuBarURL);
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
