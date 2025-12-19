package ca.mcgill.ecse.cheecsemanager.fxml.controller.shelf;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Grid visualization of a shelf's cheese wheel layout. Each cell is either empty (outlined) or
 * contains a cheese icon (yellow circle) that is clickable.
 */
public class ShelfCheeseGrid extends GridPane {

  private TOShelf shelf;
  private final Map<String, Integer> locationToCheese = new HashMap<>(); // key: col,row ->
                                                                         // cheeseWheelId

  public ShelfCheeseGrid() {
    setHgap(6);
    setVgap(6);
    getStyleClass().addAll("panel", "shelf-cheese-grid");
  }

  public void setShelf(TOShelf shelf) {
    this.shelf = shelf;
    buildIndex();
    render();
  }

  private void buildIndex() {
    locationToCheese.clear();
    if (shelf == null)
      return;
    Integer[] cols = shelf.getColumnNrs();
    Integer[] rows = shelf.getRowNrs();
    Integer[] ids = shelf.getCheeseWheelIDs();
    if (cols == null || rows == null || ids == null)
      return;
    for (int i = 0; i < ids.length; i++) {
      locationToCheese.put(cols[i] + "," + rows[i], ids[i]);
    }
  }

  private void render() {
    getChildren().clear();
    if (shelf == null)
      return;
    int maxCols = Math.max(1, shelf.getMaxColumns());
    int maxRows = Math.max(1, shelf.getMaxRows());

    for (int row = 1; row <= maxRows; row++) {
      for (int col = 1; col <= maxCols; col++) {
        String key = col + "," + row;
        Integer cheeseId = locationToCheese.get(key);
        StackPane cell = createCell(col, row, cheeseId);
        add(cell, col - 1, maxRows - row + 1); // GridPane 0-indexed
      }
    }
  }

  private StackPane createCell(int col, int row, Integer cheeseWheelId) {
    StackPane pane = new StackPane();
    pane.setMinSize(40, 40);
    pane.setPrefSize(40, 40);
    pane.setMaxSize(40, 40);
    pane.getStyleClass().add("shelf-cell");
    pane.setAlignment(Pos.CENTER);

    if (cheeseWheelId != null) {
      Circle cheese = new Circle(14, Color.web("#f6c453")); // cheddar-ish color
      cheese.setStroke(Color.web("#c7922b"));
      cheese.setStrokeWidth(2);
      cheese.getStyleClass().add("cheese-wheel-icon");

      Text cheeseLabel = new Text(String.valueOf(cheeseWheelId));
      cheeseLabel.getStyleClass().add("cheese-wheel-label");


      pane.getChildren().addAll(cheese, cheeseLabel);
      pane.getStyleClass().add("occupied");
      pane.setOnMouseClicked(e -> onCheeseClick(e, cheeseWheelId));
      pane.setUserData(cheeseWheelId);
      pane.setAccessibleText("CheeseWheel " + cheeseWheelId);
    } else {
      Text empty = new Text("");
      empty.setOpacity(0.25);
      pane.getChildren().add(empty); // keep size
      pane.getStyleClass().add("empty");
    }
    return pane;
  }

  private void onCheeseClick(MouseEvent event, Integer cheeseWheelId) {

    TOCheeseWheel wheel = CheECSEManagerFeatureSet3Controller.getCheeseWheel(cheeseWheelId);
    NavigationState<Supplier<TOCheeseWheel>> state =
        new NavigationState<Supplier<TOCheeseWheel>>("CheeseWheel " + cheeseWheelId,
            PageType.REDIRECT_DISPLAY, "view/page/cheesewheel/CheeseWheelDisplayOne.fxml");
    state.setData(() -> wheel);
    ApplicationNavigator.getInstance().switchPage("CheeseWheel", new PageSwitchEvent(state));
  }
}
