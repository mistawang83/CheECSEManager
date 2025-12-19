package ca.mcgill.ecse.cheecsemanager.application;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CheECSEManagerApplication extends Application {

  private static CheECSEManager cheecsemanager;
  public static final String PACKAGE_ID = "/ca/mcgill/ecse/cheecsemanager/";

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(CheECSEManagerApplication.class
        .getResource(PACKAGE_ID.concat("view/page/cheecsemanager/CheECSEManagerTab.fxml")));
    Scene scene = new Scene(fxmlLoader.load(), 980, 640);
    scene.getStylesheets().add(CheECSEManagerApplication.class
        .getResource(PACKAGE_ID.concat("style/main.css")).toExternalForm());

    ApplicationSettings.getInstance().isDarkModeProperty().addListener((obs, oldVal, newVal) -> {
      updateTheme(scene, newVal);
    });
    updateTheme(scene, ApplicationSettings.getInstance().isDarkMode());

    stage.setTitle("CheECSEManager");
    stage.setScene(scene);
    stage.show();
  }

  private void updateTheme(Scene scene, boolean isDarkMode) {
    scene.getStylesheets()
        .removeIf(sheet -> sheet.contains("_light_theme.css") || sheet.contains("_dark_theme.css"));
    String themePath =
        isDarkMode ? PACKAGE_ID + "style/_dark_theme.css" : PACKAGE_ID + "style/_light_theme.css";
    scene.getStylesheets()
        .add(CheECSEManagerApplication.class.getResource(themePath).toExternalForm());
  }

  public static void main(String[] args) {
    launch();
  }

  public static CheECSEManager getCheecseManager() {
    if (cheecsemanager == null) {
      // these attributes are default, you should set them later with the setters
      cheecsemanager = CheECSEManagerPersistence.load();
    }
    return cheecsemanager;
  }

}
