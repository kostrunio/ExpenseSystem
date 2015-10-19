package pl.kostro.expensesystem;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Msg {
  private static final String BUNDLE_NAME = "pl.kostro.expensesystem.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  private Msg() {
  }

  public static String get(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}
