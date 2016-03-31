package pl.kostro.expensesystem.view.design;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import pl.kostro.expensesystem.components.calendar.ExpenseCalendar;
import pl.kostro.expensesystem.components.table.CategoryTable;
import pl.kostro.expensesystem.components.table.UserLimitTable;

@SuppressWarnings("serial")
public class MonthDesign extends HorizontalLayout {
  protected VerticalLayout calendarLayout;
  
  protected HorizontalLayout navigationLayout;
  protected Button previousMonthButton;
  protected TextField thisMonthField;
  protected Button nextMonthButton;
  
  protected CategoryTable categoryTable;
  protected UserLimitTable userLimitTable;
  protected PopupDateField firstDateField;
  protected PopupDateField lastDateField;
  protected ExpenseCalendar monthCalendar;

  public MonthDesign() {
    setSizeFull();
    setWidthUndefined();
    addComponent(buildCalendarLayout());
    addComponent(buildSummaryLayout());
  }

  private VerticalLayout buildCalendarLayout() {
    calendarLayout = new VerticalLayout();

    // navigationLayout
    HorizontalLayout navigationLayout = buildNavigationLayout();
    navigationLayout.setSizeUndefined();
    calendarLayout.addComponent(navigationLayout);
    calendarLayout.setComponentAlignment(navigationLayout, Alignment.MIDDLE_CENTER);

    // monthCalendar
    monthCalendar = new ExpenseCalendar();
    calendarLayout.addComponent(monthCalendar);

    return calendarLayout;
  }

  private HorizontalLayout buildNavigationLayout() {
    navigationLayout = new HorizontalLayout();
    navigationLayout.setSpacing(true);
    navigationLayout.setWidth("100%");

    // previousMonthButton
    previousMonthButton = new Button();
    previousMonthButton.setCaption("<");
    previousMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
    previousMonthButton.setImmediate(true);
    navigationLayout.addComponent(previousMonthButton);
    navigationLayout.setComponentAlignment(previousMonthButton, Alignment.MIDDLE_RIGHT);

    // thisMonthField
    thisMonthField = new TextField();
    navigationLayout.addComponent(thisMonthField);
    navigationLayout.setComponentAlignment(thisMonthField, Alignment.MIDDLE_CENTER);

    // nextMonthButton
    nextMonthButton = new Button();
    nextMonthButton.setCaption(">");
    nextMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_RIGHT);
    nextMonthButton.setImmediate(true);
    navigationLayout.addComponent(nextMonthButton);
    navigationLayout.setComponentAlignment(nextMonthButton, Alignment.MIDDLE_LEFT);

    return navigationLayout;
  }

  private VerticalLayout buildSummaryLayout() {
    // common part: create layout
    VerticalLayout summaryLayout = new VerticalLayout();
    summaryLayout.setSizeUndefined();
    summaryLayout.setSpacing(true);

    // dateLayout
    HorizontalLayout dateLayout = buildDateLayout();
    summaryLayout.addComponent(dateLayout);
    summaryLayout.setComponentAlignment(dateLayout, new Alignment(20));
    
    // categoryTable
    categoryTable = new CategoryTable();
    summaryLayout.addComponent(categoryTable);
    summaryLayout.setComponentAlignment(categoryTable, new Alignment(20));

    // userLimitTable
    userLimitTable = new UserLimitTable();
    summaryLayout.addComponent(userLimitTable);
    summaryLayout.setComponentAlignment(userLimitTable, new Alignment(20));

    return summaryLayout;
  }
  
  private HorizontalLayout buildDateLayout() {
    // common part: create layout
    HorizontalLayout dateLayout = new HorizontalLayout();

    // firstDateField
    firstDateField = new PopupDateField();
    firstDateField.setDateFormat("dd-MM-yyyy");
    firstDateField.setTextFieldEnabled(false);
    firstDateField.setEnabled(false);
    dateLayout.addComponent(firstDateField);
    dateLayout.setComponentAlignment(firstDateField, new Alignment(20));

    // lastDateField
    lastDateField = new PopupDateField();
    lastDateField.setDateFormat("dd-MM-yyyy");
    lastDateField.setTextFieldEnabled(false);
    lastDateField.setEnabled(false);
    dateLayout.addComponent(lastDateField);
    dateLayout.setComponentAlignment(lastDateField, new Alignment(20));

    return dateLayout;
  }
}
