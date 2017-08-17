package pl.kostro.expensesystem.utils.calendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.vaadin.addon.calendar.item.BasicItem;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;

public class Converter {

  private ExpenseSheetService eshs;

  public Converter() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
  }

  public List<BasicItem> transformExpensesToEvents(ExpenseSheet expenseSheet,
      Map<LocalDate, DateExpense> dateExpenseMap) {
    Set<LocalDate> dateSet = dateExpenseMap.keySet();
    List<BasicItem> calendarEvents = new ArrayList<>();
    BasicItem event;
    for (LocalDate date : dateSet) {
      DateExpense dateExpense = dateExpenseMap.get(date);

      // userLimit events
      for (UserLimit userLimit : eshs.getUserLimitListDesc(expenseSheet)) {
        UserLimitExpense userLimitExpenseMap = dateExpense.getUserLimitExpenseMap().get(userLimit);
        if (userLimitExpenseMap == null)
          continue;
        event = new BasicItem(userLimitExpenseMap.getSumString(), null,
            date.atStartOfDay(ZoneId.systemDefault()));
        event.setAllDay(true);
        event.setStyleName("" + userLimitExpenseMap.getUserLimit().getOrder());
        calendarEvents.add(event);
      }
    }
    return calendarEvents;
  }
}
