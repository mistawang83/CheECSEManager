package ca.mcgill.ecse.cheecsemanager.application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ApplicationSettings {

  private static ApplicationSettings instance;

  private ApplicationSettings() {
    // Private constructor to prevent instantiation
  }

  public static ApplicationSettings getInstance() {
    if (instance == null) {
      instance = new ApplicationSettings();
    }
    return instance;
  }


  /** THEMES **/

  private BooleanProperty isDarkMode = new SimpleBooleanProperty(false);

  public BooleanProperty isDarkModeProperty() {
    return isDarkMode;
  }

  public boolean isDarkMode() {
    return isDarkMode.get();
  }

  public void setDarkMode(boolean isDark) {
    isDarkMode.set(isDark);
  }

  /** COLLAPSED VIEW **/
  private BooleanProperty collapsedView = new SimpleBooleanProperty(false);

  public BooleanProperty collapsedViewProperty() {
    return collapsedView;
  }

  public boolean isCollapsedView() {
    return collapsedView.get();
  }

  public void setCollapsedView(boolean isCollapsed) {
    collapsedView.set(isCollapsed);
  }


}
