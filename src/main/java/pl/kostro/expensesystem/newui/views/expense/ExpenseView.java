package pl.kostro.expensesystem.newui.views.expense;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.*;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.newui.views.chart.ChartView;
import pl.kostro.expensesystem.newui.views.main.MainView;
import pl.kostro.expensesystem.newui.views.month.MonthView;
import pl.kostro.expensesystem.newui.views.settings.SettingsView;
import pl.kostro.expensesystem.newui.views.settingsPage.ExpenseSheetPasswordWindow;
import pl.kostro.expensesystem.newui.views.table.TableView;
import pl.kostro.expensesystem.newui.views.userSummary.UserSummaryView;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Route(value = "", layout = MainView.class)
@RouteAlias(value = "expenseSheet", layout = MainView.class)
@PageTitle("ExpenseSheet")
public class ExpenseView extends ExpenseDesign implements HasUrlParameter<Integer> {
  
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;

  private Logger logger = LogManager.getLogger();
  private LocalDate date = LocalDate.now();
  private ExpenseSheetEntity expenseSheet;
  private ComponentEventListener<ClickEvent<Button>> editClick = event -> {
    root.removeAll();
    root.add(new SettingsView());
  };
  private ComponentEventListener<ClickEvent<Button>> chartClick = event -> {
    searchPanel.setVisible(false);
    mainView.removeAll();
//    if (yearMenu.isEnabled()) {
//      yearMenu.setEnabled(false);
      monthMenu.setVisible(false);
      filterButton.setEnabled(false);
      tableButton.setEnabled(false);
      userSummaryButton.setEnabled(false);
      mainView.add(new ChartView());
    /*} else {
      expenseSheet.setFilter(null);
//      yearMenu.setEnabled(true);
      monthMenu.setVisible(true);
      filterButton.setEnabled(true);
      tableButton.setEnabled(true);
      userSummaryButton.setEnabled(true);
      mainView.add(new MonthView());
    }*/
  };
  private ComponentEventListener<ClickEvent<Button>> filterClick = event -> {
    searchPanel.setVisible(!searchPanel.isVisible());
    if (searchPanel.isVisible()) {
      prepareSearchLayout();
    } else {
      expenseSheet.setFilter(null);
      categoryCombo.clear();
      userCombo.clear();
      formulaField.clear();
      commentCombo.clear();
      mainView.removeAll();
      mainView.add(new MonthView());
    }
  };
  private ComponentEventListener<ClickEvent<Button>> tableClick = event -> {
    searchPanel.setVisible(false);
    mainView.removeAll();
//    if (yearMenu.isEnabled()) {
//      yearMenu.setEnabled(false);
      monthMenu.setVisible(false);
      filterButton.setEnabled(false);
      userSummaryButton.setEnabled(false);
      mainView.add(new TableView());
    /*} else {
      expenseSheet.setFilter(null);
//      yearMenu.setEnabled(true);
      monthMenu.setVisible(true);
      filterButton.setEnabled(true);
      userSummaryButton.setEnabled(true);
      mainView.add(new MonthView());
    }*/
  };
  private ComponentEventListener<ClickEvent<Button>> userSummaryClick = event -> {
    searchPanel.setVisible(false);
    mainView.removeAll();
//    if (yearMenu.isEnabled()) {
//      yearMenu.setEnabled(false);
      monthMenu.setVisible(false);
      filterButton.setEnabled(false);
      tableButton.setEnabled(false);
      mainView.add(new UserSummaryView());
    /*} else {
      expenseSheet.setFilter(null);
//      yearMenu.setEnabled(true);
      monthMenu.setVisible(true);
      filterButton.setEnabled(true);
      tableButton.setEnabled(true);
      mainView.add(new MonthView());
    }*/
  };
  private ComponentEventListener<ClickEvent<MenuItem>> yearCommand = selectedItem -> {
  date = date.withYear(Integer.parseInt(selectedItem.getSource().getText())).withDayOfMonth(1);
  VaadinSession.getCurrent().setAttribute(LocalDate.class, date);
  mainView.removeAll();
  mainView.add(new MonthView());
//  checkedYear(selectedItem.getSource().getText());
  };
  private ComponentEventListener<ClickEvent<MenuItem>> monthCommand = selectedItem -> {
    mainView.removeAll();
    date = date.withMonth(CalendarUtils.getMonthNumber(selectedItem.getSource().getText())).withDayOfMonth(1);
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date);
    mainView.add(new MonthView());
//    checkedMonth(selectedItem.getSource().getText());
  };
//  private NewItemProvider addComment = event -> Optional.of(event);

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<CategoryEntity>, CategoryEntity>> categoryChanged = event -> refreshFilter();

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<UserLimitEntity>, UserLimitEntity>> userChanged = event -> refreshFilter();

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<TextField, String>> formulaChanged = event -> refreshFilter();

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>> commentChanged = event -> refreshFilter();

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
    mainView.removeAll();
    mainView.add(new MonthView());
  };

  public ExpenseView() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
  }

/*  public void checkedYear(String yearString) {
    for (MenuItem item : yearMenu.getItems()) {
      item.setChecked(false);
      if (item.getText().equals(yearString))
        item.setChecked(true);
    }
  }*/

/*  public void checkedMonth(String monthString) {
    for (MenuItem item : monthMenu.getItems()) {
      item.setChecked(false);
      if (item.getText().equals(monthString))
        item.setChecked(true);
    }
  }*/

  private void prepareSearchLayout() {
    categoryCombo.setItemLabelGenerator(item -> item.getName());
    categoryCombo.setItems(expenseSheet.getCategoryList());
    userCombo.setItemLabelGenerator(item -> item.getUser().getName());
    userCombo.setItems(expenseSheet.getUserLimitList());
//    commentCombo.setNewItemProvider(addComment);
//    commentCombo.setEmptySelectionAllowed(true);
    commentCombo.setItems(eshts.getAllComments(expenseSheet));
  }

  private void prepareView() {
    root.add(buildHeader(expenseSheet.getName()));
    editButton.addClickListener(editClick);
    chartButton.addClickListener(chartClick);
    Component content = buildContent();
    for (String year : eshts.getYearList(expenseSheet))
      yearMenu.addItem(year, yearCommand).setCheckable(false);/*was true*/
    filterButton.addClickListener(filterClick);
    categoryCombo.addValueChangeListener(categoryChanged);
    userCombo.addValueChangeListener(userChanged);
    formulaField.addValueChangeListener(formulaChanged);
    commentCombo.addValueChangeListener(commentChanged);
    tableButton.addClickListener(tableClick);
    userSummaryButton.addClickListener(userSummaryClick);
    for (String monthName : CalendarUtils.getMonthsName())
      monthMenu.addItem(monthName, monthCommand).setCheckable(false);/*was true*/
    root.add(content);
    root.expand(content);

    date = date.withDayOfMonth(1);
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date);
    mainView.add(new MonthView());
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter Integer parameter) {
    logger.info("Enter");
    RealUserEntity loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    if (parameter == null) {
      expenseSheet = loggedUser.getDefaultExpenseSheet();
    } else {
      expenseSheet = eshts.findExpenseSheet(loggedUser, parameter);
    }
    if (expenseSheet == null) {
      return;
    }
    expenseSheet.setFilter(null);
    eshs.fetchCategoryList(expenseSheet);
    eshs.fetchExpenseList(expenseSheet);
    eshs.fetchUserLimitList(expenseSheet);
    VaadinSession.getCurrent().setAttribute(ExpenseSheetEntity.class, expenseSheet);
//    MainView menuView = ((ExpenseSystemUI) getUI()).getMainView();
//    menuView.setActiveView("expenseSheet/" + expenseSheet.getId());
    if (expenseSheet.getSecretKey() == null) {
      expenseSheet.setSecretKey(loggedUser.getClearPassword());
      if (expenseSheet.getUserLimitList().size() > 0) {
        try {
          expenseSheet.getUserLimitList().get(0).getLimit();
        } catch (NullPointerException e) {
          expenseSheet.setSecretKey(null);
          new ExpenseSheetPasswordWindow().open();
          return;
        }
      }
    }
    if (!expenseSheet.getEncrypted())
      eshs.encrypt(expenseSheet);
    root.removeAll();
    if (mainView != null)
      mainView.removeAll();
    prepareView();
  }

}
