package pl.kostro.expensesystem.views.mainPage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.kostro.expensesystem.components.calendar.ExpenseCalendar;
import pl.kostro.expensesystem.components.table.CategoryTable;
import pl.kostro.expensesystem.components.table.UserLimitTable;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.UserSummaryService;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class MonthView extends HorizontalLayout {

  private ExpenseSheet expenseSheet;

  private CategoryTable categoryTable;
  private UserLimitTable userLimitTable;
  private PopupDateField firstDateField;
  private PopupDateField lastDateField;
  private ExpenseCalendar monthCalendar;
  private TextField thisMonthField;

  private Calendar calendar;

  private static final long serialVersionUID = 2594978831652398190L;

  public MonthView(Calendar date) {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.calendar = date;
    UserSummaryService.setFirstDay(calendar);

    setSizeFull();

    // calendarLayout
    setWidthUndefined();
    addComponent(buildCalendarLayout());
    addComponent(buildSummaryLayout());

    showCalendar(expenseSheet, calendar);
  }

  private VerticalLayout buildCalendarLayout() {
    // calendarLayout
    VerticalLayout calendarLayout = new VerticalLayout();

    // navigationLayout
    HorizontalLayout navigationLayout = buildNavigationLayout();
    navigationLayout.setSizeUndefined();
    calendarLayout.addComponent(navigationLayout);
    calendarLayout.setComponentAlignment(navigationLayout, Alignment.MIDDLE_CENTER);

    // monthCalendar
    monthCalendar = new ExpenseCalendar(this, expenseSheet, calendar, categoryTable);
    calendarLayout.addComponent(monthCalendar);

    return calendarLayout;
  }

  private HorizontalLayout buildNavigationLayout() {
    // common part: create layout
    HorizontalLayout navigationLayout = new HorizontalLayout();
    navigationLayout.setSpacing(true);
    navigationLayout.setWidth("100%");

    // previousMonthButton
    Button previousMonthButton = new Button();
    previousMonthButton.setCaption("<");
    previousMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
    previousMonthButton.setImmediate(true);
    previousMonthButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = -8048895457148394023L;
      @Override
      public void buttonClick(ClickEvent event) {
        calendar.add(java.util.Calendar.MONTH, -1);
        showCalendar(expenseSheet, calendar);
      }
    });
    navigationLayout.addComponent(previousMonthButton);
    navigationLayout.setComponentAlignment(previousMonthButton, Alignment.MIDDLE_RIGHT);

    // thisMonthField
    thisMonthField = new TextField();
    navigationLayout.addComponent(thisMonthField);
    navigationLayout.setComponentAlignment(thisMonthField, Alignment.MIDDLE_CENTER);

    // nextMonthButton
    Button nextMonthButton = new Button();
    nextMonthButton.setCaption(">");
    nextMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_RIGHT);
    nextMonthButton.setImmediate(true);
    nextMonthButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = -8048895457148394023L;
      @Override
      public void buttonClick(ClickEvent event) {
        calendar.add(java.util.Calendar.MONTH, 1);
        showCalendar(expenseSheet, calendar);
      }
    });
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
    categoryTable = new CategoryTable(expenseSheet);
    summaryLayout.addComponent(categoryTable);
    summaryLayout.setComponentAlignment(categoryTable, new Alignment(20));

    // userLimitTable
    userLimitTable = new UserLimitTable(expenseSheet);
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

  public void showCalendar(ExpenseSheet expenseSheet, java.util.Calendar calendar) {
    thisMonthField.setReadOnly(false);
    thisMonthField.setValue(new SimpleDateFormat("MMMM yyyy", UI.getCurrent().getLocale()).format(calendar.getTime()));
    thisMonthField.setReadOnly(true);
    firstDateField.setValue(UserSummaryService.getFirstDay(calendar.getTime()));
    lastDateField.setValue(UserSummaryService.getLastDay(calendar.getTime()));
    monthCalendar.setStartDate(firstDateField.getValue());
    monthCalendar.setEndDate(lastDateField.getValue());
  }

  public void fulfillTables() {
    categoryTable.fulfill();
    userLimitTable.fulfill(calendar);
  }

  


}
