package pl.kostro.expensesystem.ui.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox.NewItemProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.ui.ExpenseSystemUI;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.ui.view.design.ExpenseDesign;
import pl.kostro.expensesystem.ui.view.settingsPage.ExpenseSheetPasswordWindow;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

public class ExpenseView extends ExpenseDesign implements View {
  
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;

  private Logger logger = LogManager.getLogger();
  private LocalDate date = LocalDate.now();
  private ExpenseSheetEntity expenseSheet;
  private Button.ClickListener editClick = event -> {
    root.removeAllComponents();
    root.addComponent(new SettingsView());
  };
  private ClickListener chartClick = event -> {
    searchPanel.setVisible(false);
    mainView.removeAllComponents();
    if (yearMenu.isEnabled()) {
      yearMenu.setEnabled(false);
      monthMenu.setVisible(false);
      filterButton.setEnabled(false);
      tableButton.setEnabled(false);
      userSummaryButton.setEnabled(false);
      mainView.addComponent(new ChartView());
    } else {
      expenseSheet.setFilter(null);
      yearMenu.setEnabled(true);
      monthMenu.setVisible(true);
      filterButton.setEnabled(true);
      tableButton.setEnabled(true);
      userSummaryButton.setEnabled(true);
      mainView.addComponent(new MonthView());
    }
  };
  private ClickListener filterClick = event -> {
    searchPanel.setVisible(!searchPanel.isVisible());
    if (searchPanel.isVisible()) {
      prepareSearchLayout();
    } else {
      expenseSheet.setFilter(null);
      categoryCombo.clear();
      userCombo.clear();
      formulaField.clear();
      commentCombo.clear();
      mainView.removeAllComponents();
      mainView.addComponent(new MonthView());
    }
  };
  private ClickListener tableClick = event -> {
    searchPanel.setVisible(false);
    mainView.removeAllComponents();
    if (yearMenu.isEnabled()) {
      yearMenu.setEnabled(false);
      monthMenu.setVisible(false);
      filterButton.setEnabled(false);
      userSummaryButton.setEnabled(false);
      mainView.addComponent(new TableView());
    } else {
      expenseSheet.setFilter(null);
      yearMenu.setEnabled(true);
      monthMenu.setVisible(true);
      filterButton.setEnabled(true);
      userSummaryButton.setEnabled(true);
      mainView.addComponent(new MonthView());
    }
  };
  private ClickListener userSummaryClick = event -> {
    searchPanel.setVisible(false);
    mainView.removeAllComponents();
    if (yearMenu.isEnabled()) {
      yearMenu.setEnabled(false);
      monthMenu.setVisible(false);
      filterButton.setEnabled(false);
      tableButton.setEnabled(false);
      mainView.addComponent(new UserSummaryView());
    } else {
      expenseSheet.setFilter(null);
      yearMenu.setEnabled(true);
      monthMenu.setVisible(true);
      filterButton.setEnabled(true);
      tableButton.setEnabled(true);
      mainView.addComponent(new MonthView());
    }
  };
  private MenuBar.Command yearCommand = selectedItem -> {
  date = date.withYear(Integer.parseInt(selectedItem.getText())).withDayOfMonth(1);
  VaadinSession.getCurrent().setAttribute(LocalDate.class, date);
  mainView.removeAllComponents();
  mainView.addComponent(new MonthView());
  checkedYear(selectedItem.getText());
  };
  private MenuBar.Command monthCommand = selectedItem -> {
    mainView.removeAllComponents();
    date = date.withMonth(CalendarUtils.getMonthNumber(selectedItem.getText())).withDayOfMonth(1);
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date);
    mainView.addComponent(new MonthView());
    checkedMonth(selectedItem.getText());
  };
  private NewItemProvider addComment = event -> Optional.of(event);

  private ValueChangeListener<CategoryEntity> categoryChanged = event -> refreshFilter();

  private ValueChangeListener<UserLimitEntity> userChanged = event -> refreshFilter();

  private ValueChangeListener<String> formulaChanged = event -> refreshFilter();

  private ValueChangeListener<String> commentChanged = event -> refreshFilter();

  private void refreshFilter() {
    UserEntity filterUser = null;
    String filterFormula = null;
    String filterComment = null;
    if (userCombo.getValue() instanceof UserLimitEntity) {
      filterUser = userCombo.getValue().getUser();
    }
    if (formulaField.getValue() != null) {
      filterFormula = formulaField.getValue().replaceAll(",", ".");
    }
    if (commentCombo.getValue() != null) {
      filterComment = commentCombo.getValue();
    }
    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(categoryCombo.getValue());
    List<UserEntity> users = new ArrayList<>();
    users.add(filterUser);
    expenseSheet.setFilter(new Filter(categories, users, filterFormula, filterComment));
    mainView.removeAllComponents();
    mainView.addComponent(new MonthView());
  };

  public ExpenseView() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
  }

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
    categoryCombo.setItemCaptionGenerator(item -> item.getName());
    categoryCombo.setItems(expenseSheet.getCategoryList());
    userCombo.setItemCaptionGenerator(item -> item.getUser().getName());
    userCombo.setItems(expenseSheet.getUserLimitList());
    commentCombo.setNewItemProvider(addComment);
    commentCombo.setEmptySelectionAllowed(true);
    commentCombo.setItems(eshts.getAllComments(expenseSheet));
  }

  private void prepareView() {
    root.addComponent(buildHeader(expenseSheet.getName()));
    editButton.addClickListener(editClick);
    chartButton.addClickListener(chartClick);
    Component content = buildContent();
    for (String year : eshts.getYearList(expenseSheet))
      yearMenu.addItem(year, yearCommand).setCheckable(true);
    filterButton.addClickListener(filterClick);
    categoryCombo.addValueChangeListener(categoryChanged);
    userCombo.addValueChangeListener(userChanged);
    formulaField.addValueChangeListener(formulaChanged);
    commentCombo.addValueChangeListener(commentChanged);
    tableButton.addClickListener(tableClick);
    userSummaryButton.addClickListener(userSummaryClick);
    for (String monthName : CalendarUtils.getMonthsName())
      monthMenu.addItem(monthName, monthCommand).setCheckable(true);
    root.addComponent(content);
    root.setExpandRatio(content, 1);

    date = date.withDayOfMonth(1);
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date);
    mainView.addComponent(new MonthView());
  }

  @Override
  public void enter(ViewChangeEvent event) {
    logger.info("Enter");
    RealUserEntity loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    if (event.getParameters().isEmpty()) {
      expenseSheet = loggedUser.getDefaultExpenseSheet();
    } else {
      expenseSheet = eshts.findExpenseSheet(loggedUser, Integer.parseInt(event.getParameters()));
    }
    if (expenseSheet == null) {
      return;
    }
    expenseSheet.setFilter(null);
    eshs.fetchCategoryList(expenseSheet);
    eshs.fetchExpenseList(expenseSheet);
    eshs.fetchUserLimitList(expenseSheet);
    VaadinSession.getCurrent().setAttribute(ExpenseSheetEntity.class, expenseSheet);
    MainView menuView = ((ExpenseSystemUI) getUI()).getMainView();
    menuView.setActiveView("expenseSheet/" + expenseSheet.getId());
    if (expenseSheet.getSecretKey() == null) {
      expenseSheet.setSecretKey(loggedUser.getClearPassword());
      if (expenseSheet.getUserLimitList().size() > 0) {
        try {
          expenseSheet.getUserLimitList().get(0).getLimit();
        } catch (NullPointerException e) {
          expenseSheet.setSecretKey(null);
          UI.getCurrent().addWindow(new ExpenseSheetPasswordWindow());
          return;
        }
      }
    }
    if (!expenseSheet.getEncrypted())
      eshs.encrypt(expenseSheet);
    root.removeAllComponents();
    if (mainView != null)
      mainView.removeAllComponents();
    prepareView();
  }

}
