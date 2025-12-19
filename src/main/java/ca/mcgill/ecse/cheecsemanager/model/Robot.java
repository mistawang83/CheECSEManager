/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import java.util.*;

// line 1 "../../../../../RobotStateMachine.ump"
// line 94 "../../../../../CheECSEManager.ump"
public class Robot
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Robot Attributes
  private int currentRow;
  private int currentColumn;
  private boolean isFacingAisle;

  //Robot State Machines
  public enum Status { Idle, Final, AtEntranceNotFacingAisle, AtEntranceFacingAisle, AtCheeseWheel }
  private Status status;

  //Robot Associations
  private Shelf currentShelf;
  private CheeseWheel currentCheeseWheel;
  private List<LogEntry> log;
  private CheECSEManager cheECSEManager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Robot(boolean aIsFacingAisle, CheECSEManager aCheECSEManager)
  {
    currentRow = 0;
    currentColumn = 0;
    isFacingAisle = aIsFacingAisle;
    log = new ArrayList<LogEntry>();
    boolean didAddCheECSEManager = setCheECSEManager(aCheECSEManager);
    if (!didAddCheECSEManager)
    {
      throw new RuntimeException("Unable to create robot due to cheECSEManager. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStatus(Status.Idle);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCurrentRow(int aCurrentRow)
  {
    boolean wasSet = false;
    currentRow = aCurrentRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentColumn(int aCurrentColumn)
  {
    boolean wasSet = false;
    currentColumn = aCurrentColumn;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsFacingAisle(boolean aIsFacingAisle)
  {
    boolean wasSet = false;
    isFacingAisle = aIsFacingAisle;
    wasSet = true;
    return wasSet;
  }

  public int getCurrentRow()
  {
    return currentRow;
  }

  public int getCurrentColumn()
  {
    return currentColumn;
  }

  public boolean getIsFacingAisle()
  {
    return isFacingAisle;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsFacingAisle()
  {
    return isFacingAisle;
  }

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean initializeRobot(String aShelfId)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        if (isValidShelfInput(aShelfId))
        {
        // line 7 "../../../../../RobotStateMachine.ump"
          doInitializeRobot(aShelfId);
          setStatus(Status.AtEntranceNotFacingAisle);
          wasEventProcessed = true;
          break;
        }
        break;
      case AtEntranceNotFacingAisle:
        // line 34 "../../../../../RobotStateMachine.ump"
        rejectInitialize(aShelfId);
        setStatus(Status.AtEntranceNotFacingAisle);
        wasEventProcessed = true;
        break;
      case AtEntranceFacingAisle:
        // line 59 "../../../../../RobotStateMachine.ump"
        rejectInitialize(aShelfId);
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      case AtCheeseWheel:
        // line 88 "../../../../../RobotStateMachine.ump"
        rejectInitialize(aShelfId);
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean treatPurchase()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        // line 10 "../../../../../RobotStateMachine.ump"
        rejectTreatmentBeforeInitialize();
        setStatus(Status.Idle);
        wasEventProcessed = true;
        break;
      case AtEntranceNotFacingAisle:
        setStatus(Status.AtEntranceNotFacingAisle);
        wasEventProcessed = true;
        break;
      case AtEntranceFacingAisle:
        // line 62 "../../../../../RobotStateMachine.ump"
        rejectTreatment();
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      case AtCheeseWheel:
        // line 91 "../../../../../RobotStateMachine.ump"
        rejectTreatment();
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean turnLeft()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        // line 13 "../../../../../RobotStateMachine.ump"
        rejectTurnLeft();
        setStatus(Status.Idle);
        wasEventProcessed = true;
        break;
      case AtEntranceNotFacingAisle:
        // line 40 "../../../../../RobotStateMachine.ump"
        doTurnLeft();
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      case AtEntranceFacingAisle:
        // line 65 "../../../../../RobotStateMachine.ump"
        rejectTurnLeft();
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      case AtCheeseWheel:
        // line 94 "../../../../../RobotStateMachine.ump"
        rejectTurnLeft();
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean turnRight()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        // line 16 "../../../../../RobotStateMachine.ump"
        rejectTurnRight();
        setStatus(Status.Idle);
        wasEventProcessed = true;
        break;
      case AtEntranceNotFacingAisle:
        // line 43 "../../../../../RobotStateMachine.ump"
        rejectTurnRight();
        setStatus(Status.AtEntranceNotFacingAisle);
        wasEventProcessed = true;
        break;
      case AtEntranceFacingAisle:
        // line 68 "../../../../../RobotStateMachine.ump"
        doTurnRight();
        setStatus(Status.AtEntranceNotFacingAisle);
        wasEventProcessed = true;
        break;
      case AtCheeseWheel:
        // line 97 "../../../../../RobotStateMachine.ump"
        rejectTurnRight();
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean goToShelf(String aShelfId)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        // line 19 "../../../../../RobotStateMachine.ump"
        rejectGoToShelf(aShelfId);
        setStatus(Status.Idle);
        wasEventProcessed = true;
        break;
      case AtEntranceNotFacingAisle:
        if (isValidShelfInput(aShelfId))
        {
        // line 37 "../../../../../RobotStateMachine.ump"
          moveToShelf(aShelfId);
          setStatus(Status.AtEntranceNotFacingAisle);
          wasEventProcessed = true;
          break;
        }
        break;
      case AtEntranceFacingAisle:
        // line 71 "../../../../../RobotStateMachine.ump"
        rejectGoToShelf(aShelfId);
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      case AtCheeseWheel:
        // line 100 "../../../../../RobotStateMachine.ump"
        rejectGoToShelf(aShelfId);
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean goToCheeseWheel(Integer aCheeseWheelId)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        // line 22 "../../../../../RobotStateMachine.ump"
        rejectGoToCheeseWheel(aCheeseWheelId);
        setStatus(Status.Idle);
        wasEventProcessed = true;
        break;
      case AtEntranceNotFacingAisle:
        // line 46 "../../../../../RobotStateMachine.ump"
        rejectGoToCheeseWheel(aCheeseWheelId);
        setStatus(Status.AtEntranceNotFacingAisle);
        wasEventProcessed = true;
        break;
      case AtEntranceFacingAisle:
        if (isValidCheeseWheelInput(aCheeseWheelId))
        {
        // line 74 "../../../../../RobotStateMachine.ump"
          moveToCheeseWheel(aCheeseWheelId);
          setStatus(Status.AtCheeseWheel);
          wasEventProcessed = true;
          break;
        }
        break;
      case AtCheeseWheel:
        if (isValidCheeseWheelInput(aCheeseWheelId))
        {
        // line 103 "../../../../../RobotStateMachine.ump"
          moveToCheeseWheel(aCheeseWheelId);
          setStatus(Status.AtCheeseWheel);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean goToEntrance()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        // line 25 "../../../../../RobotStateMachine.ump"
        rejectGoToEntrance();
        setStatus(Status.Idle);
        wasEventProcessed = true;
        break;
      case AtEntranceNotFacingAisle:
        // line 49 "../../../../../RobotStateMachine.ump"
        rejectGoToEntrance();
        setStatus(Status.AtEntranceNotFacingAisle);
        wasEventProcessed = true;
        break;
      case AtEntranceFacingAisle:
        // line 77 "../../../../../RobotStateMachine.ump"
        rejectGoToEntrance();
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      case AtCheeseWheel:
        // line 106 "../../../../../RobotStateMachine.ump"
        moveToEntrance();
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean treatCheeseWheel()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        // line 28 "../../../../../RobotStateMachine.ump"
        rejectTreatCheeseWheel();
        setStatus(Status.Idle);
        wasEventProcessed = true;
        break;
      case AtEntranceNotFacingAisle:
        // line 52 "../../../../../RobotStateMachine.ump"
        rejectTreatCheeseWheel();
        setStatus(Status.AtEntranceNotFacingAisle);
        wasEventProcessed = true;
        break;
      case AtEntranceFacingAisle:
        // line 80 "../../../../../RobotStateMachine.ump"
        rejectTreatCheeseWheel();
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      case AtCheeseWheel:
        // line 109 "../../../../../RobotStateMachine.ump"
        doTreatCheeseWheel();
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean deactivateRobot()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Idle:
        setStatus(Status.Final);
        wasEventProcessed = true;
        break;
      case AtEntranceNotFacingAisle:
        setStatus(Status.Final);
        wasEventProcessed = true;
        break;
      case AtEntranceFacingAisle:
        // line 83 "../../../../../RobotStateMachine.ump"
        rejectDeactivate();
        setStatus(Status.AtEntranceFacingAisle);
        wasEventProcessed = true;
        break;
      case AtCheeseWheel:
        // line 112 "../../../../../RobotStateMachine.ump"
        rejectDeactivate();
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;

    // entry actions and do activities
    switch(status)
    {
      case Final:
        delete();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Shelf getCurrentShelf()
  {
    return currentShelf;
  }

  public boolean hasCurrentShelf()
  {
    boolean has = currentShelf != null;
    return has;
  }
  /* Code from template association_GetOne */
  public CheeseWheel getCurrentCheeseWheel()
  {
    return currentCheeseWheel;
  }

  public boolean hasCurrentCheeseWheel()
  {
    boolean has = currentCheeseWheel != null;
    return has;
  }
  /* Code from template association_GetMany */
  public LogEntry getLog(int index)
  {
    LogEntry aLog = log.get(index);
    return aLog;
  }

  public List<LogEntry> getLog()
  {
    List<LogEntry> newLog = Collections.unmodifiableList(log);
    return newLog;
  }

  public int numberOfLog()
  {
    int number = log.size();
    return number;
  }

  public boolean hasLog()
  {
    boolean has = log.size() > 0;
    return has;
  }

  public int indexOfLog(LogEntry aLog)
  {
    int index = log.indexOf(aLog);
    return index;
  }
  /* Code from template association_GetOne */
  public CheECSEManager getCheECSEManager()
  {
    return cheECSEManager;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setCurrentShelf(Shelf aNewCurrentShelf)
  {
    boolean wasSet = false;
    if (aNewCurrentShelf == null)
    {
      Shelf existingCurrentShelf = currentShelf;
      currentShelf = null;
      
      if (existingCurrentShelf != null && existingCurrentShelf.getRobot() != null)
      {
        existingCurrentShelf.setRobot(null);
      }
      wasSet = true;
      return wasSet;
    }

    Shelf currentCurrentShelf = getCurrentShelf();
    if (currentCurrentShelf != null && !currentCurrentShelf.equals(aNewCurrentShelf))
    {
      currentCurrentShelf.setRobot(null);
    }

    currentShelf = aNewCurrentShelf;
    Robot existingRobot = aNewCurrentShelf.getRobot();

    if (!equals(existingRobot))
    {
      aNewCurrentShelf.setRobot(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setCurrentCheeseWheel(CheeseWheel aNewCurrentCheeseWheel)
  {
    boolean wasSet = false;
    if (aNewCurrentCheeseWheel == null)
    {
      CheeseWheel existingCurrentCheeseWheel = currentCheeseWheel;
      currentCheeseWheel = null;
      
      if (existingCurrentCheeseWheel != null && existingCurrentCheeseWheel.getRobot() != null)
      {
        existingCurrentCheeseWheel.setRobot(null);
      }
      wasSet = true;
      return wasSet;
    }

    CheeseWheel currentCurrentCheeseWheel = getCurrentCheeseWheel();
    if (currentCurrentCheeseWheel != null && !currentCurrentCheeseWheel.equals(aNewCurrentCheeseWheel))
    {
      currentCurrentCheeseWheel.setRobot(null);
    }

    currentCheeseWheel = aNewCurrentCheeseWheel;
    Robot existingRobot = aNewCurrentCheeseWheel.getRobot();

    if (!equals(existingRobot))
    {
      aNewCurrentCheeseWheel.setRobot(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLog()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public LogEntry addLog(String aDescription)
  {
    return new LogEntry(aDescription, this);
  }

  public boolean addLog(LogEntry aLog)
  {
    boolean wasAdded = false;
    if (log.contains(aLog)) { return false; }
    Robot existingRobot = aLog.getRobot();
    boolean isNewRobot = existingRobot != null && !this.equals(existingRobot);
    if (isNewRobot)
    {
      aLog.setRobot(this);
    }
    else
    {
      log.add(aLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLog(LogEntry aLog)
  {
    boolean wasRemoved = false;
    //Unable to remove aLog, as it must always have a robot
    if (!this.equals(aLog.getRobot()))
    {
      log.remove(aLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLogAt(LogEntry aLog, int index)
  {  
    boolean wasAdded = false;
    if(addLog(aLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLog()) { index = numberOfLog() - 1; }
      log.remove(aLog);
      log.add(index, aLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLogAt(LogEntry aLog, int index)
  {
    boolean wasAdded = false;
    if(log.contains(aLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLog()) { index = numberOfLog() - 1; }
      log.remove(aLog);
      log.add(index, aLog);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLogAt(aLog, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setCheECSEManager(CheECSEManager aNewCheECSEManager)
  {
    boolean wasSet = false;
    if (aNewCheECSEManager == null)
    {
      //Unable to setCheECSEManager to null, as robot must always be associated to a cheECSEManager
      return wasSet;
    }
    
    Robot existingRobot = aNewCheECSEManager.getRobot();
    if (existingRobot != null && !equals(existingRobot))
    {
      //Unable to setCheECSEManager, the current cheECSEManager already has a robot, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    CheECSEManager anOldCheECSEManager = cheECSEManager;
    cheECSEManager = aNewCheECSEManager;
    cheECSEManager.setRobot(this);

    if (anOldCheECSEManager != null)
    {
      anOldCheECSEManager.setRobot(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (currentShelf != null)
    {
      currentShelf.setRobot(null);
    }
    if (currentCheeseWheel != null)
    {
      currentCheeseWheel.setRobot(null);
    }
    while (log.size() > 0)
    {
      LogEntry aLog = log.get(log.size() - 1);
      aLog.delete();
      log.remove(aLog);
    }
    
    CheECSEManager existingCheECSEManager = cheECSEManager;
    cheECSEManager = null;
    if (existingCheECSEManager != null)
    {
      existingCheECSEManager.setRobot(null);
    }
  }


  /**
   * Helper methods implemented by Simon Wang
   */
  // line 120 "../../../../../RobotStateMachine.ump"
   private Shelf getShelfById(String shelfId){
    for (Shelf shelf : cheECSEManager.getShelves()) {
      if (shelf.getId().equals(shelfId)) {
        return shelf;
      }
    }
    return null;
  }

  // line 128 "../../../../../RobotStateMachine.ump"
   private CheeseWheel getCheeseWheelById(int id){
    for (CheeseWheel cw : cheECSEManager.getCheeseWheels()) {
        if (cw.getId() == id) {
          return cw;
        }
      }
      return null;
  }


  /**
   * 
   * This method is a guard to make sure that an input shelfId is valid for 
   * methods that require a shelfId input. It throws an exception if invalid.
   * 
   * @author Simon Wang
   * @param aShelfId
   */
  // line 144 "../../../../../RobotStateMachine.ump"
   private boolean isValidShelfInput(String aShelfId){
    if (aShelfId.equals("")) {
      throw new RuntimeException("A shelf must be specified.");
    } else if (getShelfById(aShelfId) == null) {
      throw new RuntimeException("The shelf " + aShelfId + " does not exist.");
    }
    return true;
  }


  /**
   * 
   * This method is an action that initializes a Robot by setting its current
   * shelf to the input shelf ID, and setting the currentRow and currentColumn 
   * to default values (1 and 0 respectively). Then, a log entry of the robot's
   * current shelf is made.
   * 
   * @author Simon Wang
   * @param aShelfId
   */
  // line 162 "../../../../../RobotStateMachine.ump"
   private void doInitializeRobot(String aShelfId){
    Shelf aShelf = getShelfById(aShelfId);
    setCurrentShelf(aShelf);
    setCurrentRow(1);
    setCurrentColumn(0);
    logShelf();
  }


  /**
   * 
   * This method is an action that performs a left turn. It simply creates 
   * a log entry for a left turn.
   * 
   * @author Simon Wang
   */
  // line 176 "../../../../../RobotStateMachine.ump"
   private void doTurnLeft(){
    logTurn("left");
  }


  /**
   * 
   * This method is an action that performs a right turn. It simply creates 
   * a log entry for a right turn.
   * 
   * @author Simon Wang
   */
  // line 186 "../../../../../RobotStateMachine.ump"
   private void doTurnRight(){
    logTurn("right");
  }


  /**
   * 
   * This method is an action that makes the robot move to a given shelf ID.
   * It sets the robot's current shelf as the input shelf. It also creates a 
   * log entry for the straight distance traveled by the robot to make the move.
   * 
   * @author Simon Wang
   * @param aShelfId
   */
  // line 198 "../../../../../RobotStateMachine.ump"
   private void moveToShelf(String aShelfId){
    Shelf aShelf = getShelfById(aShelfId);
    int currentShelfIndex = cheECSEManager.indexOfShelve(this.getCurrentShelf());
    int targetShelfIndex = cheECSEManager.indexOfShelve(aShelf);
    int distance = (targetShelfIndex - currentShelfIndex) * 2;
    logStraight(distance);
    setCurrentShelf(aShelf);
    logShelf();
  }


  /**
   * 
   * This method is an action that makes the robot move to given cheese wheel ID.
   * It sets the robot's current shelf as the given cheese wheel. It also creates
   * log entry for the straight distance traveled by the robot to make the move,
   * as well as another log entry for the height adjustement made.
   * 
   * @author Simon Wang
   * @param aCheeseWheelId
   */
  // line 217 "../../../../../RobotStateMachine.ump"
   private void moveToCheeseWheel(Integer aCheeseWheelId){
    CheeseWheel aCheeseWheel = getCheeseWheelById(aCheeseWheelId);
    int colDiff = aCheeseWheel.getLocation().getColumn() - getCurrentColumn();
    int rowDiff = aCheeseWheel.getLocation().getRow() - getCurrentRow();
    int heightDiff = rowDiff * 40;
    if (colDiff != 0) {
      logStraight(colDiff);
    }
    if (rowDiff != 0) {
      logHeightAdjustment(heightDiff);
    }
    setCurrentCheeseWheel(aCheeseWheel);
    setCurrentRow(aCheeseWheel.getLocation().getRow());
    setCurrentColumn(aCheeseWheel.getLocation().getColumn());
    logCheeseWheel();
  }


  /**
   * 
   * This method is an guard that ensures that an input cheese wheel ID is valid
   * for all events that take a cheese wheel ID as input. It throws an exception if invalid.
   * 
   * @author Simon Wang
   * @param aCheeseWheelId
   */
  // line 241 "../../../../../RobotStateMachine.ump"
   private boolean isValidCheeseWheelInput(Integer aCheeseWheelId){
    CheeseWheel aCheeseWheel = getCheeseWheelById(aCheeseWheelId);
    if (aCheeseWheel.getLocation().getShelf() != getCurrentShelf()) {
      throw new RuntimeException("Cheese wheel #" + aCheeseWheelId + " is not on shelf #" + getCurrentShelf().getId() + ".");
    }
    return true;
  }


  /**
   * 
   * This method is an action that makes the robot move to the entrance of the 
   * shelf it is currently associated to. It logs the straight distance traveled
   * by the robot and logs the height adjustment made.
   * 
   * @author Simon Wang
   */
  // line 256 "../../../../../RobotStateMachine.ump"
   private void moveToEntrance(){
    int colDiff = 0 - getCurrentColumn();
    int rowDiff = 1 - getCurrentRow();
    int heightDiff = rowDiff * 40;
    if (colDiff != 0) {
      logStraight(colDiff);
    }
    if (rowDiff != 0) {
      logHeightAdjustment(heightDiff);
    }
    setCurrentRow(1);
    setCurrentColumn(0);
    setCurrentCheeseWheel(null);
  }


  /**
   * 
   * This method is an action that makes the robot treat the current cheese wheel.
   * It logs the action in a log entry.
   * 
   * @author Simon Wang
   */
  // line 276 "../../../../../RobotStateMachine.ump"
   private void doTreatCheeseWheel(){
    logTreatCheeseWheel();
  }


  /**
   * Helper methods to perform logging actions
   */
  // line 282 "../../../../../RobotStateMachine.ump"
   private void logShelf(){
    String description = "At shelf #" + getCurrentShelf().getId() + ";";
    LogEntry entry = new LogEntry(description, this);
  }

  // line 286 "../../../../../RobotStateMachine.ump"
   private void logCheeseWheel(){
    String description = "At cheese wheel #" + getCurrentCheeseWheel().getId() + ";";
    LogEntry entry = new LogEntry(description, this);
  }

  // line 290 "../../../../../RobotStateMachine.ump"
   private void logStraight(int n){
    String plus = "";
    if (n > 0) {
      plus="+";
    }
    String description = "Straight " + plus + n + " meters;";
    LogEntry entry = new LogEntry(description, this);
  }

  // line 299 "../../../../../RobotStateMachine.ump"
   private void logHeightAdjustment(int n){
    String plus = "";
    if (n > 0) {
      plus = "+";
    }
    String description = "Adjust height " + plus + n + " centimeters;";
    LogEntry entry = new LogEntry(description, this);
  }

  // line 307 "../../../../../RobotStateMachine.ump"
   private void logTurn(String leftOrRight){
    String description = "Turn " + leftOrRight + ";";
    LogEntry entry = new LogEntry(description, this);
  }

  // line 311 "../../../../../RobotStateMachine.ump"
   private void logTreatCheeseWheel(){
    String description = "Treat cheese wheel #" + getCurrentCheeseWheel().getId() + ";";
    LogEntry entry = new LogEntry(description, this);
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   * @param aShelfId
   */
  // line 323 "../../../../../RobotStateMachine.ump"
   private void rejectInitialize(String aShelfId){
    throw new RuntimeException("The robot has already been initialized.");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   */
  // line 331 "../../../../../RobotStateMachine.ump"
   private void rejectTreatment(){
    throw new RuntimeException("The robot cannot be triggered to perform treatment again during active treatment.");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   */
  // line 339 "../../../../../RobotStateMachine.ump"
   private void rejectTreatmentBeforeInitialize(){
    throw new RuntimeException("The robot must be initialized first.");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   */
  // line 348 "../../../../../RobotStateMachine.ump"
   private void rejectDeactivate(){
    throw new RuntimeException("The robot cannot be deactivated during active treatment.");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   */
  // line 357 "../../../../../RobotStateMachine.ump"
   private void rejectTurnLeft(){
    throw new RuntimeException("The robot cannot be turned left.");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   */
  // line 366 "../../../../../RobotStateMachine.ump"
   private void rejectTurnRight(){
    throw new RuntimeException("The robot cannot be turned right.");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   * @param aShelfId
   */
  // line 376 "../../../../../RobotStateMachine.ump"
   private void rejectGoToShelf(String aShelfId){
    throw new RuntimeException("The robot cannot be moved to shelf #" + aShelfId + ".");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   * @param aCheeseWheelId
   */
  // line 386 "../../../../../RobotStateMachine.ump"
   private void rejectGoToCheeseWheel(Integer aCheeseWheelId){
    throw new RuntimeException("The robot cannot be moved to cheese wheel #" + aCheeseWheelId + ".");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   */
  // line 395 "../../../../../RobotStateMachine.ump"
   private void rejectGoToEntrance(){
    throw new RuntimeException("The robot cannot be moved to the entrance of the aisle.");
  }


  /**
   * 
   * This method is an action that rejects the transition event.
   * 
   * @author Simon Wang
   */
  // line 404 "../../../../../RobotStateMachine.ump"
   private void rejectTreatCheeseWheel(){
    throw new RuntimeException("The robot cannot be perform treatment.");
  }


  public String toString()
  {
    return super.toString() + "["+
            "currentRow" + ":" + getCurrentRow()+ "," +
            "currentColumn" + ":" + getCurrentColumn()+ "," +
            "isFacingAisle" + ":" + getIsFacingAisle()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "currentShelf = "+(getCurrentShelf()!=null?Integer.toHexString(System.identityHashCode(getCurrentShelf())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "currentCheeseWheel = "+(getCurrentCheeseWheel()!=null?Integer.toHexString(System.identityHashCode(getCurrentCheeseWheel())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "cheECSEManager = "+(getCheECSEManager()!=null?Integer.toHexString(System.identityHashCode(getCheECSEManager())):"null");
  }
}