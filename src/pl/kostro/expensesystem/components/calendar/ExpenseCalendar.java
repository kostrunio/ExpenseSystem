package pl.kostro.expensesystem.components.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.kostro.expensesystem.components.table.CategoryTable;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Converter;
import pl.kostro.expensesystem.utils.DateExpense;
import pl.kostro.expensesystem.views.mainPage.DayView;
import pl.kostro.expensesystem.views.mainPage.MonthView;

import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClick;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;

public class ExpenseCalendar extends com.vaadin.ui.Calendar {

  private static final long serialVersionUID = -7763299334451153708L;
  
  public ExpenseCalendar(final MonthView monthView, final ExpenseSheet expenseSheet, final Calendar calendar, final CategoryTable categoryTable) {
    
    setEventProvider(new CalendarEventProvider() {

      private static final long serialVersionUID = -2219052651460350955L;

      @Override
      public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        Map<Date, DateExpense> eventToShow = ExpenseSheetService.prepareExpenseMap(expenseSheet, startDate, endDate,
            UserSummaryService.getFirstDay(calendar.getTime()), UserSummaryService.getLastDay(calendar.getTime()));
        monthView.fulfillTables();
        return Converter.transformExpensesToEvents(expenseSheet, eventToShow);
      }

    });
    
    setHandler(new BasicDateClickHandler() {

      private static final long serialVersionUID = 2426375506359469533L;

      @Override
      public void dateClick(DateClickEvent event) {
        calendar.setTime(event.getDate());
        monthView.removeAllComponents();
        monthView.addComponent(new DayView(expenseSheet, calendar));
      }
    });
    
    setHandler(new BasicWeekClickHandler() {

      private static final long serialVersionUID = -5488623945839839169L;

      @Override
      protected void setDates(WeekClick event, Date start, Date end) {
        // Do nothing
      }
    });

    setHandler(new EventClickHandler() {

      private static final long serialVersionUID = -5971206190211782099L;

      @Override
      public void eventClick(EventClick event) {
        calendar.setTime(event.getCalendarEvent().getStart());
        monthView.removeAllComponents();
        monthView.addComponent(new DayView(expenseSheet, calendar));

      }
    });
  }
}
