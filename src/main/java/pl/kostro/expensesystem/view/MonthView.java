package pl.kostro.expensesystem.view;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.kostro.expensesystem.event.ExpenseSystemEvent.BrowserResizeEvent;
import pl.kostro.expensesystem.event.ExpenseSystemEventBus;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.view.design.MonthDesign;

import com.google.common.eventbus.Subscribe;
import com.google.gwt.core.server.ServerGwtBridge.Properties;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class MonthView extends MonthDesign {

  private Logger logger = LogManager.getLogger();
  private UserSummaryService uss = new UserSummaryService();
  private Calendar date;
  private Button.ClickListener prevClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      date.add(java.util.Calendar.MONTH, -1);
      showCalendar();
    }
  };
  private Button.ClickListener nextClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      date.add(java.util.Calendar.MONTH, 1);
      showCalendar();
    }
  };
  private Property.ValueChangeListener thisMonthChange = new Property.ValueChangeListener() {
    @Override
    public void valueChange(ValueChangeEvent event) {
      date.setTime(thisMonthField.getValue());
      showCalendar();
    }
  };

  public MonthView() {
    logger.info("create");
    ExpenseSystemEventBus.register(this);
    this.date = VaadinSession.getCurrent().getAttribute(Calendar.class);
    uss.setFirstDay(date);
    previousMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
    previousMonthButton.addClickListener(prevClick);
    nextMonthButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_RIGHT);
    nextMonthButton.addClickListener(nextClick);
    firstDateField.setDateFormat("yyyy-MM-dd");
    thisMonthField.setDateFormat("MMMM yyyy");
    thisMonthField.addValueChangeListener(thisMonthChange);
    lastDateField.setDateFormat("yyyy-MM-dd");

    setCalendarSize();
    showCalendar();
  }

  public void showCalendar() {
    if (getParent() != null && getParent().getParent().getParent().getParent() instanceof ExpenseView) {
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedYear(date.get(Calendar.YEAR)+"");
      ((ExpenseView)getParent().getParent().getParent().getParent()).checkedMonth(uss.getMonthName(date.get(Calendar.MONTH)));
    }
    thisMonthField.setValue(date.getTime());
    firstDateField.setValue(uss.getFirstDay(date.getTime()));
    lastDateField.setValue(uss.getLastDay(date.getTime()));
    monthCalendar.setMonthView(this);
    monthCalendar.setStartDate(firstDateField.getValue());
    monthCalendar.setEndDate(lastDateField.getValue());
  }

  public void fulfillTables() {
    categoryTable.fulfill();
    userLimitTable.fulfill(date);
  }

  private void setCalendarSize() {
    if (Page.getCurrent().getBrowserWindowWidth() < 800) {
      monthCalendar.setWidth(Page.getCurrent().getBrowserWindowWidth()-30+"px");
      monthCalendar.setHeight(Page.getCurrent().getBrowserWindowWidth()-30+"px");
    }
  }

  @Subscribe
  public void browserResized(final BrowserResizeEvent event) {
    setCalendarSize();
  }


}
