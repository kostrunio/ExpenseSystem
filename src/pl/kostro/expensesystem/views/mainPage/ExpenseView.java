package pl.kostro.expensesystem.views.mainPage;

import java.util.Calendar;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Filter;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.MenuBar.MenuItem;

public class ExpenseView extends VerticalLayout implements View {

  private static final long serialVersionUID = -7668118300710655240L;

  private Calendar calendar = Calendar.getInstance();

  private ExpenseSheet expenseSheet;

  private HorizontalLayout searchLayout = new HorizontalLayout();
  
  final VerticalLayout mainView = new VerticalLayout();

  private ComboBox categoryCombo;

  private ComboBox userCombo;

  private TextField formulaField;

  private ComboBox commentCombo;

  private MenuBar monthMenu;

  public ExpenseView(final ExpenseSheet expenseSheet) {
    this.expenseSheet = expenseSheet;

    setMargin(true);
    setSpacing(true);

    addComponent(buildYearMenu());
    addComponent(buildMonthMenu());
    addComponent(buildSearchLayout());
    
    addComponent(mainView);

    calendar.set(Calendar.DAY_OF_MONTH, 1);
    mainView.addComponent(new MonthView(expenseSheet, calendar));
  }

  private Component buildYearMenu() {
    MenuBar.Command yearCommand = new MenuBar.Command() {

      private static final long serialVersionUID = -5557223656934648266L;

      @Override
      public void menuSelected(MenuItem selectedItem) {
        UserSummaryService.setFirstDay(calendar, selectedItem.getText());
        mainView.removeAllComponents();
        mainView.addComponent(new MonthView(expenseSheet, calendar));
      }
    };
    
    final HorizontalLayout menuLayout = new HorizontalLayout();
    menuLayout.setImmediate(false);
    menuLayout.setSpacing(true);

    // yearMenu
    final MenuBar yearMenu = new MenuBar();
    yearMenu.setImmediate(false);
    for (String year : ExpenseSheetService.getYearList(expenseSheet))
      yearMenu.addItem(year, yearCommand);
    
    menuLayout.addComponent(yearMenu);

    // filterButton
    final Button filterButton = new Button();
    filterButton.setCaption("Filtrowanie");
    filterButton.setImmediate(true);
    filterButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 5300096175827668413L;

      @Override
      public void buttonClick(ClickEvent event) {
        searchLayout.setVisible(!searchLayout.isVisible());
        prepareSearchLayout(expenseSheet, searchLayout.isVisible());
      }

    });

    menuLayout.addComponent(filterButton);
    menuLayout.setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);

    // cleanFilterButton
    final Button cleanFilterButton = new Button();
    cleanFilterButton.setCaption("Wyczyœæ");
    cleanFilterButton.setImmediate(true);
    cleanFilterButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 3632539483442849389L;

      @Override
      public void buttonClick(ClickEvent event) {
        if (searchLayout.isVisible()) {
          categoryCombo.removeAllItems();
          userCombo.removeAllItems();
          formulaField.setValue("");
          commentCombo.removeAllItems();
          expenseSheet.setFilter(null);
          searchLayout.setVisible(false);
          mainView.removeAllComponents();
          mainView.addComponent(new MonthView(expenseSheet, calendar));
        }
      }
    });

    menuLayout.addComponent(cleanFilterButton);
    menuLayout.setComponentAlignment(cleanFilterButton, Alignment.MIDDLE_RIGHT);

    // findButton
    final Button findButton = new Button();
    findButton.setCaption("Wyszukaj");
    findButton.setImmediate(true);
    findButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 8661846123260446001L;

      @Override
      public void buttonClick(ClickEvent event) {
        mainView.removeAllComponents();
        if (yearMenu.isEnabled()) {
          yearMenu.setEnabled(false);
          monthMenu.setEnabled(false);
          filterButton.setEnabled(false);
          cleanFilterButton.setEnabled(false);
          mainView.addComponent(new FindExpenseView(expenseSheet));
        } else {
          yearMenu.setEnabled(true);
          monthMenu.setEnabled(true);
          filterButton.setEnabled(true);
          cleanFilterButton.setEnabled(true);
          mainView.addComponent(new MonthView(expenseSheet, calendar));
        }
      }
    });

    menuLayout.addComponent(findButton);
    menuLayout.setComponentAlignment(findButton, Alignment.MIDDLE_RIGHT);

    return menuLayout;
  }

  private Component buildMonthMenu() {
    MenuBar.Command monthCommand = new MenuBar.Command() {

      private static final long serialVersionUID = 3896360510777453107L;

      @Override
      public void menuSelected(MenuItem selectedItem) {
        mainView.removeAllComponents();
        UserSummaryService.setFirstDay(calendar, UserSummaryService.getMonthNumber(selectedItem.getText()));
        mainView.addComponent(new MonthView(expenseSheet, calendar));
      }

    };
    
    monthMenu = new MenuBar();
    monthMenu.setImmediate(false);
    for (String monthName : UserSummaryService.getMonthsName())
      monthMenu.addItem(monthName, monthCommand);
    monthMenu.setSizeUndefined();

    return monthMenu;
  }

  private Component buildSearchLayout() {
    searchLayout.setVisible(false);

    // categoryCombo
    categoryCombo = new ComboBox();
    categoryCombo.setCaption("Kategoria");
    searchLayout.addComponent(categoryCombo);

    // userCombo
    userCombo = new ComboBox();
    userCombo.setCaption("U¿ytkownik");
    searchLayout.addComponent(userCombo);

    // formulaField
    formulaField = new TextField();
    formulaField.setCaption("Formu³a");
    searchLayout.addComponent(formulaField);

    // commentCombo
    commentCombo = new ComboBox();
    commentCombo.setCaption("Komentarz");
    searchLayout.addComponent(commentCombo);

    // searchButton
    final Button searchButton = new Button();
    searchButton.setCaption("Zastosuj");
    searchButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(ClickEvent event) {
        User filterUser = null;
        String filterFormula = null;
        String filterComment = null;
        if (userCombo.getValue() instanceof UserLimit) {
          filterUser = ((UserLimit) userCombo.getValue()).getUser();
        }
        if (formulaField.getValue() != null) {
          filterFormula = formulaField.getValue().toString();
        }
        if (commentCombo.getValue() != null) {
          filterComment = commentCombo.getValue().toString();
        }
        expenseSheet.setFilter(new Filter((Category) categoryCombo.getValue(), filterUser, filterFormula, filterComment));
        mainView.removeAllComponents();
        mainView.addComponent(new MonthView(expenseSheet, calendar));
      }
    });

    searchLayout.addComponent(searchButton);
    searchLayout.setComponentAlignment(searchButton, Alignment.MIDDLE_RIGHT);

    return searchLayout;
  }
  
  private void prepareSearchLayout(ExpenseSheet expenseSheet, boolean visible) {
    if (visible) {
      categoryCombo.addItems(expenseSheet.getCategoryList());
      userCombo.addItems(expenseSheet.getUserLimitList());
      commentCombo.setNewItemsAllowed(true);
      commentCombo.setNullSelectionAllowed(true);
      commentCombo.addItems(ExpenseSheetService.getCommentsList(expenseSheet));
    }
  }

  @Override
  public void enter(ViewChangeEvent event) {
    // TODO Auto-generated method stub

  }

}
