package pl.kostro.expensesystem.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.view.design.ExpenseDesign;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetPasswordWindow;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
public class ExpenseView extends ExpenseDesign implements View {

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetService ess = new ExpenseSheetService();
  private UserSummaryService uss = new UserSummaryService();
  private Calendar calendar = Calendar.getInstance();
  private ExpenseSheet expenseSheet;
  private Button.ClickListener editClick = new ClickListener() {
    @Override
    public void buttonClick(final ClickEvent event) {
      root.removeAllComponents();
      root.addComponent(new SettingsView());
    }
  };
  private Button.ClickListener chartClick = new ClickListener() {
    @Override
    public void buttonClick(final ClickEvent event) {
      root.removeAllComponents();
      root.addComponent(new ChartView());
    }
  };
  private Button.ClickListener filterClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      searchPanel.setVisible(!searchPanel.isVisible());
      if (searchPanel.isVisible()) {
        prepareSearchLayout();
      } else {
        expenseSheet.setFilter(null);
        mainView.removeAllComponents();
        mainView.addComponent(new MonthView());
      }
    }
  };
  private Button.ClickListener tableClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      searchPanel.setVisible(false);
      mainView.removeAllComponents();
      if (yearMenu.isEnabled()) {
        yearMenu.setEnabled(false);
        monthMenu.setVisible(false);
        filterButton.setEnabled(false);
        mainView.addComponent(new TableView());
      } else {
        expenseSheet.setFilter(null);
        yearMenu.setEnabled(true);
        monthMenu.setVisible(true);
        filterButton.setEnabled(true);
        mainView.addComponent(new MonthView());
      }
    }
  };
  private Button.ClickListener searchClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      User filterUser = null;
      String filterFormula = null;
      String filterComment = null;
      if (userCombo.getValue() instanceof UserLimit) {
        filterUser = ((UserLimit) userCombo.getValue()).getUser();
      }
      if (formulaField.getValue() != null) {
        filterFormula = formulaField.getValue().toString().replaceAll(",", ".");
      }
      if (commentCombo.getValue() != null) {
        filterComment = commentCombo.getValue().toString();
      }
      List<Category> categories = new ArrayList<Category>();
      categories.add((Category) categoryCombo.getValue());
      List<User> users = new ArrayList<User>();
      users.add((User) filterUser);
      expenseSheet.setFilter(new Filter(categories, users, filterFormula, filterComment));
      mainView.removeAllComponents();
      mainView.addComponent(new MonthView());
    }
  };
  private MenuBar.Command yearCommand = new MenuBar.Command() {
    @Override
    public void menuSelected(MenuItem selectedItem) {
      CalendarUtils.setFirstDay(calendar, selectedItem.getText());
      mainView.removeAllComponents();
      mainView.addComponent(new MonthView());
      checkedYear(selectedItem.getText());
    }
  };
  private MenuBar.Command monthCommand = new MenuBar.Command() {
    @Override
    public void menuSelected(MenuItem selectedItem) {
      mainView.removeAllComponents();
      CalendarUtils.setFirstDay(calendar, CalendarUtils.getMonthNumber(selectedItem.getText()));
      mainView.addComponent(new MonthView());
      checkedMonth(selectedItem.getText());
    }
  };

  public void checkedYear(String yearString) {
    for (MenuItem item : yearMenu.getItems()) {
      item.setChecked(false);
      if (item.getText().equals(yearString))
        item.setChecked(true);
    }
  }

  public void checkedMonth(String monthString) {
    for (MenuItem item : monthMenu.getItems()) {
      item.setChecked(false);
      if (item.getText().equals(monthString))
        item.setChecked(true);
    }
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

  private void prepareView() {
    root.addComponent(buildHeader(expenseSheet.getName()));
    editButton.addClickListener(editClick);
    chartButton.addClickListener(chartClick);
    Component content = buildContent();
    for (String year : ExpenseSheetService.getYearList(expenseSheet))
      yearMenu.addItem(year, yearCommand).setCheckable(true);
    filterButton.addClickListener(filterClick);
    tableButton.addClickListener(tableClick);
    for (String monthName : CalendarUtils.getMonthsName())
      if (!monthName.isEmpty())
        monthMenu.addItem(monthName, monthCommand).setCheckable(true);
    searchButton.addClickListener(searchClick);
    root.addComponent(content);
    root.setExpandRatio(content, 1);

    calendar.set(Calendar.DAY_OF_MONTH, 1);
    VaadinSession.getCurrent().setAttribute(Calendar.class, calendar);
    mainView.addComponent(new MonthView());
  }

  @Override
  public void enter(ViewChangeEvent event) {
    logger.info("Enter");
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
      ess.encrypt(expenseSheet);
    root.removeAllComponents();
    if (mainView != null)
      mainView.removeAllComponents();
    prepareView();
  }

}
