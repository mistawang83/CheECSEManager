package ca.mcgill.ecse.cheecsemanager.features;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.model.*;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;

/**
 * @author Simon Wang
 * @author George Zhou
 * @author Benjamin Coriat
 * @author Lazarus Sarghi
 */
public class RobotStepDefinitions {
  private String error;
  private String presentedActionLog;
  private Map<String, Map<String, Integer>> shelfDimensions = new HashMap<>();
  private static CheECSEManager CheECSEManager = CheECSEManagerApplication.getCheecseManager();

  // Private helper method to setup action log
  private void setupActionLog(Robot robot, String expectedLog) {
    // Clear existing log entries
    while (robot.hasLog()) {
      LogEntry entry = robot.getLog(0);
      robot.removeLog(entry);
      entry.delete();
    }

    // Parse the expected log string and create entries
    if (expectedLog != null && !expectedLog.trim().isEmpty()) {
      // Split by semicolon to get individual log entries
      String[] logParts = expectedLog.split(";");

      for (String part : logParts) {
        part = part.trim();
        if (!part.isEmpty()) {
          // Add the semicolon back for consistency with your examples
          String logDescription = part + ";";
          // This automatically adds to robot's log
          new LogEntry(logDescription, robot);
        }
      }
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   * @author Lazarus Sarghi
   */
  private void initializeRobotAtState(String state, String shelfId, Integer cheeseWheelId) {
    // Should be null
    assertNull(CheECSEManager.getRobot());
    RobotController.activateRobot(0, 0);
    Robot robot = CheECSEManager.getRobot();

    // Set robot state
    switch (state) {
      case "Idle":
        assertEquals(Robot.Status.Idle, robot.getStatus());
        break;

      case "AtEntranceNotFacingAisle":
        RobotController.initializeRobot(shelfId);
        break;

      case "AtEntranceFacingAisle":
        RobotController.initializeRobot(shelfId);
        RobotController.turnLeft();
        break;

      case "AtCheeseWheel":

        RobotController.initializeRobot(shelfId);
        RobotController.turnLeft();
        RobotController.moveToCheeseWheel(cheeseWheelId); // Assuming null means go to any cheese
                                                          // wheel
        break;
    }
  }

  /**
   * @author George Zhou
   * @author Lazarus Sarghi
   * @author Simon Wang
   */
  @DataTableType
  public Shelf shelfEntryTransformer(Map<String, String> row) {
    String id = row.get("id");
    int nrColumns = Integer.parseInt(row.get("nrColumns"));
    int nrRows = Integer.parseInt(row.get("nrRows"));

    // Create shelf with all locations using model layer
    Shelf shelf = new Shelf(id, CheECSEManager);

    // Store dimensions to create locations later
    Map<String, Integer> dimensions = new HashMap<>();
    dimensions.put("nrColumns", nrColumns);
    dimensions.put("nrRows", nrRows);
    shelfDimensions.put(id, dimensions);
    return shelf;
  }


  /**
   * @author Simon Wang
   * @author Lazarus Sarghi
   */
  @Given("the following shelf exists in the system")
  public void the_following_shelf_exists_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    dataTable.asList(Shelf.class);

  }

  /**
   * @author Benjamin Coriat
   */
  @Given("all locations are created for shelf {string}")
  public void all_locations_are_created_for_shelf(String shelfId) {
    // Verify that the shelf exists and has locations
    Shelf shelf = Shelf.getWithId(shelfId);
    // Get the stored dimensions for this shelf
    Map<String, Integer> dimensions = shelfDimensions.get(shelfId);
    int nrColumns = dimensions.get("nrColumns");
    int nrRows = dimensions.get("nrRows");

    // Create the locations for the shelf
    for (int col = 1; col <= nrColumns; col++) {
      for (int r = 1; r <= nrRows; r++) {
        shelf.addLocation(col, r);
      }
    }
  }

  /**
   * @author George Zhou
   * @author Lazarus Sarghi
   * @author Simon Wang
   */
  @DataTableType
  public Farmer farmerEntryTransformer(Map<String, String> row) {
    String email = row.get("email");
    String password = row.get("password");
    String name = row.get("name");
    String address = row.get("address");
    Farmer farmer = new Farmer(email, password, address, CheECSEManager);
    farmer.setName(name);
    return farmer;
  }

  /**
   * @author Benjamin Coriat
   */
  @Given("the following farmer exists in the system")
  public void the_following_farmer_exists_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    dataTable.asList(Farmer.class);

  }

  /**
   * @author George Zhou
   * @author Lazarus Sarghi
   * @author Simon Wang
   */
  @DataTableType
  public Purchase purchaseEntryTransformer(Map<String, String> row) {

    int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
    CheeseWheel.MaturationPeriod monthsAged =
        CheeseWheel.MaturationPeriod.valueOf(row.get("monthsAged"));
    Date purchaseDate = Date.valueOf(row.get("purchaseDate"));
    String farmerEmail = row.get("farmerEmail");
    Farmer farmer = (Farmer) Farmer.getWithEmail(farmerEmail);

    Purchase purchase = new Purchase(purchaseDate, CheECSEManager, farmer);

    for (int i = 0; i < nrCheeseWheels; i++) {
      new CheeseWheel(monthsAged, false, purchase, CheECSEManager);
    }

    return purchase;
  }

  /**
   * @author Benjamin Coriat
   */
  @Given("the following purchase exists in the system")
  public void the_following_purchase_exists_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {

    // Transforms it into a list of Purchase objects
    dataTable.asList(Purchase.class);

  }

  /**
   * @author Lazarus Sarghi
   */
  @Given("all cheese wheels for the following purchases are created")
  public void all_cheese_wheels_for_the_following_purchases_are_created(
      io.cucumber.datatable.DataTable dataTable) {

    // Check if the IDs exist, as a sanity check
    List<String> ids = dataTable.cells().stream().skip(1).map(row -> row.get(0)).toList();
    for (String id : ids) {
      int purchaseId = Integer.parseInt(id);
      CheECSEManager.getTransactions().stream()
          .filter(t -> t instanceof Purchase && t.getId() == purchaseId).findFirst()
          .orElseThrow(() -> new RuntimeException(
              "Purchase with ID " + purchaseId + " does not exist in the system."));
    }
  }

  /**
   * @author Benjamin Coriat
   */
  @Given("cheese wheel {int} is at shelf location with column {int} and row {int} of shelf {string}")
  public void cheese_wheel_is_at_shelf_location_with_column_and_row_of_shelf(
      Integer cheeseWheelIndex, Integer column, Integer row, String shelfID) {
    // Write code here that turns the phrase above into concrete actions
    cheeseWheelIndex = cheeseWheelIndex - 1;

    CheeseWheel cheeseWheel = CheECSEManager.getCheeseWheel(cheeseWheelIndex);

    Shelf shelf = Shelf.getWithId(shelfID);

    ShelfLocation location = null;
    for (int i = 0; i < shelf.getLocations().size(); i++) {
      ShelfLocation newloc = shelf.getLocations().get(i);
      if (newloc.getColumn() == column && newloc.getRow() == row) {
        location = newloc;
        break;
      }
    }

    if (location == null) {
      location = shelf.addLocation(column, row);
    }

    if (!location.hasCheeseWheel()) {
      cheeseWheel.setLocation(location);
    }
  }


  /**
   * @author George Zhou
   * @author Lazarus Sarghi
   */
  @DataTableType
  public CheeseWheel cheeseWheelIdTransformer(Map<String, String> row) {
    int id = Integer.parseInt(row.get("id"));
    CheeseWheel cheeseWheel = null;
    for (CheeseWheel cw : CheECSEManager.getCheeseWheels()) {
      if (cw.getId() == id) {
        cheeseWheel = cw;
        break;
      }
    }
    if (cheeseWheel == null) {
      return null;
    }

    if (row.containsKey("newMonthsAged")) {
      MaturationPeriod newMonthsAged = MaturationPeriod.valueOf(row.get("newMonthsAged"));
      cheeseWheel.setMonthsAged(newMonthsAged);
    }
    return cheeseWheel;
  }

  /**
   * @author Benjamin Coriat
   */
  @Given("the following cheese wheels are spoiled")
  public void the_following_cheese_wheels_are_spoiled(io.cucumber.datatable.DataTable dataTable) {

    List<CheeseWheel> cheeseWheels = dataTable.asList(CheeseWheel.class);
    for (CheeseWheel cw : cheeseWheels) {
      cw.setIsSpoiled(true);
    }

  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @Given("the robot is marked as {string} and at shelf {string} with action log {string}")
  public void the_robot_is_marked_as_and_at_shelf_with_action_log(String state, String shelfId,
      String actionLog) {
    initializeRobotAtState(state, shelfId, 1);
    // Set action log
    setupActionLog(CheECSEManager.getRobot(), actionLog);
  }

  /**
   * @author Benjamin Coriat
   */
  @Given("the months aged value of the following cheese wheels is increased")
  public void the_months_aged_value_of_the_following_cheese_wheels_is_increased(
      io.cucumber.datatable.DataTable dataTable) {
    dataTable.asList(CheeseWheel.class);
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @Given("the robot is marked as {string}")
  public void the_robot_is_marked_as(String state) {
    // Write code here that turns the phrase above into concrete actions
    initializeRobotAtState(state, "A12", 1);
  }

  /**
   * @author George Zhou
   * @author Lazarus Sarghi
   * @author Simon Wang
   */
  @DataTableType
  public WholesaleCompany wholesaleCompanyEntryTransformer(Map<String, String> row) {
    String name = row.get("name");
    String address = row.get("address");

    WholesaleCompany company = CheECSEManager.addCompany(name, address);
    return company;
  }


  /**
   * @author Benjamin Coriat
   */
  @Given("the following wholesale company exists in the system")
  public void the_following_wholesale_company_exists_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {

    dataTable.asList(WholesaleCompany.class);

  }

  /**
   * @author George Zhou
   * @author Lazarus Sarghi
   * @author Simon Wang
   */
  @DataTableType
  public Order orderEntryTransformer(Map<String, String> row) {
    Date transactionDate = Date.valueOf(row.get("transactionDate"));
    int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
    CheeseWheel.MaturationPeriod monthsAged =
        CheeseWheel.MaturationPeriod.valueOf(row.get("monthsAged"));
    Date deliveryDate = Date.valueOf(row.get("deliveryDate"));
    String companyName = row.get("company");

    WholesaleCompany company = WholesaleCompany.getWithName(companyName);

    Order order = new Order(transactionDate, CheECSEManager, nrCheeseWheels, monthsAged,
        deliveryDate, company);

    return order;
  }

  /**
   * @author Benjamin Coriat
   */
  @Given("the following order exists in the system")
  public void the_following_order_exists_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    dataTable.asList(Order.class);

  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @Given("the robot is marked as {string} and at cheese wheel {int} on shelf {string} with action log {string}")
  public void the_robot_is_marked_as_and_at_cheese_wheel_on_shelf_with_action_log(String state,
      Integer cheeseWheelId, String shelfId, String actionLog) {
    initializeRobotAtState(state, shelfId, cheeseWheelId);
    // Set action log
    setupActionLog(CheECSEManager.getRobot(), actionLog);
  }

  /**
   * @author Benjamin Coriat
   */
  @Given("all non-spoiled cheese wheels from purchase {int} are added to order {int}")
  public void all_non_spoiled_cheese_wheels_from_purchase_are_added_to_order(Integer purchaseId,
      Integer orderId) {
    // Write code here that turns the phrase above into concrete actions
    Order anOrder = null;
    for (Transaction transaction : CheECSEManager.getTransactions()) {
      if (transaction instanceof Order && transaction.getId() == orderId) {
        anOrder = (Order) transaction;
        break;
      }
    }
    assertNotNull(anOrder);

    for (CheeseWheel wheel : CheECSEManager.getCheeseWheels()) {
      if (wheel.getPurchase() != null && wheel.getPurchase().getId() == purchaseId
          && !wheel.isIsSpoiled()) {
        anOrder.addCheeseWheel(wheel);
        if (anOrder.numberOfCheeseWheels() == anOrder.getNrCheeseWheels()) {
          break;
        }
      }
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the facility manager attempts to activate the robot")
  public void the_facility_manager_attempts_to_activate_the_robot() {
    try {
      if (CheECSEManager.getRobot() != null) {
        error = "The robot has already been activated.";
      } else {
        new Robot(false, CheECSEManager);
        error = "";
      }
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the robot controller attempts to turn the robot left")
  public void the_robot_controller_attempts_to_turn_the_robot_left() {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }
    try {
      CheECSEManager.getRobot().turnLeft();
      error = "";
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the robot controller attempts to turn the robot right")
  public void the_robot_controller_attempts_to_turn_the_robot_right() {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }
    try {
      CheECSEManager.getRobot().turnRight();
      error = "";
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the robot controller attempts to move the robot to cheese wheel {int}")
  public void the_robot_controller_attempts_to_move_the_robot_to_cheese_wheel(
      Integer cheeseWheelId) {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }
    try {
      CheECSEManager.getRobot().goToCheeseWheel(cheeseWheelId);
      error = "";
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the robot controller attempts to move the robot to the entrance")
  public void the_robot_controller_attempts_to_move_the_robot_to_the_entrance() {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }
    try {
      CheECSEManager.getRobot().goToEntrance();
      error = "";
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the robot controller attempts to trigger the robot to perform treatment")
  public void the_robot_controller_attempts_to_trigger_the_robot_to_perform_treatment() {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }
    try {
      CheECSEManager.getRobot().treatCheeseWheel();
      error = "";
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the robot controller attempts to move the robot to shelf {string}")
  public void the_robot_controller_attempts_to_move_the_robot_to_shelf(String shelfId) {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }
    try {
      CheECSEManager.getRobot().goToShelf(shelfId);
      error = "";
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the facility manager attempts to trigger the robot to perform treatment on {string} old cheese wheels of purchase {int}")
  public void the_facility_manager_attempts_to_trigger_the_robot_to_perform_treatment_on_old_cheese_wheels_of_purchase(
      String monthsAged, Integer purchaseId) {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }

    // Validate monthsAged input
    try {
      MaturationPeriod.valueOf(monthsAged);
    } catch (IllegalArgumentException e) {
      error = "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
      return;
    }

    // Find the purchase
    Purchase targetPurchase = findPurchaseById(purchaseId);
    if (targetPurchase == null) {
      error = "The purchase " + purchaseId + " does not exist.";
      return;
    }

    // Perform treatment
    try {
      CheECSEManager.getRobot().treatPurchase();
      error = performTreatmentLogic(targetPurchase, monthsAged);
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the facility manager attempts to deactivate the robot")
  public void the_facility_manager_attempts_to_deactivate_the_robot() {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }
    try {
      CheECSEManager.getRobot().deactivateRobot();
      error = "";
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the facility manager attempts to initialize the robot with shelf {string}")
  public void the_facility_manager_attempts_to_initialize_the_robot_with_shelf(String shelfId) {
    if (CheECSEManager.getRobot() == null) {
      error = "The robot must be activated first.";
      return;
    }
    try {
      CheECSEManager.getRobot().initializeRobot(shelfId);
      error = "";
    } catch (RuntimeException e) {
      error = e.getMessage();
    }
  }

  /**
   * @author Simon Wang
   * @author George Zhou
   */
  @When("the facility manager attempts to view the action log of the robot")
  public void the_facility_manager_attempts_to_view_the_action_log_of_the_robot() {
    // presentedActionLog = RobotController.getActionLog();
    if (CheECSEManager.getRobot() == null) {
      presentedActionLog = "";
      return;
    }
    presentedActionLog = CheECSEManager.getRobot().getLog().stream().map(LogEntry::getDescription)
        .collect(Collectors.joining(" "));
  }

  /**
   * @author Lazarus Sarghi
   * @author George Zhou
   */
  @Then("the robot shall be marked as {string}")
  public void the_robot_shall_be_marked_as(String state) {
    Robot robot = CheECSEManager.getRobot();
    Robot.Status robotStatus = robot.getStatus();
    Robot.Status expected = Robot.Status.valueOf(state);
    assertEquals(expected, robotStatus);
  }

  /**
   * @author George Zhou
   */
  @Then("the current shelf of the robot shall be not specified")
  public void the_current_shelf_of_the_robot_shall_be_not_specified() {
    Robot robot = CheECSEManager.getRobot();
    assertNull(robot.getCurrentShelf());
  }

  /**
   * @author George Zhou
   */
  @Then("the current cheese wheel of the robot shall be not specified")
  public void the_current_cheese_wheel_of_the_robot_shall_be_not_specified() {
    Robot robot = CheECSEManager.getRobot();
    assertNull(robot.getCurrentCheeseWheel());
  }

  /**
   * @author George Zhou
   */
  @Then("the action log of the robot shall be empty")
  public void the_action_log_of_the_robot_shall_be_empty() {
    Robot robot = CheECSEManager.getRobot();
    assertFalse(robot.hasLog());
  }

  /**
   * @author George Zhou
   */
  @Then("the presented action log of the robot shall be empty")
  public void the_presented_action_log_of_the_robot_shall_be_empty() {
    assertEquals("", presentedActionLog);
  }

  /**
   * @author George Zhou
   */
  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String expectedError) {
    assertEquals(expectedError, error.trim());
  }

  /**
   * @author Lazarus Sarghi
   */
  @Then("the current shelf of the robot shall be {string}")
  public void the_current_shelf_of_the_robot_shall_be(String shelfId) {
    Robot robot = CheECSEManager.getRobot();
    assertEquals(shelfId, robot.getCurrentShelf().getId());
  }

  /**
   * @author Lazarus Sarghi
   */
  @Then("the action log of the robot shall be {string}")
  public void the_action_log_of_the_robot_shall_be(String expectedLog) {
    Robot robot = CheECSEManager.getRobot();
    assertNotNull(robot);
    String logs = RobotController.getActionLog();
    assertEquals(expectedLog, logs);
  }

  /**
   * @author Lazarus Sarghi
   */
  @Then("the presented action log of the robot shall be {string}")
  public void the_presented_action_log_of_the_robot_shall_be(String expectedLog) {
    assertEquals(expectedLog, presentedActionLog);
  }

  /**
   * @author Lazarus Sarghi
   */
  @Then("the number of robots in the system shall be {int}")
  public void the_number_of_robots_in_the_system_shall_be(Integer expectedCount) {
    Robot robot = CheECSEManager.getRobot();
    int actualCount;
    if (robot == null) {
      actualCount = 0;
    } else {
      actualCount = 1;
    }
    assertEquals(expectedCount.intValue(), actualCount);
  }

  /**
   * @author George Zhou
   */
  @Then("the current cheese wheel of the robot shall {int}")
  public void the_current_cheese_wheel_of_the_robot_shall(Integer cheeseWheelId) {
    Robot robot = CheECSEManager.getRobot();
    assertEquals(cheeseWheelId.intValue(), robot.getCurrentCheeseWheel().getId());
  }

  /**
   * @author George Zhou
   */
  @Then("the current cheese wheel of the robot shall be {int}")
  public void the_current_cheese_wheel_of_the_robot_shall_be(Integer cheeseWheelId) {
    Robot robot = CheECSEManager.getRobot();
    assertEquals(cheeseWheelId.intValue(), robot.getCurrentCheeseWheel().getId());
  }

  private static Purchase findPurchaseById(int id) {
    List<Transaction> transactions = CheECSEManager.getTransactions();
    for (Transaction tx : transactions) {
      if (tx instanceof Purchase) {
        Purchase purchase = (Purchase) tx;
        if (purchase.getId() == id) {
          return purchase;
        }
      }
    }
    return null; // not found
  }

  private static List<CheeseWheel> getCheeseWheelsToTreat(Purchase purchase, String monthsAged) {
    List<CheeseWheel> wheelsToTreat = new ArrayList<>();

    // Get all cheese wheels from the purchase
    List<CheeseWheel> allWheels = purchase.getCheeseWheels();

    // Filter by maturation period and not spoiled
    for (CheeseWheel wheel : allWheels) {
      if (wheel.getMonthsAged().toString().equals(monthsAged) && !wheel.getIsSpoiled()) {
        wheelsToTreat.add(wheel);
      }
    }
    return wheelsToTreat;
  }

  private static String performTreatmentLogic(Purchase purchase, String monthsAged) {
    Robot robot = CheECSEManager.getRobot();

    // 1. Get cheese wheels from this purchase with the specified maturation period
    List<CheeseWheel> wheelsToTreat = getCheeseWheelsToTreat(purchase, monthsAged); // call helper
                                                                                    // method

    if (wheelsToTreat.isEmpty()) {
      return ""; // No cheese wheels to treat
    }

    // 2. For each cheese wheel, move the robot and treat it
    String lastShelfId = null;
    for (CheeseWheel wheel : wheelsToTreat) {
      String targetShelfId = wheel.getLocation().getShelf().getId();

      // Move robot to the target shelf if needed
      if (!targetShelfId.equals(robot.getCurrentShelf().getId()) && lastShelfId == null) {
        // This should update the robot's current shelf
        RobotController.moveToShelf(targetShelfId);
        RobotController.turnLeft();
      }

      // If the robot is already on a shelf but not the target shelf
      if (!targetShelfId.equals(robot.getCurrentShelf().getId()) && lastShelfId != null) {
        RobotController.moveToEntrance();
        RobotController.turnRight();
        RobotController.moveToShelf(targetShelfId);
        RobotController.turnLeft();
      }

      // If the robot is already on the target shelf
      if (targetShelfId.equals(robot.getCurrentShelf().getId()) && lastShelfId == null) {
        RobotController.turnLeft();
      }

      // Move to cheese wheel and treat it
      RobotController.moveToCheeseWheel(wheel.getId());
      RobotController.performTreatment();

      lastShelfId = targetShelfId; // Update last shelf ID
    }

    // 3. Return robot to entrance of the last shelf and turn right
    if (lastShelfId != null) {
      RobotController.moveToEntrance();
      RobotController.turnRight();
    }

    return "";
  }
}
