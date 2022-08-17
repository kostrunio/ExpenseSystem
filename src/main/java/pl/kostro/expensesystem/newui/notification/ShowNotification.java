package pl.kostro.expensesystem.newui.notification;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class ShowNotification {
  
  private static void setSuccessStyle(Notification notification) {
//    notification.setClassName(ValoTheme.NOTIFICATION_SUCCESS + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Notification.Position.BOTTOM_CENTER);
    notification.setDuration(2000);
    notification.open();
  }
  
  private static void setWarnStyle(Notification notification) {
//    notification.setClassName(ValoTheme.NOTIFICATION_WARNING + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Notification.Position.BOTTOM_CENTER);
    notification.setDuration(2000);
    notification.open();
  }
  
  private static void setErrorStyle(Notification notification) {
//    notification.setClassName(ValoTheme.NOTIFICATION_ERROR + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Notification.Position.BOTTOM_CENTER);
    notification.setDuration(5000);
    notification.open();
  }
  
  public static void registerOK() {
    Notification notification = prepareNotification(Msg.get("notification.registerOK"), Msg.get("notification.registerUserOK"));
    setSuccessStyle(notification);
  }
  
  public static void registerProblem(String user) {
    Notification notification = prepareNotification(Msg.get("notification.registerUserProblem"), MessageFormat.format(Msg.get("notification.registerUserDesc"), user));
    setErrorStyle(notification);
  }
  
  public static void registerProblem() {
    Notification notification = prepareNotification(Msg.get("notification.registerProblem"), Msg.get("notification.registerDesc"));
    setErrorStyle(notification);
  }

  public static void logonProblem() {
    Notification notification = prepareNotification(Msg.get("notification.logonProblem"), Msg.get("notification.logonDesc"));
    setErrorStyle(notification);
  }
  
  public static void dbProblem(String msg) {
    Notification notification = prepareNotification(Msg.get("notification.dbProblem"), MessageFormat.format(Msg.get("notification.dbDesc"), msg));
    setErrorStyle(notification);
  }
  
  public static void changeSummary(String userName, BigDecimal actSum, BigDecimal newSum) {
    Notification notification = prepareNotification(Msg.get("notification.sum"), MessageFormat.format(Msg.get("notification.sumDesc"), new Object[] {userName, actSum, newSum}));
    setWarnStyle(notification);
  }

  public static void fieldEmpty(String fieldName) {
    Notification notification = prepareNotification(Msg.get("notification.formProblem"), MessageFormat.format(Msg.get("notification.formDesc"), fieldName));
    setErrorStyle(notification);
  }

  public static void noSuchUser(String name) {
    Notification notification = prepareNotification(Msg.get("notification.noSuchUserProblem"), MessageFormat.format(Msg.get("notification.noSuchUserDesc"), name));
    setErrorStyle(notification);
  }

  public static void removeOwnerProblem() {
    Notification notification = prepareNotification(Msg.get("notification.removeOwnerProblem"), Msg.get("notification.removeOwnerDesc"));
    setErrorStyle(notification);
  }

  public static void badSheetPassword() {
    Notification notification = prepareNotification(Msg.get("notification.badSheetPasswordProblem"), Msg.get("notification.badSheetPasswordDesc"));
    setErrorStyle(notification);
  }

  public static void passwordProblem() {
    Notification notification = prepareNotification(Msg.get("notification.newSheetProblem"), Msg.get("notification.newSheetDesc"));
    setErrorStyle(notification);
  }

  public static void changeEmailOK() {
    Notification notification = prepareNotification(Msg.get("notification.changeOK"), Msg.get("notification.emailChangeOK"));
    setSuccessStyle(notification);
  }

  public static void changePasswordOK() {
    Notification notification = prepareNotification(Msg.get("notification.changeOK"), Msg.get("notification.passwordChangeOK"));
    setSuccessStyle(notification);
  }

  private static Notification prepareNotification(String title, String description) {
    VerticalLayout layout = new VerticalLayout();
    Span titleLine = new Span(title);
    Span descLine = new Span(description);
    layout.add(titleLine, descLine);
    return new Notification(layout);
  }
}
