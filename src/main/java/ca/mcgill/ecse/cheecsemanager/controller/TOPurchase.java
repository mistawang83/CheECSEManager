/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.controller;
import java.sql.Date;
import java.util.*;

// line 50 "../../../../../CheECSEManagerTransferObjects.ump"
public class TOPurchase
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOPurchase Attributes
  private int id;
  private String farmerEmail;
  private Date purchaseDate;
  private int nrCheeseWheels;
  private List<String> monthsAgeds;
  private List<String> isSpoileds;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOPurchase(int aId, String aFarmerEmail, Date aPurchaseDate, int aNrCheeseWheels)
  {
    id = aId;
    farmerEmail = aFarmerEmail;
    purchaseDate = aPurchaseDate;
    nrCheeseWheels = aNrCheeseWheels;
    monthsAgeds = new ArrayList<String>();
    isSpoileds = new ArrayList<String>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setFarmerEmail(String aFarmerEmail)
  {
    boolean wasSet = false;
    farmerEmail = aFarmerEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setNrCheeseWheels(int aNrCheeseWheels)
  {
    boolean wasSet = false;
    nrCheeseWheels = aNrCheeseWheels;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetMany */
  public boolean addMonthsAged(String aMonthsAged)
  {
    boolean wasAdded = false;
    wasAdded = monthsAgeds.add(aMonthsAged);
    return wasAdded;
  }

  public boolean removeMonthsAged(String aMonthsAged)
  {
    boolean wasRemoved = false;
    wasRemoved = monthsAgeds.remove(aMonthsAged);
    return wasRemoved;
  }
  /* Code from template attribute_SetMany */
  public boolean addIsSpoiled(String aIsSpoiled)
  {
    boolean wasAdded = false;
    wasAdded = isSpoileds.add(aIsSpoiled);
    return wasAdded;
  }

  public boolean removeIsSpoiled(String aIsSpoiled)
  {
    boolean wasRemoved = false;
    wasRemoved = isSpoileds.remove(aIsSpoiled);
    return wasRemoved;
  }

  public int getId()
  {
    return id;
  }

  public String getFarmerEmail()
  {
    return farmerEmail;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }

  public int getNrCheeseWheels()
  {
    return nrCheeseWheels;
  }
  /* Code from template attribute_GetMany */
  public String getMonthsAged(int index)
  {
    String aMonthsAged = monthsAgeds.get(index);
    return aMonthsAged;
  }

  public String[] getMonthsAgeds()
  {
    String[] newMonthsAgeds = monthsAgeds.toArray(new String[monthsAgeds.size()]);
    return newMonthsAgeds;
  }

  public int numberOfMonthsAgeds()
  {
    int number = monthsAgeds.size();
    return number;
  }

  public boolean hasMonthsAgeds()
  {
    boolean has = monthsAgeds.size() > 0;
    return has;
  }

  public int indexOfMonthsAged(String aMonthsAged)
  {
    int index = monthsAgeds.indexOf(aMonthsAged);
    return index;
  }
  /* Code from template attribute_GetMany */
  public String getIsSpoiled(int index)
  {
    String aIsSpoiled = isSpoileds.get(index);
    return aIsSpoiled;
  }

  public String[] getIsSpoileds()
  {
    String[] newIsSpoileds = isSpoileds.toArray(new String[isSpoileds.size()]);
    return newIsSpoileds;
  }

  public int numberOfIsSpoileds()
  {
    int number = isSpoileds.size();
    return number;
  }

  public boolean hasIsSpoileds()
  {
    boolean has = isSpoileds.size() > 0;
    return has;
  }

  public int indexOfIsSpoiled(String aIsSpoiled)
  {
    int index = isSpoileds.indexOf(aIsSpoiled);
    return index;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "farmerEmail" + ":" + getFarmerEmail()+ "," +
            "nrCheeseWheels" + ":" + getNrCheeseWheels()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}