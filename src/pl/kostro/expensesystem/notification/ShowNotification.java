package pl.kostro.expensesystem.notification;

import java.math.BigDecimal;
import java.text.MessageFormat;

import pl.kostro.expensesystem.Msg;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

public class ShowNotification {
  
  private static void setErrorStyle(Notification notification) {
    notification.setStyleName(ValoTheme.NOTIFICATION_ERROR + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(5000);
    notification.show(Page.getCurrent());
  }
  
  public static void registerProblem() {
    Notification notification = new Notification(Msg.get("notification.registerProblem"));
    notification.setDescription(Msg.get("notification.resiterDesc"));
    setErrorStyle(notification);
  }

  public static void logonProblem() {
    Notification notification = new Notification(Msg.get("notification.logonProblem"));
    notification.setDescription(Msg.get("notification.logonDesc"));
    setErrorStyle(notification);
  }
  
  public static void dbProblem(String msg) {
    Notification notification = new Notification(Msg.get("notification.dbProblem"));
    notification.setDescription((MessageFormat.format(Msg.get("notification.dbDesc"), new Object[] {msg})));
    setErrorStyle(notification);
  }
  
  public static void changeSummary(String userName, BigDecimal actSum, BigDecimal newSum) {
    Notification notification = new Notification(Msg.get("notification.sum"));
    notification.setDescription(MessageFormat.format(Msg.get("notification.sumDesc"), new Object[] {userName, actSum, newSum}));
    notification.setStyleName(ValoTheme.NOTIFICATION_WARNING + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(2000);
    notification.show(Page.getCurrent());
  }

  public static void fieldEmpty(String fieldName) {
    Notification notification = new Notification(Msg.get("notification.formProblem"));
    notification.setDescription(MessageFormat.format(Msg.get("notification.formDesc"), new Object[] {fieldName}));
    setErrorStyle(notification);
  }

  public static void noSuchUser(String name) {
    Notification notification = new Notification(Msg.get("notification.noSuchUserProblem"));
    notification.setDescription(MessageFormat.format(Msg.get("notification.noSuchUserDesc"), new Object[] {name}));
    setErrorStyle(notification);
  }

  public static void removeOwnerProblem() {
    Notification notification = new Notification(Msg.get("notification.removeOwnerProblem"));
    notification.setDescription(Msg.get("notification.removeOwnerDesc"));
    setErrorStyle(notification);
  }

}
