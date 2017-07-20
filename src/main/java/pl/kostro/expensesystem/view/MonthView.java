package pl.kostro.expensesystem.view;

import java.sql.Date;
import java.time.ZoneId;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.kostro.expensesystem.event.ExpenseSystemEvent.BrowserResizeEvent;
import pl.kostro.expensesystem.event.ExpenseSystemEventBus;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.view.design.MonthDesign;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class MonthView extends MonthDesign {

  private Logger logger = LogManager.getLogger();
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
    logger.info("create");
    ExpenseSystemEventBus.register(this);
    this.date = VaadinSession.getCurrent().getAttribute(Calendar.class);
    CalendarUtils.setFirstDay(date);
    previousMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
    previousMonthButton.addClickListener(prevClick);
    nextMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_RIGHT);
    nextMonthButton.addClickListener(nextClick);
    firstDateField.setDateFormat("yyyy-MM-dd");
    thisMonthField.setDateFormat("MMMM yyyy");
    thisMonthField.addValueChangeListener(event -> {
      date.setTime(Date.from(thisMonthField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
      showCalendar();
    });
    lastDateField.setDateFormat("yyyy-MM-dd");

    setCalendarSize();
    showCalendar();
  }

  public void showCalendar() {
    if (getParent() != null && getParent().getParent().getParent().getParent() instanceof ExpenseView) {
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedYear(date.get(Calendar.YEAR)+"");
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedMonth(CalendarUtils.getMonthName(date.get(Calendar.MONTH)));
    }
    thisMonthField.setValue(date.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    firstDateField.setValue(CalendarUtils.getFirstDay(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    lastDateField.setValue(CalendarUtils.getLastDay(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    monthCalendar.setMonthView(this);
    monthCalendar.setStartDate(Date.from(firstDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    monthCalendar.setEndDate(Date.from(lastDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
