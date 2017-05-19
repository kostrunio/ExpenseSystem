package pl.kostro.expensesystem.utils.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

import pl.kostro.expensesystem.ApplicationContextProvider;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;

public class Converter {
  
  private ExpenseSheetService eshs;
  
  public Converter() {
    eshs = ApplicationContextProvider.getBean(ExpenseSheetService.class);
  }
	
	public List<CalendarEvent> transformExpensesToEvents(ExpenseSheet expenseSheet, Map<Date, DateExpense> dateExpenseMap) {
		Set<Date> dateSet = dateExpenseMap.keySet();
		List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		BasicEvent event;
		for (Date date : dateSet) {
			DateExpense dateExpense = dateExpenseMap.get(date);
			
			//userLimit events
			for (UserLimit userLimit : eshs.getUserLimitListDesc(expenseSheet)) {
				UserLimitExpense userLimitExpenseMap = dateExpense.getUserLimitExpenseMap().get(userLimit);
				if (userLimitExpenseMap == null)
					continue;
				event = new BasicEvent(userLimitExpenseMap.getSumString(), null, date);
				event.setAllDay(true);
				event.setStyleName(""+userLimitExpenseMap.getUserLimit().getOrder());
				calendarEvents.add(event);
			}
		}
		return calendarEvents;
	}
}
