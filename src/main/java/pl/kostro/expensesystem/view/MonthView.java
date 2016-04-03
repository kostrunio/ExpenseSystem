package pl.kostro.expensesystem.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.view.design.MonthDesign;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class MonthView extends MonthDesign {

  private Calendar date;
  private Button.ClickListener prevClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      date.add(java.util.Calendar.MONTH, -1);
      showCalendar();
    }
  };
  private Button.ClickListener nextClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      date.add(java.util.Calendar.MONTH, 1);
      showCalendar();
    }
  };

  public MonthView() {
    this.date = VaadinSession.getCurrent().getAttribute(Calendar.class);
    UserSummaryService.setFirstDay(date);
    previousMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
    previousMonthButton.addClickListener(prevClick);
    nextMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_RIGHT);
    nextMonthButton.addClickListener(nextClick);
    firstDateField.setDateFormat("yyyy-MM-dd");
    lastDateField.setDateFormat("yyyy-MM-dd");

    showCalendar();
  }

  public void showCalendar() {
    thisMonthField.setReadOnly(false);
    thisMonthField.setValue(new SimpleDateFormat("MMMM yyyy", UI.getCurrent().getLocale()).format(date.getTime()));
    thisMonthField.setReadOnly(true);
    firstDateField.setValue(UserSummaryService.getFirstDay(date.getTime()));
    lastDateField.setValue(UserSummaryService.getLastDay(date.getTime()));
    monthCalendar.setMonthView(this);
    monthCalendar.setStartDate(firstDateField.getValue());
    monthCalendar.setEndDate(lastDateField.getValue());
  }

  public void fulfillTables() {
    categoryTable.fulfill();
    userLimitTable.fulfill(date);
  }

  


}
