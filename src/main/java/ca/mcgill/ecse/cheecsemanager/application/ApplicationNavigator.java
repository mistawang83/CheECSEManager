package ca.mcgill.ecse.cheecsemanager.application;

import java.util.HashMap;
import java.util.Map;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseController;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.cheecsemanager.CheECSEManagerTabController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;

public class ApplicationNavigator {

  Map<String, BaseController> registeredPages = new HashMap<String, BaseController>();
  private static ApplicationNavigator instance;

  private ApplicationNavigator() {}

  public static ApplicationNavigator getInstance() {
    if (instance == null) {
      instance = new ApplicationNavigator();
    }
    return instance;
  }

  public void updatePages(String exceptionPageId) {
    for (String pageId : registeredPages.keySet()) {
      if (pageId.equals(exceptionPageId)) {
        continue;
      }
      NavigationState<?> state = new NavigationState<>(pageId, PageType.DISPLAY,
          registeredPages.get(pageId).getDefaultFxmlPath());
      silentSwitchPage(pageId, new PageSwitchEvent(state));
    }
  }

  /**
   * Registers a page with the given id and controller.
   * 
   * @param pageId
   * @param controller
   */
  public void registerPage(String pageId, BaseController controller) {
    registeredPages.put(pageId, controller);
  }

  /**
   * Switches to the page with the given id and fires the given event on the page's controller.
   * 
   * @param pageId
   * @param event
   * @author Lazarus Sarghi
   */
  public void switchPage(String pageId, PageSwitchEvent event) {

    BaseController controller = registeredPages.get(pageId);

    if (event.getNavigationState().getPageType() == PageType.UPDATE) {
      updatePages(pageId);
    }
    if (controller == null) {
      throw new IllegalArgumentException("No page registered with id: " + pageId);
    }
    controller.fireNavigationEvent(event);

    String currentTab = CheECSEManagerTabController.getInstance().currentTab();
    if (currentTab != null && !currentTab.equals(pageId)) {
      CheECSEManagerTabController.getInstance().switchTab(pageId);
    }
  }

  private void silentSwitchPage(String pageId, PageSwitchEvent event) {
    BaseController controller = registeredPages.get(pageId);
    if (controller == null) return;
    controller.fireNavigationEvent(event); // refresh data only
  }

}
