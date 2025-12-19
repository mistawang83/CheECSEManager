package ca.mcgill.ecse.cheecsemanager.fxml.controller.cheesewheel;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ToastFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.InvalidInputException;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.google.common.base.Supplier;

public class CheeseWheelShelfFormController implements Initializable {

  @FXML
  private Label labelShelfId;
  @FXML
  private ChoiceBox<String> inputFieldShelfId;
  @FXML
  private Label labelRowNr;
  @FXML
  private ChoiceBox<Integer> inputFieldRowNr;
  @FXML
  private Label labelColNr;
  @FXML
  private ChoiceBox<Integer> inputFieldColNr;

  @FXML
  private Button buttonCancel;
  @FXML
  private Button buttonSave;

  @FXML
  private Text textError;

  private TOCheeseWheel currentCheeseWheel;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    // Initialize shelf id section
    labelShelfId.setText(FormHelper.convertCamelCaseToWords("shelfId"));
    List<TOShelf> shelfList = CheECSEManagerFeatureSet1Controller.getShelves();
      for (TOShelf shelf : shelfList) {
          inputFieldShelfId.getItems().addAll(shelf.getShelfID());
    	}
    inputFieldShelfId.getSelectionModel().selectedItemProperty().addListener(
    (obs, oldShelf, newShelf) -> {
      if (newShelf != null) {
        updateRowColChoices(newShelf);
      }
    }
);
    // Initialize row nr section
    labelRowNr.setText("Row Number");
    // Initialize col nr section
    labelColNr.setText("Column Number");
  }

  public void setData(TOForm<?, TOCheeseWheel> toForm) {
    if (toForm.getPageType().equals(PageType.UPDATE)) {
      handleUpdateInit(toForm.getData());
    }
  }


  public void handleUpdateInit(TOCheeseWheel cheeseWheel) {
    this.currentCheeseWheel = cheeseWheel;
    if (currentCheeseWheel.getShelfID() != null) {
      inputFieldShelfId.setValue(currentCheeseWheel.getShelfID());
      updateRowColChoices(currentCheeseWheel.getShelfID());
    }
    inputFieldRowNr.setValue(currentCheeseWheel.getRow());
    inputFieldColNr.setValue(currentCheeseWheel.getColumn());
    System.out.println("Preloaded CheeseWheel ShelfLocation data");
  }

  @FXML
  private void onSave(ActionEvent event) {
    try {
      String inputShelfId = inputFieldShelfId.getValue();
      if (inputShelfId == null) throw new InvalidInputException("Please enter a shelf ID");
      Integer inputRowNr = inputFieldRowNr.getValue();
      if (inputRowNr == null) throw new InvalidInputException("Please enter a row number");
      Integer inputColNr = inputFieldColNr.getValue();
      if (inputColNr == null) throw new InvalidInputException("Please enter a column number");
      String savedStatus = "";
      savedStatus = CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(
          currentCheeseWheel.getId(), inputShelfId, inputColNr, inputRowNr);
      currentCheeseWheel =
          CheECSEManagerFeatureSet3Controller.getCheeseWheel(currentCheeseWheel.getId());

      // Validate Controller response
      if (!savedStatus.isEmpty() && !savedStatus.equals("Assigned cheese wheel to shelf location."))
        throw new InvalidInputException(savedStatus);
      else {
        textError.setText("");
        buttonSave.setDisable(true);
        ToastFactory.createSuccess(buttonSave,
            "Successfully saved item. Redirecting back to table...");
        FormHelper.triggerAfterDelay(() -> {
          ApplicationNavigator.getInstance().updatePages("CheeseWheel");

          NavigationState<TOForm<?, TOCheeseWheel>> navState =
              new NavigationState<>(null, PageType.BACK, null);
          navState.setData(new TOForm<>(currentCheeseWheel, PageType.UPDATE));

          ApplicationNavigator.getInstance().switchPage("CheeseWheel",
              new PageSwitchEvent(navState));

          NavigationState<TOForm<?, TOCheeseWheel>> updateState = new NavigationState<>(null,
              PageType.UPDATE, "view/page/cheesewheel/CheeseWheelDisplayOne.fxml");
          updateState.setData(new TOForm<>(currentCheeseWheel, PageType.UPDATE));

          ApplicationNavigator.getInstance().switchPage("CheeseWheel",
              new PageSwitchEvent(updateState));


        }, 2);
      }

    } catch (RuntimeException e) {
      textError.setText(FormHelper.formatTextMessage(e));
    }
  }

  public void onCancel(ActionEvent actionEvent) {
    ApplicationNavigator.getInstance().switchPage("CheeseWheel",
        new PageSwitchEvent(new NavigationState<>(null, PageType.BACK, null)));
  }

  private void updateRowColChoices(String shelfId) {
  // find the selected shelf
  TOShelf shelf = CheECSEManagerFeatureSet1Controller.getShelves().stream()
      .filter(s -> s.getShelfID().equals(shelfId))
      .findFirst()
      .orElse(null);

  if (shelf == null) return;

  // Clear old values
  inputFieldRowNr.getItems().clear();
  inputFieldColNr.getItems().clear();

  // Populate new values
  for (int r = 1; r <= shelf.getMaxRows(); r++) {
    inputFieldRowNr.getItems().add(r);
  }

  for (int c = 1; c <= shelf.getMaxColumns(); c++) {
    inputFieldColNr.getItems().add(c);
  }

  // Reset displayed values
  inputFieldRowNr.setValue(null);
  inputFieldColNr.setValue(null);
}
}
