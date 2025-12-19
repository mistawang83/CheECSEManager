package ca.mcgill.ecse.cheecsemanager.fxml.controller.settings;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import ca.mcgill.ecse.cheecsemanager.application.ApplicationSettings;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.cheecsemanager.CheECSEManagerTabController;

import java.net.URL;
import java.util.ResourceBundle;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class SettingsDisplayController implements Initializable {

  @FXML
  private ToggleButton themeToggle;
  @FXML
  private ToggleButton tabLayoutToggle;

  private static final String LIGHT_THEME_PATH = PACKAGE_ID + "style/_light_theme.css";
  private static final String DARK_THEME_PATH = PACKAGE_ID + "style/_dark_theme.css";

  private boolean isDarkMode = false;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    isDarkMode = ApplicationSettings.getInstance().isDarkMode();

    // Initialize tab layout toggle state
    updateTabLayoutToggleState(ApplicationSettings.getInstance().isCollapsedView());

    Platform.runLater(() -> updateThemeToggleState());
  }

  @FXML
  public void onThemeToggle(ActionEvent event) {
    isDarkMode = themeToggle.isSelected();
    updateThemeToggleState();
    switchTheme();
    ApplicationSettings.getInstance().setDarkMode(isDarkMode);
  }

  @FXML
  public void onTabLayoutToggle(ActionEvent event) {
    boolean collapse = tabLayoutToggle.isSelected();
    ApplicationSettings.getInstance().setCollapsedView(collapse);
    updateTabLayoutToggleState(collapse);
  }

  private void updateThemeToggleState() {
    if (isDarkMode) {
      themeToggle.setText("ON");
      themeToggle.setSelected(true);
    } else {
      themeToggle.setText("OFF");
      themeToggle.setSelected(false);
    }
  }

  private void updateTabLayoutToggleState(boolean collapsed) {
    if (collapsed) {
      tabLayoutToggle.setText("ON");
      tabLayoutToggle.setSelected(true);
    } else {
      tabLayoutToggle.setText("OFF");
      tabLayoutToggle.setSelected(false);
    }
  }

  private void switchTheme() {
    Scene scene = getScene();
    if (scene == null) {
      return;
    }

    // Remove all theme stylesheets
    scene.getStylesheets()
        .removeIf(sheet -> sheet.contains("_light_theme.css") || sheet.contains("_dark_theme.css"));

    // Add the appropriate theme
    String themePath =
        ApplicationSettings.getInstance().isDarkMode() ? DARK_THEME_PATH : LIGHT_THEME_PATH;
    String themeUrl = getClass().getResource(themePath).toExternalForm();
    scene.getStylesheets().add(themeUrl);
  }

  private Scene getScene() {
    // Get the scene from the toggle button
    return themeToggle != null && themeToggle.getScene() != null ? themeToggle.getScene() : null;
  }
}
