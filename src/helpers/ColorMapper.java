package helpers;

import java.util.HashMap;

import static helpers.Enums.SquareColor.*;

public class ColorMapper {
	private HashMap<Enums.SquareColor, String> map;

	public ColorMapper() {
		map = new HashMap<>();
		map.put(WHITE, "-fx-background-color: white;");
		map.put(BLACK, "-fx-background-color: black;");
		map.put(LIGHT_GREY, "-fx-background-color: #A6A6A6;;");
		map.put(DARK_GREY, "-fx-background-color: #444444;;");
	}

	public String getColor(Enums.SquareColor color) {
		if (map.containsKey(color)) {
			return map.get(color);
		}
		return "";
	}

}
