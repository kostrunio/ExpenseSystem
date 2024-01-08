package pl.kostro.expensesystem.ui.views.month;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button.ClickListener;
import lombok.extern.slf4j.Slf4j;
import pl.kostro.expensesystem.ui.event.ExpenseSystemEvent.BrowserResizeEvent;
import pl.kostro.expensesystem.ui.event.ExpenseSystemEventBus;
import pl.kostro.expensesystem.ui.views.expense.ExpenseView;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;

import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
public class MonthView extends MonthDesign {

  private LocalDate date;

  private final ClickListener prevClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.minusMonths(1));
    showCalendar();
  };
  private final ClickListener nextClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.plusMonths(1));
    showCalendar();
  };
  private final ValueChangeListener<LocalDate> monthChanged = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getValue());
    showCalendar();
  };

  public MonthView() {
    log.info("create");
    ExpenseSystemEventBus.register(this);
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
  }

  public void showCalendar() {
    if (getParent() != null && getParent().getParent().getParent().getParent() instanceof ExpenseView) {
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedYear(date.getYear()+"");
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedMonth(CalendarUtils.getMonthName(date.getMonthValue()-1));
    }
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class).withDayOfMonth(1);
    thisMonthField.setValue(date);
    firstDateField.setValue(date.withDayOfMonth(1));
    lastDateField.setValue(date.withDayOfMonth(date.lengthOfMonth()));
    monthCalendar.setMonthView(this);
    monthCalendar.setStartDate(firstDateField.getValue().atStartOfDay(ZoneId.systemDefault()));
    monthCalendar.setEndDate(lastDateField.getValue().atStartOfDay(ZoneId.systemDefault()));
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
