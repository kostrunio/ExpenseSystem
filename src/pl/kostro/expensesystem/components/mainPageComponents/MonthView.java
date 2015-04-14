package pl.kostro.expensesystem.components.mainPageComponents;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.CategoryExpense;
import pl.kostro.expensesystem.utils.Converter;
import pl.kostro.expensesystem.utils.DateExpense;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClick;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;

public class MonthView extends CustomComponent {

  @AutoGenerated
  private VerticalLayout mainLayout;

  @AutoGenerated
  private HorizontalLayout workingLayout;

  @AutoGenerated
  private VerticalLayout summaryLayout;

  @AutoGenerated
  private Table userLimitTable;

  @AutoGenerated
  private Table categoryTable;

  @AutoGenerated
  private HorizontalLayout dateLayout;

  @AutoGenerated
  private PopupDateField lastDateField;

  @AutoGenerated
  private PopupDateField firstDateField;

  @AutoGenerated
  private Calendar monthCalendar;

  @AutoGenerated
  private HorizontalLayout navigationLayout;

  @AutoGenerated
  private Button nextMonthButton;

  @AutoGenerated
  private Button thisMonthButton;

  @AutoGenerated
  private Button previousMonthButton;

  private java.util.Calendar calendar;

  private static final long serialVersionUID = 2594978831652398190L;

  /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

  /**
   * The constructor should first build the main layout, set the composition
   * root and then do any custom initialization.
   * 
   * The constructor will not be automatically regenerated by the visual editor.
   */
  public MonthView(final ExpenseSheet expenseSheet, java.util.Calendar date) {
    buildMainLayout();
    setCompositionRoot(mainLayout);

    // TODO add user code here
    this.calendar = date;

    UserSummaryService.setFirstDay(calendar);
    previousMonthButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = -8048895457148394023L;

      @Override
      public void buttonClick(ClickEvent event) {
        calendar.add(java.util.Calendar.MONTH, -1);
        thisMonthButton.setCaption(new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));
        showCalendar(expenseSheet, calendar);
      }
    });

    thisMonthButton.setCaption(new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));

    nextMonthButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = -8048895457148394023L;

      @Override
      public void buttonClick(ClickEvent event) {
        calendar.add(java.util.Calendar.MONTH, 1);
        thisMonthButton.setCaption(new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));
        showCalendar(expenseSheet, calendar);
      }
    });

    monthCalendar.setEventProvider(new CalendarEventProvider() {

      private static final long serialVersionUID = -2219052651460350955L;

      @Override
      public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        Map<Date, DateExpense> eventToShow = ExpenseSheetService.prepareExpenseMap(expenseSheet, startDate, endDate, UserSummaryService.getFirstDay(calendar.getTime()),
            UserSummaryService.getLastDay(calendar.getTime()));
        showCategoryTable(expenseSheet);
        showUserLimitTable(expenseSheet);
        return Converter.transformExpensesToEvents(eventToShow);
      }

    });

    monthCalendar.setHandler(new BasicDateClickHandler() {

      private static final long serialVersionUID = 2426375506359469533L;

      @Override
      public void dateClick(DateClickEvent event) {
        calendar.setTime(event.getDate());
        setCompositionRoot(new DayView(expenseSheet, calendar));
      }
    });

    monthCalendar.setHandler(new BasicWeekClickHandler() {

      private static final long serialVersionUID = -5488623945839839169L;

      @Override
      protected void setDates(WeekClick event, Date start, Date end) {
        // Do nothing
      }
    });

    showCalendar(expenseSheet, calendar);

    categoryTable.setPageLength(expenseSheet.getCategoryList().size());
    categoryTable.addContainerProperty("Kategoria", String.class, null);
    categoryTable.addContainerProperty("Suma", BigDecimal.class, null);

    userLimitTable.setPageLength(expenseSheet.getUserLimitList().size());
    userLimitTable.addContainerProperty("U�ytkownik", String.class, null);
    userLimitTable.addContainerProperty("Suma", BigDecimal.class, null);
    userLimitTable.addContainerProperty("Zosta�o", BigDecimal.class, null);

  }

  public void showCalendar(ExpenseSheet expenseSheet, java.util.Calendar calendar) {
    firstDateField.setValue(UserSummaryService.getFirstDay(calendar.getTime()));
    lastDateField.setValue(UserSummaryService.getLastDay(calendar.getTime()));
    monthCalendar.setStartDate(firstDateField.getValue());
    monthCalendar.setEndDate(lastDateField.getValue());
  }

  public void showCategoryTable(ExpenseSheet expenseSheet) {
    categoryTable.removeAllItems();
    for (Category category : expenseSheet.getCategoryList()) {
      CategoryExpense categoryExpense = ExpenseSheetService.getCategoryExpenseMap(expenseSheet, category);
      Object newItemId = categoryTable.addItem();
      Item row = categoryTable.getItem(newItemId);
      row.getItemProperty("Kategoria").setValue(category.getName());
      row.getItemProperty("Suma").setValue(categoryExpense != null ? categoryExpense.getSum() : new BigDecimal(0));
    }
  }

  public void showUserLimitTable(ExpenseSheet expenseSheet) {
    userLimitTable.removeAllItems();
    for (UserLimit userLimit : expenseSheet.getUserLimitList()) {
      UserSummary userSummary = UserSummaryService.findUserSummary(userLimit, UserSummaryService.getFirstDay(calendar.getTime()));
      Object newItemId = userLimitTable.addItem();
      Item row = userLimitTable.getItem(newItemId);
      row.getItemProperty("U�ytkownik").setValue(userLimit.getUser().getName());
      row.getItemProperty("Suma").setValue(userSummary.getSum());
      if (userLimit.isContinuousSummary()) {
        row.getItemProperty("Zosta�o").setValue(UserSummaryService.calculateSum(userLimit, calendar.getTime()));
      } else {
    	  row.getItemProperty("Zosta�o").setValue(userSummary.getLimit().subtract(userSummary.getSum()));
      }
    }
  }

  @AutoGenerated
  private VerticalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setImmediate(false);
    mainLayout.setWidth("100%");
    mainLayout.setHeight("100%");
    mainLayout.setMargin(false);

    // top-level component properties
    setWidth("100.0%");
    setHeight("100.0%");

    // navigationLayout
    navigationLayout = buildNavigationLayout();
    mainLayout.addComponent(navigationLayout);
    mainLayout.setComponentAlignment(navigationLayout, new Alignment(20));

    // workingLayout
    workingLayout = buildWorkingLayout();
    mainLayout.addComponent(workingLayout);

    return mainLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildNavigationLayout() {
    // common part: create layout
    navigationLayout = new HorizontalLayout();
    navigationLayout.setImmediate(false);
    navigationLayout.setWidth("100.0%");
    navigationLayout.setHeight("-1px");
    navigationLayout.setMargin(false);
    navigationLayout.setSpacing(true);

    // previousMonthButton
    previousMonthButton = new Button();
    previousMonthButton.setCaption("<");
    previousMonthButton.setImmediate(true);
    previousMonthButton.setWidth("-1px");
    previousMonthButton.setHeight("-1px");
    navigationLayout.addComponent(previousMonthButton);
    navigationLayout.setComponentAlignment(previousMonthButton, new Alignment(33));

    // thisMonthButton
    thisMonthButton = new Button();
    thisMonthButton.setCaption("Button");
    thisMonthButton.setImmediate(true);
    thisMonthButton.setWidth("-1px");
    thisMonthButton.setHeight("-1px");
    navigationLayout.addComponent(thisMonthButton);
    navigationLayout.setComponentAlignment(thisMonthButton, new Alignment(48));

    // nextMonthButton
    nextMonthButton = new Button();
    nextMonthButton.setCaption(">");
    nextMonthButton.setImmediate(true);
    nextMonthButton.setWidth("-1px");
    nextMonthButton.setHeight("-1px");
    navigationLayout.addComponent(nextMonthButton);
    navigationLayout.setComponentAlignment(nextMonthButton, new Alignment(34));

    return navigationLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildWorkingLayout() {
    // common part: create layout
    workingLayout = new HorizontalLayout();
    workingLayout.setImmediate(false);
    workingLayout.setWidth("-1px");
    workingLayout.setHeight("-1px");
    workingLayout.setMargin(false);

    // monthCalendar
    monthCalendar = new Calendar();
    monthCalendar.setImmediate(false);
    monthCalendar.setWidth("-1px");
    monthCalendar.setHeight("-1px");
    workingLayout.addComponent(monthCalendar);

    // summaryLayout
    summaryLayout = buildSummaryLayout();
    workingLayout.addComponent(summaryLayout);

    return workingLayout;
  }

  @AutoGenerated
  private VerticalLayout buildSummaryLayout() {
    // common part: create layout
    summaryLayout = new VerticalLayout();
    summaryLayout.setImmediate(false);
    summaryLayout.setWidth("-1px");
    summaryLayout.setHeight("-1px");
    summaryLayout.setMargin(false);
    summaryLayout.setSpacing(true);

    // dateLayout
    dateLayout = buildDateLayout();
    summaryLayout.addComponent(dateLayout);

    // categoryTable
    categoryTable = new Table();
    categoryTable.setImmediate(false);
    categoryTable.setWidth("-1px");
    categoryTable.setHeight("-1px");
    summaryLayout.addComponent(categoryTable);

    // userLimitTable
    userLimitTable = new Table();
    userLimitTable.setImmediate(false);
    userLimitTable.setWidth("-1px");
    userLimitTable.setHeight("-1px");
    summaryLayout.addComponent(userLimitTable);

    return summaryLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildDateLayout() {
    // common part: create layout
    dateLayout = new HorizontalLayout();
    dateLayout.setImmediate(false);
    dateLayout.setWidth("-1px");
    dateLayout.setHeight("-1px");
    dateLayout.setMargin(false);

    // firstDateField
    firstDateField = new PopupDateField();
    firstDateField.setImmediate(false);
    firstDateField.setWidth("-1px");
    firstDateField.setHeight("-1px");
    dateLayout.addComponent(firstDateField);

    // lastDateField
    lastDateField = new PopupDateField();
    lastDateField.setImmediate(false);
    lastDateField.setWidth("-1px");
    lastDateField.setHeight("-1px");
    dateLayout.addComponent(lastDateField);

    return dateLayout;
  }

}
