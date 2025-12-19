package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

public class ItemPanel extends GridPane {

  public enum FillDirection {
    HORIZONTAL, VERTICAL
  }

  private final ObjectProperty<FillDirection> fillDirection =
      new SimpleObjectProperty<>(FillDirection.VERTICAL);

  private final ObjectProperty<Double> maxWidthRatio = new SimpleObjectProperty<>(0.25);

  public ItemPanel() {

    super();
    this.setMaxWidth(getCalculatedWidth());
    this.getStyleClass().add("panel");
    setHgap(8);
    setVgap(8);

    // Relayout when children change
    getChildren().addListener((ListChangeListener<Node>) c -> relayoutChildren());
    // Relayout when direction changes
    fillDirection.addListener((obs, o, n) -> relayoutChildren());
  }

  private double getCalculatedWidth() {
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double quarterScreen = bounds.getWidth() * (maxWidthRatio.get());
    return quarterScreen;
  }

  // Property accessors for FXML
  public final FillDirection getFillDirection() {
    return fillDirection.get();
  }

  public final void setFillDirection(FillDirection direction) {
    this.fillDirection.set(direction);
  }

  public final ObjectProperty<FillDirection> fillDirectionProperty() {
    return fillDirection;
  }

  public final Double getMaxWidthRatio() {
    return maxWidthRatio.get();
  }

  public final void setMaxWidthRatio(Double ratio) {
    this.maxWidthRatio.set(ratio);
    this.setMaxWidth(getCalculatedWidth());
  }

  public final ObjectProperty<Double> maxWidthRatioProperty() {
    return maxWidthRatio;
  }


  private void relayoutChildren() {
    int index = 0;
    for (Node child : getChildren()) {
      if (child == null || !child.isManaged())
        continue;
      if (getFillDirection() == FillDirection.HORIZONTAL) {
        GridPane.setRowIndex(child, 0);
        GridPane.setColumnIndex(child, index);
      } else { // VERTICAL
        GridPane.setRowIndex(child, index);
        GridPane.setColumnIndex(child, 0);
      }
      GridPane.setHalignment(child, HPos.CENTER);
      GridPane.setValignment(child, VPos.CENTER);
      index++;
    }
  }
}
