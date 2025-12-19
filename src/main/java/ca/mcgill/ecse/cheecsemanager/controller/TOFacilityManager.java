/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.controller;

// line 45 "../../../../../CheECSEManagerTransferObjects.ump"
public class TOFacilityManager
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOFacilityManager Attributes
  private String email;
  private String password;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOFacilityManager(String aEmail, String aPassword)
  {
    email = aEmail;
    password = aPassword;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "]";
  }
}