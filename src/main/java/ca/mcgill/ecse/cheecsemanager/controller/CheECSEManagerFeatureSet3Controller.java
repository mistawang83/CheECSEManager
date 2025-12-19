package ca.mcgill.ecse.cheecsemanager.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;


/**
 * This class implements the 3rd feature set of the Controller layer of the CheECSEManager. The
 * feature set includes the following features: 1. Register Farmer 2. Update Cheese Wheel 3. View
 * Cheese Wheels
 * 
 * @author Aditya Shetty
 */

public class CheECSEManagerFeatureSet3Controller {

  /**
   * This method allows the registration of a new Farmer in the system after validating all input
   * parameters including email, password, and address.
   * 
   * @author Aditya Shetty
   * @param email The email of the farmer being registered.
   * @param password The password chosen by the farmer.
   * @param name The name of the farmer.
   * @param address The address of the farmer.
   * @return String message describing the output result of the method (Success or Failure).
   */
  public static String registerFarmer(String email, String password, String name, String address) {
	  try {
		  CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
		    List<Farmer> farmers = manager.getFarmers();
		    if (!email.contains("@")) {
		      return "Email must contain @ symbol.";
		    }
		    if (email.trim().contains(" ")) {
		      return "Email must not contain spaces.";
		    }
		    if (email.indexOf("@") == 0) {
		      return "Email must have characters before @.";
		    }
		    if (email.indexOf("@") != email.lastIndexOf("@")) {
		      return "Email must contain exactly one @ symbol.";
		    }
		    int atIndex = email.indexOf("@");
		    int dotIndex = email.lastIndexOf(".");
		    if (dotIndex < atIndex + 2) {
		      return "Email must contain a dot after @.";
		    }
		    if (dotIndex >= email.length() - 1) {
		      return "Email must have characters after dot.";
		    }
		    if (email.equals("manager@cheecse.fr")) {
		      return "Email cannot be manager@cheecse.fr.";
		    }
		    if (password == null || password.trim().isEmpty()) {
		      return "Password must not be empty.";
		    }
		    if (address == null || address.trim().isEmpty()) {
		      return "Address must not be empty.";
		    }

		    for (Farmer f : farmers) {
		      if (f.getEmail().equals(email)) {
		        return "The farmer email already exists.";
		      }
		    }

		    Farmer farmer = new Farmer(email, password, address, manager);
		    farmer.setName(name);
		    CheECSEManagerPersistence.save();
		    return "";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
    
  }

  /**
   * This method allows the Manager to update a CheeseWheel’s maturation period and spoilage status.
   * It ensures valid transitions between maturation states and maintains relationships with orders
   * and shelf locations.
   * 
   * @author Aditya Shetty
   * @param cheeseWheelID The ID of the CheeseWheel to be updated.
   * @param newMonthsAged The new maturation period to be set.
   * @param newIsSpoiled The new spoilage status to be applied.
   * @return String message describing the output result of the method (Success or Failure).
   */
  public static String updateCheeseWheel(Integer cheeseWheelID, String newMonthsAged,
      Boolean newIsSpoiled) {
	  try {
		  CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
		    List<CheeseWheel> cheeseWheels = manager.getCheeseWheels();

		    CheeseWheel currentWheel = null;
		    for (CheeseWheel wheel : cheeseWheels) {
		      if (wheel.getId() == cheeseWheelID) {
		        currentWheel = wheel;
		        break;
		      }
		    }

		    if (currentWheel == null) {
		      return "The cheese wheel with id " + cheeseWheelID + " does not exist.";
		    }

		    try {
		      MaturationPeriod.valueOf(newMonthsAged);
		    } catch (IllegalArgumentException e) {
		      return "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
		    }

		    MaturationPeriod updatedAge = MaturationPeriod.valueOf(newMonthsAged);
		    MaturationPeriod currentAge = currentWheel.getMonthsAged();
		    if (currentAge.compareTo(updatedAge) > 0) {
		      return "Cannot decrease the monthsAged of a cheese wheel.";
		    }

		    if (newIsSpoiled) {
		      currentWheel.setIsSpoiled(true);
		      if (currentWheel.hasOrder()) {
		        currentWheel.getOrder().removeCheeseWheel(currentWheel);
		      }
		      if (currentWheel.hasLocation()) {
		        ShelfLocation loc = currentWheel.getLocation();
		        currentWheel.setLocation(null);
		        loc.setCheeseWheel(null);
		      }
		    } else {
		      if (currentWheel.hasOrder()) {
		        Order order = currentWheel.getOrder();
		        if (order.getMonthsAged() != updatedAge) {
		          order.removeCheeseWheel(currentWheel);
		        }
		      }
		      currentWheel.setIsSpoiled(false);
		    }

		    currentWheel.setMonthsAged(updatedAge);
		    CheECSEManagerPersistence.save();
		    return "Cheese wheel updated successfully.";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }

    
  }

  /**
   * This method returns a single CheeseWheel identified by its ID, represented as a TOCheeseWheel
   * object.
   * 
   * @author Aditya Shetty
   * @param cheeseWheelID The ID of the cheese wheel to retrieve.
   * @return TOCheeseWheel object representing the requested cheese wheel.
   */
  public static TOCheeseWheel getCheeseWheel(Integer cheeseWheelID) {
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    for (CheeseWheel cheeseWheel : manager.getCheeseWheels()) {
      if (cheeseWheel.getId() == cheeseWheelID) {
        return convertToTO(cheeseWheel);
      }
    }
    return null;
  }

  /**
   * This method retrieves all CheeseWheels in the system and converts them into Transfer Objects
   * (TOCheeseWheel) for display in the UI.
   * 
   * @author Aditya Shetty
   * @return List of TOCheeseWheel objects representing all cheese wheels in the system.
   */
  public static List<TOCheeseWheel> getCheeseWheels() {
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    List<TOCheeseWheel> toCheeseWheels = new ArrayList<>();
    for (CheeseWheel cheeseWheel : manager.getCheeseWheels()) {
      toCheeseWheels.add(convertToTO(cheeseWheel));
    }
    return toCheeseWheels;
  }

  // Helper method to convert a CheeseWheel object into a TOCheeseWheel object.
  // returns null if cheeseWheel not found
  private static TOCheeseWheel convertToTO(CheeseWheel cheeseWheel) {
    if (cheeseWheel == null) {
      return null;
    }

    int id = cheeseWheel.getId();
    String monthsAged = cheeseWheel.getMonthsAged().toString();
    boolean isSpoiled = cheeseWheel.getIsSpoiled();
    java.sql.Date purchaseDate = cheeseWheel.getPurchase().getTransactionDate();

    String shelfID = null;
    int column = -1;
    int row = -1;

    if (cheeseWheel.hasLocation()) {
      ShelfLocation location = cheeseWheel.getLocation();
      Shelf shelf = location.getShelf();
      shelfID = shelf.getId();
      column = location.getColumn();
      row = location.getRow();
    }

    boolean isOrdered = cheeseWheel.hasOrder();

    return new TOCheeseWheel(id, monthsAged, isSpoiled, purchaseDate, shelfID, column, row,
        isOrdered);
  }

}
