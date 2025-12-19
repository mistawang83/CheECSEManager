package ca.mcgill.ecse.cheecsemanager.fxml.controller.purchase;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ToastFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.InvalidInputException;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class PurchaseFormController implements Initializable {

  @FXML
  private Label labelFarmer;
  @FXML
  private ChoiceBox<String> choiceBoxFarmer;
  @FXML
  private Label labelNrCheeseWheels;
  @FXML
  private TextField inputFieldNrCheeseWheels;
  @FXML
  private Label labelMonthsAged;
  @FXML
  private ChoiceBox<MaturationPeriod> choiceBoxMonthsAged;
  @FXML
  private Label labelPurchaseDate;
  @FXML
  private DatePicker inputFieldPurchaseDate;

  @FXML
  private Button buttonCancel;
  @FXML
  private Button buttonSave;

  @FXML
  private Text textError;



  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

	  List<TOFarmer> farmerList = CheECSEManagerFeatureSet7Controller.getFarmers();
      for (TOFarmer farmer : farmerList) {
          choiceBoxFarmer.getItems().addAll(farmer.getEmail());
    	}

    // Initialize Label text with formatting
    labelFarmer.setText(FormHelper.convertCamelCaseToWords("farmerEmail"));
    labelNrCheeseWheels.setText(FormHelper.convertCamelCaseToWords("nrCheeseWheels"));
    inputFieldNrCheeseWheels.setPromptText("Enter number of cheese wheels to buy");
    // Initialize Label text with formatting
    labelMonthsAged.setText(FormHelper.convertCamelCaseToWords("monthsAged"));
    // Initialize enum dropdown
    choiceBoxMonthsAged.getItems().addAll(MaturationPeriod.values());
    // Initialize purchase date section
    labelPurchaseDate.setText(FormHelper.convertCamelCaseToWords("purchaseDate"));
    inputFieldPurchaseDate.setValue(LocalDate.now());
  }

  @FXML
  private void onSave(ActionEvent event) {
    try {
      MaturationPeriod monthsAged = choiceBoxMonthsAged.getValue();
      String monthsStr = monthsAged.toString();
      String savedStatus = "";
      String farmerEmail = choiceBoxFarmer.getValue();
      Integer nrCW = Integer.valueOf(inputFieldNrCheeseWheels.getText());
      LocalDate purchaseDate = inputFieldPurchaseDate.getValue();
      Date sqlDate = Date.valueOf(purchaseDate);
      savedStatus = CheECSEManagerFeatureSet4Controller.buyCheeseWheels(farmerEmail, sqlDate, nrCW,
          monthsStr);


      // Validate Controller response
      if (!savedStatus.isEmpty() && !savedStatus.equals("The cheese wheels were purchased."))
        throw new InvalidInputException(savedStatus);
      else {
        textError.setText("");
        buttonSave.setDisable(true);
        ToastFactory.createSuccess(buttonSave,
            "Successfully created purchase. Redirecting back to table...");
        FormHelper
            .triggerAfterDelay(() -> ApplicationNavigator.getInstance().switchPage("Purchase",
                new PageSwitchEvent(new NavigationState<>(null, PageType.BACK, null))), 2);
      }

    } catch (RuntimeException e) {
      textError.setText(FormHelper.formatTextMessage(e));
    }
  }

  public void onCancel(ActionEvent actionEvent) {
    ApplicationNavigator.getInstance().switchPage("CheeseWheel",
        new PageSwitchEvent(new NavigationState<>(null, PageType.BACK, null)));
  }
}
