package model;

import java.io.Serializable;
import helpers.Enums;

public class Square implements Serializable {

    private boolean flagged;
    private boolean black;
    private boolean userSelected;
    private boolean possible;
    private String style;

    public Square() {
        flagged = false;
        black = false;
        userSelected = false;
        setStyle(Enums.SquareColor.WHITE);
    }

    public void toggleFlag() {
        flagged = !flagged;
    }

    public void toggleBlack() {
        black = !black;
    }

    public void toggleUserSelected() { userSelected = !userSelected; }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isBlack() {
        return black;
    }

    public boolean isUserSelected() {
        return userSelected;
    }

    public void setStyle(Enums.SquareColor color) {
        String css = "-fx-background-radius: 0, 0, 0; ";
        switch (color) {
            case WHITE: css += "-fx-background-color: white;"; break;
            case BLACK: css += "-fx-background-color: black;"; break;
            case LIGHT_GREY: css += "-fx-background-color: #A6A6A6;"; break;
            case DARK_GREY: css += "-fx-background-color: #444444;"; break;
        }
        this.style = css;
    }

    public String getStyle() {
        return style;
    }

    public String getStateString() {
        String format = "%-10s%-10b%n%-10s%-10b%n%-10s%-10s%n%-10s%-10s%n";
        return String.format(format, "flagged", flagged, "black", black, "selected", userSelected, "style", style);
    }

    public void setPossible(boolean possible) {
        this.possible = possible;
    }

    public boolean isPossible() {
        return possible;
    }
}
