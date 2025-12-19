package ca.mcgill.ecse.cheecsemanager.fxml.controller.facilitymanager;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFacilityManager;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class FacilityManagerFormController implements Initializable {

  @FXML
  private Label labelEmail;
  @FXML
  private TextField inputFieldEmail;
  @FXML
  private Label labelPassword;
  @FXML
  private TextField inputFieldPassword;

  @FXML
  private Button buttonCancel;
  @FXML
  private Button buttonSave;

  @FXML
  private Text textError;

  
  @FXML
  private TextField inputFieldPasswordVisible;
  @FXML
  private Button buttonTogglePassword;

  private Object currentFacilityManager;

  private ImageView hideIcon = new ImageView(
        new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/hide.png"))));
  private ImageView showIcon = new ImageView(
          new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/view.png"))));


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    showIcon.setFitWidth(14);
    showIcon.setFitHeight(14);
    hideIcon.setFitWidth(14);
    hideIcon.setFitHeight(14);

    // Keep visible and covered password fields in sync
    inputFieldPasswordVisible.textProperty().bindBidirectional(inputFieldPassword.textProperty());
    // reveal button graphic to showIcon by default
    buttonTogglePassword.setGraphic(showIcon);
    buttonTogglePassword.setText("");
    // Initialize Label text with formatting
    labelEmail.setText(FormHelper.convertCamelCaseToWords("email"));
    inputFieldEmail.setText(FacilityManagerDisplayOneController.MANAGER_EMAIL);
    inputFieldEmail.setDisable(true);
    // Initialize Label text with formatting
    labelPassword.setText(FormHelper.convertCamelCaseToWords("password"));
    if (currentFacilityManager == null) {
      currentFacilityManager = CheECSEManagerFeatureSet1Controller.getFacilityManager();
    }
  }

  public void setData(TOForm<?, Object> toForm) {
    if (toForm.getPageType().equals(PageType.UPDATE)) {
      // Object to store current Facility Manager
      currentFacilityManager = toForm;
      TOFacilityManager manager = CheECSEManagerFeatureSet1Controller.getFacilityManager();
      if (manager == null) {
        currentFacilityManager = null;
      }
    }
  }

  @FXML
  private void onSave(ActionEvent event) {
    try {
      String password = inputFieldPassword.getText();
      String email = inputFieldEmail.getText();
      String savedStatus = "";

      // Perform Update Operation
      if (currentFacilityManager != null) {
        savedStatus = CheECSEManagerFeatureSet1Controller.updateFacilityManager(password);
      } else {
        savedStatus = CheECSEManagerFeatureSet1Controller.createFacilityManager(email, password);
      }

      // The controller method returns the password on success
      if (savedStatus.equals(password)) {
        savedStatus = "";
      }

      // Validate Controller response
      if (!savedStatus.isEmpty())
        throw new InvalidInputException(savedStatus);
      else {
        textError.setText("");
        buttonSave.setDisable(true);
        ToastFactory.createSuccess(buttonSave,
            "Successfully saved item. Redirecting back to table...");
        FormHelper.triggerAfterDelay(
            () -> buttonCancel
                .fireEvent(new PageSwitchEvent(new NavigationState<>(null, PageType.BACK, null))),
            2);
      }

    } catch (RuntimeException e) {
      textError.setText(FormHelper.formatTextMessage(e));
    }
  }

  @FXML
  private void onTogglePassword() {
      boolean showing = inputFieldPasswordVisible.isVisible();
      
      inputFieldPasswordVisible.setVisible(!showing);
      inputFieldPasswordVisible.setManaged(!showing);

      inputFieldPassword.setVisible(showing);
      inputFieldPassword.setManaged(showing);

      if (!showing) {
        buttonTogglePassword.setGraphic(hideIcon);
      } else {
        buttonTogglePassword.setGraphic(showIcon);
      }
  }

  public void onCancel(ActionEvent actionEvent) {
    buttonCancel.fireEvent(new PageSwitchEvent(new NavigationState<>(null, PageType.BACK, null)));
  }

}
