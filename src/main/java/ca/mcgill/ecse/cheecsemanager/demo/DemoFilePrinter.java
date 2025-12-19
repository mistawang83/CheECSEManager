package ca.mcgill.ecse.cheecsemanager.demo;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.FacilityManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;

public class DemoFilePrinter {
	
	public static void print(CheECSEManager cheECSEManager) {
		System.out.println("CheECSEManager:");
		FacilityManager manager = cheECSEManager.getManager();
		System.out.println("Manager:  email = " + manager.getEmail() + "; password = " + manager.getPassword());
		for (Shelf s : cheECSEManager.getShelves()) {
			System.out.println("Shelf:    " + s.getId() + "; " + s.getLocation(s.getLocations().size() - 1).getColumn() + " columns; " + s.getLocation(s.getLocations().size() - 1).getRow() + " rows");
			for (ShelfLocation sl : s.getLocations()) {
				System.out.print("          " + sl.getColumn() + "/" + sl.getRow());
				CheeseWheel c = sl.getCheeseWheel();
				if (c != null) {
					System.out.println(": cheese #" + sl.getCheeseWheel().getId() + " " + sl.getCheeseWheel().getMonthsAged().toString() + (sl.getCheeseWheel().isIsSpoiled() == false ? "" : " (spoiled)"));
				} else { 
					System.out.println();
				}
			}
			
		}
		for (Farmer f : cheECSEManager.getFarmers()) {
			System.out.println("Farmer:   " + f.getName() + "; email = " + f.getEmail() + "; password = " + f.getPassword() + "; address = " + f.getAddress());
			for (Purchase p : f.getPurchases()) {
				System.out.println("  Purchase #" + p.getId() + ": purchase date = " + p.getTransactionDate());
				for (CheeseWheel c : p.getCheeseWheels()) {
					System.out.println("    cheese #" + c.getId()  + " " + c.getMonthsAged().toString() + (c.isIsSpoiled() == false ? "" : " (spoiled)"));
				}
			}
		}
		for (WholesaleCompany c : cheECSEManager.getCompanies()) {
			System.out.println("Company:  " + c.getName() + "; address = " + c.getAddress());
			for (Order o : c.getOrders()) {
				System.out.println("  Order #" + o.getId() + ": order date = " + o.getTransactionDate() + "; delivery date = " + o.getDeliveryDate() + "; # cheese = " + o.getNrCheeseWheels() + " " + o.getMonthsAged() + " (" + (o.getNrCheeseWheels() - o.getCheeseWheels().size()) + " missing)");
				for (CheeseWheel cw : o.getCheeseWheels()) {
					System.out.println("    cheese #" + cw.getId()  + " " + cw.getMonthsAged().toString() + (cw.isIsSpoiled() == false ? "" : " (spoiled)") + " (from Purchase #" + cw.getPurchase().getId() + ")");
				}
			}
		}
	}

}
