package pl.kostro.expensesystem.view;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.kostro.expensesystem.event.ExpenseSystemEvent.BrowserResizeEvent;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.event.ExpenseSystemEventBus;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.view.design.MonthDesign;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class MonthView extends MonthDesign {

  private UserSummaryService uss;
  
  private Logger logger = LogManager.getLogger();
  private LocalDate date;

  private ExpenseSheet expenseSheet;

  private Button.ClickListener prevClick = event -> {
    date = date.minusMonths(1);
    showCalendar();
    fulfillTables();
    uss.checkSummary(expenseSheet, date);
  };
  private Button.ClickListener nextClick = event -> {
    date = date.plusMonths(1);
    showCalendar();
    fulfillTables();
    uss.checkSummary(expenseSheet, date);
  };
  private ValueChangeListener<LocalDate> monthChanged = event -> {
    date = event.getValue();
    showCalendar();
    fulfillTables();
    uss.checkSummary(expenseSheet, date);
  };

  public MonthView() {
    logger.info("create");
    ExpenseSystemEventBus.register(this);
    uss = AppCtxProvider.getBean(UserSummaryService.class);
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class).withDayOfMonth(1);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    previousMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
    previousMonthButton.addClickListener(prevClick);
    nextMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_RIGHT);
    nextMonthButton.addClickListener(nextClick);
    firstDateField.setDateFormat("yyyy-MM-dd");
    thisMonthField.setDateFormat("MMMM yyyy");
    thisMonthField.addValueChangeListener(monthChanged);
    lastDateField.setDateFormat("yyyy-MM-dd");

    setCalendarSize();
    showCalendar();
    fulfillTables();
    uss.checkSummary(expenseSheet, date);
  }

  public void showCalendar() {
    if (getParent() != null && getParent().getParent().getParent().getParent() instanceof ExpenseView) {
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedYear(date.getYear()+"");
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedMonth(CalendarUtils.getMonthName(date.getMonthValue()-1));
    }
    thisMonthField.setValue(date);
    firstDateField.setValue(date.withDayOfMonth(1));
    lastDateField.setValue(date.withDayOfMonth(date.lengthOfMonth()));
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
