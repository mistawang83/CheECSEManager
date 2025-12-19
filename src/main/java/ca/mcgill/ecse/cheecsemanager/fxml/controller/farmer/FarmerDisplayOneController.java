package ca.mcgill.ecse.cheecsemanager.fxml.controller.farmer;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.DetailView;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ItemPanel;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ObscuredLabel;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.DetailInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

import java.text.SimpleDateFormat;

import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class FarmerDisplayOneController implements Initializable {

  @FXML
  private DetailView detailView;

  @FXML
  private TilePane cheeseView;

  private TOFarmer farmer;
  private Supplier<TOFarmer> supplier;

  private Map<String, AttributeInfo> defaultScope =
      Map.ofEntries(Map.entry("name", new AttributeInfo("BASIC", 0)),
          Map.entry("email", new AttributeInfo("BASIC", 1)),
          Map.entry("address", new AttributeInfo("BASIC", 3)),
          Map.entry("password", new AttributeInfo("BAISC", 4)));

  private Map<String, AttributeInfo> scope = defaultScope;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }

  private void refresh() {
    if (supplier != null) {
      // Reloads data to ensure latest values are fetched each time page is opened using Back
      this.farmer = this.supplier.get();
    }

    if (this.farmer != null) {
      populateData();
      populateCheese();
    }
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<TOFarmer> supplier) {
    this.supplier = supplier;
    this.scope = scope;
    refresh();
  }

  public void setData(TOForm<?, TOFarmer> form) {
    this.supplier = new Supplier<TOFarmer>() {
      @Override
      public TOFarmer get() {
        return form.getData();
      }
    };
    this.scope = defaultScope;
    refresh();
  }

  // Overload to match reflective navigation call pattern: setData(String title, Map scope, Object
  // data)
  @SuppressWarnings("unchecked")
  public void setData(String title, Map<String, AttributeInfo> scope, Object data) {
    Supplier<TOFarmer> supplierArg = null;
    if (data instanceof Supplier) {
      try {
        supplierArg = (Supplier<TOFarmer>) data;
      } catch (ClassCastException e) {
        // Ignore invalid cast; leave supplierArg null
      }
    }
    if (scope == null) {
      scope = this.defaultScope;
    }
    setData(scope, supplierArg);
  }

  public void populateData() {

    Map<String, DetailInfo> details = new HashMap<>();

    // Field - name
    {
      StackPane pane = new StackPane();
      Text nameText = new Text(farmer.getName());
      pane.setId("name, 1");
      nameText.getStyleClass().add("text-field-display");
      pane.getChildren().add(nameText);
      details.put("name", new DetailInfo("name", farmer.getName(), pane));
    }

    // Field - email
    {
      StackPane pane = new StackPane();
      Text emailText = new Text(farmer.getEmail());
      pane.setId("email, 1");
      emailText.getStyleClass().add("text-field-display");
      pane.getChildren().add(emailText);
      details.put("email", new DetailInfo("email", farmer.getEmail(), pane));
    }

    // Field - address
    {
      StackPane pane = new StackPane();
      Text addressText = new Text(farmer.getAddress());
      pane.setId("address, 1");
      addressText.getStyleClass().add("text-field-display");
      pane.getChildren().add(addressText);
      details.put("address", new DetailInfo("address", farmer.getAddress(), pane));
    }

    // Field - password
    {
      StackPane pane = new StackPane();
      ObscuredLabel passwordText = new ObscuredLabel(farmer.getPassword());
      pane.setId("password, 1");
      pane.getChildren().add(passwordText);
      details.put("password", new DetailInfo("password", farmer.getPassword(), pane));
    }

    detailView.mapDetails(scope, details);

    // Sort Grid
    int newRow = LayoutHelper.sortGrid(detailView, scope);
    // Add Update Button
    Button updateButton = LayoutHelper.createGridEditButton(getClass());
    updateButton.setOnAction(this::updateFarmer);
    detailView.add(updateButton, 1, newRow);

  }

  public void populateCheese() {
    cheeseView.getChildren().clear();

    Integer[] cheeseIds = farmer.getCheeseWheelIDs();
    java.sql.Date[] purchaseDates = farmer.getPurchaseDates();
    String[] monthsAgeds = farmer.getMonthsAgeds();

    // Use the minimum length across arrays to avoid IndexOutOfBounds if data inconsistent
    int length = Math.min(cheeseIds.length, Math.min(purchaseDates.length, monthsAgeds.length));

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    for (int i = 0; i < length; i++) {
      Integer id = cheeseIds[i];
      Date date = purchaseDates[i];
      String monthsAged = monthsAgeds[i];

      VBox panel = new VBox();
      panel.setAlignment(Pos.TOP_CENTER);
      panel.getStyleClass().addAll("panel", "cheese-wheel-panel");

      Label idLabel = new Label("ID: " + id);
      idLabel.getStyleClass().add("text-normal");
      Label dateLabel = new Label("Purchased: " + (date == null ? "-" : dateFormat.format(date)));
      dateLabel.getStyleClass().add("text-normal");
      Label monthsLabel = new Label("Months Aged: " + monthsAged);
      monthsLabel.getStyleClass().add("text-normal");

      panel.getChildren().addAll(idLabel, dateLabel, monthsLabel);

      cheeseView.getChildren().add(panel);
    }
  }

  @FXML
  public void updateFarmer(ActionEvent event) {
    NavigationState<TOForm<?, TOFarmer>> state =
        new NavigationState<>("Update Farmer", PageType.UPDATE, "view/page/farmer/FarmerForm.fxml");
    state.setData(new TOForm<>(farmer, PageType.UPDATE));
    ApplicationNavigator.getInstance().switchPage("Farmer", new PageSwitchEvent(state));
  }

  @FXML
  public void onBack(ActionEvent event) {
    NavigationState<?> state = new NavigationState<>(null, PageType.BACK, null);
    ApplicationNavigator.getInstance().switchPage("Farmer", new PageSwitchEvent(state));
  }
}
