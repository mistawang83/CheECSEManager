package ca.mcgill.ecse.cheecsemanager.fxml.basecontroller;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.BreadcrumbManager;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import org.controlsfx.control.BreadCrumbBar;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public abstract class BaseController {

  protected TreeItem<String> root = new TreeItem<>();
  protected BreadcrumbManager breadcrumbManager;

  protected PageType[] handledPageTypes;

  protected abstract BreadCrumbBar<String> getBreadcrumbBar();

  protected abstract Pane getParentContainer();

  protected abstract Pane getChildContainer();

  protected PageType currentPageType;

  protected String pageId;
  protected String defaultFxmlPath;

  public String getDefaultFxmlPath() {
    return defaultFxmlPath;
  }

  public void initializeBreadcrumbNavigation(String rootPage) {
    this.root = new TreeItem<>(rootPage);
    breadcrumbManager = new BreadcrumbManager(getBreadcrumbBar(), getParentContainer());
  }

  public void fireNavigationEvent(PageSwitchEvent event) {
    getParentContainer().fireEvent(event);
  }

  public FXMLLoader loadPage(NavigationState<?> navigationState) throws IOException {
    if (!getChildContainer().getChildren().isEmpty()) {
      getChildContainer().getChildren().clear();
    }
    FXMLLoader loader =
        new FXMLLoader(getClass().getResource(PACKAGE_ID.concat(navigationState.getPageName())));
    Parent child = loader.load();
    getChildContainer().getChildren().add(child);
    breadcrumbManager.addNavItem(navigationState);
    return loader;
  }

  protected void handleDisplay() {
    getBreadcrumbBar().setSelectedCrumb(root);
  }

  protected void handleAdd(NavigationState<?> state, FXMLLoader loader) throws Exception {
    TreeItem<String> addItem = new TreeItem<>(state.getTitle());
    root.getChildren().add(addItem);
    getBreadcrumbBar().setSelectedCrumb(addItem);
    if (state.getData() == null)
      return;
    Object redirectAddController = loader.getController();
    Class<?> parameterType = state.getData().getClass();
    redirectAddController.getClass().getMethod("setData", parameterType)
        .invoke(redirectAddController, state.getData());
  }


  protected void handleRedirectDisplay(NavigationState<?> state, FXMLLoader loader)
      throws Exception {
    TreeItem<String> redirectItem = new TreeItem<>(state.getTitle());
    getBreadcrumbBar().getSelectedCrumb().getChildren().add(redirectItem);
    getBreadcrumbBar().setSelectedCrumb(redirectItem);
    Object redirectController = loader.getController();
    /**
     * `setData` takes 3 params, Multiplicity, Attribute scope map, Actual data with templete
     * Different values of data are as follows TOForm<?, ?> - non-root contained entities
     * Supplier<TO> - root-contained one multiplicity Supplier<List<TO>> - root-contained many
     * multiplicity
     */
    redirectController.getClass().getMethod("setData", String.class, Map.class, Object.class)
        .invoke(redirectController, state.getMultiplicity(), state.getScope(), state.getData());

  }

  protected void handleUpdate(NavigationState<?> state, FXMLLoader loader) throws Exception {
    TreeItem<String> redirectUpdateItem = new TreeItem<>(state.getTitle());
    getBreadcrumbBar().getSelectedCrumb().getChildren().add(redirectUpdateItem);
    getBreadcrumbBar().setSelectedCrumb(redirectUpdateItem);
    Object redirectUpdateController = loader.getController();
    Class<?> parameterType = state.getData().getClass();
    redirectUpdateController.getClass().getMethod("setData", parameterType)
        .invoke(redirectUpdateController, state.getData());
  }

  protected void handleBack() {
    // Removing 2 crumbs
    TreeItem<String> parent = getBreadcrumbBar().getSelectedCrumb().getParent();
    if (parent != null) {
      parent.getChildren().clear();
      if (parent.getParent() != null) {
        parent = parent.getParent();
        parent.getChildren().clear();
      }
    }
    breadcrumbManager.forceUpdateUi(parent);
    NavigationState<?> last = breadcrumbManager.removeLastAndGetSecondLast();
    // Adding new Crumb
    getParentContainer().fireEvent(new PageSwitchEvent(last));
  }

  protected void registerPage(String pageId, String fxmlPath) {

    this.pageId = pageId;
    this.defaultFxmlPath = fxmlPath;

    var parentContainer = getParentContainer();
    initializeBreadcrumbNavigation(pageId);
    parentContainer.addEventHandler(PageSwitchEvent.PAGE_SWITCH, this::changePage);
    displayPage(pageId, fxmlPath);

    ApplicationNavigator.getInstance().registerPage(pageId, this);

  }

  protected void switchPage(PageSwitchEvent event) {
    getParentContainer().fireEvent(event);
  }

  protected void switchPage(String pageId, PageSwitchEvent event) {
    ApplicationNavigator.getInstance().switchPage(pageId, event);
  }


  protected void displayPage(String pageId, String fxmlPath) {
    getParentContainer()
        .fireEvent(new PageSwitchEvent(new NavigationState<>(pageId, PageType.DISPLAY, fxmlPath)));
  }

  protected void changePage(PageSwitchEvent event) {
    try {
      NavigationState<?> navigationState = event.getNavigationState();
      PageType type = navigationState.getPageType();

      setCurrentPageType(type);

      if (type == PageType.BACK) {
        handleBack();
        return;
      }

      if (handledPageTypes != null && !Arrays.asList(handledPageTypes).contains(type)) {
        return;
      }

      FXMLLoader loader = loadPage(navigationState);
      switch (type) {
        case DISPLAY -> handleDisplay();
        case ADD -> handleAdd(navigationState, loader);
        case UPDATE -> handleUpdate(navigationState, loader);
        case REDIRECT_DISPLAY -> handleRedirectDisplay(navigationState, loader);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public PageType getCurrentPageType() {
    return currentPageType;
  }

  public void setCurrentPageType(PageType type) {
    this.currentPageType = type;
  }
}
