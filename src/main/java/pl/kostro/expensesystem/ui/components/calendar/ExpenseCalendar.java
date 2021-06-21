package pl.kostro.expensesystem.ui.components.calendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import com.vaadin.server.VaadinSession;

import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItem;
import org.vaadin.addon.calendar.item.CalendarItemProvider;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.BackwardHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.DateClickHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ForwardHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemClickHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.WeekClickHandler;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.calendar.Converter;
import pl.kostro.expensesystem.utils.transform.model.DateExpense;
import pl.kostro.expensesystem.ui.view.DayView;
import pl.kostro.expensesystem.ui.view.MonthView;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

public class ExpenseCalendar extends Calendar<BasicItem> {

  private Converter converter = new Converter();

  private ExpenseSheetTransformService eshts;
  private MonthView monthView;
  private ExpenseSheetEntity expenseSheet;
  private LocalDate date;
  private CalendarItemProvider<BasicItem> eventProvider = (startDate, endDate) -> {
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class);
    Map<LocalDate, DateExpense> eventToShow = eshts.prepareExpenseMap(expenseSheet,
        startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
        endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), date.withDayOfMonth(1),
        date.withDayOfMonth(date.lengthOfMonth()));
    eshts.checkSummary(expenseSheet, date);
    monthView.fulfillTables();
    return converter.transformExpensesToEvents(expenseSheet, eventToShow);
  };

  private DateClickHandler dateClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getDate().toLocalDate());
    monthView.removeAllComponents();
    monthView.addComponent(new DayView());
  };

  private WeekClickHandler weekClick = event -> {
  };

  private ItemClickHandler eventClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getCalendarItem().getStart().toLocalDate());
    monthView.removeAllComponents();
    monthView.addComponent(new DayView());
  };

  private BackwardHandler prevClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.minusMonths(1));
    monthView.showCalendar();
  };
  private ForwardHandler nextClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.plusMonths(1));
    monthView.showCalendar();
  };

  public ExpenseCalendar() {
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);

    setDataProvider(eventProvider);

    setHandler(dateClick);

    setHandler(weekClick);

    setHandler(eventClick);

    setHandler(prevClick);

    setHandler(nextClick);
    
  }

  public void setMonthView(MonthView monthView) {
    this.monthView = monthView;
  }
}
