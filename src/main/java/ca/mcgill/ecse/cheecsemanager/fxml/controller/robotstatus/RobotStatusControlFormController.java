package ca.mcgill.ecse.cheecsemanager.fxml.controller.robotstatus;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.PurchasesOrdersController;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.controller.TOPurchase;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.InvalidInputException;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RobotStatusControlFormController implements Initializable {

  @FXML
  private ComboBox<String> initInputShelfId;
  @FXML
  private ComboBox<String> moveInputShelfId;
  @FXML
  private ComboBox<Integer> cheeseInputShelfId;

  @FXML
  private Button buttonInitialize;
  @FXML
  private Button buttonMoveShelf;
  @FXML
  private Button buttonTurnLeft;
  @FXML
  private Button buttonTurnRight;
  @FXML
  private Button buttonTreatCheese;
  @FXML
  private Button buttonReturnEntrance;
  @FXML
  private Button buttonBack;
  @FXML
  private Text textError;
  @FXML
  private Label textStatus;
  @FXML
  private Label labelCurrentShelf;

  @FXML
  private HBox statusPanel; // top status HBox
  @FXML
  private HBox uninitializedButtons;
  @FXML
  private HBox initializedButtons;
  @FXML
  private HBox bottomPanel; // bottom HBox with Back

  @FXML
  private ChoiceBox<MaturationPeriod> choiceBoxMonthsAged;

  @FXML
  private ComboBox<Integer> inputPurchaseId;

  @FXML
  private Button buttonViewLogs;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Constrain HBox max widths to 50% of the screen
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double halfScreen = bounds.getWidth() * 0.30;
    if (statusPanel != null)
      statusPanel.setMaxWidth(halfScreen);
    if (uninitializedButtons != null)
      uninitializedButtons.setMaxWidth(halfScreen);
    inputPurchaseId.setPromptText("Purchase ID");
    initInputShelfId.setPromptText("Shelf ID");
    moveInputShelfId.setPromptText("Shelf ID");
    cheeseInputShelfId.setPromptText("Cheese ID");
    List<TOShelf> shelfList = CheECSEManagerFeatureSet1Controller.getShelves();
      for (TOShelf shelf : shelfList) {
          initInputShelfId.getItems().addAll(shelf.getShelfID());
          moveInputShelfId.getItems().addAll(shelf.getShelfID());
    	}
    moveInputShelfId.getSelectionModel().selectedItemProperty().addListener(
      (obs, oldShelf, newShelf) -> {
        if (newShelf != null) {
          updateCheeseChoices(newShelf);
        }
    });
    List<TOPurchase> purchaseList = PurchasesOrdersController.getPurchases();
      for (TOPurchase p : purchaseList) {
          inputPurchaseId.getItems().addAll(p.getId());
    	}
    if (initializedButtons != null)
      initializedButtons.setMaxWidth(halfScreen);
    if (bottomPanel != null)
      bottomPanel.setMaxWidth(halfScreen);
    
    choiceBoxMonthsAged.getItems().addAll(MaturationPeriod.values());
    updateUi();
  }

  private boolean robotIsActive() {
    return !RobotController.getRobotStatus().equals("Inactive");
  }

  private boolean robotIsInitialized() {
    return robotIsActive() && !RobotController.getRobotCurrentShelfId().equals("-");
  }

  private void updateUi() {
    boolean active = robotIsActive();
    boolean initialized = robotIsInitialized();

    textStatus.setText("Robot Status — " + RobotController.getRobotStatus());

    String currentShelf = RobotController.getRobotCurrentShelfId();
    if (currentShelf.equals("-")) {
      labelCurrentShelf.setText("At Entrance");
    } else {
      labelCurrentShelf.setText("At Shelf " + currentShelf);
    }

    buttonInitialize.setDisable(!active || initialized);
  }

  @FXML
  private void onInitialize(ActionEvent event) {
    if (!robotIsActive()) {
      textError.setText("Activate robot first.");
      return;
    }
    try {
      String shelfId = initInputShelfId.getValue();
      if (shelfId == null) throw new InvalidInputException("Please enter a shelf ID");
      String status = RobotController.initializeRobot(shelfId);
      if (!status.isEmpty())
        throw new InvalidInputException(status);
    } catch (RuntimeException e) {
      textError.setText(FormHelper.formatTextMessage(e));
    }
    updateUi();
  }

  @FXML
  private void onMoveShelf(ActionEvent event) {
    try {
      String shelfId = moveInputShelfId.getValue();
      if (shelfId == null) throw new InvalidInputException("Please enter a shelf ID");
      String status = RobotController.moveToShelf(shelfId);
      if (!status.isEmpty())
        throw new InvalidInputException(status);
    } catch (RuntimeException e) {
      textError.setText(FormHelper.formatTextMessage(e));
    }
    updateUi();
  }

  @FXML
  private void onTurnLeft(ActionEvent event) {
    String status = RobotController.turnLeft();
    textError.setText(status);
    updateUi();
  }

  @FXML
  private void onTurnRight(ActionEvent event) {
    String status = RobotController.turnRight();
    textError.setText(status);
    updateUi();
  }

  @FXML
  private void onMoveToCheese(ActionEvent event) {
    try {
      Integer cheeseId = cheeseInputShelfId.getValue();
      if (cheeseId == null) {
        throw new InvalidInputException("Please enter a cheese wheel ID");
      }
      String status = RobotController.moveToCheeseWheel(cheeseId);
      if (!status.isEmpty())
        throw new InvalidInputException(status);
    } catch (RuntimeException e) {
      textError.setText(FormHelper.formatTextMessage(e));
    }
    updateUi();
  }

  @FXML
  private void onTreatCheese(ActionEvent event) {
    String status = RobotController.performTreatment();
    textError.setText(status);
    updateUi();
  }

  @FXML
  private void onReturnEntrance(ActionEvent event) {
    String status = RobotController.moveToEntrance();
    textError.setText(status);
    updateUi();
  }

  @FXML
  private void onBack(ActionEvent event) {
    buttonBack.fireEvent(new PageSwitchEvent(new NavigationState<>(null, PageType.BACK, null)));
  }

  @FXML
  private void onTreatmentProtocol(ActionEvent event) {
    Integer purchaseId = inputPurchaseId.getValue();
    MaturationPeriod monthsAged = choiceBoxMonthsAged.getValue();

    if (purchaseId == null || monthsAged == null) {
      textError.setText("Please fill in all fields.");
      return;
    }

    String status =
        RobotController.purchaseTreatment(monthsAged.toString(), purchaseId);
    textError.setText(status);
    updateUi();
  }

  @FXML
  private void onViewLogs() {
    buttonViewLogs.fireEvent(new PageSwitchEvent(new NavigationState<>("Robot Logs",
        PageType.REDIRECT_DISPLAY, "view/page/robotstatus/RobotStatusLogDisplay.fxml")));
  }

  private void updateCheeseChoices(String shelfId) {
    // Clear previous options
    cheeseInputShelfId.getItems().clear();
    cheeseInputShelfId.setValue(null);

    // Get all cheese wheels
    List<TOCheeseWheel> cheeseList = CheECSEManagerFeatureSet3Controller.getCheeseWheels();

    for (TOCheeseWheel cheese : cheeseList) {
        if (shelfId.equals(cheese.getShelfID())) {
            cheeseInputShelfId.getItems().add(cheese.getId());
        }
    }
  }
}
