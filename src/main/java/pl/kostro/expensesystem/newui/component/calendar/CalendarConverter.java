package pl.kostro.expensesystem.newui.component.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.stefan.fullcalendar.Entry;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.utils.transform.model.DateExpense;
import pl.kostro.expensesystem.utils.transform.model.UserLimitExpense;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class CalendarConverter {

  private ExpenseSheetTransformService eshts;

  @Autowired
  public CalendarConverter(ExpenseSheetTransformService eshts) {
    this.eshts = eshts;
  }

  public List<Entry> transformExpensesToEvents(ExpenseSheetEntity expenseSheet) {
    Set<LocalDate> dateSet = expenseSheet.getDateExpenseMap().keySet();
    List<Entry> calendarEvents = new ArrayList<>();
    Entry event;
    for (LocalDate date : dateSet) {
      DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(date);

      // userLimit events
      for (UserLimitEntity userLimit : eshts.getUserLimitListDesc(expenseSheet)) {
        UserLimitExpense userLimitExpenseMap = dateExpense.getUserLimitExpenseMap().get(userLimit);
        if (userLimitExpenseMap == null)
          continue;
        event = new Entry();
        event.setTitle(userLimitExpenseMap.getSumString());
        event.setStart(date);
        event.setEnd(date);
        event.setAllDay(true);
//        event.setStyleName("" + userLimitExpenseMap.getUserLimit().getOrder());
        calendarEvents.add(event);
      }
    }
    return calendarEvents;
  }
}
