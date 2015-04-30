package pl.kostro.expensesystem.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

public class Converter {
	
	public static List<CalendarEvent> transformExpensesToEvents(ExpenseSheet expenseSheet, Map<Date, DateExpense> dateExpenseMap) {
		Set<Date> dateSet = dateExpenseMap.keySet();
		List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		BasicEvent event;
		for (Date date : dateSet) {
			DateExpense dateExpense = dateExpenseMap.get(date);
			
			//userLimit events
			for (UserLimit userLimit : expenseSheet.getUserLimitListDesc()) {
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
