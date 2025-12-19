package ca.mcgill.ecse.cheecsemanager.fxml.controller.cheesewheel;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.TableColumnFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class CheeseWheelDisplayManyController implements Initializable {


  @FXML
  public VBox parentContainer;

  @FXML
  private TableView<TOCheeseWheel> table;

  private List<TOCheeseWheel> cheeseWheelList;
  private Supplier<List<TOCheeseWheel>> supplier;
  private Map<String, AttributeInfo> scope =
      Map.ofEntries(Map.entry("id", new AttributeInfo("BASIC", 0)),
          Map.entry("monthsAged", new AttributeInfo("BASIC", 1)),
          Map.entry("isSpoiled", new AttributeInfo("BASIC", 2)),
          Map.entry("purchaseDate", new AttributeInfo("BASIC", 3)),
          Map.entry("shelfID", new AttributeInfo("BASIC", 4)),
          Map.entry("column", new AttributeInfo("BASIC", 5)),
          Map.entry("row", new AttributeInfo("BASIC", 6)),
          Map.entry("isOrdered", new AttributeInfo("BASIC", 7)));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }

  private void refresh() {
    if (this.supplier != null) {
      cheeseWheelList = this.supplier.get();
    } else {
      cheeseWheelList = CheECSEManagerFeatureSet3Controller.getCheeseWheels();
    }
    if (cheeseWheelList == null || cheeseWheelList.isEmpty()) {
      cheeseWheelList = Collections.emptyList();
      table.setPlaceholder(new Label("Please add a new CheeseWheel"));
    }
    populateData();
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<List<TOCheeseWheel>> supplier) {
    // Reloads data to ensure latest values are fetched each time page is opened using Back
    this.supplier = supplier;
    this.scope = scope;
    refresh();
  }

  public void populateData() {
    ObservableList<TOCheeseWheel> list = FXCollections.observableArrayList(cheeseWheelList);

    table.getColumns().clear();

    // Column - id
    if (scope.containsKey("id") && scope.get("id").getScope().equals("BASIC")) {
      TableColumn<TOCheeseWheel, TOCheeseWheel> columnId =
          TableColumnFactory.createIdColumn("id", e -> e != null ? ("" + e.getId()) : "", w -> {
            NavigationState<Supplier<TOCheeseWheel>> state =
                new NavigationState<>("View CheeseWheel", PageType.REDIRECT_DISPLAY,
                    "view/page/cheesewheel/CheeseWheelDisplayOne.fxml");
            state.setData(() -> CheECSEManagerFeatureSet3Controller.getCheeseWheel(w.getId()));
            ApplicationNavigator.getInstance().switchPage("CheeseWheel",
                new PageSwitchEvent(state));
          });

      columnId.setText("Cheese ID"); // ADD THIS LINE
      columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
      table.getColumns().add(columnId);
    }
    // Column - monthsAged
    if (scope.containsKey("monthsAged") && scope.get("monthsAged").getScope().equals("BASIC")) {
      TableColumn<TOCheeseWheel, String> columnMonthsAged =
          TableColumnFactory.createTableColumn("monthsAged");
      columnMonthsAged.setCellValueFactory(new PropertyValueFactory<>("monthsAged"));
      table.getColumns().add(columnMonthsAged);
    }
    // Column - isSpoiled
    if (scope.containsKey("isSpoiled") && scope.get("isSpoiled").getScope().equals("BASIC")) {
      TableColumn<TOCheeseWheel, CheckBox> columnIsSpoiled =
          TableColumnFactory.createCheckboxColumn("isSpoiled", TOCheeseWheel::getIsOrdered);
      columnIsSpoiled.setCellValueFactory(new PropertyValueFactory<>("isSpoiled"));
      table.getColumns().add(columnIsSpoiled);
    }
    // Column - purchaseDate
    if (scope.containsKey("purchaseDate") && scope.get("purchaseDate").getScope().equals("BASIC")) {
      TableColumn<TOCheeseWheel, String> columnPurchaseDate =
          TableColumnFactory.createTableColumn("purchaseDate");
      columnPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
      table.getColumns().add(columnPurchaseDate);
    }
    // Column - shelfID
    if (scope.containsKey("shelfID") && scope.get("shelfID").getScope().equals("BASIC")) {
      TableColumn<TOCheeseWheel, String> columnShelfID =
          TableColumnFactory.createTableColumn("shelfID");
      columnShelfID.setText("Shelf ID"); // Explicitly set the display text
      columnShelfID.setCellValueFactory(new PropertyValueFactory<>("shelfID"));
      table.getColumns().add(columnShelfID);
    }
    // Column - column
    if (scope.containsKey("column") && scope.get("column").getScope().equals("BASIC")) {
      TableColumn<TOCheeseWheel, String> columnColumn =
          TableColumnFactory.createTableColumn("column");
      columnColumn.setCellValueFactory(new PropertyValueFactory<>("column"));
      table.getColumns().add(columnColumn);
    }
    // Column - row
    if (scope.containsKey("row") && scope.get("row").getScope().equals("BASIC")) {
      TableColumn<TOCheeseWheel, String> columnRow = TableColumnFactory.createTableColumn("row");
      columnRow.setCellValueFactory(new PropertyValueFactory<>("row"));
      table.getColumns().add(columnRow);
    }
    // Column - columnIsOrdered
    if (scope.containsKey("isOrdered") && scope.get("isOrdered").getScope().equals("BASIC")) {
      TableColumn<TOCheeseWheel, CheckBox> columnIsOrdered =
          TableColumnFactory.createCheckboxColumn("isOrdered", TOCheeseWheel::getIsOrdered);
      columnIsOrdered.setCellValueFactory(new PropertyValueFactory<>("isOrdered"));
      table.getColumns().add(columnIsOrdered);
    }

    // Sort Columns
    LayoutHelper.sortTableColumns(table, scope);
    TableColumn<TOCheeseWheel, HBox> columnAction = TableColumnFactory.createTableColumn("");
    // columnAction.setCellFactory(column -> new ActionButtonCell<>(this::updateCheeseWheel, null));
    columnAction.setCellFactory(column -> new TableCell<>() {
      private final Button editBtn = new Button("");
      private final Button assignBtn = new Button("");
      private final HBox box = new HBox(5, editBtn, assignBtn);
      {
        Image imgEdit =
            new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/edit.png")));
        ImageView iconEdit = new ImageView(imgEdit);
        iconEdit.setFitWidth(16);
        iconEdit.setFitHeight(16);
        editBtn.setGraphic(iconEdit);
        editBtn.getStyleClass().add("edit-button");
        editBtn.setOnAction(e -> {
          TOCheeseWheel data = getTableRow().getItem();
          if (data != null)
            updateCheeseWheel(data);
        });

        Image imgShelf =
            new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/shelf.png")));
        ImageView iconShelf = new ImageView(imgShelf);
        iconShelf.setFitWidth(16);
        iconShelf.setFitHeight(16);
        assignBtn.setGraphic(iconShelf);
        assignBtn.getStyleClass().add("edit-button");
        assignBtn.setOnAction(e -> {
          TOCheeseWheel data = getTableRow().getItem();
          if (data != null)
            assignCheeseWheel(data);
        });
      }

      @Override
      protected void updateItem(HBox item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          box.getChildren().clear();
          box.getChildren().add(editBtn);
          box.getChildren().add(assignBtn);
          box.setSpacing(16);
          box.setAlignment(Pos.CENTER);
          setGraphic(box);
        }
      }
    });
    table.getColumns().add(columnAction);
    // Set items first so rows are realized, then apply the row factory
    table.setItems(list);

    // Asynchronously refresh width after table is loaded
    Platform.runLater(() -> LayoutHelper.refreshColumnWidths(table));
  }

  // Handle ID Scope
  private void initIdScope() {}

  // Logic to Handle ALL Scope
  private void initAllScope() {
    Predicate<String> isColumnPresent =
        id -> table.getColumns().stream().anyMatch(col -> id.equals(col.getId()));
  }


  protected void updateCheeseWheel(TOCheeseWheel cheeseWheel) {
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


}
