package helpers;

import java.io.File;
import java.util.ArrayList;

public class HelperDriver {
    public static void main(String[] args) {
        ArrayList<File> files = FileManager.getSavedPuzzles();
        assert files != null;
        for (File file : files) {
            System.out.println(file.getName());
        }
    }
}
