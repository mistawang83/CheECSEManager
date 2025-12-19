package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;

/**
 * This controller implements the second feature set by handling the features for adding and
 * deleting shelves from the CheECSEManager.
 * 
 * @author Lazarus Sarghi
 */
public class CheECSEManagerFeatureSet2Controller {

  /**
   * Verifies the shelf ID formatting. The following checks are made. If these checks are violated,
   * their error messages are as such:
   * 
   * - The id must be three characters long.
   * 
   * - The first character must be a letter.
   * 
   * - The second and third characters must be digits.
   * 
   * @author Lazarus Sarghi
   * 
   * @param id The shelf ID to verify.
   * @return A string containing an error message if the ID is invalid, null otherwise.
   */
  private static String verifyShelfId(String id) {
    // Check length
    if (id.length() != 3) {
      return "The id must be three characters long.";
    }

    char letterChar = id.charAt(0);
    // Check if the first character is a letter
    if (!Character.isLetter(letterChar)) {
      return "The first character must be a letter.";
    }

    char firstDigitChar = id.charAt(1);
    char secondDigitChar = id.charAt(2);
    // Check if the other two characters are digits
    if (!Character.isDigit(firstDigitChar) || !Character.isDigit(secondDigitChar)) {
      return "The second and third characters must be digits.";
    }

    return null;
  }

  /**
   * Verifies the shelf dimensions. The following checks are made. If these checks are violated,
   * their error messages are as such:
   * 
   * 
   * - The number of columns must be greater than zero.
   * 
   * - The number of rows must be greater than zero.
   * 
   * - The number of rows must be at most ten.
   * 
   * @author Lazarus Sarghi
   * 
   * @param nrColumns The number of columns to verify.
   * @param nrRows The number of rows to verify.
   * @return A string containing an error message if the dimensions are invalid, null otherwise.
   */
  private static String verifyShelfColumnsAndRows(Integer nrColumns, Integer nrRows) {
    if (nrColumns <= 0) {
      return "Number of columns must be greater than zero.";
    }
    if (nrRows <= 0) {
      return "Number of rows must be greater than zero.";
    }
    if (nrRows > 10) {
      return "Number of rows must be at the most ten.";
    }
    return null;
  }

  /**
   * 
   * As the name suggests, this method adds a shelf, containing its id and dimensions, to the
   * CheECSEManager system.
   * 
   * @author Lazarus Sarghi
   * 
   * @param id The id of the shelf. Must be 3 characters long; first character a letter, the other
   *        two are digits. Must also be unique.
   * @param nrColumns The number of columns in the shelf, minimally 1.
   * @param nrRows The number of rows in the shelf, must be between 1 and 10.
   * @return If successful, it returns an empty string. An error message is returned otherwise.
   */
  public static String addShelf(String id, Integer nrColumns, Integer nrRows) {
	  try {
		  CheECSEManager system = CheECSEManagerApplication.getCheecseManager();

		    // Check if the shelf already exits
		    if (Shelf.getWithId(id) != null) {
		      return "The shelf " + id + " already exists.";
		    }

		    // Verify that the ID is valid
		    String idError = verifyShelfId(id);
		    if (idError != null) {
		      return idError;
		    }

		    // Verify the columns and rows
		    String dimensionError = verifyShelfColumnsAndRows(nrColumns, nrRows);
		    if (dimensionError != null) {
		      return dimensionError;
		    }

		    // It's safe to add the shelf.
		    Shelf shelf = system.addShelve(id);
		    for (int row = 1; row <= nrRows; row++) {
		      for (int column = 1; column <= nrColumns; column++) {
		        shelf.addLocation(column, row);
		      }
		    }
		    CheECSEManagerPersistence.save(); // This line should be added

		    return "";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
    
  }

  /**
   * 
   * As the name suggests, this method deletes a shelf from the CheECSEManager system, based on its
   * id.
   * 
   * @author Lazarus Sarghi
   * 
   * @param id The id of the shelf to be deleted. The shelf must exist in the system.
   * @return If successful, it returns an empty string. An error message is returned otherwise.
   */
  public static String deleteShelf(String id) {
	  try {
		// Get the shelf, check if it exists
		    Shelf shelf = Shelf.getWithId(id);
		    if (shelf == null) {
		      return "The shelf " + id + " does not exist.";
		    }

		    // Check if the shelf has any cheese wheels
		    for (var location : shelf.getLocations()) {
		      if (location.getCheeseWheel() != null) {
		        return "Cannot delete a shelf that contains cheese wheels.";
		      }
		    }

		    // Should be safe to delete the shelf.
		    CheECSEManager system = CheECSEManagerApplication.getCheecseManager();
		    shelf.delete();
		    system.removeShelve(shelf);
		    CheECSEManagerPersistence.save();

		    return "";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }

    
  }

}
