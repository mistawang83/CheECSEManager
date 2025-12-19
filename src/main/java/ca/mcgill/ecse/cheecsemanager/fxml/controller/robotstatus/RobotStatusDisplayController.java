package ca.mcgill.ecse.cheecsemanager.fxml.controller.robotstatus;

import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class RobotStatusDisplayController implements Initializable {

  @FXML
  private Button buttonActivateToggle;
  @FXML
  private Button buttonInitRobot;
  @FXML
  private Button buttonViewLogs;
  @FXML
  private Button buttonReturnRobot;
  @FXML
  private Text textStatus;
  @FXML
  private HBox panelActivation; // renamed to match FXML
  @FXML
  private HBox panelLogs; // renamed to match FXML
  @FXML
  private Text textError;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    updateUi();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double halfScreen = bounds.getWidth() * 0.25;
    if (panelActivation != null)
      panelActivation.setMaxWidth(halfScreen);
    if (panelLogs != null)
      panelLogs.setMaxWidth(halfScreen);
  }

  private void updateUi() {
    String robotStatus = RobotController.getRobotStatus();
    textStatus.setText("Robot Status: " + robotStatus);
    boolean isActive = robotIsActive();

    buttonActivateToggle.setText(isActive ? "Deactivate Robot" : "Activate Robot");
    buttonInitRobot.setDisable(!isActive);

    textStatus.setText(isActive ? "Active" : "Inactive");
  }

  private Boolean robotIsActive() {
    String robotStatus = RobotController.getRobotStatus();
    return !robotStatus.equals("Inactive");
  }

  @FXML
  private void onToggleActivate() {
    boolean isActive = robotIsActive();

    String error = "";
    if (isActive) {
      error = RobotController.deactivateRobot();
    } else {
      error = RobotController.activateRobot(1, 0); // default activation position
    }

    textError.setText(error);
    updateUi();
  }

  @FXML
  private void onGoToControl() {
    buttonInitRobot.fireEvent(new PageSwitchEvent(new NavigationState<>("Robot Control Panel",
        PageType.ADD, "view/page/robotstatus/RobotStatusControlForm.fxml")));
  }

  @FXML
  private void onViewLogs() {
    buttonViewLogs.fireEvent(new PageSwitchEvent(new NavigationState<>("Robot Logs",
        PageType.REDIRECT_DISPLAY, "view/page/robotstatus/RobotStatusLogDisplay.fxml")));
  }

}
