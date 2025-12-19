package ca.mcgill.ecse.cheecsemanager.fxml.controller.facilitymanager;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseDisplayController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FacilityManagerDisplayController extends BaseDisplayController
    implements Initializable {

  @FXML
  public VBox parentContainer, childContainer;
  @FXML
  private Button buttonAdd;


  @Override
  protected Pane getChildContainer() {
    return childContainer;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {}

  @FXML
  public void onViewAccount(ActionEvent event) {
    var state = new NavigationState<>("View FacilityManager", PageType.ADD,
        "view/page/facilitymanager/FacilityManagerDisplayOne.fxml");
    ApplicationNavigator.getInstance().switchPage("FacilityManager", new PageSwitchEvent(state));
  }

  @FXML
  public void onUpdateAccount(ActionEvent event) {
    var state = new NavigationState<>("Update FacilityManager", PageType.ADD,
        "view/page/facilitymanager/FacilityManagerForm.fxml");
    ApplicationNavigator.getInstance().switchPage("FacilityManager", new PageSwitchEvent(state));
  }

  @FXML
  public void onSettings(ActionEvent event) {
    var state = new NavigationState<>("Settings", PageType.DISPLAY,
        "view/page/settings/SettingsDisplay.fxml");
    ApplicationNavigator.getInstance().switchPage("Settings", new PageSwitchEvent(state));
  }
}
