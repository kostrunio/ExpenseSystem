package pl.kostro.expensesystem.components.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Converter;
import pl.kostro.expensesystem.utils.DateExpense;
import pl.kostro.expensesystem.view.DayView;
import pl.kostro.expensesystem.view.MonthView;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClick;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;

@SuppressWarnings("serial")
public class ExpenseCalendar extends com.vaadin.ui.Calendar {
  private MonthView monthView;
  private ExpenseSheet expenseSheet;
  private Calendar calendar;
  private CalendarEventProvider eventProvider = new CalendarEventProvider() {
    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
      Map<Date, DateExpense> eventToShow = ExpenseSheetService.prepareExpenseMap(expenseSheet, startDate, endDate,
          UserSummaryService.getFirstDay(calendar.getTime()), UserSummaryService.getLastDay(calendar.getTime()));
      monthView.fulfillTables();
      return Converter.transformExpensesToEvents(expenseSheet, eventToShow);
    }
  };
  private BasicDateClickHandler dateClick = new BasicDateClickHandler() {
    @Override
    public void dateClick(DateClickEvent event) {
      calendar.setTime(event.getDate());
      monthView.removeAllComponents();
      monthView.addComponent(new DayView(expenseSheet, calendar));
    }
  };
  private BasicWeekClickHandler weekClick = new BasicWeekClickHandler() {
    @Override
    protected void setDates(WeekClick event, Date start, Date end) {
      // Do nothing
    }
  };
  private EventClickHandler eventClick = new EventClickHandler() {
    @Override
    public void eventClick(EventClick event) {
      calendar.setTime(event.getCalendarEvent().getStart());
      monthView.removeAllComponents();
      monthView.addComponent(new DayView(expenseSheet, calendar));
    }
  };

  public ExpenseCalendar() {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.calendar = VaadinSession.getCurrent().getAttribute(Calendar.class);
    
    setEventProvider(eventProvider);
    
    setHandler(dateClick);
    
    setHandler(weekClick);

    setHandler(eventClick);
  }

  public void setMonthView(MonthView monthView) {
    this.monthView = monthView;
  }
}
