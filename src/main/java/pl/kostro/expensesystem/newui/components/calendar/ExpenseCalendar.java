package pl.kostro.expensesystem.newui.components.calendar;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.stefan.fullcalendar.*;
import org.vaadin.stefan.fullcalendar.dataprovider.CallbackEntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.newui.notification.ShowNotification;
import pl.kostro.expensesystem.newui.views.day.DayView;
import pl.kostro.expensesystem.newui.views.month.MonthView;
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

  private ComponentEventListener<DayNumberClickedEvent> dateClicked = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getDate());
    monthView.removeAll();
    monthView.add(new DayView());
  };

/*  private WeekClickHandler weekClick = event -> {
  };*/

  private ComponentEventListener<EntryClickedEvent> eventClicked = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getEntry().getStart().toLocalDate());
    monthView.removeAll();
    monthView.add(new DayView());
  };

  public ExpenseCalendar() {
    super(4);
    converter = AppCtxProvider.getBean(CalendarConverter.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);

    setWeekNumbersVisible(false);
    setEntryProvider(eventProvider);

    addDayNumberClickedListener(dateClicked);

    addEntryClickedListener(eventClicked);

  }

  public void setMonthView(MonthView monthView) {
    this.monthView = monthView;
  }
}
