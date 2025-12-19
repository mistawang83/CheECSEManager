package ca.mcgill.ecse.cheecsemanager.fxml.controller.order;

import ca.mcgill.ecse.cheecsemanager.controller.PurchasesOrdersController;
import ca.mcgill.ecse.cheecsemanager.controller.TOOrder;
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

public class OrderDisplayManyController implements Initializable {

    @FXML
    private VBox parentContainer;

    @FXML
    private TableView<TOOrder> table;

    private List<TOOrder> orderList;
    private Supplier<List<TOOrder>> supplier;
    private Map<String, AttributeInfo> scope =
            Map.ofEntries(Map.entry("id", new AttributeInfo("BASIC", 0)),
                    Map.entry("companyName", new AttributeInfo("BASIC", 1)),
                    Map.entry("orderDate", new AttributeInfo("BASIC", 2)),
                    Map.entry("deliveryDate", new AttributeInfo("BASIC", 3)),
                    Map.entry("monthsAged", new AttributeInfo("BASIC", 4)),
                    Map.entry("nrCheeseWheelsOrdered", new AttributeInfo("BASIC", 5)),
                    Map.entry("nrCheeseWheelsMissing", new AttributeInfo("BASIC", 6)));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    private void refresh() {
        if (supplier != null) {
            orderList = supplier.get();
        } else {
            orderList = PurchasesOrdersController.getOrders();
        }

        if (orderList == null || orderList.isEmpty()) {
            orderList = Collections.emptyList();
            table.setPlaceholder(new Label("No orders have been placed."));
        }

        populateData();
    }

    public void setData(Map<String, AttributeInfo> scope, Supplier<List<TOOrder>> supplier) {
        this.scope = scope;
        this.supplier = supplier;
        refresh();
    }

    private void populateData() {
        ObservableList<TOOrder> list = FXCollections.observableArrayList(orderList);
        table.getColumns().clear();

        // ID column
        if (scope.containsKey("id")) {
            TableColumn<TOOrder, Integer> col = TableColumnFactory.createTableColumn("ID");
            col.setText("ID");
            col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // company name column
        if (scope.containsKey("companyName")) {
            TableColumn<TOOrder, String> col = TableColumnFactory.createTableColumn("Company Name");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("companyName"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // order date column
        if (scope.containsKey("orderDate")) {
            TableColumn<TOOrder, Date> col = TableColumnFactory.createTableColumn("Order Date");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("orderDate"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // delivery date column
        if (scope.containsKey("deliveryDate")) {
            TableColumn<TOOrder, Date> col = TableColumnFactory.createTableColumn("Delivery Date");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("deliveryDate"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // months aged column
        if (scope.containsKey("monthsAged")) {
            TableColumn<TOOrder, String> col = TableColumnFactory.createTableColumn("Months Aged");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("monthsAged"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // Nr of cheese
        if (scope.containsKey("nrCheeseWheelsOrdered")) {
            TableColumn<TOOrder, Integer> col = TableColumnFactory.createTableColumn("# Ordered");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("nrCheeseWheelsOrdered"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // Nr Missing
        if (scope.containsKey("nrCheeseWheelsMissing")) {
            TableColumn<TOOrder, Integer> col = TableColumnFactory.createTableColumn("# Missing");
            col.setCellValueFactory(
                    new javafx.scene.control.cell.PropertyValueFactory<>("nrCheeseWheelsMissing"));
            wrapWithHBox(col);
            table.getColumns().add(col);
        }

        // Table sorting (like WholesaleCompanyDisplayMany)
        LayoutHelper.sortTableColumns(table, scope);

        table.setItems(list);

        // Adjust column sizes
        Platform.runLater(() -> LayoutHelper.refreshColumnWidths(table));
    }

    private <T> void wrapWithHBox(TableColumn<TOOrder, T> col) {
        col.setCellFactory(new Callback<TableColumn<TOOrder, T>, TableCell<TOOrder, T>>() {
            @Override
            public TableCell<TOOrder, T> call(TableColumn<TOOrder, T> param) {
                return new TableCell<TOOrder, T>() {
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
