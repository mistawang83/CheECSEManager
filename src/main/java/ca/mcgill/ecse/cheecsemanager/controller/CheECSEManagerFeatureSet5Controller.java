package ca.mcgill.ecse.cheecsemanager.controller;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;


/**
 * This class implements the 5th feature set of the Controller layer of the CheECSEManager. The
 * feature set includes the following features: 1. Sell Cheese Wheels to Wholesale Company; 2. Add
 * Wholesale Company; 3. Update Wholesale Company
 * 
 * @author George Zhou
 */

public class CheECSEManagerFeatureSet5Controller {

  /**
   * This method creates a new sales order to sell cheese wheels to a wholesale company. It
   * validates all input parameters, ensures the wholesale company exists, and creates an order with
   * available cheese wheels of the specified maturation period.
   * 
   * @author George Zhou
   * @param nameCompany The name of the wholesale company to sell to
   * @param orderDate The date when the order is placed
   * @param nrCheeseWheels The number of cheese wheels to sell (must be greater than zero)
   * @param monthsAged The aging period of the cheese wheels (Six, Twelve, TwentyFour, or ThirtySix)
   * @param deliveryDate The date when the cheese wheels should be delivered (must be on or after
   *        order date)
   * @return Empty string if successful, otherwise an error message
   */
  public static String sellCheeseWheels(String nameCompany, Date orderDate, Integer nrCheeseWheels,
      String monthsAged, Date deliveryDate) {
	  try {
		  CheECSEManager cheeseManager = CheECSEManagerApplication.getCheecseManager();

		    // Validate number of cheese wheels input
		    if (nrCheeseWheels <= 0) {
		      return "nrCheeseWheels must be greater than zero.";
		    }

		    // Validate maturation period input
		    MaturationPeriod maturation; // Declare the variable outside the try/catch
		    try {
		      maturation = MaturationPeriod.valueOf(monthsAged);
		    } catch (IllegalArgumentException e) {
		      return "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
		    }

		    // Find the target company
		    WholesaleCompany targetCompany = null;
		    for (var company : cheeseManager.getCompanies()) {
		      if (company.getName().equals(nameCompany)) {
		        targetCompany = company;
		        break;
		      }
		    }
		    // If the company doesn't exist, return an error message
		    if (targetCompany == null) {
		      return "The wholesale company " + nameCompany + " does not exist.";
		    }

		    // Ensure the delivery date is after or equal to the order date
		    if (!deliveryDate.after(orderDate) && !deliveryDate.equals(orderDate)) {
		      return "The delivery date must be on or after the transaction date.";
		    }

		    Order newOrder = new Order(orderDate, cheeseManager, nrCheeseWheels, maturation, deliveryDate,
		        targetCompany);
        
        
        int nrMonths = 0;
        switch (maturation) {
          case Six:
            nrMonths = 6;
            break;
          case Twelve:
            nrMonths = 12;
            break;
          case TwentyFour:
            nrMonths = 24;
            break;
          case ThirtySix:
            nrMonths = 36;
            break;
        }
        
		    // Add cheese wheels to the order
		    for (CheeseWheel wheel : cheeseManager.getCheeseWheels()) {
                  
        // order and delivery dates difference

        long diffInMonths = ChronoUnit.MONTHS.between(wheel.getPurchase().getTransactionDate().toLocalDate(), deliveryDate.toLocalDate());
          
		      if (!wheel.isIsSpoiled() && (diffInMonths >= nrMonths) && !wheel.hasOrder() && wheel.getMonthsAged() == maturation) {
		        newOrder.addCheeseWheel(wheel);
		        if (newOrder.numberOfCheeseWheels() == newOrder.getNrCheeseWheels()) {
		          break;
		        }
		      }
		    }
		    CheECSEManagerPersistence.save();
		    return "";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
    
  }

  /**
   * This method adds a new wholesale company to the system. It validates that both the company name
   * and address are provided and not empty, and ensures that no duplicate company names exist in
   * the system before creating the new company.
   * 
   * @author George Zhou
   * @param name The name of the wholesale company to add (must not be empty)
   * @param address The address of the wholesale company (must not be empty)
   * @return Empty string if successful, otherwise an error message
   */
  public static String addWholesaleCompany(String name, String address) {
	  try {
		  CheECSEManager cheeseManager = CheECSEManagerApplication.getCheecseManager();
		    if (name == null || name.trim().length() == 0) {
		      return "Name must not be empty.";
		    }
		    if (address == null || address.trim().length() == 0) {
		      return "Address must not be empty.";
		    }

		    // Check for duplicate company names
		    for (var company : cheeseManager.getCompanies()) {
		      if (company.getName().equals(name)) {
		        return "The wholesale company already exists.";
		      }
		    }

		    // Add the new company safely
		    cheeseManager.addCompany(name, address);
		    CheECSEManagerPersistence.save();

		    return "";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
    

  }

  /**
   * This method updates an existing wholesale company's information. It validates the new name and
   * address, ensures the target company exists, and checks that the new company name doesn't
   * conflict with any existing companies.
   * 
   * @author George Zhou
   * @param name The current name of the company to update
   * @param newName The new name for the company (must not be empty)
   * @param newAddress The new address for the company (must not be empty)
   * @return Empty string if successful, otherwise an error message
   */
  public static String updateWholesaleCompany(String name, String newName, String newAddress) {
	  try {
		  CheECSEManager cheeseManager = CheECSEManagerApplication.getCheecseManager();
		    if (newName == null || newName.trim().length() == 0) {
		      return "The name must not be empty."; // Must be exactly the same as in the step definition
		    }
		    if (newAddress == null || newAddress.trim().length() == 0) {
		      return "The address must not be empty."; // Must be exactly the same as in the step definitio
		    }

		    WholesaleCompany targetCompany = null;
		    // Find the company to update
		    for (var company : cheeseManager.getCompanies()) {
		      if (company.getName().equals(name)) {
		        targetCompany = company;
		        break;
		      }
		    }
		    // If the company doesn't exist, return an error message
		    if (targetCompany == null) {
		      return "The wholesale company " + name + " does not exist.";
		    }

		    // Check if the new name already exists in another company
		    for (WholesaleCompany company : cheeseManager.getCompanies()) {
		      if (company.getName().equals(newName) && company != targetCompany) {
		        return "The wholesale company " + newName + " already exists.";
		      }
		    }

		    // Update the company's details
		    targetCompany.setName(newName);
		    targetCompany.setAddress(newAddress);
		    CheECSEManagerPersistence.save();
		    return "";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
    
  }
}
