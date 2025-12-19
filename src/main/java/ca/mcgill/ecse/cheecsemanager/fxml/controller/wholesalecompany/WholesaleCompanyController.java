package ca.mcgill.ecse.cheecsemanager.fxml.controller.wholesalecompany;

import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseController;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.order.OrderController;
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

public class WholesaleCompanyController extends BaseController implements Initializable {

  @FXML
  public BreadCrumbBar<String> breadCrumbBar;

  @FXML
  public VBox parentContainer, childContainer;

  public static WholesaleCompanyController instance;

  public WholesaleCompanyController() {}

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
    registerPage("WholesaleCompany", "view/page/wholesalecompany/WholesaleCompanyDisplay.fxml");
  }

  public void refresh() {
    // Use the default registered page
    NavigationState<?> state = new NavigationState<>(
        "WholesaleCompany", 
        PageType.DISPLAY,
        "view/page/wholesalecompany/WholesaleCompanyDisplay.fxml"  
    );
    PageSwitchEvent event = new PageSwitchEvent(state);
    changePage(event);
  }
  
  public static WholesaleCompanyController getInstance() {
    return instance;
  }

}
