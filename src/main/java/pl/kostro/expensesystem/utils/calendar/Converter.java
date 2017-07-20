package pl.kostro.expensesystem.utils.calendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.v7.ui.components.calendar.event.BasicEvent;
import com.vaadin.v7.ui.components.calendar.event.CalendarEvent;

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
	
	public List<CalendarEvent> transformExpensesToEvents(ExpenseSheet expenseSheet, Map<LocalDate, DateExpense> dateExpenseMap) {
		Set<LocalDate> dateSet = dateExpenseMap.keySet();
		List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		BasicEvent event;
		for (LocalDate date : dateSet) {
			DateExpense dateExpense = dateExpenseMap.get(date);
			
			//userLimit events
			for (UserLimit userLimit : eshs.getUserLimitListDesc(expenseSheet)) {
				UserLimitExpense userLimitExpenseMap = dateExpense.getUserLimitExpenseMap().get(userLimit);
				if (userLimitExpenseMap == null)
					continue;
				event = new BasicEvent(userLimitExpenseMap.getSumString(), null, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				event.setAllDay(true);
				event.setStyleName(""+userLimitExpenseMap.getUserLimit().getOrder());
				calendarEvents.add(event);
			}
		}
		return calendarEvents;
	}
}
