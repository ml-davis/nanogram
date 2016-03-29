package helpers;

import model.Board;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {
    private String os;

    public FileManager() {
        this.os = System.getProperty("os.name").toLowerCase();
    }

    public boolean savePuzzle(Board board, String name) {
        File path = new File("games");
        try {
            ObjectOutputStream outputStream;
            if (isWindows()) {
                outputStream = new ObjectOutputStream(new FileOutputStream(path + "\\" + name));
            } else {
                outputStream = new ObjectOutputStream(new FileOutputStream(path + "/" + name));
            }
            outputStream.writeObject(board);
            outputStream.close();
            System.out.println("Saved file " + path + "/" + name);
            return true;

        } catch (IOException e) {
            e.printStackTrace(System.out);
            System.out.println("Failed to save file " + path + "/" + name);
            return false;
        }
    }

    public static ArrayList<File> getSavedPuzzles() {
        File folder = new File("games");
        File[] files = folder.listFiles();
        if (files != null) {
            return new ArrayList<>(Arrays.asList(files));
        }
        return null;
    }

    private boolean isWindows() {
        return os.contains("windows");
    }
}
