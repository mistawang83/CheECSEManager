package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import java.util.Map;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.DetailInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class DetailView extends GridPane {


  private Map<String, DetailInfo> details;
  private Map<String, AttributeInfo> scope;

  public DetailView() {}

  public void mapDetails(Map<String, AttributeInfo> scope, Map<String, DetailInfo> details) {
    this.scope = scope;
    this.details = details;
    refresh();
  }

  private void refresh() {
    // Clear
    this.getChildren().clear();

    // Add row constraints
    RowConstraints row = new RowConstraints();
    row.setMinHeight(30);
    row.setVgrow(Priority.SOMETIMES);

    // Then populate the entries
    for (var entry : scope.entrySet()) {
      String key = entry.getKey();
      AttributeInfo info = entry.getValue();

      DetailInfo detailNode = details.get(key);
      if (detailNode != null) {

        // First, the label for the attribute
        Label label = new Label(FormHelper.convertCamelCaseToWords(key));
        label.setId(key + ", 0");
        this.add(label, 0, info.getOrder());

        // Then, the value container
        Node container = detailNode.getContainer();
        container.setId(key + ", 1");
        this.add(container, 1, info.getOrder());
      }
    }

    this.getRowConstraints().add(row);
  }
}
