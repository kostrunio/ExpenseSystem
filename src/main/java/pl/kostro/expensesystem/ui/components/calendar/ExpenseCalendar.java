package pl.kostro.expensesystem.ui.components.calendar;

import com.vaadin.server.VaadinSession;
import lombok.Setter;
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
import pl.kostro.expensesystem.ui.notification.ShowNotification;
import pl.kostro.expensesystem.ui.views.day.DayView;
import pl.kostro.expensesystem.ui.views.month.MonthView;
import pl.kostro.expensesystem.utils.transform.model.UserSumChange;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ExpenseCalendar extends Calendar<BasicItem> {

  private CalendarConverter converter;
  private ExpenseSheetTransformService eshts;

  @Setter
  private MonthView monthView;
  private ExpenseSheetEntity expenseSheet;
  private LocalDate date;
  private final CalendarItemProvider<BasicItem> eventProvider = (startDate, endDate) -> {
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class);
    eshts.prepareExpenseMap(expenseSheet,
        startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
        endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), date.withDayOfMonth(1),
        date.withDayOfMonth(date.lengthOfMonth()));
    List<UserSumChange> listToNotify = eshts.checkSummary(expenseSheet, date);
    if (listToNotify != null) {
      for (UserSumChange sumChange : listToNotify) {
        ShowNotification.changeSummary(sumChange.getUserName(), sumChange.getPrevSum(), sumChange.getSum());
      }
    }

    monthView.fulfillTables();
    return converter.transformExpensesToEvents(expenseSheet);
  };

  private final DateClickHandler dateClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getDate().toLocalDate());
    monthView.removeAllComponents();
    monthView.addComponent(new DayView());
  };

  private final WeekClickHandler weekClick = event -> {
  };

  private final ItemClickHandler eventClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getCalendarItem().getStart().toLocalDate());
    monthView.removeAllComponents();
    monthView.addComponent(new DayView());
  };

  private final BackwardHandler prevClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.minusMonths(1));
    monthView.showCalendar();
  };
  private final ForwardHandler nextClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.plusMonths(1));
    monthView.showCalendar();
  };

  public ExpenseCalendar() {
    converter = AppCtxProvider.getBean(CalendarConverter.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);

    setDataProvider(eventProvider);

    setHandler(dateClick);

    setHandler(weekClick);

    setHandler(eventClick);

    setHandler(prevClick);

    setHandler(nextClick);
    
  }

}
