package pl.kostro.expensesystem.components.mainPageComponents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Converter;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClick;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;

public class MonthView extends CustomComponent {

	private static final long serialVersionUID = 2594978831652398190L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Calendar monthCalendar;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public MonthView(final ExpenseSheet expenseSheet, String monthName, String year) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		final ExpenseSheetService expenseSheetService = new ExpenseSheetService();
		monthCalendar.setEventProvider(new CalendarEventProvider() {

			private static final long serialVersionUID = -2219052651460350955L;

			@Override
			public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
				return Converter.transformExpensesToEvents(expenseSheetService.prepareDateExpenseMap(expenseSheet, startDate, endDate));
			}
			
		});
		
		monthCalendar.setHandler(new BasicDateClickHandler() {
			
			private static final long serialVersionUID = 2426375506359469533L;

			@Override
			public void dateClick(DateClickEvent event) {
				setCompositionRoot(new DayView(expenseSheet, event.getDate()));
			}
		});
		
		monthCalendar.setHandler(new BasicWeekClickHandler() {
			
			private static final long serialVersionUID = -5488623945839839169L;

			@Override
			protected void setDates(WeekClick event, Date start, Date end) {
				//Do nothing
			}
		});
		
		showCalendar(monthName, year);
	}

	public void showCalendar(String monthName, String year) {
		try {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			if (monthName.equals("stycze�")) {
				monthCalendar.setStartDate(df.parse("01-01-"+year));
				monthCalendar.setEndDate(df.parse("31-01-"+year));
			} else if (monthName.equals("luty")) {
				monthCalendar.setStartDate(df.parse("01-02-"+year));
				monthCalendar.setEndDate(df.parse("28-02-"+year));
			} else if (monthName.equals("marzec")) {
				monthCalendar.setStartDate(df.parse("01-03-"+year));
				monthCalendar.setEndDate(df.parse("31-03-"+year));
			} else if (monthName.equals("kwiecie�")) {
				monthCalendar.setStartDate(df.parse("01-04-"+year));
				monthCalendar.setEndDate(df.parse("30-04-"+year));
			} else if (monthName.equals("maj")) {
				monthCalendar.setStartDate(df.parse("01-05-"+year));
				monthCalendar.setEndDate(df.parse("31-05-"+year));
			} else if (monthName.equals("czerwiec")) {
				monthCalendar.setStartDate(df.parse("01-06-"+year));
				monthCalendar.setEndDate(df.parse("30-06-"+year));
			} else if (monthName.equals("lipiec")) {
				monthCalendar.setStartDate(df.parse("01-07-"+year));
				monthCalendar.setEndDate(df.parse("31-07-"+year));
			} else if (monthName.equals("sierpie�")) {
				monthCalendar.setStartDate(df.parse("01-08-"+year));
				monthCalendar.setEndDate(df.parse("31-08-"+year));
			} else if (monthName.equals("wrzesie�")) {
				monthCalendar.setStartDate(df.parse("01-09-"+year));
				monthCalendar.setEndDate(df.parse("30-09-"+year));
			} else if (monthName.equals("pa�dziernik")) {
				monthCalendar.setStartDate(df.parse("01-10-"+year));
				monthCalendar.setEndDate(df.parse("31-10-"+year));
			} else if (monthName.equals("listopad")) {
				monthCalendar.setStartDate(df.parse("01-11-"+year));
				monthCalendar.setEndDate(df.parse("30-11-"+year));
			} else if (monthName.equals("grudzie�")) {
				monthCalendar.setStartDate(df.parse("01-12-"+year));
				monthCalendar.setEndDate(df.parse("31-12-"+year));
			}
		}catch(ParseException e) {
			e.printStackTrace();
		}
		
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// monthCalendar
		monthCalendar = new Calendar();
		monthCalendar.setImmediate(false);
		monthCalendar.setWidth("-1px");
		monthCalendar.setHeight("-1px");
		mainLayout.addComponent(monthCalendar);
		
		return mainLayout;
	}

}
