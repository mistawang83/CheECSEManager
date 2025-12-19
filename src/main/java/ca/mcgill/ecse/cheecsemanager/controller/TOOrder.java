/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.controller;
import java.sql.Date;

// line 59 "../../../../../CheECSEManagerTransferObjects.ump"
public class TOOrder
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOOrder Attributes
  private int id;
  private String companyName;
  private Date orderDate;
  private Date deliveryDate;
  private int nrCheeseWheelsOrdered;
  private int nrCheeseWheelsMissing;
  private String monthsAged;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOOrder(int aId, String aCompanyName, Date aOrderDate, Date aDeliveryDate, int aNrCheeseWheelsOrdered, int aNrCheeseWheelsMissing, String aMonthsAged)
  {
    id = aId;
    companyName = aCompanyName;
    orderDate = aOrderDate;
    deliveryDate = aDeliveryDate;
    nrCheeseWheelsOrdered = aNrCheeseWheelsOrdered;
    nrCheeseWheelsMissing = aNrCheeseWheelsMissing;
    monthsAged = aMonthsAged;
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

  public boolean setCompanyName(String aCompanyName)
  {
    boolean wasSet = false;
    companyName = aCompanyName;
    wasSet = true;
    return wasSet;
  }

  public boolean setOrderDate(Date aOrderDate)
  {
    boolean wasSet = false;
    orderDate = aOrderDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeliveryDate(Date aDeliveryDate)
  {
    boolean wasSet = false;
    deliveryDate = aDeliveryDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setNrCheeseWheelsOrdered(int aNrCheeseWheelsOrdered)
  {
    boolean wasSet = false;
    nrCheeseWheelsOrdered = aNrCheeseWheelsOrdered;
    wasSet = true;
    return wasSet;
  }

  public boolean setNrCheeseWheelsMissing(int aNrCheeseWheelsMissing)
  {
    boolean wasSet = false;
    nrCheeseWheelsMissing = aNrCheeseWheelsMissing;
    wasSet = true;
    return wasSet;
  }

  public boolean setMonthsAged(String aMonthsAged)
  {
    boolean wasSet = false;
    monthsAged = aMonthsAged;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public String getCompanyName()
  {
    return companyName;
  }

  public Date getOrderDate()
  {
    return orderDate;
  }

  public Date getDeliveryDate()
  {
    return deliveryDate;
  }

  public int getNrCheeseWheelsOrdered()
  {
    return nrCheeseWheelsOrdered;
  }

  public int getNrCheeseWheelsMissing()
  {
    return nrCheeseWheelsMissing;
  }

  public String getMonthsAged()
  {
    return monthsAged;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "companyName" + ":" + getCompanyName()+ "," +
            "nrCheeseWheelsOrdered" + ":" + getNrCheeseWheelsOrdered()+ "," +
            "nrCheeseWheelsMissing" + ":" + getNrCheeseWheelsMissing()+ "," +
            "monthsAged" + ":" + getMonthsAged()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orderDate" + "=" + (getOrderDate() != null ? !getOrderDate().equals(this)  ? getOrderDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "deliveryDate" + "=" + (getDeliveryDate() != null ? !getDeliveryDate().equals(this)  ? getDeliveryDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}