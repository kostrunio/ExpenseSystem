package pl.kostro.expensesystem.newui.components.calendar;

import com.vaadin.flow.server.VaadinSession;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.dataprovider.CallbackEntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.newui.views.month.MonthView;
import pl.kostro.expensesystem.newui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.transform.model.UserSumChange;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.time.LocalDate;
import java.util.List;

public class ExpenseCalendar extends FullCalendar {

  private CalendarConverter converter;
  private ExpenseSheetTransformService eshts;

  private MonthView monthView;
  private ExpenseSheetEntity expenseSheet;
  private LocalDate date;

  private CallbackEntryProvider<Entry> eventProvider = EntryProvider.fromCallbacks(
          query -> {
            date = VaadinSession.getCurrent().getAttribute(LocalDate.class);
            eshts.prepareExpenseMap(expenseSheet,
                    query.getStart().toLocalDate(),
                    query.getEnd().toLocalDate(),
                    date.withDayOfMonth(1),
                    date.withDayOfMonth(date.lengthOfMonth()));
            List<UserSumChange> listToNotify = eshts.checkSummary(expenseSheet, date);
            if (listToNotify != null) {
              for (UserSumChange sumChange : listToNotify) {
                ShowNotification.changeSummary(sumChange.getUserName(), sumChange.getPrevSum(), sumChange.getSum());
              }
            }

            monthView.fulfillTables();
            return converter.transformExpensesToEvents(expenseSheet).stream();
          },
          entryId -> null
  );

/*  private DateClickHandler dateClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getDate().toLocalDate());
    monthView.removeAllComponents();
    monthView.addComponent(new DayView());
  };*/

/*  private WeekClickHandler weekClick = event -> {
  };*/

/*  private ItemClickHandler eventClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getCalendarItem().getStart().toLocalDate());
    monthView.removeAllComponents();
    monthView.addComponent(new DayView());
  };*/

/*  private BackwardHandler prevClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.minusMonths(1));
    monthView.showCalendar();
  };*/
/*  private ForwardHandler nextClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.plusMonths(1));
    monthView.showCalendar();
  };*/

  public ExpenseCalendar() {
    super(4);
    converter = AppCtxProvider.getBean(CalendarConverter.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);

    setEntryProvider(eventProvider);

//    setHandler(dateClick);

//    setHandler(weekClick);

//    setHandler(eventClick);

//    setHandler(prevClick);

//    setHandler(nextClick);
  }

  public void setMonthView(MonthView monthView) {
    this.monthView = monthView;
  }
}
