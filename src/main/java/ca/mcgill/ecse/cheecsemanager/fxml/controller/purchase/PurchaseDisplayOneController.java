package ca.mcgill.ecse.cheecsemanager.fxml.controller.purchase;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.TOPurchase;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class PurchaseDisplayOneController implements Initializable {

  @FXML
  private GridPane gridPane;

  private TOPurchase purchase;
  private Supplier<TOPurchase> supplier;
  private Map<String, AttributeInfo> scope =
      Map.ofEntries(Map.entry("id", new AttributeInfo("BASIC", 0)),
          Map.entry("transactionDate", new AttributeInfo("BASIC", 1)),
          Map.entry("nrCheeseWheels", new AttributeInfo("BASIC", 2)),
          Map.entry("monthsAged", new AttributeInfo("BASIC", 3)),
          Map.entry("farmerEmail", new AttributeInfo("BASIC", 4)));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }

  private void refresh() {
    if (supplier != null) {
      this.purchase = this.supplier.get();
    }
    if (this.purchase != null)
      populateData();
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<TOPurchase> supplier) {
    this.supplier = supplier;
    this.scope = scope;
    refresh();
  }

  public void populateData() {
    gridPane.getChildren().clear();
    gridPane.getRowConstraints().clear();
    RowConstraints row = new RowConstraints();
    row.setMinHeight(10);
    row.setPrefHeight(30);
    row.setVgrow(Priority.SOMETIMES);

    // Field - transactionDate
    if (scope.containsKey("transactionDate")
        && scope.get("transactionDate").getScope().equals("BASIC")) {
      Label label = new Label(FormHelper.convertCamelCaseToWords("transactionDate"));
      label.setId("transactionDate, 0");
      gridPane.add(label, 0, 1);
      StackPane pane = new StackPane();
      Text text = new Text(String.valueOf(purchase.getPurchaseDate()));
      pane.setId("transactionDate, 1");
      pane.getChildren().add(text);
      gridPane.add(pane, 1, 1);
      gridPane.getRowConstraints().add(row);
    }
    // Field - nrCheeseWheels
    if (scope.containsKey("nrCheeseWheels")
        && scope.get("nrCheeseWheels").getScope().equals("BASIC")) {
      Label label = new Label(FormHelper.convertCamelCaseToWords("nrCheeseWheels"));
      label.setId("nrCheeseWheels, 0");
      gridPane.add(label, 0, 2);
      StackPane pane = new StackPane();
      Text text = new Text(String.valueOf(purchase.getNrCheeseWheels()));
      pane.setId("nrCheeseWheels, 1");
      pane.getChildren().add(text);
      gridPane.add(pane, 1, 2);
      gridPane.getRowConstraints().add(row);
    }
    // Field - monthsAged
    if (scope.containsKey("monthsAged") && scope.get("monthsAged").getScope().equals("BASIC")) {
      Label label = new Label(FormHelper.convertCamelCaseToWords("monthsAged"));
      label.setId("monthsAged, 0");
      gridPane.add(label, 0, 3);
      StackPane pane = new StackPane();
      Text text = new Text(String.valueOf(purchase.getMonthsAgeds()));
      pane.setId("monthsAged, 1");
      pane.getChildren().add(text);
      gridPane.add(pane, 1, 3);
      gridPane.getRowConstraints().add(row);
    }
    // Field - farmerEmail
    if (scope.containsKey("farmerEmail") && scope.get("farmerEmail").getScope().equals("BASIC")) {
      Label label = new Label(FormHelper.convertCamelCaseToWords("farmerEmail"));
      label.setId("farmerEmail, 0");
      gridPane.add(label, 0, 4);
      StackPane pane = new StackPane();
      Text text = new Text(String.valueOf(purchase.getFarmerEmail()));
      pane.setId("farmerEmail, 1");
      pane.getChildren().add(text);
      gridPane.add(pane, 1, 4);
      gridPane.getRowConstraints().add(row);
    }

    int newRow = LayoutHelper.sortGrid(gridPane, scope);
    Button updateButton = LayoutHelper.createGridEditButton(getClass());
    updateButton.setOnAction(this::updatePurchase);
    gridPane.add(updateButton, 1, newRow);
    gridPane.getRowConstraints().add(row);
  }

  @FXML
  public void updatePurchase(ActionEvent event) {
    NavigationState<TOForm<?, TOPurchase>> state = new NavigationState<>("Update Purchase",
        PageType.UPDATE, "view/page/purchase/PurchaseForm.fxml");
    state.setData(new TOForm<>(purchase, PageType.UPDATE));
    ApplicationNavigator.getInstance().switchPage("Purchase", new PageSwitchEvent(state));
  }

  @FXML
  public void onBack(ActionEvent event) {
    NavigationState<?> state = new NavigationState<>("Back", PageType.BACK, null);
    ApplicationNavigator.getInstance().switchPage("Purchase", new PageSwitchEvent(state));
  }
}
