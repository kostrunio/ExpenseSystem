package pl.kostro.expensesystem.view.design;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import pl.kostro.expensesystem.components.calendar.ExpenseCalendar;
import pl.kostro.expensesystem.components.grid.CategorySumGrid;
import pl.kostro.expensesystem.components.grid.UserLimitSumLeftGrid;

public class MonthDesign extends HorizontalLayout {
  protected VerticalLayout calendarLayout = new VerticalLayout();
  protected HorizontalLayout navigationLayout = new HorizontalLayout();
  protected Button previousMonthButton = new Button();
  protected DateField thisMonthField = new DateField();
  protected Button nextMonthButton = new Button();
  protected ExpenseCalendar monthCalendar = new ExpenseCalendar();
  protected VerticalLayout summaryLayout = new VerticalLayout();
  protected HorizontalLayout dateLayout = new HorizontalLayout();
  protected DateField firstDateField = new DateField();
  protected DateField lastDateField = new DateField();
  protected CategorySumGrid categoryTable = new CategorySumGrid();
  protected UserLimitSumLeftGrid userLimitTable = new UserLimitSumLeftGrid();

  public MonthDesign() {
    setHeight("100%");
    addComponents(createCalendarLayout(), createSummaryLayout());
    setExpandRatio(calendarLayout, 1);
  }

  private Component createCalendarLayout() {
    calendarLayout.setSpacing(false);
    calendarLayout.setMargin(false);
    monthCalendar.setWidth("710px");
    monthCalendar.setHeight("100%");
    calendarLayout.addComponents(createNavigationLayout(), monthCalendar);
    calendarLayout.setComponentAlignment(navigationLayout, Alignment.TOP_CENTER);
    calendarLayout.setComponentAlignment(monthCalendar, Alignment.TOP_CENTER);
    return calendarLayout;
  }

  private Component createNavigationLayout() {
    navigationLayout.setWidth("100%");
    previousMonthButton.setIcon(VaadinIcons.ARROW_LEFT);
    previousMonthButton.setStyleName("icon-only borderless");
    thisMonthField.setResolution(DateResolution.MONTH);
    thisMonthField.setWidth("180px");
    nextMonthButton.setIcon(VaadinIcons.ARROW_RIGHT);
    nextMonthButton.setStyleName("icon-only borderless");
    navigationLayout.addComponents(previousMonthButton, thisMonthField, nextMonthButton);
    navigationLayout.setComponentAlignment(previousMonthButton, Alignment.TOP_RIGHT);
    navigationLayout.setComponentAlignment(thisMonthField, Alignment.TOP_CENTER);
    return navigationLayout;
  }

  private Component createSummaryLayout() {
    summaryLayout.setStyleName("summary-layout");
    summaryLayout.setSizeUndefined();
    summaryLayout.setMargin(false);
    categoryTable.setWidth("310px");
    userLimitTable.setWidth("310px");
    summaryLayout.addComponents(createDateLayout(), categoryTable, userLimitTable);
    summaryLayout.setComponentAlignment(dateLayout, Alignment.TOP_CENTER);
    summaryLayout.setComponentAlignment(categoryTable, Alignment.TOP_CENTER);
    summaryLayout.setComponentAlignment(userLimitTable, Alignment.TOP_CENTER);
    return summaryLayout;
  }

  private Component createDateLayout() {
    firstDateField.setWidth("140px");
    firstDateField.setEnabled(false);
    lastDateField.setWidth("140px");
    lastDateField.setEnabled(false);
    dateLayout.addComponents(firstDateField, lastDateField);
    dateLayout.setComponentAlignment(firstDateField, Alignment.TOP_RIGHT);
    return dateLayout;
  }
}
