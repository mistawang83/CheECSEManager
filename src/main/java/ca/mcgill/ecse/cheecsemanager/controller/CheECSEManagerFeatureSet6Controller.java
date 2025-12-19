package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * This controller class handles operations related to wholesale companies
 * for Feature 6 of the cheese management system. It provides methods to:
 * - Delete wholesale companies from the system
 * - View details of individual wholesale companies
 * - View a list of all wholesale companies
 *
 * This class works with the WholesaleCompany model to perform these operations
 * and returns the results in TOWholesaleCompany objects for display.
 *
 * @author Jiahao Li
 */
public class CheECSEManagerFeatureSet6Controller {
    /**
     * Deletes a wholesale company by its name
     * Makes sure the company exists and has no orders before deleting
     * If the company has orders, it cannot be deleted
     *
     * @param name the name of the company to delete
     * @return empty string if deleted successfully, error message if something went wrong
     *
     * @author Jiahao Li
     */
    public static String deleteWholesaleCompany(String name) {
    	try {
    		// First I need to get the system
            CheECSEManager theSystem = CheECSEManagerApplication.getCheecseManager();

            // Check if name is null
            if (name == null) {
                return "The name must not be empty.";
            }

            // Check if name has only spaces
            boolean onlySpaces = true;
            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) != ' ') {
                    onlySpaces = false;
                    break;
                }
            }
            if (onlySpaces) {
                return "The name must not be empty.";
            }

            // Now I need to find the company with this name
            WholesaleCompany companyToDelete = null;
            boolean foundCompany = false;

            // Get the list of companies
            List<WholesaleCompany> allCompanies = theSystem.getCompanies();

            for (int i = 0; i < allCompanies.size(); i++) {
                WholesaleCompany currentCompany = allCompanies.get(i);

                // Check if this company has the name we're looking for
                if (currentCompany.getName().equals(name)) {
                    companyToDelete = currentCompany;
                    foundCompany = true;
                    break; // Stop looking once we found it
                }
            }

            // If we didn't find the company, return error
            if (!foundCompany) {
                return "The wholesale company " + name + " does not exist.";
            }

            // Check if company has any orders
            int orderCount = companyToDelete.numberOfOrders();
            if (orderCount > 0) {
                return "Cannot delete a wholesale company that has ordered cheese.";
            }

            // If everything is good, delete the company
            companyToDelete.delete();
		    CheECSEManagerPersistence.save();

            return "";
		  } catch (RuntimeException e) {
		    return e.getMessage();
		  }
        
    }

    /**
     * Finds and shows information about one specific wholesale company
     * Shows the company's address and all the orders it has placed
     *
     * @param name the name of the company to look for
     * @return the company's information if found, nothing if company doesn't exist
     *
     * @author Jiahao Li
     */
    public static TOWholesaleCompany getWholesaleCompany(String name) {
        if (name == null) {
            return null;
        }

        // Check if name has only spaces
        boolean onlySpaces = true;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) != ' ') {
                onlySpaces = false;
                break;
            }
        }
        if (onlySpaces) {
            return null;
        }

        CheECSEManager theSystem = CheECSEManagerApplication.getCheecseManager();
        List<WholesaleCompany> allCompanies = theSystem.getCompanies();

        for (int i = 0; i < allCompanies.size(); i++) {
            WholesaleCompany currentCompany = allCompanies.get(i);

            if (currentCompany.getName().equals(name)) {
                // Create TO object with company name and address
                TOWholesaleCompany result = new TOWholesaleCompany(currentCompany.getName(), currentCompany.getAddress());

                // Add all orders for this company
                int numberOfOrders = currentCompany.numberOfOrders();
                for (int j = 0; j < numberOfOrders; j++) {
                    Order currentOrder = currentCompany.getOrder(j);

                    // Get all the order details
                    Date orderDate = currentOrder.getTransactionDate();
                    String monthsAged = currentOrder.getMonthsAged().toString();
                    int nrOrdered = currentOrder.getNrCheeseWheels();
                    int nrAssigned = currentOrder.numberOfCheeseWheels();
                    int nrMissing = nrOrdered - nrAssigned;
                    Date deliveryDate = currentOrder.getDeliveryDate();

                    // Add all details to the TO object
                    result.addOrderDate(orderDate);
                    result.addMonthsAged(monthsAged);
                    result.addNrCheeseWheelsOrdered(nrOrdered);
                    result.addNrCheeseWheelsMissing(nrMissing);
                    result.addDeliveryDate(deliveryDate);
                }

                return result;
            }
        }

        return null;
    }

    /**
     * Shows a list of all wholesale companies in the system
     * For each company, shows its address and all its orders
     *
     * @return a list containing all wholesale companies and their information
     *
     * @author Jiahao Li
     */
    public static List<TOWholesaleCompany> getWholesaleCompanies() {
        List<TOWholesaleCompany> resultList = new ArrayList<>();

        CheECSEManager theSystem = CheECSEManagerApplication.getCheecseManager();

        List<WholesaleCompany> allCompanies = theSystem.getCompanies();

        for (int i = 0; i < allCompanies.size(); i++) {
            WholesaleCompany currentCompany = allCompanies.get(i);

            // Create TO object for this company
            TOWholesaleCompany companyTO = new TOWholesaleCompany(currentCompany.getName(), currentCompany.getAddress());

            // Add all orders for this company
            int orderCount = currentCompany.numberOfOrders();
            for (int j = 0; j < orderCount; j++) {
                Order currentOrder = currentCompany.getOrder(j);

                // Get order details
                Date orderDate = currentOrder.getTransactionDate();
                String monthsAged = currentOrder.getMonthsAged().toString();
                int nrOrdered = currentOrder.getNrCheeseWheels();
                int nrAssigned = currentOrder.numberOfCheeseWheels();
                int nrMissing = nrOrdered - nrAssigned;
                Date deliveryDate = currentOrder.getDeliveryDate();

                // Add order details to TO object
                companyTO.addOrderDate(orderDate);
                companyTO.addMonthsAged(monthsAged);
                companyTO.addNrCheeseWheelsOrdered(nrOrdered);
                companyTO.addNrCheeseWheelsMissing(nrMissing);
                companyTO.addDeliveryDate(deliveryDate);
            }

            // Add this company TO to the result list
            resultList.add(companyTO);
        }

        // Return the list of all companies
        return resultList;
    }
}