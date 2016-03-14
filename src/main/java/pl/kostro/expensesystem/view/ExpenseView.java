package pl.kostro.expensesystem.view;

import java.util.Calendar;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.view.design.MonthDesign;
import pl.kostro.expensesystem.views.chart.ChartSheetView;
import pl.kostro.expensesystem.views.mainPage.FindExpenseView;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetPasswordWindow;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetSettingsView;

@SuppressWarnings("serial")
public class ExpenseView extends MonthDesign implements View {

  private Calendar calendar = Calendar.getInstance();
  private ExpenseSheet expenseSheet;

  private ClickListener editClick = new ClickListener() {
    @Override
    public void buttonClick(final ClickEvent event) {
      root.removeAllComponents();
      root.addComponent(new ExpenseSheetSettingsView());
    }
  };
  private ClickListener chartClick = new ClickListener() {
    @Override
    public void buttonClick(final ClickEvent event) {
      root.removeAllComponents();
      root.addComponent(new ChartSheetView());
    }
  };
  private ClickListener filterClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      searchPanel.setVisible(!searchPanel.isVisible());
      if (searchPanel.isVisible()) {
        prepareSearchLayout();
      } else {
        expenseSheet.setFilter(null);
        mainView.removeAllComponents();
        // mainView.addComponent(new MonthView(calendar));
      }
    }
  };
  private ClickListener findClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      searchPanel.setVisible(false);
      mainView.removeAllComponents();
      if (yearMenu.isEnabled()) {
        yearMenu.setEnabled(false);
        monthMenu.setEnabled(false);
        filterButton.setEnabled(false);
        mainView.addComponent(new FindExpenseView(calendar));
      } else {
        expenseSheet.setFilter(null);
        yearMenu.setEnabled(true);
        monthMenu.setEnabled(true);
        filterButton.setEnabled(true);
        // mainView.addComponent(new MonthView(calendar));
      }
    }
  };
  private ClickListener prevClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      calendar.add(java.util.Calendar.MONTH, -1);
      showCalendar(calendar);
    }
  };
  private ClickListener nextClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      calendar.add(java.util.Calendar.MONTH, 1);
      showCalendar(calendar);
    }
  };
  private MenuBar.Command yearCommand = new MenuBar.Command() {
    @Override
    public void menuSelected(MenuItem selectedItem) {
      UserSummaryService.setFirstDay(calendar, selectedItem.getText());
      mainView.removeAllComponents();
      // mainView.addComponent(new MonthView(calendar));
      for (MenuItem item : yearMenu.getItems()) {
        item.setChecked(false);
      }
      selectedItem.setChecked(true);
    }
  };
  private MenuBar.Command monthCommand = new MenuBar.Command() {
    @Override
    public void menuSelected(MenuItem selectedItem) {
      mainView.removeAllComponents();
      UserSummaryService.setFirstDay(calendar, UserSummaryService.getMonthNumber(selectedItem.getText()));
      // mainView.addComponent(new MonthView(calendar));
      for (MenuItem item : monthMenu.getItems()) {
        item.setChecked(false);
      }
      selectedItem.setChecked(true);
    }
  };

  public void prepareView() {
    setCaption();
    editButton.addClickListener(editClick);
    chartButton.addClickListener(chartClick);
    filterButton.addClickListener(filterClick);
    findButton.addClickListener(findClick);
    previousMonthButton.addClickListener(prevClick);
    nextMonthButton.addClickListener(nextClick);
    thisMonthField.setDateFormat("MMMM yyyy");

    yearMenu.removeItems();
    for (String year : ExpenseSheetService.getYearList(expenseSheet))
      yearMenu.addItem(year, yearCommand).setCheckable(true);
    
    monthMenu.removeItems();
    for (String monthName : UserSummaryService.getMonthsName())
      if (!monthName.isEmpty())
        monthMenu.addItem(monthName, monthCommand).setCheckable(true);
    showCalendar(calendar);
  }

  private void setCaption() {
    titleLabel.setValue(expenseSheet.getName().toUpperCase());
    editButton.setDescription(Msg.get("expenseSheet.edit"));
    chartButton.setDescription(Msg.get("expenseSheet.chart"));
    filterButton.setDescription(Msg.get("expense.filter"));
    findButton.setDescription(Msg.get("expense.find"));
    categoryCombo.setCaption(Msg.get("expense.category"));
    userCombo.setCaption(Msg.get("expense.user"));
    formulaField.setCaption(Msg.get("expense.formula"));
    commentCombo.setCaption(Msg.get("expense.comment"));
    searchButton.setDescription(Msg.get("expense.search"));
  }

  private void prepareSearchLayout() {
    categoryCombo.removeAllItems();
    categoryCombo.addItems(expenseSheet.getCategoryList());
    userCombo.removeAllItems();
    userCombo.addItems(expenseSheet.getUserLimitList());
    formulaField.clear();
    commentCombo.setNewItemsAllowed(true);
    commentCombo.setNullSelectionAllowed(true);
    commentCombo.removeAllItems();
    commentCombo.addItems(ExpenseSheetService.getCommentsList(expenseSheet));
  }

  public void showCalendar(java.util.Calendar calendar) {
    thisMonthField.setValue(calendar.getTime());
    // firstDateField.setValue(UserSummaryService.getFirstDay(calendar.getTime()));
    // lastDateField.setValue(UserSummaryService.getLastDay(calendar.getTime()));
    // monthCalendar.setStartDate(firstDateField.getValue());
    // monthCalendar.setEndDate(lastDateField.getValue());
  }

  @Override
  public void enter(ViewChangeEvent event) {
    RealUser loggedUser = VaadinSession.getCurrent().getAttribute(RealUser.class);
    if (event.getParameters().isEmpty()) {
      expenseSheet = loggedUser.getDefaultExpenseSheet();
    } else {
      expenseSheet = ExpenseSheetService.findExpenseSheet(loggedUser, Integer.parseInt(event.getParameters()));
    }
    if (expenseSheet == null) {
      return;
    }
    expenseSheet.setFilter(null);
    VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, expenseSheet);
    MainView menuView = ((ExpenseSystemUI) getUI()).getMainView();
    menuView.setActiveView("expenseSheet/" + expenseSheet.getId());
    if (expenseSheet.getKey() == null) {
      expenseSheet.setKey(loggedUser.getClearPassword());
      if (expenseSheet.getUserLimitList().size() > 0) {
        try {
          expenseSheet.getUserLimitList().get(0).getLimit();
        } catch (NullPointerException e) {
          expenseSheet.setKey(null);
          UI.getCurrent().addWindow(new ExpenseSheetPasswordWindow());
          return;
        }
      }
    }
    if (!expenseSheet.getEncrypted())
      ExpenseSheetService.encrypt(expenseSheet);
//    root.removeAllComponents();
//    mainView.removeAllComponents();
    prepareView();
  }
}
