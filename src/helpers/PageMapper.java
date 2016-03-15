package helpers;

import java.util.HashMap;

public class PageMapper {
    private HashMap<Enums.Page, String> mapper;

    public PageMapper() {
        mapper = new HashMap<>();
        mapper.put(Enums.Page.FRONT_PAGE, "../view/FrontPage.fxml");
        mapper.put(Enums.Page.CREATOR_PAGE_ONE, "../view/CreatorPageOne.fxml");
        mapper.put(Enums.Page.CREATOR_PAGE_TWO, "../view/CreatorPageTwo.fxml");
        mapper.put(Enums.Page.SOLVE_PAGE, "../view/SolvePage.fxml");
    }

    public String getPage(Enums.Page page) {
        if (mapper.containsKey(page)) {
            return mapper.get(page);
        }
        return "Invalid page";
    }
}
