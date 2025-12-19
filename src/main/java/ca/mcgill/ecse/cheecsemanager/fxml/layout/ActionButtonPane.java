package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class ActionButtonPane<S> extends HBox {
  private Button updateButton;
  private Consumer<S> updateAction;

  private Button deleteButton;
  private Consumer<S> deleteAction;
  private final HBox hBox = new HBox();

  public ActionButtonPane(Consumer<S> updateAction, Consumer<S> deleteAction) {
    super();
    if (updateAction != null) {
      this.updateButton = new Button("");
      this.updateAction = updateAction;
      this.updateButton.setOnAction(e -> {
        S data = (S) this.getUserData();
        if (data != null)
          this.updateAction.accept(data);
      });
      Image img = new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/edit.png")));
      ImageView icon = new ImageView(img);
      icon.setFitWidth(16);
      icon.setFitHeight(16);
      this.updateButton.setGraphic(icon);
      this.updateButton.getStyleClass().add("edit-button");
      this.getChildren().add(this.updateButton);
    }

    if (deleteAction != null) {
      this.deleteButton = new Button("");
      this.deleteAction = deleteAction;
      this.deleteButton.setOnAction(e -> {
        S data = (S) this.getUserData();
        if (data != null)
          this.deleteAction.accept(data);
      });
      Image img = new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/delete.png")));
      ImageView icon = new ImageView(img);
      icon.setFitWidth(16);
      icon.setFitHeight(16);
      this.deleteButton.setGraphic(icon);
      this.deleteButton.getStyleClass().add("delete-button");
      this.getChildren().add(this.deleteButton);
    }
  }
}

