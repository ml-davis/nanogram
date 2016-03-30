package helpers;

import model.Board;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {
    private String os;

    public FileManager() {
        this.os = System.getProperty("os.name").toLowerCase();
    }

    public boolean savePuzzle(Board board, String name) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(getFilePath() + name));
            outputStream.writeObject(board);
            outputStream.close();
            System.out.println("Saved file: " + getFilePath() + name);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save file: " + getFilePath() + name);
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

    public Board getPuzzle(String puzzleName) {
        ArrayList<File> puzzles = getSavedPuzzles();
        Board board;
        if (puzzles != null) {
            for (File puzzle : puzzles) {
                if (puzzle.getName().equals(puzzleName)) {
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(
                                new FileInputStream(getFilePath() + puzzle.getName()));
                        board = (Board) inputStream.readObject();
                        System.out.println("Loaded puzzle: " + getFilePath() + puzzle.getName());
                        return board;
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Failed to load puzzle: " + getFilePath() + puzzle.getName());
                    }

                }
            }
        }
        return null;
    }

    private String getFilePath() {
        if (isWindows()) {
            return "games\\";
        } else {
            return "games/";
        }
    }

    private boolean isWindows() {
        return os.contains("windows");
    }
}
