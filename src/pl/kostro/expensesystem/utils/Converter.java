package pl.kostro.expensesystem.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

public class Converter {
	
	public static List<CalendarEvent> transformExpensesToEvents(Map<Date, DateExpense> dateExpenseMap) {
		Set<Date> dateSet = dateExpenseMap.keySet();
		List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		BasicEvent event;
		for (Date date : dateSet) {
			DateExpense dateExpense = dateExpenseMap.get(date);
			event = new BasicEvent(dateExpense.getSumString(), null, date);
			event.setAllDay(true);
			calendarEvents.add(event);
		}
		return calendarEvents;
	}
}
