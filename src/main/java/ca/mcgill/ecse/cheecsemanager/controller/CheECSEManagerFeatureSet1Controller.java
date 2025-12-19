package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.FacilityManager;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;

import java.util.List;

public class CheECSEManagerFeatureSet1Controller {

  public static TOFacilityManager getFacilityManager() {
    CheECSEManager sys = CheECSEManagerApplication.getCheecseManager();
    FacilityManager manager = sys.getManager();
    if (manager == null) {
      return null;
    }
    return new TOFacilityManager(manager.getEmail(), manager.getPassword());
  }

  public static String createFacilityManager(String email, String password) {

    CheECSEManager sys = CheECSEManagerApplication.getCheecseManager();
    if (sys.getManager() != null) {
      return "Facility Manager already exists.";
    }
    if (password.length() < 4) {
      return "Password must be at least 4 characters long.";
    }
    if (!(password.contains("!") || password.contains("#") || password.contains("$"))) {
      return "Password must contain a special character from !, #, or $.";
    }
    boolean lower = false, upper = false;
    for (int i = 0; i < password.length(); i++) {
      if (!lower && Character.isLowerCase(password.charAt(i))) {
        lower = true;
      }
      if (!upper && Character.isUpperCase(password.charAt(i))) {
        upper = true;
      }
    }
    if (!upper) {
      throw new IllegalArgumentException("Password must contain an uppercase character.");
    }
    if (!lower) {
      throw new IllegalArgumentException("Password must contain a lowercase character.");
    }
    FacilityManager manager = new FacilityManager(email, password, sys);
    CheECSEManagerPersistence.save();
    return "";
  }

  /**
   * This method allows the facility manager to update their password, provided the new password is
   * in the valid format.
   * 
   * @author Noah Venturelli
   * @param password
   * @return String message of either the new password, or the reason why the password is invalid
   */
  public static String updateFacilityManager(String password) {
    try {
      CheECSEManager sys = CheECSEManagerApplication.getCheecseManager();
      if (password.length() < 4) {
        return "Password must be at least 4 characters long.";
      }
      if (!(password.contains("!") || password.contains("#") || password.contains("$"))) {
        return "Password must contain a special character from !, #, or $.";
      }
      boolean lower = false, upper = false;
      for (int i = 0; i < password.length(); i++) {
        if (!lower && Character.isLowerCase(password.charAt(i))) {
          lower = true;
        }
        if (!upper && Character.isUpperCase(password.charAt(i))) {
          upper = true;
        }
      }
      if (!upper) {
        return "Password must contain an uppercase character.";
      }
      if (!lower) {
        return "Password must contain a lowercase character.";
      }
      sys.getManager().setPassword(password);
      CheECSEManagerPersistence.save();
      return password;

    } catch (RuntimeException e) {
      return e.getMessage();
    }

  }

  /**
   * This method displays a shelf from the system according to the provided id.
   * 
   * @author Noah Venturelli
   * @param id
   * @return Shelf to be displayed
   */
  public static TOShelf getShelf(String id) {
    Shelf shelf = null;
    TOShelf toshelf = null;
    CheECSEManager sys = CheECSEManagerApplication.getCheecseManager();
    for (Shelf s : sys.getShelves()) {
      if (s.getId().equals(id)) {
        shelf = s;
        break;
      }
    }
    int rows = 0, columns = 0;
    // shelf.
    if (shelf == null) {
      return null;
    }
    toshelf = new TOShelf(id, 0, 0);
    for (ShelfLocation l : shelf.getLocations()) {
      if (l.getRow() > rows) {
        rows = l.getRow();
      }
      if (l.getColumn() > columns) {
        columns = l.getColumn();
      }
      if (l.hasCheeseWheel()) {
        toshelf.addRowNr(l.getRow());
        toshelf.addColumnNr(l.getColumn());
        toshelf.addCheeseWheelID(l.getCheeseWheel().getId());
        String age = (l.getCheeseWheel().getMonthsAged().toString());
        toshelf.addMonthsAged(age);
      }

    }
    toshelf.setMaxColumns(columns);
    toshelf.setMaxRows(rows);

    return toshelf;
  }

  /**
   * This method displays all shelves in the system.
   * 
   * @author Noah Venturelli
   * @return Shelves to be displayed
   */
  // returns all shelves
  public static List<TOShelf> getShelves() {
    CheECSEManager sys = CheECSEManagerApplication.getCheecseManager();
    List<TOShelf> shelves = new java.util.ArrayList<TOShelf>();

    for (Shelf shelf : sys.getShelves()) {
      TOShelf toshelf = new TOShelf(shelf.getId(), 0, 0);
      int rows = 0, columns = 0;
      for (ShelfLocation l : shelf.getLocations()) {
        if (l.getRow() > rows) {
          rows = l.getRow();
        }
        if (l.getColumn() > columns) {
          columns = l.getColumn();
        }
        if (l.hasCheeseWheel()) {
          toshelf.addRowNr(rows);
          toshelf.addColumnNr(columns);
          toshelf.addCheeseWheelID(l.getCheeseWheel().getId());
          String age = (l.getCheeseWheel().getMonthsAged().toString());
          toshelf.addMonthsAged(age);
        }

      }
      toshelf.setMaxColumns(columns);
      toshelf.setMaxRows(rows);
      // shelves.add(new TOShelf(shelf.getId(), columns, rows));
      shelves.add(toshelf);

    }
    return shelves;

  }
}
