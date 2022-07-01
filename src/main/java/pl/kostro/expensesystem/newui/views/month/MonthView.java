package pl.kostro.expensesystem.newui.views.month;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

@Route(value = "month")
@PageTitle("Month")
public class MonthView extends MonthDesign {

  private Logger logger = LogManager.getLogger();
  private LocalDate date;

  private ComponentEventListener<ClickEvent<Button>> prevClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.minusMonths(1));
    showCalendar();
  };
  private ComponentEventListener<ClickEvent<Button>> nextClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.plusMonths(1));
    showCalendar();
  };
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<DatePicker, LocalDate>> monthChanged = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getValue());
    showCalendar();
  };

  public MonthView() {
    DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
    singleFormatI18n.setDateFormat("yyyy-MM-dd");
    DatePicker.DatePickerI18n monthFormatI18n = new DatePicker.DatePickerI18n();
    monthFormatI18n.setDateFormat("MMMM yyyy");


    logger.info("create");
//    ExpenseSystemEventBus.register(this);
    previousMonthButton.addClickShortcut(Key.ARROW_LEFT);
    previousMonthButton.addClickListener(prevClick);
    nextMonthButton.addClickShortcut(Key.ARROW_RIGHT);
    nextMonthButton.addClickListener(nextClick);
    firstDateField.setI18n(singleFormatI18n);
    thisMonthField.setI18n(monthFormatI18n);
    thisMonthField.addValueChangeListener(monthChanged);
    lastDateField.setI18n(singleFormatI18n);

//    setCalendarSize();
    showCalendar();
  }

  public void showCalendar() {
/*    if (getParent() != null && getParent().getParent().getParent().getParent() instanceof ExpenseView) {
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedYear(date.getYear()+"");
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedMonth(CalendarUtils.getMonthName(date.getMonthValue()-1));
    }*/
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class).withDayOfMonth(1);
    thisMonthField.setValue(date);
    firstDateField.setValue(date.withDayOfMonth(1));
    lastDateField.setValue(date.withDayOfMonth(date.lengthOfMonth()));
    monthCalendar.setMonthView(this);
//    monthCalendar.setStartDate(firstDateField.getValue().atStartOfDay(ZoneId.systemDefault()));
//    monthCalendar.setEndDate(lastDateField.getValue().atStartOfDay(ZoneId.systemDefault()));
  }

  public void fulfillTables() {
    categoryTable.fulfill();
    userLimitTable.fulfill(date);
  }

/*  private void setCalendarSize() {
    if (Page.getCurrent().getBrowserWindowWidth() < 800) {
      monthCalendar.setWidth(Page.getCurrent().getBrowserWindowWidth()-30+"px");
      monthCalendar.setHeight(Page.getCurrent().getBrowserWindowWidth()-30+"px");
    }
  }*/

}
