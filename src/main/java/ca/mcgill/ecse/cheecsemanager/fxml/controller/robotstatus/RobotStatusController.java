package ca.mcgill.ecse.cheecsemanager.fxml.controller.robotstatus;

import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

import java.net.URL;
import java.util.ResourceBundle;

public class RobotStatusController extends BaseController implements Initializable {

  @FXML
  public BreadCrumbBar<String> breadCrumbBar;

  @FXML
  public VBox parentContainer, childContainer;

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
    handledPageTypes = new PageType[] {PageType.DISPLAY, PageType.ADD, PageType.UPDATE,
        PageType.REDIRECT_DISPLAY, PageType.BACK};
    registerPage("RobotStatus", "view/page/robotstatus/RobotStatusDisplay.fxml");
  }

}
