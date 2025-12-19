package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class ObscuredLabel extends HBox {

  private Button revealButton = new Button();
  private Label textLabel = new Label();
  private Boolean hidden = true;
  private String obscuredText;

  private ImageView hideIcon;
  private ImageView showIcon;

  public ObscuredLabel(String obscuredText) {
    this.obscuredText = obscuredText;

    this.getStyleClass().add("obscured-label");

    // Reveal Button
    revealButton.getStyleClass().add("reveal-button");
    revealButton.setOnAction(e -> updateText(!hidden));

    hideIcon = new ImageView(
        new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/hide.png"))));
    showIcon = new ImageView(
        new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/view.png"))));
    showIcon.setFitWidth(14);
    showIcon.setFitHeight(14);
    hideIcon.setFitWidth(14);
    hideIcon.setFitHeight(14);

    // Main label
    textLabel.getStyleClass().add("obscured-text");
    // Fit label to available space
    HBox.setHgrow(textLabel, Priority.ALWAYS);
    textLabel.setMaxWidth(Double.MAX_VALUE);

    updateText(hidden);

    this.getChildren().addAll(textLabel, revealButton);

  }

  public void updateText(Boolean hidden) {
    this.hidden = hidden;
    if (hidden) {
      textLabel.setText("••••••••");
      revealButton.setGraphic(showIcon);
    } else {
      textLabel.setText(obscuredText);
      revealButton.setGraphic(hideIcon);
    }
  }

}
