package pl.kostro.expensesystem.notification;

import java.math.BigDecimal;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

public class ShowNotification {

  public static void logonProblem() {
    Notification notification = new Notification("B³¹d logowania");
    notification.setDescription("B³êdna nazwa u¿ytkownika lub has³o");
    notification.setStyleName(ValoTheme.NOTIFICATION_ERROR + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(5000);
    notification.show(Page.getCurrent());
  }
  
  public static void dbProblem() {
    Notification notification = new Notification("Problem z baza danych");
    notification.setDescription("Nie udalo sie polaczyc do bazy danych");
    notification.setStyleName(ValoTheme.NOTIFICATION_ERROR + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(5000);
    notification.show(Page.getCurrent());
  }
  
  public static void changeSummary(String userName, BigDecimal actSum, BigDecimal newSum) {
    Notification notification = new Notification("Suma wydatków");
    StringBuilder desc = new StringBuilder();
    desc.append("Zaktualizowano wydatki dla: " + userName + "\n");
    desc.append("Bylo: " + actSum + ", jest: " + newSum);
    notification.setDescription(desc.toString());
    notification.setStyleName(ValoTheme.NOTIFICATION_WARNING + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(2000);
    notification.show(Page.getCurrent());
  }

  public static void fieldEmpty(String fieldName) {
    Notification notification = new Notification("Blad formularza");
    notification.setDescription("Pole " + fieldName + " nie mo¿e byc puste");
    notification.setStyleName(ValoTheme.NOTIFICATION_ERROR + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(5000);
    notification.show(Page.getCurrent());
  }

  public static void expenseSheetAdded() {
    Notification notification = new Notification("Arkusz dodany");
    notification.setDescription("Prosze ponownie sie zalogowac");
    notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
    notification.setPosition(Position.BOTTOM_CENTER);
    notification.setDelayMsec(5000);
    notification.show(Page.getCurrent());
  }

}
