package pl.kostro.expensesystem.view;

import java.util.Calendar;

import pl.kostro.expensesystem.event.ExpenseSystemEvent.BrowserResizeEvent;
import pl.kostro.expensesystem.event.ExpenseSystemEventBus;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.view.design.MonthDesign;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
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
    ExpenseSystemEventBus.register(this);
    this.date = VaadinSession.getCurrent().getAttribute(Calendar.class);
    UserSummaryService.setFirstDay(date);
    previousMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
    previousMonthButton.addClickListener(prevClick);
    nextMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_RIGHT);
    nextMonthButton.addClickListener(nextClick);
    firstDateField.setDateFormat("yyyy-MM-dd");
    thisMonthField.setDateFormat("MMMM yyyy");
    lastDateField.setDateFormat("yyyy-MM-dd");

    setCalendarSize();
    showCalendar();
  }

  @Override
  public void detach() {
      super.detach();
      ExpenseSystemEventBus.unregister(this);
  }

  public void showCalendar() {
    thisMonthField.setReadOnly(false);
    thisMonthField.setValue(date.getTime());
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

  private void setCalendarSize() {
    if (Page.getCurrent().getBrowserWindowWidth() < 800) {
      monthCalendar.setWidth(Page.getCurrent().getBrowserWindowWidth()-30+"px");
      monthCalendar.setHeight(Page.getCurrent().getBrowserWindowWidth()-30+"px");
    }
  }

  @Subscribe
  public void browserResized(final BrowserResizeEvent event) {
    setCalendarSize();
  }


}
