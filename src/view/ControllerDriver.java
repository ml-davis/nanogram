package view;

import controller.PageLoader;

public class ControllerDriver {
    public static void main(String[] args) {
        PageLoader pageLoader = new PageLoader();
        pageLoader.addSavedPuzzlesToMenuBar();
    }
}
