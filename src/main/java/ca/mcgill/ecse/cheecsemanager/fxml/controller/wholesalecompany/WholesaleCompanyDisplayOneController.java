package ca.mcgill.ecse.cheecsemanager.fxml.controller.wholesalecompany;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.DetailView;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.DetailInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ItemPanel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.sql.Date;

public class WholesaleCompanyDisplayOneController implements Initializable {

  @FXML
  private DetailView detailView;

  @FXML
  private TableView orderView;

  @FXML 
  private ItemPanel orderPanel;

  private TOWholesaleCompany wholesaleCompany;
  private Supplier<TOWholesaleCompany> supplier;
  private final Map<String, AttributeInfo> defaultScope =
      Map.ofEntries(Map.entry("name", new AttributeInfo("BASIC", 0)),
          Map.entry("address", new AttributeInfo("BASIC", 1)),
          Map.entry("monthsAgeds", new AttributeInfo("ID", 2)));

  private Map<String, AttributeInfo> scope = defaultScope;

  public class OrderRow {
    private Date orderDate;
    private String monthsAged;
    private Integer nrOrdered;
    private Integer nrMissing;
    private Date deliveryDate;

    public OrderRow(Date orderDate, String monthsAged, Integer nrOrdered,
                    Integer nrMissing, Date deliveryDate) {
        this.orderDate = orderDate;
        this.monthsAged = monthsAged;
        this.nrOrdered = nrOrdered;
        this.nrMissing = nrMissing;
        this.deliveryDate = deliveryDate;
    }

    public Date getOrderDate() { return orderDate; }
    public String getMonthsAged() { return monthsAged; }
    public Integer getNrOrdered() { return nrOrdered; }
    public Integer getNrMissing() { return nrMissing; }
    public Date getDeliveryDate() { return deliveryDate; }
}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    orderView.prefWidthProperty().bind(orderPanel.widthProperty());
    orderView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    refresh();
  }


  private void refresh() {
    if (supplier != null) {
      // Reloads data to ensure latest values are fetched each time page is opened using Back
      this.wholesaleCompany = this.supplier.get();
    }

    if (this.wholesaleCompany != null)
      populateData();
  }

  public void setData(String title, Map<String, AttributeInfo> scope, Object data) {
    if (data instanceof Supplier<?>) {
      this.supplier = ((Supplier<TOWholesaleCompany>) data);
    }
    if (scope == null) {
      this.scope = defaultScope;
    } else {
      this.scope = scope;
    }
    refresh();
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<TOWholesaleCompany> supplier) {
    this.supplier = supplier;
    this.scope = scope;
    refresh();
  }

  public void populateData() {

    Map<String, DetailInfo> details = new HashMap<>();

    // Field - name
    {
      StackPane pane = new StackPane();
      Text nameText = new Text(wholesaleCompany.getName());
      nameText.getStyleClass().add("text-field-display");
      pane.setId("name, 1");
      pane.getChildren().add(nameText);
      details.put("name", new DetailInfo("name", wholesaleCompany.getName(), pane));
    }

    // Field - address
    {
      StackPane pane = new StackPane();
      Text addressText = new Text(wholesaleCompany.getAddress());
      addressText.getStyleClass().add("text-field-display");
      pane.setId("address, 1");
      pane.getChildren().add(addressText);
      details.put("address", new DetailInfo("address", wholesaleCompany.getAddress(), pane));
    }
    detailView.mapDetails(scope, details);
    // Sort Grid
    int newRow = LayoutHelper.sortGrid(detailView, scope);
    // Add Update Button
    Button updateButton = LayoutHelper.createGridEditButton(getClass());
    updateButton.setOnAction(this::updateWholesaleCompany);
    detailView.add(updateButton, 1, newRow);

    populateOrdersTable();
  }

  private void populateOrdersTable() {

    // Clear old columns (important when navigating Back)
    orderView.getColumns().clear();

    TableColumn<OrderRow, Date> orderDateCol = new TableColumn<>("Order Date");
    orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

    TableColumn<OrderRow, String> monthsAgedCol = new TableColumn<>("Months Aged");
    monthsAgedCol.setCellValueFactory(new PropertyValueFactory<>("monthsAged"));

    TableColumn<OrderRow, Integer> nrOrderedCol = new TableColumn<>("Ordered");
    nrOrderedCol.setCellValueFactory(new PropertyValueFactory<>("nrOrdered"));

    TableColumn<OrderRow, Integer> nrMissingCol = new TableColumn<>("Missing");
    nrMissingCol.setCellValueFactory(new PropertyValueFactory<>("nrMissing"));

    TableColumn<OrderRow, Date> deliveryDateCol = new TableColumn<>("Delivery Date");
    deliveryDateCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

    orderDateCol.setStyle("-fx-alignment: CENTER;");
    monthsAgedCol.setStyle("-fx-alignment: CENTER;");
    nrOrderedCol.setStyle("-fx-alignment: CENTER;");
    nrMissingCol.setStyle("-fx-alignment: CENTER;");
    deliveryDateCol.setStyle("-fx-alignment: CENTER;");

    orderView.getColumns().addAll(orderDateCol, monthsAgedCol, nrOrderedCol, nrMissingCol, deliveryDateCol);

    // Build the table rows from your parallel lists
    ObservableList<OrderRow> rows = FXCollections.observableArrayList();

    int size = wholesaleCompany.numberOfOrderDates(); // assumes all lists sync in size

    for (int i = 0; i < size; i++) {
        rows.add(new OrderRow(
                wholesaleCompany.getOrderDate(i),
                wholesaleCompany.getMonthsAged(i),
                wholesaleCompany.getNrCheeseWheelsOrdered(i),
                wholesaleCompany.getNrCheeseWheelsMissing(i),
                wholesaleCompany.getDeliveryDate(i)
        ));
    }

    orderView.setItems(rows);
}


  @FXML
  public void updateWholesaleCompany(ActionEvent event) {
    NavigationState<TOForm<?, TOWholesaleCompany>> state =
        new NavigationState<>("Update WholesaleCompany", PageType.UPDATE,
            "view/page/wholesalecompany/WholesaleCompanyForm.fxml");
    state.setData(new TOForm<>(wholesaleCompany, PageType.UPDATE));
    ApplicationNavigator.getInstance().switchPage("WholesaleCompany", new PageSwitchEvent(state));
  }

  @FXML
  public void onBack(ActionEvent event) {
    NavigationState<?> state = new NavigationState<>("Back", PageType.BACK, null);
    ApplicationNavigator.getInstance().switchPage("WholesaleCompany", new PageSwitchEvent(state));
  }
}
