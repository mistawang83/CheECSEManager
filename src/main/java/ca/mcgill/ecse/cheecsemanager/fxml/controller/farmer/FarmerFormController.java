package ca.mcgill.ecse.cheecsemanager.fxml.controller.farmer;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
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

public class FarmerFormController implements Initializable {

  @FXML
  private Label labelEmail;
  @FXML
  private TextField inputFieldEmail;
  @FXML
  private Label labelPassword;
  @FXML
  private TextField inputFieldPassword;
  @FXML
  private TextField inputFieldPasswordVisible;
  @FXML
  private Button buttonTogglePassword;
  @FXML
  private Label labelName;
  @FXML
  private TextField inputFieldName;
  @FXML
  private Label labelAddress;
  @FXML
  private TextField inputFieldAddress;

  @FXML
  private Button buttonCancel;
  @FXML
  private Button buttonSave;

  @FXML
  private Text textError;

  private TOFarmer currentFarmer;

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
    labelName.setText("Name (optional)");
    // Initialize Label text with formatting
    labelEmail.setText(FormHelper.convertCamelCaseToWords("email"));
    // Initialize Label text with formatting
    labelPassword.setText(FormHelper.convertCamelCaseToWords("password"));
    // Initialize Label text with formatting
    labelAddress.setText(FormHelper.convertCamelCaseToWords("address"));
  }

  public void setData(TOForm<?, TOFarmer> toForm) {
    if (toForm.getPageType().equals(PageType.UPDATE)) {
      handleUpdateInit(toForm.getData());
    }
  }


  public void handleUpdateInit(TOFarmer farmer) {
    this.currentFarmer = farmer;

    inputFieldEmail.setText(farmer.getEmail());
    inputFieldPassword.setText(farmer.getPassword());
    inputFieldAddress.setText(farmer.getAddress());
    inputFieldName.setText(farmer.getName());

    // Mark Key field as Disabled
    inputFieldEmail.setDisable(true);

    System.out.println("Preloaded Farmer data");
  }

  @FXML
  private void onSave(ActionEvent event) {
    try {
      String email = inputFieldEmail.getText();
      String password = inputFieldPassword.getText();
      String name = inputFieldName.getText();
      String address = inputFieldAddress.getText();
      String savedStatus = "";
      // Perform Update Operation
      if (currentFarmer != null) {
        savedStatus =
            CheECSEManagerFeatureSet7Controller.updateFarmer(email, password, name, address);
        // Refresh current farmer from persistence to ensure latest values
        currentFarmer = CheECSEManagerFeatureSet7Controller.getFarmer(email);
      }
      // Perform Add Operation
      if (currentFarmer == null) {
        savedStatus =
            CheECSEManagerFeatureSet3Controller.registerFarmer(email, password, name, address);
        // Load newly created farmer
        currentFarmer = CheECSEManagerFeatureSet7Controller.getFarmer(email);
      }

      // Validate Controller response
      if (!savedStatus.isEmpty())
        throw new InvalidInputException(savedStatus);
      else {
        textError.setText("");
        buttonSave.setDisable(true);
        ToastFactory.createSuccess(buttonSave,
            "Successfully saved item. Redirecting back to table...");
        FormHelper.triggerAfterDelay(() -> {
          // Ensure pages state is refreshed
          ApplicationNavigator.getInstance().updatePages("Farmer");

          // First navigate back to list/table view
          NavigationState<TOForm<?, TOFarmer>> backState =
              new NavigationState<>(null, PageType.BACK, null);
          backState.setData(new TOForm<>(currentFarmer, PageType.UPDATE));
          ApplicationNavigator.getInstance().switchPage("Farmer", new PageSwitchEvent(backState));

          // Then navigate to display-one page for the updated/added farmer
          NavigationState<TOForm<?, TOFarmer>> updateState = new NavigationState<>(null,
              PageType.UPDATE, "view/page/farmer/FarmerDisplayOne.fxml");
          updateState.setData(new TOForm<>(currentFarmer, PageType.UPDATE));
          ApplicationNavigator.getInstance().switchPage("Farmer", new PageSwitchEvent(updateState));
        }, 2);
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
