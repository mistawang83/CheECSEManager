package ca.mcgill.ecse.cheecsemanager.fxml.controller.purchase;

import ca.mcgill.ecse.cheecsemanager.controller.PurchasesOrdersController;
import ca.mcgill.ecse.cheecsemanager.controller.TOPurchase;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.TableColumnFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

public class PurchaseDisplayManyController implements Initializable {
    @FXML
    private VBox parentContainer;

    @FXML
    private TableView<TOPurchase> table;

    private List<TOPurchase> purchaseList;
    private Supplier<List<TOPurchase>> supplier;
    private Map<String, AttributeInfo> scope =
            Map.ofEntries(Map.entry("id", new AttributeInfo("BASIC", 0)),
                    Map.entry("farmerEmail", new AttributeInfo("BASIC", 1)),
                    Map.entry("purchaseDate", new AttributeInfo("BASIC", 2)),
                    Map.entry("nrCheeseWheels", new AttributeInfo("BASIC", 3)));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    private void refresh() {
        if (supplier != null) {
            purchaseList = supplier.get();
        } else {
            purchaseList = PurchasesOrdersController.getPurchases();
        }

        if (purchaseList == null || purchaseList.isEmpty()) {
            purchaseList = Collections.emptyList();
            table.setPlaceholder(new Label("No purchases have been placed."));
        }

        populateData();
    }

    public void setData(Map<String, AttributeInfo> scope, Supplier<List<TOPurchase>> supplier) {
        this.scope = scope;
        this.supplier = supplier;
        refresh();
    }

    private void populateData() {
        ObservableList<TOPurchase> list = FXCollections.observableArrayList(purchaseList);
        table.getColumns().clear();

        // ID column
        if (scope.containsKey("id")) {
            TableColumn<TOPurchase, Integer> col = TableColumnFactory.createTableColumn("ID");
            col.setText("ID");
            col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // farmer email column
        if (scope.containsKey("farmerEmail")) {
            TableColumn<TOPurchase, String> col =
                    TableColumnFactory.createTableColumn("Farmer Email");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("farmerEmail"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // purchase date column
        if (scope.containsKey("purchaseDate")) {
            TableColumn<TOPurchase, Date> col =
                    TableColumnFactory.createTableColumn("Purchase Date");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("purchaseDate"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // nr cheese column
        if (scope.containsKey("nrCheeseWheels")) {
            TableColumn<TOPurchase, Integer> col =
                    TableColumnFactory.createTableColumn("# Cheese Wheels");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("nrCheeseWheels"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // Table sorting (like WholesaleCompanyDisplayMany)
        LayoutHelper.sortTableColumns(table, scope);

        table.setItems(list);

        // Adjust column sizes
        Platform.runLater(() -> LayoutHelper.refreshColumnWidths(table));
    }

    private <T> void wrapWithHBox(TableColumn<TOPurchase, T> col) {
        col.setCellFactory(new Callback<TableColumn<TOPurchase, T>, TableCell<TOPurchase, T>>() {
            @Override
            public TableCell<TOPurchase, T> call(TableColumn<TOPurchase, T> param) {
                return new TableCell<TOPurchase, T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                            return;
                        }
                        Label label = new Label(item.toString());
                        HBox h = new HBox(label);
                        h.setSpacing(6);
                        setGraphic(h);
                        setText(null);
                        h.setAlignment(Pos.CENTER);
                    }
                };
            }
        });
        col.setPrefWidth(75);
        col.setStyle("-fx-alignment: center;");
    }
}
