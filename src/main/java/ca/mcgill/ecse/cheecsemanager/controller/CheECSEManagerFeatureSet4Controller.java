package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.User;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;

import java.sql.Date;

/**
 * This class implements the 4th feature set of the Controller layer of the CheECSEManager. The
 * feature set includes the following features: 1. Buy Cheese Wheels; 2. Assign Cheese Wheel to
 * Shelf; 3. Remove Cheese Wheel from Shelf.
 * 
 * @author Simon Wang
 */
public class CheECSEManagerFeatureSet4Controller {

  // Helper function to get a specific cheese wheel, returns null if cheeseWheelID
  // is not found.
  private static CheeseWheel getCheesewheelById(CheECSEManager app, Integer cheeseWheelID) {
    for (CheeseWheel c : app.getCheeseWheels()) {
      if (c.getId() == cheeseWheelID) {
        return c;
      }
    }
    return null;
  }

  // Helper function to get a Shelf by its ID. Returns null if ID not found.
  private static Shelf getShelfById(CheECSEManager app, String shelfID) {
    if (app.numberOfShelves() != 0) {
      Shelf targetShelf = Shelf.getWithId(shelfID);
      return targetShelf;
    }
    return null;
  }

  // Helper function to get a shelf location from the targetShelf, returns null if
  // col/row not found.
  private static ShelfLocation getShelfLocation(Shelf targetShelf, Integer col, Integer row) {
    for (ShelfLocation loc : targetShelf.getLocations()) {
      if (loc.getColumn() == col && loc.getRow() == row) {
        return loc;
      }
    }
    return null;
  }

  // Finding Farmer instance private method
  private static Farmer findFarmerByEmail(String email) {
    User u = User.getWithEmail(email);
    if (u instanceof Farmer) {
      Farmer targetFarmer = (Farmer) u;
      return targetFarmer;
    }
    return null; // no match or not a Farmer
  }

  /**
   * This method allows cheese wheels to be bought from farmers, creating a Purchase instance
   * linking a cheese wheel and a farmer
   * 
   * @author Simon Wang
   * @param emailFarmer
   * @param purchaseDate
   * @param nrCheeseWheels
   * @param monthsAged
   * @return String message describing output result of the method (Success or Failure)
   */
  public static String buyCheeseWheels(String emailFarmer, Date purchaseDate,
      Integer nrCheeseWheels, String monthsAged) {
	  try {
		  CheECSEManager application = CheECSEManagerApplication.getCheecseManager();
		    Farmer targetFarmer = findFarmerByEmail(emailFarmer);
		    if (targetFarmer == null) {
		      return "The farmer with email " + emailFarmer + " does not exist.";
		    } else if (nrCheeseWheels <= 0) {
		      return "nrCheeseWheels must be greater than zero.";
		    }
		    MaturationPeriod period;
		    try {
		      period = MaturationPeriod.valueOf(monthsAged);
		    } catch (IllegalArgumentException e) {
		      return "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
		    }
		    Purchase newPurchase = new Purchase(purchaseDate, application, targetFarmer);

		    for (int i = 0; i < nrCheeseWheels; i++) {
		      new CheeseWheel(period, false, newPurchase, application);
		    }
		    CheECSEManagerPersistence.save();
		    return "The cheese wheels were purchased.";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
    

  }

  /**
   * This method will assign a cheese wheel with given cheeseWheelID to a shelf with given shelfID,
   * at a specific columnNr and rowNr.
   * 
   * @author Simon Wang
   * @param cheeseWheelID
   * @param shelfID
   * @param columnNr
   * @param rowNr
   * @return String message to describe the output (Success or Failure)
   */
  public static String assignCheeseWheelToShelf(Integer cheeseWheelID, String shelfID,
      Integer columnNr, Integer rowNr) {
	  try {
		  CheECSEManager application = CheECSEManagerApplication.getCheecseManager();
		    CheeseWheel cheeseWheel = getCheesewheelById(application, cheeseWheelID);
		    if (cheeseWheel == null) {
		      return "The cheese wheel with id " + cheeseWheelID.toString() + " does not exist.";
		    } else if (cheeseWheel.isIsSpoiled()) {
		      return "Cannot place a spoiled cheese wheel on a shelf.";
		    }

		    Shelf targetShelf = getShelfById(application, shelfID);
		    if (targetShelf == null) {
		      return "Could not find shelf with ID " + shelfID;
		    }

		    ShelfLocation targetLocation = getShelfLocation(targetShelf, columnNr, rowNr);
		    if (targetLocation == null) {
		      return "The shelf location does not exist.";
		    } else if (targetLocation.hasCheeseWheel()) {
		      return "The shelf location is already occupied.";
		    }

		    if (cheeseWheel.setLocation(targetLocation)) {
			    CheECSEManagerPersistence.save();
		      return "Assigned cheese wheel to shelf location.";
		    }

		    return "Could not assign cheese wheel to shelf location";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
  }

  /**
   * This method will remove a cheese wheel with given cheeseWheelID from the shelf it is assigned
   * to.
   * 
   * @author Simon Wang
   * @param cheeseWheelID
   * @return String message describing output result of the method (Success or Failure)
   */
  public static String removeCheeseWheelFromShelf(Integer cheeseWheelID) {
	  try {
		  CheECSEManager application = CheECSEManagerApplication.getCheecseManager();
		    CheeseWheel cheeseWheel = getCheesewheelById(application, cheeseWheelID);
		    if (cheeseWheel == null) {
		      return "The cheese wheel with id " + cheeseWheelID.toString() + " does not exist.";
		    } else if (cheeseWheel.getLocation() == null) {
		      return "The cheese wheel is not on any shelf.";
		    }
		    if (cheeseWheel.setLocation(null)) {
			    CheECSEManagerPersistence.save();
		      return "The cheese wheel has been removed from its shelf.";
		    }
		    return "Could not remove cheese wheel from shelf.";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
    
  }
}
