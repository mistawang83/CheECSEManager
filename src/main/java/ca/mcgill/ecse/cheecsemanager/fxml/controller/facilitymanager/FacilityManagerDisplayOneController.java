package ca.mcgill.ecse.cheecsemanager.fxml.controller.facilitymanager;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.DetailView;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ObscuredLabel;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.DetailInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FacilityManagerDisplayOneController implements Initializable {

  public static final String MANAGER_EMAIL = "manager@cheecse.fr";
  public static final String MANAGER_DEFAULT_PASSWORD = "defaultPassword1!";

  @FXML
  private DetailView detailView;

  String email;
  String password;

  private Map<String, AttributeInfo> scope =
      Map.ofEntries(Map.entry("email", new AttributeInfo("BASIC", 0)),
          Map.entry("password", new AttributeInfo("BASIC", 1)));


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }

  private void refresh() {
    var manager = CheECSEManagerFeatureSet1Controller.getFacilityManager();
    if (manager == null) {
      // Terrible plaintext password
      email = MANAGER_EMAIL;
      password = MANAGER_DEFAULT_PASSWORD;
    } else {
      email = manager.getEmail();
      password = manager.getPassword();
    }
    populateData();

  }

  public void populateData() {

    Map<String, DetailInfo> details = new HashMap<>();

    // Field - email
    {
      StackPane pane = new StackPane();
      Text emailText = new Text(email);
      pane.setId("email, 1");
      emailText.getStyleClass().add("text-field-display");
      pane.getChildren().add(emailText);
      details.put("email", new DetailInfo("email", email, pane));
    }

    // Field - password
    {
      StackPane pane = new StackPane();
      ObscuredLabel passwordText = new ObscuredLabel(password);
      passwordText.setId("password, 1");
      pane.getChildren().add(passwordText);
      details.put("password", new DetailInfo("password", password, pane));
    }

    detailView.mapDetails(scope, details);

    int newRow = LayoutHelper.sortGrid(detailView, scope);
    // Add Update Button
    Button updateButton = LayoutHelper.createGridEditButton(getClass());
    updateButton.setOnAction(this::updateFacilityManager);
    detailView.add(updateButton, 1, newRow);
  }


  @FXML
  public void updateFacilityManager(ActionEvent event) {
    NavigationState<TOForm<?, ?>> state = new NavigationState<>("Update FacilityManager",
        PageType.UPDATE, "view/page/facilitymanager/FacilityManagerForm.fxml");
    state.setData(new TOForm<>(null, PageType.UPDATE));
    detailView.fireEvent(new PageSwitchEvent(state));
  }

  @FXML
  public void onBack(ActionEvent event) {
    NavigationState<?> state = new NavigationState<>("Back", PageType.BACK, null);
    detailView.fireEvent(new PageSwitchEvent(state));
  }

}
