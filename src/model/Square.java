package model;

import java.io.Serializable;

import helpers.ColorMapper;
import helpers.Enums;

import static helpers.Enums.SquareColor.WHITE;

public class Square implements Serializable {

    private boolean flagged;
    private boolean black;
    private boolean userSelected;
    private Enums.SquareColor color;
    private String style;

    public Square() {
        flagged = false;
        black = false;
        userSelected = false;
        color = WHITE;
        setStyle(color);
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
		ColorMapper c = new ColorMapper();
		css += c.getColor(color);
		this.color = color;
		this.style = css;
    }

    public Enums.SquareColor getColor() {
		return color;
	}

    public String getStyle() {
        return style;
    }

    public String getStateString() {
        String format = "%-10s%-10b%n%-10s%-10b%n%-10s%-10s%n%-10s%-10s%n";
        return String.format(format, "flagged", flagged, "black", black, "selected", userSelected, "style", style);
    }
}
