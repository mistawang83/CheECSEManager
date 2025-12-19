package ca.mcgill.ecse.cheecsemanager.fxml.state;

import javafx.scene.Node;

public class DetailInfo {
  private String label;
  private String value;
  private Node container;

  public DetailInfo(String label, String value, Node container) {
    this.label = label;
    this.value = value;
    this.container = container;
  }

  public Node getContainer() {
    return container;
  }

  public String getLabel() {
    return label;
  }

  public String getValue() {
    return value;
  }
}
