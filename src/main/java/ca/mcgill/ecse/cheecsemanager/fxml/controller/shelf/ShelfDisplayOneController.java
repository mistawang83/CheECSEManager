package ca.mcgill.ecse.cheecsemanager.fxml.controller.shelf;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ItemPanel;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class ShelfDisplayOneController implements Initializable {

  @FXML
  private ItemPanel shelfInfoPanel;
  @FXML
  private ShelfCheeseGrid cheeseGrid;

  @FXML
  private Label labelShelfId;
  @FXML
  private Label labelMaxColumns;
  @FXML
  private Label labelMaxRows;

  private Supplier<TOShelf> supplier;
  private TOShelf shelf;
  private Map<String, AttributeInfo> scope =
      Map.ofEntries(Map.entry("shelfID", new AttributeInfo("BASIC", 0)),
          Map.entry("maxColumns", new AttributeInfo("BASIC", 1)),
          Map.entry("maxRows", new AttributeInfo("BASIC", 2)));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }

  private void refresh() {
    System.out.println("supplier = " + supplier);
    if (supplier != null) {
      shelf = supplier.get();
    }
    if (shelf != null)
      populateData();
  }

  // Overload to match reflective navigation call: setData(String, Map, Object)
  public void setData(String title, Map<String, AttributeInfo> scope, Object data) {
    Supplier<TOShelf> supplierArg = null;
    if (data instanceof Supplier<?>) {
      supplierArg = (Supplier<TOShelf>) data;
    }
    if (scope == null) {
      scope = this.scope;
    }
    setData(scope, supplierArg);
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<TOShelf> supplier) {
    this.scope = scope;
    this.supplier = supplier;
    refresh();
  }

  private void populateData() {
    if (scope.containsKey("shelfID")) {
      labelShelfId.setText("ID: " + shelf.getShelfID());
    }
    if (scope.containsKey("maxColumns")) {
      labelMaxColumns.setText("Columns: " + shelf.getMaxColumns());
    }
    if (scope.containsKey("maxRows")) {
      labelMaxRows.setText("Rows: " + shelf.getMaxRows());
    }
    // Provide shelf to grid for visual layout
    cheeseGrid.setShelf(shelf);
  }

  public void onBack(ActionEvent event) {
    NavigationState<?> state = new NavigationState<>("Back", PageType.BACK, null);
    ApplicationNavigator.getInstance().switchPage("Shelf", new PageSwitchEvent(state));
  }

}
