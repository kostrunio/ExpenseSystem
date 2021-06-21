package pl.kostro.expensesystem.utils.calendar;

import org.vaadin.addon.calendar.item.BasicItem;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.utils.transform.model.DateExpense;
import pl.kostro.expensesystem.utils.transform.model.UserLimitExpense;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Converter {

  private ExpenseSheetTransformService eshts;

  public Converter() {
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
  }

  public List<BasicItem> transformExpensesToEvents(ExpenseSheetEntity expenseSheet) {
    Set<LocalDate> dateSet = expenseSheet.getDateExpenseMap().keySet();
    List<BasicItem> calendarEvents = new ArrayList<>();
    BasicItem event;
    for (LocalDate date : dateSet) {
      DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(date);

      // userLimit events
      for (UserLimitEntity userLimit : eshts.getUserLimitListDesc(expenseSheet)) {
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
