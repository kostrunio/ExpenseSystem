package pl.kostro.expensesystem.components.calendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import com.vaadin.server.VaadinSession;
import com.vaadin.v7.ui.Calendar;
import com.vaadin.v7.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.v7.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.v7.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.v7.ui.components.calendar.CalendarComponentEvents.WeekClick;
import com.vaadin.v7.ui.components.calendar.event.CalendarEvent;
import com.vaadin.v7.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.v7.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.v7.ui.components.calendar.handler.BasicWeekClickHandler;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.calendar.Converter;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.view.DayView;
import pl.kostro.expensesystem.view.MonthView;

@SuppressWarnings("serial")
public class ExpenseCalendar extends Calendar {
  
  private Converter converter = new Converter();
  
  private ExpenseSheetService eshs;
  private MonthView monthView;
  private ExpenseSheet expenseSheet;
  private LocalDate date;
  private CalendarEventProvider eventProvider = (startDate, endDate) -> {
    Map<LocalDate, DateExpense> eventToShow = eshs.prepareExpenseMap(expenseSheet, startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
        date.withDayOfMonth(1), date.withDayOfMonth(date.lengthOfMonth()));
    return converter.transformExpensesToEvents(expenseSheet, eventToShow);
  };
  private BasicDateClickHandler dateClick = new BasicDateClickHandler() {
    @Override
    public void dateClick(DateClickEvent event) {
      date = event.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      monthView.removeAllComponents();
      monthView.addComponent(new DayView());
    }
  };
  private BasicWeekClickHandler weekClick = new BasicWeekClickHandler() {
    @Override
    protected void setDates(WeekClick event, Date startDate, Date endDate) {
      // Do nothing
    }
  };
  private EventClickHandler eventClick = event -> {
    date = event.getCalendarEvent().getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    monthView.removeAllComponents();
    monthView.addComponent(new DayView());
  };

  public ExpenseCalendar() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.date = VaadinSession.getCurrent().getAttribute(LocalDate.class);
    
    setEventProvider(eventProvider);
    
    setHandler(dateClick);
    
    setHandler(weekClick);

    setHandler(eventClick);
  }

  public void setMonthView(MonthView monthView) {
    this.monthView = monthView;
  }
}
