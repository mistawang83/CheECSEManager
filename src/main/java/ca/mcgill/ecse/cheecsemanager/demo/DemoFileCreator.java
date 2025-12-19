package ca.mcgill.ecse.cheecsemanager.demo;

import java.sql.Date;
import java.util.Calendar;

import ca.mcgill.ecse.cheecsemanager.demo.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.FacilityManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;

public class DemoFileCreator {
	
	public static void main(String[] args) {
		CheECSEManager cheECSEManager = createDemoData();
		// save
		CheECSEManagerPersistence.setFilename("app.data");
		CheECSEManagerPersistence.save(cheECSEManager);
		// test
		DemoFileVerifier.verify(cheECSEManager);
	}

	private static CheECSEManager createDemoData() {
		// root
		CheECSEManager cheECSEManager = new CheECSEManager();
		
		// manager
		new FacilityManager("manager@cheecse.fr", "p#Ssw0rd", cheECSEManager);

		// shelves incl. shelf locations
		Shelf sA = new Shelf("A12", cheECSEManager);
		int sAmaxRows = 4;
		createShelfLocations(sA, 5, sAmaxRows);
		Shelf sB = new Shelf("B21", cheECSEManager);
		int sBmaxRows = 3;
		createShelfLocations(sB, 3, sBmaxRows);
		
		// farmers
		Farmer f = new Farmer("farmer@cheesy.fr", "P@ssw0rd", "112 Av", cheECSEManager);
		f.setName("Farmer A");
		
		// purchases incl. cheese wheels (location, spoiled, and monthsAged for each cheese wheel)
		Calendar purchaseCalendar = Calendar.getInstance();
		purchaseCalendar.set(2025, Calendar.APRIL, 1, 0, 0, 0);
		Purchase p1 = new Purchase(new Date(purchaseCalendar.getTimeInMillis()), cheECSEManager, f);
		createCheeseWheel(p1, sA, sAmaxRows, 2, 1, MaturationPeriod.Twelve, false, cheECSEManager);
		createCheeseWheel(p1, sA, sAmaxRows, 3, 2, MaturationPeriod.Twelve, false, cheECSEManager);
		createCheeseWheel(p1, sB, sBmaxRows, 2, 2, MaturationPeriod.Twelve, false, cheECSEManager);
		purchaseCalendar.set(2025, Calendar.APRIL, 2, 0, 0, 0);
		Purchase p2 = new Purchase(new Date(purchaseCalendar.getTimeInMillis()), cheECSEManager, f);
		createCheeseWheel(p2, sA, sAmaxRows, 5, 2, MaturationPeriod.Six, false, cheECSEManager);
		createCheeseWheel(p2, sA, sAmaxRows, 5, 1, MaturationPeriod.Six, false, cheECSEManager);
		purchaseCalendar.set(2025, Calendar.APRIL, 3, 0, 0, 0);
		Purchase p3 = new Purchase(new Date(purchaseCalendar.getTimeInMillis()), cheECSEManager, f);
		createCheeseWheel(p3, sB, sBmaxRows, 1, 3, MaturationPeriod.Six, false, cheECSEManager);
		purchaseCalendar.set(2025, Calendar.APRIL, 4, 0, 0, 0);
		Purchase p4 = new Purchase(new Date(purchaseCalendar.getTimeInMillis()), cheECSEManager, f);
		createCheeseWheel(p4, null, 0, 0, 0, MaturationPeriod.Six, true, cheECSEManager);
		createCheeseWheel(p4, sA, sAmaxRows, 4, 3, MaturationPeriod.ThirtySix, false, cheECSEManager);
		createCheeseWheel(p4, sA, sAmaxRows, 1, 1, MaturationPeriod.Six, false, cheECSEManager);
		purchaseCalendar.set(2025, Calendar.APRIL, 5, 0, 0, 0);
		Purchase p5 = new Purchase(new Date(purchaseCalendar.getTimeInMillis()), cheECSEManager, f);
		createCheeseWheel(p5, sA, sAmaxRows, 4, 2, MaturationPeriod.TwentyFour, false, cheECSEManager);
		createCheeseWheel(p5, sA, sAmaxRows, 1, 2, MaturationPeriod.TwentyFour, false, cheECSEManager);
		createCheeseWheel(p5, null, 0, 0, 0, MaturationPeriod.Twelve, true, cheECSEManager);
		
		// wholesale companies
		WholesaleCompany wc = new WholesaleCompany("Cheesy Bites", "112 Ave", cheECSEManager);
		
		// orders incl. assigned cheese wheels
		Calendar transactionCalendar = Calendar.getInstance();
		Calendar deliveryCalendar = Calendar.getInstance();
		transactionCalendar.set(2025, Calendar.APRIL, 4, 0, 0, 0);
		deliveryCalendar.set(2025, Calendar.DECEMBER, 1, 0, 0, 0);
		Order o1 = new Order(new Date(transactionCalendar.getTimeInMillis()), cheECSEManager, 2, MaturationPeriod.Six, new Date(deliveryCalendar.getTimeInMillis()), wc);
		o1.addCheeseWheel(p2.getCheeseWheel(0));
		o1.addCheeseWheel(p2.getCheeseWheel(1));
		transactionCalendar.set(2025, Calendar.APRIL, 4, 0, 0, 0);
		deliveryCalendar.set(2026, Calendar.MAY, 1, 0, 0, 0);
		Order o2 = new Order(new Date(transactionCalendar.getTimeInMillis()), cheECSEManager, 3, MaturationPeriod.Twelve, new Date(deliveryCalendar.getTimeInMillis()), wc);
		o2.addCheeseWheel(p1.getCheeseWheel(0));
		o2.addCheeseWheel(p1.getCheeseWheel(1));
		o2.addCheeseWheel(p1.getCheeseWheel(2));
		
		return cheECSEManager;
	}

	private static void createShelfLocations(Shelf shelf, int maxColumns, int maxRows) {
		for (int column = 1; column < maxColumns + 1; column++) {
			for (int row = 1; row < maxRows + 1; row++) {
				new ShelfLocation(column, row, shelf);
			}
		}
	}

	private static void createCheeseWheel(Purchase purchase, Shelf shelf, int maxRows, int column, int row, MaturationPeriod monthsAged, Boolean spoiled, CheECSEManager cheECSEManager) {
		CheeseWheel c = new CheeseWheel(monthsAged, spoiled, purchase, cheECSEManager);
		if (shelf != null) {
			c.setLocation(shelf.getLocation((column - 1) * maxRows + row - 1));			
		}
	}

}
