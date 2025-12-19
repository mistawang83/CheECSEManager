package ca.mcgill.ecse.cheecsemanager.demo.persistence;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;

public class CheECSEManagerPersistence {

  private static String filename = "app.data";
  private static JsonSerializer serializer = new JsonSerializer("ca.mcgill.ecse.cheecsemanager");

  public static void save(CheECSEManager cheECSEManager) {
    serializer.serialize(cheECSEManager, filename);
  }

  public static CheECSEManager load() {
    var cheECSEManager = (CheECSEManager) serializer.deserialize(filename);
    // model cannot be loaded - create empty root object
    if (cheECSEManager == null) {
      cheECSEManager = new CheECSEManager();
    } else {
      cheECSEManager.reinitialize();
    }
    return cheECSEManager;
  }

  public static void setFilename(String filename) {
	  CheECSEManagerPersistence.filename = filename;
  }

}
