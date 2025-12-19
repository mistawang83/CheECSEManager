package ca.mcgill.ecse.cheecsemanager.fxml.controller.purchase;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseController;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.order.OrderController;
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

public class PurchaseController extends BaseController implements Initializable {

  @FXML
  public BreadCrumbBar<String> breadCrumbBar;

  @FXML
  public VBox parentContainer, childContainer;

  public static PurchaseController instance;

  public PurchaseController() {}

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
    super.registerPage("Purchase", "view/page/purchase/PurchaseDisplay.fxml");

  }

  public void changePage(PageSwitchEvent event) {
    try {
      NavigationState<?> navigationState = event.getNavigationState();
      PageType type = navigationState.getPageType();
      if (type == PageType.BACK) {
        super.handleBack();
        return;
      }
      FXMLLoader loader = super.loadPage(navigationState);
      switch (type) {
        case DISPLAY -> super.handleDisplay();
        case ADD -> super.handleAdd(navigationState, loader);
        case UPDATE -> super.handleUpdate(navigationState, loader);
        case REDIRECT_DISPLAY -> super.handleRedirectDisplay(navigationState, loader);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void refresh() {
    // Use the default registered page
    NavigationState<?> state = new NavigationState<>(
        "Purchase", 
        PageType.DISPLAY,
        "view/page/purchase/PurchaseDisplay.fxml"  
    );
    PageSwitchEvent event = new PageSwitchEvent(state);
    changePage(event);
  }
  
  public static PurchaseController getInstance() {
    return instance;
  }
}
