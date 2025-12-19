package ca.mcgill.ecse.cheecsemanager.fxml.controller.order;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
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
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.Date;
import java.time.LocalDate;

public class OrderFormController implements Initializable {

  @FXML
  private Label labelCompany;
  @FXML
  private ChoiceBox<String> choiceBoxCompany;
  @FXML
  private Label labelNrCheeseWheels;
  @FXML
  private TextField inputFieldNrCheeseWheels;
  @FXML
  private Label labelMonthsAged;
  @FXML
  private ChoiceBox<MaturationPeriod> choiceBoxMonthsAged;
  @FXML
  private Label labelTransactionDate;
  @FXML
  private DatePicker inputFieldTransactionDate;
  @FXML
  private Label labelDeliveryDate;
  @FXML
  private DatePicker inputFieldDeliveryDate;

  @FXML
  private Button buttonCancel;
  @FXML
  private Button buttonSave;

  @FXML
  private Text textError;



  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
	  
	  List<TOWholesaleCompany> companyList = CheECSEManagerFeatureSet6Controller.getWholesaleCompanies();
      for (TOWholesaleCompany company : companyList) {
          choiceBoxCompany.getItems().addAll(company.getName());
    	}

    // Initialize Label text with formatting
    labelCompany.setText(FormHelper.convertCamelCaseToWords("companyName"));
//    inputFieldCompany.setPromptText("Enter company name");
    labelNrCheeseWheels.setText(FormHelper.convertCamelCaseToWords("nrCheeseWheels"));
    inputFieldNrCheeseWheels.setPromptText("Enter number of cheese wheels");
    // Initialize Label text with formatting
    labelMonthsAged.setText(FormHelper.convertCamelCaseToWords("monthsAged"));
    // Initialize enum dropdown
    choiceBoxMonthsAged.getItems().addAll(MaturationPeriod.values());
    // Initialize transaction date section
    labelTransactionDate.setText(FormHelper.convertCamelCaseToWords("transactionDate"));
    inputFieldTransactionDate.setValue(LocalDate.now());
    // Initialize delivery date section
    labelDeliveryDate.setText(FormHelper.convertCamelCaseToWords("deliveryDate"));
  }

  @FXML
  private void onSave(ActionEvent event) {
    try {
      MaturationPeriod monthsAged = choiceBoxMonthsAged.getValue();
      String monthsStr = monthsAged.toString();
      String savedStatus = "";
      String companyName = choiceBoxCompany.getValue();
      Integer nrCW = Integer.valueOf(inputFieldNrCheeseWheels.getText());
      LocalDate transactionDate = inputFieldTransactionDate.getValue();
      Date transactionSqlDate = Date.valueOf(transactionDate);
      LocalDate deliveryDate = inputFieldDeliveryDate.getValue();
      Date deliverySqlDate = Date.valueOf(deliveryDate);
      savedStatus = CheECSEManagerFeatureSet5Controller.sellCheeseWheels(companyName,
          transactionSqlDate, nrCW, monthsStr, deliverySqlDate);


      // Validate Controller response
      if (!savedStatus.isEmpty())
        throw new InvalidInputException(savedStatus);
      else {
        textError.setText("");
        buttonSave.setDisable(true);
        ToastFactory.createSuccess(buttonSave,
            "Successfully created order. Redirecting back to table...");
        FormHelper
            .triggerAfterDelay(() -> ApplicationNavigator.getInstance().switchPage("Order",
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
