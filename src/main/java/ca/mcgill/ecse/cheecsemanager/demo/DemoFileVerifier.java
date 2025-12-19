package ca.mcgill.ecse.cheecsemanager.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse.cheecsemanager.demo.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.FacilityManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.Transaction;
import ca.mcgill.ecse.cheecsemanager.model.User;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;

public class DemoFileVerifier {

  public static void verify(CheECSEManager cheECSEManager) {
    // clear
    cheECSEManager.delete();
    // load
    cheECSEManager = CheECSEManagerPersistence.load();
    // test
    verifyLoaded(cheECSEManager);
    // output
    DemoFilePrinter.print(cheECSEManager);
  }

  private static void verifyLoaded(CheECSEManager cheECSEManager) {
    // root
    assertNotNull(cheECSEManager);

    // manager
    verifyManager("manager@cheecse.fr", "p#Ssw0rd");

    // shelves incl. shelf locations
    verifyShelf("A12", 5, 4);
    verifyShelf("B21", 3, 3);

    // farmers
    verifyFarmer("farmer@cheesy.fr", "P@ssw0rd", "112 Av", "Farmer A");

    // purchases incl. cheese wheels (location, spoiled, and monthsAged for each cheese wheel)
    verifyPurchase(1, "2025-04-01", 3, cheECSEManager);
    verifyCheeseWheel(1, "A12", 2, 1, MaturationPeriod.Twelve, false, cheECSEManager);
    verifyCheeseWheel(2, "A12", 3, 2, MaturationPeriod.Twelve, false, cheECSEManager);
    verifyCheeseWheel(3, "B21", 2, 2, MaturationPeriod.Twelve, false, cheECSEManager);
    verifyPurchase(2, "2025-04-02", 2, cheECSEManager);
    verifyCheeseWheel(4, "A12", 5, 2, MaturationPeriod.Six, false, cheECSEManager);
    verifyCheeseWheel(5, "A12", 5, 1, MaturationPeriod.Six, false, cheECSEManager);
    verifyPurchase(3, "2025-04-03", 1, cheECSEManager);
    verifyCheeseWheel(6, "B21", 1, 3, MaturationPeriod.Six, false, cheECSEManager);
    verifyPurchase(4, "2025-04-04", 3, cheECSEManager);
    verifyCheeseWheel(7, null, 0, 0, MaturationPeriod.Six, true, cheECSEManager);
    verifyCheeseWheel(8, "A12", 4, 3, MaturationPeriod.ThirtySix, false, cheECSEManager);
    verifyCheeseWheel(9, "A12", 1, 1, MaturationPeriod.Six, false, cheECSEManager);
    verifyPurchase(5, "2025-04-05", 3, cheECSEManager);
    verifyCheeseWheel(10, "A12", 4, 2, MaturationPeriod.TwentyFour, false, cheECSEManager);
    verifyCheeseWheel(11, "A12", 1, 2, MaturationPeriod.TwentyFour, false, cheECSEManager);
    verifyCheeseWheel(12, null, 0, 0, MaturationPeriod.Twelve, true, cheECSEManager);

    // wholesale companies
    verifyWholesaleCompany("Cheesy Bites", "112 Ave");

    // orders incl. assigned cheese wheels
    verifyOrder(6, "2025-04-04", 2, MaturationPeriod.Six, "2025-12-01", "Cheesy Bites",
        cheECSEManager);
    verifyCheeseWheelInOrder(6, 4, cheECSEManager);
    verifyCheeseWheelInOrder(6, 5, cheECSEManager);
    verifyOrder(7, "2025-04-04", 3, MaturationPeriod.Twelve, "2026-05-01", "Cheesy Bites",
        cheECSEManager);
    verifyCheeseWheelInOrder(7, 1, cheECSEManager);
    verifyCheeseWheelInOrder(7, 2, cheECSEManager);
    verifyCheeseWheelInOrder(7, 3, cheECSEManager);

    System.out.println("=======================");
    System.out.println("Verification successful");
    System.out.println("=======================");
  }

  private static void verifyManager(String email, String password) {
    User u = User.getWithEmail(email);
    assertNotNull(u);
    assertTrue(u instanceof FacilityManager);
    assertEquals(password, u.getPassword());
  }

  private static void verifyShelf(String id, int maxColumns, int maxRows) {
    Shelf s = Shelf.getWithId(id);
    assertNotNull(s);
    int counterColumns = 1;
    int counterRows = 1;
    while (counterColumns <= maxColumns && counterRows <= maxRows) {
      assertEquals(counterColumns,
          s.getLocation((counterColumns - 1) * maxRows + counterRows - 1).getColumn());
      assertEquals(counterRows,
          s.getLocation((counterColumns - 1) * maxRows + counterRows - 1).getRow());
      counterRows++;
      if (counterRows > maxRows) {
        counterRows = 1;
        counterColumns++;
      }
    }
  }

  private static void verifyFarmer(String email, String password, String address, String name) {
    User u = User.getWithEmail(email);
    assertNotNull(u);
    assertTrue(u instanceof Farmer);
    assertEquals(password, u.getPassword());
    assertEquals(address, ((Farmer) u).getAddress());
    assertEquals(name, ((Farmer) u).getName());
  }

  private static void verifyPurchase(int purchaseID, String purchaseDate, int numberOfCheeseWheels,
      CheECSEManager cheECSEManager) {
    Transaction foundTransaction = findTransaction(purchaseID, cheECSEManager);
    assertTrue(foundTransaction instanceof Purchase);
    assertEquals(purchaseDate, foundTransaction.getTransactionDate().toString());
    assertEquals(numberOfCheeseWheels, ((Purchase) foundTransaction).getCheeseWheels().size());
  }

  private static void verifyCheeseWheel(int cheeseWheelID, String shelfID, int column, int row,
      MaturationPeriod monthsAged, Boolean spoiled, CheECSEManager cheECSEManager) {
    CheeseWheel c = cheECSEManager.getCheeseWheel(cheeseWheelID - 1);
    if (shelfID == null) {
      assertNull(c.getLocation());
    } else {
      assertEquals(shelfID, c.getLocation().getShelf().getId());
    }
  }

  private static void verifyWholesaleCompany(String name, String address) {
    WholesaleCompany c = WholesaleCompany.getWithName(name);
    assertNotNull(c);
    assertEquals(address, c.getAddress());
  }

  private static void verifyOrder(int orderID, String orderDate, int numberOfCheeseWheels,
      MaturationPeriod monthsAged, String deliveryDate, String companyName,
      CheECSEManager cheECSEManager) {
    Transaction foundTransaction = findTransaction(orderID, cheECSEManager);
    assertTrue(foundTransaction instanceof Order);
    assertEquals(orderDate, foundTransaction.getTransactionDate().toString());
    assertEquals(numberOfCheeseWheels, ((Order) foundTransaction).getNrCheeseWheels());
    assertEquals(monthsAged, ((Order) foundTransaction).getMonthsAged());
    assertEquals(deliveryDate, ((Order) foundTransaction).getDeliveryDate().toString());
    assertEquals(companyName, ((Order) foundTransaction).getCompany().getName());
  }

  private static void verifyCheeseWheelInOrder(int orderID, int cheeseWheelID,
      CheECSEManager cheECSEManager) {
    Transaction foundTransaction = findTransaction(orderID, cheECSEManager);
    assertTrue(foundTransaction instanceof Order);
    Boolean found = false;
    for (CheeseWheel c : ((Order) foundTransaction).getCheeseWheels()) {
      if (c.getId() == cheeseWheelID) {
        found = true;
      }
    }
    assertTrue(found);
  }

  private static Transaction findTransaction(int purchaseID, CheECSEManager cheECSEManager) {
    Transaction foundTransaction = null;
    for (Transaction t : cheECSEManager.getTransactions()) {
      if (t.getId() == purchaseID) {
        foundTransaction = t;
        break;
      }
    }
    assertNotNull(foundTransaction);
    return foundTransaction;
  }

}
