package ca.mcgill.ecse.cheecsemanager.fxml.controller.cheesewheel;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.DetailView;
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
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class CheeseWheelDisplayOneController implements Initializable {

  @FXML
  private DetailView detailView;

  @FXML
  private DetailView extraDetailView;

  private TOCheeseWheel cheeseWheel;
  private Supplier<TOCheeseWheel> supplier;

  private final Map<String, AttributeInfo> defaultScope =
      Map.ofEntries(Map.entry("id", new AttributeInfo("BASIC", 0)),
          Map.entry("monthsAged", new AttributeInfo("BASIC", 1)),
          Map.entry("isSpoiled", new AttributeInfo("BASIC", 2)),
          Map.entry("purchaseDate", new AttributeInfo("BASIC", 3)),
          Map.entry("shelfID", new AttributeInfo("BASIC", 4)),
          Map.entry("column", new AttributeInfo("BASIC", 5)),
          Map.entry("row", new AttributeInfo("BASIC", 6)),
          Map.entry("isOrdered", new AttributeInfo("BASIC", 7)));

  private Map<String, AttributeInfo> purchaseScope =
      Map.ofEntries(Map.entry("farmer", new AttributeInfo("BASIC", 0)),
          Map.entry("shelfLocation", new AttributeInfo("BASIC", 1)));

  private Map<String, AttributeInfo> scope = defaultScope;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }


  private void refresh() {
    if (supplier != null) {
      // Reloads data to ensure latest values are fetched each time page is opened using Back
      this.cheeseWheel = this.supplier.get();
    }

    if (this.cheeseWheel != null) {
      populateData();
      populateExtraData();
    }
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<TOCheeseWheel> supplier) {
    this.supplier = supplier;
    this.scope = scope;
    refresh();
  }

  public void setData(TOForm<?, TOCheeseWheel> form) {
    this.supplier = new Supplier<TOCheeseWheel>() {
      @Override
      public TOCheeseWheel get() {
        return form.getData();
      }
    };
    this.scope = defaultScope;
    refresh();
  }

  public void setData(String title, Map<String, AttributeInfo> scope, Object data) {
    if (data instanceof Supplier<?>) {
      this.cheeseWheel = ((Supplier<TOCheeseWheel>) data).get();
    }
    if (scope == null) {
      this.scope = defaultScope;
    }
    refresh();
  }

  public void populateData() {

    Map<String, DetailInfo> details = new java.util.HashMap<>();

    // Field - ID
    {
      StackPane pane = new StackPane();
      Text idText = new Text(String.valueOf(cheeseWheel.getId()));
      idText.getStyleClass().add("text-field-display");
      pane.setId("id, 1");
      pane.getChildren().add(idText);
      details.put("id", new DetailInfo("id", String.valueOf(cheeseWheel.getId()), pane));
    }

    // Field - MonthsAged
    {
      StackPane pane = new StackPane();
      Text monthsAgedText = new Text(String.valueOf(cheeseWheel.getMonthsAged()));
      monthsAgedText.getStyleClass().add("text-field-display");
      pane.setId("monthsAged, 1");
      pane.getChildren().add(monthsAgedText);
      details.put("monthsAged",
          new DetailInfo("monthsAged", String.valueOf(cheeseWheel.getMonthsAged()), pane));
    }

    // Field - Is Spoiled
    {
      StackPane pane = new StackPane();
      CheckBox box = new CheckBox();
      box.setSelected(cheeseWheel.getIsSpoiled());
      box.setMouseTransparent(true);
      box.setId("isSpoiled, 1");
      pane.getChildren().add(box);
      details.put("isSpoiled",
          new DetailInfo("isSpoiled", String.valueOf(cheeseWheel.getIsSpoiled()), pane));
    }

    // Field - Purchase Date
    {
      StackPane pane = new StackPane();
      Text purchaseDateText = new Text(String.valueOf(cheeseWheel.getPurchaseDate()));
      purchaseDateText.getStyleClass().add("text-field-display");
      pane.setId("purchaseDate, 1");
      pane.getChildren().add(purchaseDateText);
      details.put("purchaseDate",
          new DetailInfo("purchaseDate", String.valueOf(cheeseWheel.getPurchaseDate()), pane));
    }

    detailView.mapDetails(scope, details);

    int newRow = LayoutHelper.sortGrid(detailView, scope);
    // Add Update Button
    Button updateButton = LayoutHelper.createGridEditButton(getClass());
    updateButton.setOnAction(this::updateCheeseWheel);
    detailView.add(updateButton, 1, newRow);

  }

  private TOFarmer getCheeseFarmer() {
    return CheECSEManagerFeatureSet7Controller.getFarmers().stream()
        .filter(farmer -> Arrays.asList(farmer.getCheeseWheelIDs()).contains(cheeseWheel.getId()))
        .findFirst().orElse(null);
  }

  private void populateExtraData() {
    Map<String, DetailInfo> details = new java.util.HashMap<>();

    // Field - Farmer
    {
      TOFarmer farmer = getCheeseFarmer();
      System.out.println("Farmer: " + farmer);
      if (farmer != null) {
        StackPane pane = new StackPane();
        Text farmerText = new Text(farmer.getEmail());
        farmerText.getStyleClass().add("text-field-display");
        pane.setId("farmer, 1");
        pane.getChildren().add(farmerText);
        details.put("farmer", new DetailInfo("farmer", farmer.getEmail(), pane));
      }
    }

    // Field - Shelf location
    {

      String location = cheeseWheel.getShelfID() == null ? "Not Assigned"
          : "Row " + cheeseWheel.getRow() + ", Col " + cheeseWheel.getColumn();
      StackPane pane = new StackPane();
      Text locationText = new Text(location);
      locationText.getStyleClass().add("text-field-display");
      pane.setId("shelfLocation, 1");
      pane.getChildren().add(locationText);
      details.put("shelfLocation", new DetailInfo("shelfLocation", location, pane));
    }

    extraDetailView.mapDetails(purchaseScope, details);

    int newRow = LayoutHelper.sortGrid(detailView, scope);
    // Add Assign Button
    {
      Button assignBtn = new Button("");
      Image imgShelf =
          new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/shelf.png")));
      ImageView iconShelf = new ImageView(imgShelf);
      iconShelf.setFitWidth(16);
      iconShelf.setFitHeight(16);
      assignBtn.setGraphic(iconShelf);
      assignBtn.getStyleClass().add("edit-button");
      assignBtn.setOnAction(e -> {
        assignCheeseWheel(cheeseWheel);
      });
      extraDetailView.add(assignBtn, 1, newRow);
    }

  }

  // Handle ID Scope
  private void initIdScope() {
    RowConstraints row = new RowConstraints();
    row.setMinHeight(10);
    row.setPrefHeight(30);
    row.setVgrow(Priority.SOMETIMES);
  }

  // Handle ALL Scope
  private void initAllScope() {
    RowConstraints row = new RowConstraints();
    row.setMinHeight(30);
    row.setVgrow(Priority.SOMETIMES);
  }

  @FXML
  public void updateCheeseWheel(ActionEvent event) {
    NavigationState<TOForm<?, TOCheeseWheel>> state = new NavigationState<>("Update CheeseWheel",
        PageType.UPDATE, "view/page/cheesewheel/CheeseWheelForm.fxml");
    state.setData(new TOForm<>(cheeseWheel, PageType.UPDATE));
    ApplicationNavigator.getInstance().switchPage("CheeseWheel", new PageSwitchEvent(state));
  }

  public void assignCheeseWheel(TOCheeseWheel cheeseWheel) {
    NavigationState<TOForm<?, TOCheeseWheel>> state =
        new NavigationState<>("Assign CheeseWheel to Shelf", PageType.UPDATE,
            "view/page/cheesewheel/CheeseWheelShelfForm.fxml");
    state.setData(new TOForm<>(cheeseWheel, PageType.UPDATE));
    ApplicationNavigator.getInstance().switchPage("CheeseWheel", new PageSwitchEvent(state));
  }

  @FXML
  public void onBack(ActionEvent event) {
    NavigationState<?> state = new NavigationState<>("Back", PageType.BACK, null);
    ApplicationNavigator.getInstance().switchPage("CheeseWheel", new PageSwitchEvent(state));
  }

}
