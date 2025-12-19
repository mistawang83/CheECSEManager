package ca.mcgill.ecse.cheecsemanager.controller;

import java.util.List;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;

/**
 * Controller class for managing farmer-related operations in the CheECSEManager application.
 * Provides methods to update, delete, and retrieve farmer information, as well as list all farmers.
 * This class interacts with the CheECSEManager model to perform operations on Farmer objects and
 * returns data using the TOFarmer transfer object.
 *
 * @author Benjamin CORIAT
 */
public class CheECSEManagerFeatureSet7Controller {

  /**
   * Updates the details of an existing farmer identified by their email address.
   *
   * @param email the email address of the farmer to update
   * @param newPassword the new password for the farmer
   * @param newName the new name for the farmer (can be null)
   * @param newAddress the new address for the farmer
   * @return an empty string if the update is successful, otherwise an error message
   * @throws IllegalArgumentException if email, password, or address is null or empty
   * @throws RuntimeException if an error occurs while updating the farmer
   * @author Benjamin CORIAT
   */
  public static String updateFarmer(String email, String newPassword, String newName,
      String newAddress) {
    CheECSEManager system = CheECSEManagerApplication.getCheecseManager();
    Farmer farmer = getFarmerByEmail(system, email);

    // Check if farmer exists
    if (farmer == null) {
      return "The farmer with email " + email + " does not exist.";
    }

    // Validate input parameters
    if (email == null || email.trim().isEmpty()) {
      return "Email cannot be empty.";
    }
    if (newPassword == null || newPassword.trim().isEmpty()) {
      return "Password must not be empty.";
    }
    if (newAddress == null || newAddress.trim().isEmpty()) {
      return "Address must not be empty.";
    }

    // Update farmer details
    try {
      farmer.setEmail(email);
      farmer.setPassword(newPassword);
      farmer.setName(newName);
      farmer.setAddress(newAddress);
      CheECSEManagerPersistence.save();
    } catch (Exception e) {
      return "Error updating farmer: " + e.getMessage();
    }

    return "";
  }

  /**
   * Retrieves a Farmer object from the system by their email address.
   *
   * @param system the CheECSEManager instance containing the farmer data
   * @param email the email address of the farmer to find
   * @return the Farmer object if found, or null if no farmer matches the email
   * @author Benjamin CORIAT
   */
  private static Farmer getFarmerByEmail(CheECSEManager system, String email) {
    for (Farmer farmer : system.getFarmers()) {
      if (farmer.getEmail().equals(email)) {
        return farmer;
      }
    }
    return null;
  }

  /**
   * Deletes a farmer identified by their email address, provided they have not supplied any cheese.
   *
   * @param email the email address of the farmer to delete
   * @return an empty string if deletion is successful, otherwise an error message
   * @author Benjamin CORIAT
   */
  public static String deleteFarmer(String email) {
    try {
      CheECSEManager system = CheECSEManagerApplication.getCheecseManager();
      Farmer farmer = getFarmerByEmail(system, email);

      if (farmer == null) {
        return "The farmer with email " + email + " does not exist.";
      }

      // Get number of cheese wheels associated with the farmer by aggregating the cheese wheel
      // counts
      // of all the purchases
      int numCheeseWheels = 0;
      for (var purchase : farmer.getPurchases()) {
        numCheeseWheels += purchase.getCheeseWheels().size();
      }
      if (numCheeseWheels > 0) {
        return "Cannot delete farmer who has supplied cheese.";
      }

      farmer.delete();
      system.removeFarmer(farmer);
      CheECSEManagerPersistence.save();

      return "";
    } catch (RuntimeException e) {
      return e.getMessage();
    }

  }

  /**
   * Retrieves the details of a farmer identified by their email address.
   *
   * @param email the email address of the farmer to retrieve
   * @return a TOFarmer object containing the farmer's details, or null if the farmer does not exist
   * @author Benjamin CORIAT
   */
  public static TOFarmer getFarmer(String email) {
    CheECSEManager system = CheECSEManagerApplication.getCheecseManager();
    Farmer farmer = getFarmerByEmail(system, email);
    if (farmer == null) {
      return null;
    }

    TOFarmer toFarmer = new TOFarmer(farmer.getEmail(), farmer.getPassword(), farmer.getName(),
        farmer.getAddress());

    for (var purchase : farmer.getPurchases()) {
      for (var cheeseWheel : purchase.getCheeseWheels()) {
        toFarmer.addPurchaseDate(cheeseWheel.getPurchase().getTransactionDate());
        toFarmer.addCheeseWheelID(cheeseWheel.getId());
        toFarmer.addIsSpoiled(cheeseWheel.getIsSpoiled() ? "true" : "false");
        toFarmer.addMonthsAged(cheeseWheel.getMonthsAged().toString());
      }
    }

    return toFarmer;
  }

  /**
   * Retrieves a list of all farmers in the system.
   *
   * @return a List of TOFarmer objects representing all farmers
   * @author Benjamin CORIAT
   */
  public static List<TOFarmer> getFarmers() {
    CheECSEManager system = CheECSEManagerApplication.getCheecseManager();
    List<TOFarmer> farmers = new java.util.ArrayList<TOFarmer>();
    for (Farmer farmer : system.getFarmers()) {
      TOFarmer toFarmer = new TOFarmer(farmer.getEmail(), farmer.getPassword(), farmer.getName(),
          farmer.getAddress());

      for (var purchase : farmer.getPurchases()) {
        for (var cheeseWheel : purchase.getCheeseWheels()) {
          toFarmer.addPurchaseDate(cheeseWheel.getPurchase().getTransactionDate());
          toFarmer.addCheeseWheelID(cheeseWheel.getId());
          toFarmer.addIsSpoiled(cheeseWheel.getIsSpoiled() ? "true" : "false");
          toFarmer.addMonthsAged(cheeseWheel.getMonthsAged().toString());
        }
      }

      farmers.add(toFarmer);
    }
    return farmers;
  }
}
