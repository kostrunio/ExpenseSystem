package pl.kostro.expensesystem.newui.views.month;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kostro.expensesystem.newui.components.calendar.ExpenseCalendar;
import pl.kostro.expensesystem.newui.components.grid.CategorySumGrid;
import pl.kostro.expensesystem.newui.components.grid.UserLimitSumLeftGrid;

public class MonthDesign extends HorizontalLayout {
  protected VerticalLayout calendarLayout = new VerticalLayout();
  protected HorizontalLayout navigationLayout = new HorizontalLayout();
  protected Button previousMonthButton = new Button();
  protected DatePicker thisMonthField = new DatePicker();
  protected Button nextMonthButton = new Button();
  protected ExpenseCalendar monthCalendar = new ExpenseCalendar();
  protected VerticalLayout summaryLayout = new VerticalLayout();
  protected HorizontalLayout dateLayout = new HorizontalLayout();
  protected DatePicker firstDateField = new DatePicker();
  protected DatePicker lastDateField = new DatePicker();
  protected CategorySumGrid categoryTable = new CategorySumGrid();
  protected UserLimitSumLeftGrid userLimitTable = new UserLimitSumLeftGrid();

  public MonthDesign() {
    setHeight("100%");
    add(createCalendarLayout(), createSummaryLayout());
    expand(calendarLayout);
  }

  private Component createCalendarLayout() {
    calendarLayout.setSpacing(false);
    calendarLayout.setMargin(false);
    monthCalendar.setWidth("710px");
    monthCalendar.setHeight("100%");
    calendarLayout.add(createNavigationLayout(), monthCalendar);
    calendarLayout.setAlignItems(Alignment.START);
//    calendarLayout.setComponentAlignment(monthCalendar, Alignment.TOP_CENTER);
    return calendarLayout;
  }

  private Component createNavigationLayout() {
    navigationLayout.setWidth("100%");
    previousMonthButton.setIcon(VaadinIcon.ARROW_LEFT.create());
    previousMonthButton.setClassName("icon-only borderless");
    DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
    singleFormatI18n.setDateFormat("yyyy-MMMM");
    thisMonthField.setI18n(singleFormatI18n);
//    thisMonthField.setResolution(DateResolution.MONTH);
    thisMonthField.setWidth("180px");
    nextMonthButton.setIcon(VaadinIcon.ARROW_RIGHT.create());
    nextMonthButton.setClassName("icon-only borderless");
    navigationLayout.add(previousMonthButton, thisMonthField, nextMonthButton);
    navigationLayout.setAlignItems(Alignment.START);
//    navigationLayout.setComponentAlignment(thisMonthField, Alignment.TOP_CENTER);
    return navigationLayout;
  }

  private Component createSummaryLayout() {
    summaryLayout.setClassName("summary-layout");
    summaryLayout.setSizeUndefined();
    summaryLayout.setMargin(false);
    categoryTable.setWidth("310px");
    userLimitTable.setWidth("310px");
    summaryLayout.add(createDateLayout(), categoryTable, userLimitTable);
    summaryLayout.setAlignItems(Alignment.START);
//    summaryLayout.setComponentAlignment(categoryTable, Alignment.TOP_CENTER);
//    summaryLayout.setComponentAlignment(userLimitTable, Alignment.TOP_CENTER);
    return summaryLayout;
  }

  private Component createDateLayout() {
    firstDateField.setWidth("140px");
    firstDateField.setEnabled(false);
    lastDateField.setWidth("140px");
    lastDateField.setEnabled(false);
    dateLayout.add(firstDateField, lastDateField);
    dateLayout.setAlignItems(Alignment.START);
    return dateLayout;
  }
}
