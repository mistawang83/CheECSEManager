package ca.mcgill.ecse.cheecsemanager.fxml.controller.cheecsemanager;

import ca.mcgill.ecse.cheecsemanager.application.ApplicationSettings;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.cheesewheel.CheeseWheelController;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.farmer.FarmerController;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.order.OrderController;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.purchase.PurchaseController;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.wholesalecompany.WholesaleCompanyController;
import ca.mcgill.ecse.cheecsemanager.fxml.controller.shelf.ShelfController;
import ca.mcgill.ecse.cheecsemanager.fxml.util.TabSwitchEvent;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.util.Map;
import java.util.HashMap;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class CheECSEManagerTabController {
  @FXML
  private TabPane tabPane;

  private static TabPane tabPaneRef; // reference used by settings
  private static final Map<Tab, String> originalTitles = new HashMap<>();
  private static boolean collapsed = false;

  private static CheECSEManagerTabController instance;

  public static CheECSEManagerTabController getInstance() {
    if (instance == null) {
      throw new IllegalStateException("CheECSEManagerTabController instance not yet initialized");
    }
    return instance;
  }

  public String currentTab() {
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
    if (selectedTab != null) {
      return selectedTab.getText();
    }
    return null;
  }

  public void switchTab(String tabName) {
    TabSwitchEvent event = new TabSwitchEvent(tabName);
    tabPane.fireEvent(event);
  }

  @FXML
  public void initialize() {
    tabPane.setMinWidth(18);
    instance = this;

    // Register event handler
    tabPane.addEventHandler(TabSwitchEvent.SWITCH_TAB, event -> {
      tabPane.getTabs().stream().filter(tab -> tab.getText().equals(event.getTabName())).findFirst()
          .ifPresent(tab -> tabPane.getSelectionModel().select(tab));
    });
    tabPaneRef = tabPane;
    // Store original titles once
    for (Tab t : tabPane.getTabs()) {
      originalTitles.putIfAbsent(t, t.getText());
    }
    // Assign graphics
    refresh();

    // Listen for theme changes to update icons
    ApplicationSettings.getInstance().isDarkModeProperty().addListener((obs, oldVal, newVal) -> {
      updateTabIcons();
    });

    // Listen for collapsed view changes to update tab titles
    ApplicationSettings.getInstance().collapsedViewProperty().addListener((obs, oldVal, newVal) -> {
      updateTabContents();
    });

    tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
      if (newTab == null) return;
      String target = newTab.getText();
      Platform.runLater(() -> {
        switch (target) {
          case "Order" -> OrderController.getInstance().refresh();
          case "Purchase" -> PurchaseController.getInstance().refresh();
          case "WholesaleCompany" -> {
            WholesaleCompanyController ctrl = WholesaleCompanyController.getInstance();
            if (ctrl.getCurrentPageType() == PageType.DISPLAY) {
              ctrl.refresh();
            }
          }
          case "Farmer" -> {
           FarmerController ctrl = FarmerController.getInstance();
            if (ctrl.getCurrentPageType() == PageType.DISPLAY) {
              ctrl.refresh();
            }
          }
          case "CheeseWheel" -> {
            CheeseWheelController ctrl = CheeseWheelController.getInstance();
            if (ctrl.getCurrentPageType() == PageType.DISPLAY) {
              ctrl.refresh();
            }
          }
          case "Shelf" -> {
            ShelfController ctrl = ShelfController.getInstance();
            if (ctrl.getCurrentPageType() == PageType.DISPLAY) {
              ctrl.refresh();
            }
          }
        }
      });
    });

  }

  private void refresh() {
    updateTabIcons();
    updateTabContents();
  }

  private void updateTabIcons() {
    // Update the icons based on current theme
    boolean darkTheme = ApplicationSettings.getInstance().isDarkMode();
    String suffix = darkTheme ? "-dark.png" : "-light.png";

    for (Tab t : tabPane.getTabs()) {
      // Get the image from resources based on tab + theme
      String text = originalTitles.getOrDefault(t, t.getText());
      String img_path = PACKAGE_ID + "image/tabs/" + text.toLowerCase() + suffix;

      Image img = new Image(getClass().getResource(img_path).toExternalForm(), 18, 18, true, true);

      ImageView icon = new ImageView(img);
      icon.setFitWidth(18);
      icon.setFitHeight(18);
      icon.setPreserveRatio(true);
      t.setGraphic(icon);
    }
  }

  private void updateTabContents() {
    // Update tab titles based on collapsed state
    boolean isCollapsed = ApplicationSettings.getInstance().isCollapsedView();

    if (isCollapsed) {
      // Collapse to icon (or initial if no icon)
      for (Tab t : tabPane.getTabs()) {
        // Remove title
        originalTitles.putIfAbsent(t, t.getText());
        t.setText("");
      }
      // Add style class for collapsed tabs
      if (!tabPane.getStyleClass().contains("tabs-collapsed")) {
        tabPane.getStyleClass().add("tabs-collapsed");
      }
    } else {
      // REstore titles
      for (Tab t : tabPane.getTabs()) {
        String title = originalTitles.getOrDefault(t, "");
        t.setText(title);
      }
      tabPane.getStyleClass().remove("tabs-collapsed");
    }
  }

}
