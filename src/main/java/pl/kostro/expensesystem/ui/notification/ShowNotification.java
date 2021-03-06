package pl.kostro.expensesystem.ui.notification;

import java.math.BigDecimal;
import java.text.MessageFormat;

import pl.kostro.expensesystem.utils.msg.Msg;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

public class ShowNotification {
  
  private static void setSuccessStyle(Notification notification) {
    notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(2000);
    notification.show(Page.getCurrent());
  }
  
  private static void setWarnStyle(Notification notification) {
    notification.setStyleName(ValoTheme.NOTIFICATION_WARNING + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(2000);
    notification.show(Page.getCurrent());
  }
  
  private static void setErrorStyle(Notification notification) {
    notification.setStyleName(ValoTheme.NOTIFICATION_ERROR + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(5000);
    notification.show(Page.getCurrent());
  }
  
  public static void registerOK() {
    Notification notification = new Notification(Msg.get("notification.registerOK"));
    notification.setDescription(Msg.get("notification.registerUserOK"));
    setSuccessStyle(notification);
  }
  
  public static void registerProblem(String user) {
    Notification notification = new Notification(Msg.get("notification.registerUserProblem"));
    notification.setDescription(MessageFormat.format(Msg.get("notification.registerUserDesc"), user));
    setErrorStyle(notification);
  }
  
  public static void registerProblem() {
    Notification notification = new Notification(Msg.get("notification.registerProblem"));
    notification.setDescription(Msg.get("notification.registerDesc"));
    setErrorStyle(notification);
  }

  public static void logonProblem() {
    Notification notification = new Notification(Msg.get("notification.logonProblem"));
    notification.setDescription(Msg.get("notification.logonDesc"));
    setErrorStyle(notification);
  }
  
  public static void dbProblem(String msg) {
    Notification notification = new Notification(Msg.get("notification.dbProblem"));
    notification.setDescription((MessageFormat.format(Msg.get("notification.dbDesc"), msg)));
    setErrorStyle(notification);
  }
  
  public static void changeSummary(String userName, BigDecimal actSum, BigDecimal newSum) {
    Notification notification = new Notification(Msg.get("notification.sum"));
    notification.setDescription(MessageFormat.format(Msg.get("notification.sumDesc"), new Object[] {userName, actSum, newSum}));
    setWarnStyle(notification);
  }

  public static void fieldEmpty(String fieldName) {
    Notification notification = new Notification(Msg.get("notification.formProblem"));
    notification.setDescription(MessageFormat.format(Msg.get("notification.formDesc"), fieldName));
    setErrorStyle(notification);
  }

  public static void noSuchUser(String name) {
    Notification notification = new Notification(Msg.get("notification.noSuchUserProblem"));
    notification.setDescription(MessageFormat.format(Msg.get("notification.noSuchUserDesc"), name));
    setErrorStyle(notification);
  }

  public static void removeOwnerProblem() {
    Notification notification = new Notification(Msg.get("notification.removeOwnerProblem"));
    notification.setDescription(Msg.get("notification.removeOwnerDesc"));
    setErrorStyle(notification);
  }

  public static void badSheetPassword() {
    Notification notification = new Notification(Msg.get("notification.badSheetPasswordProblem"));
    notification.setDescription(Msg.get("notification.badSheetPasswordDesc"));
    setErrorStyle(notification);
  }

  public static void passwordProblem() {
    Notification notification = new Notification(Msg.get("notification.newSheetProblem"));
    notification.setDescription(Msg.get("notification.newSheetDesc"));
    setErrorStyle(notification);
  }

  public static void changeEmailOK() {
    Notification notification = new Notification(Msg.get("notification.changeOK"));
    notification.setDescription(Msg.get("notification.emailChangeOK"));
    setSuccessStyle(notification);
  }

  public static void changePasswordOK() {
    Notification notification = new Notification(Msg.get("notification.changeOK"));
    notification.setDescription(Msg.get("notification.passwordChangeOK"));
    setSuccessStyle(notification);
  }
}
