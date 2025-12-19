package ca.mcgill.ecse.cheecsemanager.controller;

import java.util.stream.Collectors;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.LogEntry;
import ca.mcgill.ecse.cheecsemanager.model.Robot;
import ca.mcgill.ecse.cheecsemanager.model.Transaction;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing robot operations in the CheECSEManager system. Provide static
 * methods to activate, initialize, move, and deactivate the robot, as well as to perform treatments
 * on cheese wheels. It acts as the interface between the application layer and the robot's state
 * machine, ensuring that inputs are validated and error messages are returned consistently.
 * 
 * @author George Zhou
 */
public class RobotController {

  private static CheECSEManager CheECSEManager = CheECSEManagerApplication.getCheecseManager();

  public static String getRobotStatus() {
    Robot robot = CheECSEManager.getRobot();
    if (robot == null) {
      return "Inactive";
    } else {
      return robot.getStatusFullName();
    }
  }

  public static String getRobotCurrentShelfId() {
    Robot robot = CheECSEManager.getRobot();
    if (robot == null || robot.getCurrentShelf() == null) {
      return "-";
    } else {
      return robot.getCurrentShelf().getId();
    }
  }

  /**
   * Activates the robot in the system.
   *
   * @author George Zhou
   * @param aCurrentRow the initial row position of the robot
   * @param aCurrentColumn the initial column position of the robot
   * @return empty string if successful, otherwise an error message
   */
  public static String activateRobot(int aCurrentRow, int aCurrentColumn) {
    try {
      if (CheECSEManager.getRobot() != null) {
        // Robot already exists
        return "The robot has already been activated.";
      }
      new Robot(false, CheECSEManager);
      CheECSEManagerPersistence.save();
      return "";
    } catch (RuntimeException e) {
      return e.getMessage();
    }

  }

  /**
   * Turns the robot left if it is activated and in a valid state.
   *
   * @author George Zhou
   * @return empty string if successful, otherwise an error message
   */
  public static String turnLeft() {
    if (CheECSEManager.getRobot() == null) {
      return "The robot must be activated first.";
    }
    try {
      CheECSEManager.getRobot().turnLeft(); // Call the controller method
      CheECSEManagerPersistence.save();
      return "";
    } catch (RuntimeException e) {
      // Return the model’s error message instead of letting the exception bubble up
      return e.getMessage();
    }
  }

  /**
   * Turns the robot right if it is activated and in a valid state.
   *
   * @author George Zhou
   * @return empty string if successful, otherwise an error message
   */
  public static String turnRight() {
    if (CheECSEManager.getRobot() == null) {
      return "The robot must be activated first.";
    }
    try {
      CheECSEManager.getRobot().turnRight();
      CheECSEManagerPersistence.save();
      return "";
    } catch (RuntimeException e) {
      // Return the model’s error message instead of letting the exception bubble up
      return e.getMessage();
    }
  }

  /**
   * Moves the robot to the specified cheese wheel.
   *
   * @author George Zhou
   * @param aCheeseWheelId the ID of the cheese wheel to move to
   * @return empty string if successful, otherwise an error message
   */
  public static String moveToCheeseWheel(Integer aCheeseWheelId) {
    if (CheECSEManager.getRobot() == null) {
      return "The robot must be activated first.";
    }
    try {
      CheECSEManager.getRobot().goToCheeseWheel(aCheeseWheelId);
      CheECSEManagerPersistence.save();
      return "";
    } catch (RuntimeException e) {
      // Return the model’s error message instead of letting the exception bubble up
      return e.getMessage();
    }
  }

  /**
   * Moves the robot to the entrance of its current shelf.
   *
   * @author George Zhou
   * @return empty string if successful, otherwise an error message
   */
  public static String moveToEntrance() {
    if (CheECSEManager.getRobot() == null) {
      return "The robot must be activated first.";
    }

    try {
      CheECSEManager.getRobot().goToEntrance();
      CheECSEManagerPersistence.save();

      return "";
    } catch (RuntimeException e) {
      // Return the model’s error message instead of letting the exception bubble up
      return e.getMessage();
    }
  }

  /**
   * Performs treatment on the cheese wheel the robot is currently at.
   *
   * @author George Zhou
   * @return empty string if successful, otherwise an error message
   */
  public static String performTreatment() {
    if (CheECSEManager.getRobot() == null) {
      return "The robot must be activated first.";
    }
    try {
      CheECSEManager.getRobot().treatCheeseWheel();
      CheECSEManagerPersistence.save();
      return "";
    } catch (RuntimeException e) {
      // Return the model’s error message instead of letting the exception bubble up
      return e.getMessage();
    }
  }

  /**
   * Searches the list of transactions in the system to find a Purchase with the specified ID.
   *
   * @author George Zhou
   * @param id the unique identifier of the purchase to find
   * @return the Purchase object with the matching ID, or null if not found
   */
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

  /**
   * Retrieves all CheeseWheel objects from a given Purchase that match the specified maturation
   * period and are not spoiled.
   *
   * @author George Zhou
   * @param purchase the Purchase containing the cheese wheels to filter
   * @param monthsAged the maturation period to match
   * @return a list of CheeseWheel objects that match the specified maturation period and are not
   *         spoiled; the list may be empty if no wheels qualify
   */
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

  /**
   * Executes the detailed treatment procedure for a given Purchase.
   *
   * @author George Zhou
   * @param purchase the Purchase containing the cheese wheels to treat
   * @param monthsAged the maturation period to filter cheese wheels
   * @return an empty string if successful, or an error message if any issues arise
   */
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

  /**
   * Performs treatment on cheese wheels from a specific purchase with the given maturation period.
   *
   * @author George Zhou
   * @param monthsAged the maturation period of the cheese wheels to treat (Six, Twelve, TwentyFour,
   *        or ThirtySix)
   * @param purchaseId the ID of the purchase containing the cheese wheels
   * @return empty string if successful, otherwise an error message
   */
  public static String purchaseTreatment(String monthsAged, Integer purchaseId) {
    if (CheECSEManager.getRobot() == null) {
      return "The robot must be activated first.";
    }

    // Validate monthsAged input
    try {
      MaturationPeriod.valueOf(monthsAged);
    } catch (IllegalArgumentException e) {
      return "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
    }

    int Id = purchaseId.intValue();
    Purchase targetPurchase = findPurchaseById(Id); // call helper method
    // Check if purchase exists
    if (targetPurchase == null) {
      return "The purchase " + Id + " does not exist."; // Purchase not found
    }

    try {
      CheECSEManager.getRobot().treatPurchase(); // handle error cases in model
      return performTreatmentLogic(targetPurchase, monthsAged); // call helper method to perform
                                                                // treatment
    } catch (RuntimeException e) {
      // Return the model’s error message instead of letting the exception bubble up
      return e.getMessage();
    }
  }

  /**
   * Moves the robot to the specified shelf.
   *
   * @author George Zhou
   * @param shelfId the ID of the shelf to move to
   * @return empty string if successful, otherwise an error message
   */
  public static String moveToShelf(String shelfId) {
    if (CheECSEManager.getRobot() == null) { // Check if robot is activated
      return "The robot must be activated first.";
    }
    if (shelfId == "") { // Check if shelfId is empty not null
      return "A shelf must be specified.";
    }


    for (int i = 0; i < CheECSEManager.getShelves().size(); i++) {
      if (CheECSEManager.getShelves().get(i).getId().equals(shelfId)) { // use equals to compare
                                                                        // strings

        // Shelf found
        try {
          CheECSEManager.getRobot().goToShelf(shelfId);
          CheECSEManagerPersistence.save();
        } catch (RuntimeException e) {
          return e.getMessage();
        }
        return "";
      }
    }
    return "The shelf " + shelfId + " does not exist."; // Shelf not found
  }

  /**
   * Deactivates the robot if it is activated.
   *
   * @author George Zhou
   * @return empty string if successful, otherwise an error message
   */
  public static String deactivateRobot() {
    if (CheECSEManager.getRobot() == null) {
      return "The robot must be activated first.";
    }
    try {
      CheECSEManager.getRobot().deactivateRobot();
      CheECSEManagerPersistence.save();
    } catch (RuntimeException e) {
      // Return the model’s error message instead of letting the exception bubble up
      return e.getMessage();
    }
    return "";
  }

  /**
   * Retrieves the action log of the robot as a single string.
   *
   * @author George Zhou
   * @return the action log string, or empty string if the robot is not activated
   */
  public static String getActionLog() {
    if (CheECSEManager.getRobot() == null) {
      return "";
    } // return empty log

    return CheECSEManager.getRobot().getLog() // List<LogEntry>
        .stream() // turn it into a Stream<LogEntry>
        .map(LogEntry::getDescription).collect(Collectors.joining(" ")); // join all descriptions
                                                                         // into a single string

  }

  /**
   * Initializes the robot at the specified shelf.
   *
   * @author George Zhou
   * @param shelfId the ID of the shelf to initialize the robot at
   * @return empty string if successful, otherwise an error message
   */

  public static String initializeRobot(String shelfId) {
    if (CheECSEManager.getRobot() == null) {
      return "The robot must be activated first.";
    }
    try {
      CheECSEManager.getRobot().initializeRobot(shelfId);
      CheECSEManagerPersistence.save();
      return "";
    } catch (RuntimeException e) {
      // Return the model’s error message instead of letting the exception bubble up
      return e.getMessage();
    }
  }
}
