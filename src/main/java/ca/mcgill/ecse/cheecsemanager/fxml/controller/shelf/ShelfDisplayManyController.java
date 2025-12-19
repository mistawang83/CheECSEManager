package ca.mcgill.ecse.cheecsemanager.fxml.controller.shelf;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.DeleteDialogController;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ActionButtonPane;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ItemPanel;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ToastFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class ShelfDisplayManyController implements Initializable {

  @FXML
  public VBox parentContainer;

  // Grid container (TilePane) defined in FXML
  @FXML
  private TilePane grid;

  private List<TOShelf> shelfList;
  private Supplier<List<TOShelf>> supplier;
  private Map<String, AttributeInfo> scope =
      Map.ofEntries(Map.entry("shelfID", new AttributeInfo("BASIC", 0)),
          Map.entry("maxColumns", new AttributeInfo("BASIC", 1)),
          Map.entry("maxRows", new AttributeInfo("BASIC", 2)));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Ensure grid wraps nicely (4 columns configured in FXML via prefColumns)
    Platform.runLater(this::refresh);
  }

  private void refresh() {
    if (this.supplier != null) {
      shelfList = this.supplier.get();
    } else {
      shelfList = CheECSEManagerFeatureSet1Controller.getShelves();
    }
    if (shelfList == null || shelfList.isEmpty()) {
      shelfList = Collections.emptyList();
      grid.getChildren().setAll(new Label("Please add a new Shelf"));
      return;
    }
    populateData();
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<List<TOShelf>> supplier) {
    // Reloads data to ensure latest values are fetched each time page is opened using Back
    this.supplier = supplier;
    this.scope = scope;
    refresh();
  }

  public void populateData() {
    grid.getChildren().clear();
    for (TOShelf shelf : shelfList) {
      grid.getChildren().add(createShelfPanel(shelf));
    }
  }

  private ItemPanel createShelfPanel(TOShelf shelf) {
    var panel = new ItemPanel();
    panel.setFillDirection(ItemPanel.FillDirection.VERTICAL);
    panel.setMaxWidthRatio(0.5);
    panel.setVgap(16);

    // Header as clickable hyperlink to detail
    var header = new Hyperlink("Shelf " + shelf.getShelfID());
    header.getStyleClass().add("text-panel-heading-accent");
    header.setOnAction(e -> redirectToShelfDetail(shelf));

    var cols = new Label("Columns: " + shelf.getMaxColumns());
    var rows = new Label("Rows: " + shelf.getMaxRows());

    var actionButtons = new ActionButtonPane<>(null, this::showDialogDeleteShelf);
    actionButtons.setUserData(shelf);
    actionButtons.setSpacing(16);

    panel.getChildren().addAll(header, cols, rows, actionButtons);

    if (this.scope.get("shelfID") == null) {
      header.setDisable(true);
    }
    if (this.scope.get("maxColumns") == null) {
      cols.setVisible(false);
    }
    if (this.scope.get("maxRows") == null) {
      rows.setVisible(false);
    }

    return panel;
  }

  private void redirectToShelfDetail(TOShelf shelf) {
    String shelfId = shelf.getShelfID();
    var state = new NavigationState<Supplier<TOShelf>>("Shelf " + shelfId,
        PageType.REDIRECT_DISPLAY, "view/page/shelf/ShelfDisplayOne.fxml");
    Supplier<TOShelf> dataSupplier = () -> CheECSEManagerFeatureSet1Controller.getShelf(shelfId);
    state.setData(dataSupplier);
    ApplicationNavigator.getInstance().switchPage("Shelf", new PageSwitchEvent(state));
  }

  private void showDialogDeleteShelf(TOShelf shelf) {
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getResource(PACKAGE_ID + "view/base/DeleteDialog.fxml"));
      Parent tempContainer = fxmlLoader.load();
      // Get controller
      DeleteDialogController<TOShelf> controller = fxmlLoader.getController();
      controller.setAction(a -> deleteShelf(shelf));
      Stage stage = new Stage();
      stage.setTitle("Delete");
      stage.initModality(Modality.APPLICATION_MODAL);
      Scene scene = new Scene(tempContainer);
      scene.getStylesheets()
          .add(getClass().getResource(PACKAGE_ID.concat("style/main.css")).toExternalForm());
      stage.setScene(scene);
      stage.showAndWait();
      // Reinitialize data
      refresh();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void deleteShelf(TOShelf shelf) {
    String errMsg = CheECSEManagerFeatureSet2Controller.deleteShelf(shelf.getShelfID());
    if (errMsg.isEmpty()) {
      ToastFactory.createSuccess(parentContainer, "Deleted Shelf successfully");
      System.out.println("Deleted Shelf Successfully");
    } else {
      ToastFactory.createError(parentContainer, errMsg);
      System.out.println(errMsg);
    }
  }

  private void updateShelf(TOShelf shelf) {
    var state = new NavigationState<Supplier<TOShelf>>("Update Shelf " + shelf.getShelfID(),
        PageType.UPDATE, "view/page/shelf/ShelfForm.fxml");
    Supplier<TOShelf> dataSupplier =
        () -> CheECSEManagerFeatureSet1Controller.getShelf(shelf.getShelfID());
    state.setData(dataSupplier);
    ApplicationNavigator.getInstance().switchPage("Shelf", new PageSwitchEvent(state));
  }

}
