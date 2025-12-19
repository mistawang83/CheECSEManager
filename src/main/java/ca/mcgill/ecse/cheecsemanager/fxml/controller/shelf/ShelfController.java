package ca.mcgill.ecse.cheecsemanager.fxml.controller.shelf;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

import java.net.URL;
import java.util.ResourceBundle;

public class ShelfController extends BaseController implements Initializable {

  @FXML
  public BreadCrumbBar<String> breadCrumbBar;

  @FXML
  public VBox parentContainer, childContainer;

  public static ShelfController instance;

  public ShelfController() {}

  @Override
  protected BreadCrumbBar<String> getBreadcrumbBar() {
    return breadCrumbBar;
  }

  @Override
  protected Pane getChildContainer() {
    return childContainer;
  }

  @Override
  protected Pane getParentContainer() {
    return parentContainer;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    instance = this;
    handledPageTypes = new PageType[] {PageType.DISPLAY, PageType.ADD, PageType.UPDATE,
        PageType.REDIRECT_DISPLAY, PageType.BACK};
    registerPage("Shelf", "view/page/shelf/ShelfDisplay.fxml");
  }

  public void refresh() {
    // Use the default registered page
    NavigationState<?> state = new NavigationState<>(
        "Shelf", 
        PageType.DISPLAY,
        "view/page/shelf/ShelfDisplay.fxml"  
    );
    PageSwitchEvent event = new PageSwitchEvent(state);
    changePage(event);
  }

  public static ShelfController getInstance() {
    return instance;
  }



}
