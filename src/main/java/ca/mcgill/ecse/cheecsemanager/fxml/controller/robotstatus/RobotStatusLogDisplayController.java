package ca.mcgill.ecse.cheecsemanager.fxml.controller.robotstatus;

import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class RobotStatusLogDisplayController implements Initializable {

  @FXML
  private TableView<String> tableLogs;

  @FXML
  private TableColumn<String, String> columnDescription;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Provide the string value for the single column
    columnDescription.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));

    // Enable wrapped text and vertically centered content in each row
    columnDescription.setCellFactory(col -> new TableCell<>() {
      private final Text text = new Text();
      {
        text.wrappingWidthProperty().bind(col.widthProperty().subtract(16));
        setGraphic(text);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setAlignment(Pos.CENTER_LEFT); // vertical center, left aligned horizontally
        text.getStyleClass().add("text-normal");
      }

      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          text.setText("");
          setGraphic(null);
        } else {
          text.setText(item);
          setGraphic(text);
        }
      }
    });
    tableLogs.setFixedCellSize(-1); // allow variable row heights

    refresh();
  }

  public void refresh() {
    tableLogs.getItems().clear();
    String[] entries = RobotController.getActionLog().split(";");
    for (String entry : entries) {
      if (!entry.isBlank()) {
        tableLogs.getItems().add(entry.trim());
      }
    }
  }
}
