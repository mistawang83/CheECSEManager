package ca.mcgill.ecse.cheecsemanager.fxml.controller.cheesewheel;

import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

import java.net.URL;
import java.util.ResourceBundle;

public class CheeseWheelController extends BaseController implements Initializable {

  @FXML
  public BreadCrumbBar<String> breadCrumbBar;

  @FXML
  public VBox parentContainer, childContainer;

  public static CheeseWheelController instance;

  public CheeseWheelController() {}

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
        PageType.BACK, PageType.REDIRECT_DISPLAY};
    super.registerPage("CheeseWheel", "view/page/cheesewheel/CheeseWheelDisplay.fxml");

  }

  public void refresh() {
    // Use the default registered page
    NavigationState<?> state = new NavigationState<>(
        "CheeseWheel", 
        PageType.DISPLAY,
        "view/page/cheesewheel/CheeseWheelDisplay.fxml"  
    );
    PageSwitchEvent event = new PageSwitchEvent(state);
    changePage(event);
  }

  public static CheeseWheelController getInstance() {
    return instance;
  }
}
