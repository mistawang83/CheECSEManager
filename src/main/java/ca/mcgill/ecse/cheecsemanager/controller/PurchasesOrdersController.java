package ca.mcgill.ecse.cheecsemanager.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Transaction;


/**
 * Controller class to get purchase and order TO objects * 
 * @author Simon Wang
 */

public class PurchasesOrdersController {

  public static TOPurchase getPurchase(Integer id) {
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    for (Transaction t : manager.getTransactions()) {
      if (t.getId() == id) {
        if (t instanceof Purchase) {
          return convertPurchase((Purchase) t);
        }
      }
    }
    return null;
  }

  public static List<TOPurchase> getPurchases() {
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    List<TOPurchase> toPurchases = new ArrayList<TOPurchase>();
    for (Transaction t : manager.getTransactions()) {
      if (t instanceof Purchase) {
        toPurchases.add(convertPurchase((Purchase) t));
      }
    }
    return toPurchases;
  }

  // helper method to convert a purchase to TOPurchase
  private static TOPurchase convertPurchase(Purchase purchase) {
    if (purchase == null) {
      return null;
    }

    int id = purchase.getId();
    Date purchaseDate = purchase.getTransactionDate();
    String farmerEmail = purchase.getFarmer().getEmail();
    int nrCheeseWheels = purchase.getCheeseWheels().size();
    TOPurchase toPurchase = new TOPurchase(id, farmerEmail, purchaseDate, nrCheeseWheels);

    for (CheeseWheel c : purchase.getCheeseWheels()) {
      String monthsAged = String.valueOf(c.getMonthsAged());
      String isSpoiled = String.valueOf(c.isIsSpoiled());
      toPurchase.addMonthsAged(monthsAged);
      toPurchase.addIsSpoiled(isSpoiled);
    }

    return toPurchase;
  }

  public static TOOrder getOrder(Integer id) {
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    for (Transaction t : manager.getTransactions()) {
      if (t.getId() == id) {
        if (t instanceof Order) {
          return convertOrder((Order) t);
        }
      }
    }
    return null;
  }

  public static List<TOOrder> getOrders() {
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    List<TOOrder> toOrders = new ArrayList<TOOrder>();
    for (Transaction t : manager.getTransactions()) {
      if (t instanceof Order) {
        toOrders.add(convertOrder((Order) t));
      }
    }
    return toOrders;
  }

  private static TOOrder convertOrder(Order order) {
    if (order == null) {
      return null;
    }

    int id = order.getId();
    Date orderDate = order.getTransactionDate();
    Date deliveryDate = order.getDeliveryDate();
    String companyName = order.getCompany().getName();
    int nrCheeseWheelsOrdered = order.getNrCheeseWheels();
    int nrCheeseWheelsMissing = order.getNrCheeseWheels() - order.getCheeseWheels().size();
    String monthsAged = String.valueOf(order.getMonthsAged());
    TOOrder toOrder = new TOOrder(id, companyName, orderDate, deliveryDate, nrCheeseWheelsOrdered, nrCheeseWheelsMissing, monthsAged);

    return toOrder;
  }

}
