package ca.mcgill.ecse.cheecsemanager.persistence;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;



public class CheECSEManagerPersistence {

  private static String filename = "app.data";
  private static JsonSerializer serializer = new JsonSerializer("ca.mcgill.ecse.cheecsemanager");

  public static void setFilename(String filename) {
    CheECSEManagerPersistence.filename = filename;
  }

  public static void save() {
    save(CheECSEManagerApplication.getCheecseManager());
  }

  public static void save(CheECSEManager manager) {
    serializer.serialize(manager, filename);
  }

  public static CheECSEManager load() {
    CheECSEManager manager = (CheECSEManager) serializer.deserialize(filename);
    // model cannot be loaded - create empty CheECSEManager
    if (manager == null) {
      manager = new CheECSEManager();
    } else {
      manager.reinitialize(); // This will re-link your loaded data
    }
    return manager;
  }
}