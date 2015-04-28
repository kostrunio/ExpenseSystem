package pl.kostro.expensesystem.components.mainPageComponents;

import java.util.Calendar;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Filter;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ExpenseView extends CustomComponent {

  @AutoGenerated
  private VerticalLayout mainLayout;

  @AutoGenerated
  private VerticalLayout mainView;

  @AutoGenerated
  private HorizontalLayout searchLayout;

  @AutoGenerated
  private Button searchButton;

  @AutoGenerated
  private ComboBox comment;

  @AutoGenerated
  private TextField formula;

  @AutoGenerated
  private ComboBox user;

  @AutoGenerated
  private ComboBox category;

  @AutoGenerated
  private MenuBar monthMenu;

  @AutoGenerated
  private HorizontalLayout menuLayout;

  @AutoGenerated
  private Button cleanFilterButton;

  @AutoGenerated
  private Button filterButton;

  @AutoGenerated
  private MenuBar yearMenu;

  private static final long serialVersionUID = -7668118300710655240L;

  /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

  private Calendar calendar = Calendar.getInstance();

  /**
   * The constructor should first build the main layout, set the composition
   * root and then do any custom initialization.
   * 
   * The constructor will not be automatically regenerated by the visual editor.
   */
  public ExpenseView(final ExpenseSheet expenseSheet) {
    buildMainLayout();
    setCompositionRoot(mainLayout);

    // TODO add user code here

    filterButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 5300096175827668413L;

      @Override
      public void buttonClick(ClickEvent event) {
        searchLayout.setVisible(!searchLayout.isVisible());
        prepareSearchLayout(expenseSheet, searchLayout.isVisible());
      }

    });

    searchButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(ClickEvent event) {
        User filterUser = null;
        String filterFormula = null;
        String filterComment = null;
        if (user.getValue() instanceof UserLimit) {
          filterUser = ((UserLimit) user.getValue()).getUser();
        }
        if (formula.getValue() != null) {
          filterFormula = formula.getValue().toString();
        }
        if (comment.getValue() != null) {
          filterComment = comment.getValue().toString();
        }
        expenseSheet.setFilter(new Filter((Category) category.getValue(), filterUser, filterFormula, filterComment));
        mainView.removeAllComponents();
        mainView.addComponent(new MonthView(expenseSheet, calendar));
      }
    });

    cleanFilterButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 3632539483442849389L;

      @Override
      public void buttonClick(ClickEvent event) {
        if (searchLayout.isVisible()) {
          category.removeAllItems();
          user.removeAllItems();
          formula.setValue("");
          comment.removeAllItems();
          expenseSheet.setFilter(null);
          searchLayout.setVisible(false);
          mainView.removeAllComponents();
          mainView.addComponent(new MonthView(expenseSheet, calendar));
        }
      }
    });

    ExpenseSheetService expenseSheetService = new ExpenseSheetService();
    MenuBar.Command yearCommand = new MenuBar.Command() {

      private static final long serialVersionUID = -5557223656934648266L;

      @Override
      public void menuSelected(MenuItem selectedItem) {
        UserSummaryService.setFirstDay(calendar, selectedItem.getText());
        mainView.removeAllComponents();
        mainView.addComponent(new MonthView(expenseSheet, calendar));
      }
    };

    for (String year : expenseSheetService.getYearList(expenseSheet))
      yearMenu.addItem(year, yearCommand);

    MenuBar.Command monthCommand = new MenuBar.Command() {

      private static final long serialVersionUID = 3896360510777453107L;

      @Override
      public void menuSelected(MenuItem selectedItem) {
        mainView.removeAllComponents();
        UserSummaryService.setFirstDay(calendar, UserSummaryService.getMonthNumber(selectedItem.getText()));
        mainView.addComponent(new MonthView(expenseSheet, calendar));
      }

    };

    for (String monthName : UserSummaryService.getMonthsName()) {
      monthMenu.addItem(monthName, monthCommand);
    }

    calendar.set(Calendar.DAY_OF_MONTH, 1);
    mainView.addComponent(new MonthView(expenseSheet, calendar));
  }

  private void prepareSearchLayout(ExpenseSheet expenseSheet, boolean visible) {
    if (visible) {
      category.addItems(expenseSheet.getCategoryList());
      user.addItems(expenseSheet.getUserLimitList());
      comment.setNewItemsAllowed(true);
      comment.setNullSelectionAllowed(true);
      comment.addItems(ExpenseSheetService.getCommentsList(expenseSheet));
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

    // menuLayout
    menuLayout = buildMenuLayout();
    mainLayout.addComponent(menuLayout);

    // monthMenu
    monthMenu = new MenuBar();
    monthMenu.setImmediate(false);
    monthMenu.setWidth("100.0%");
    monthMenu.setHeight("-1px");
    mainLayout.addComponent(monthMenu);

    // searchLayout
    searchLayout = buildSearchLayout();
    mainLayout.addComponent(searchLayout);

    // mainView
    mainView = new VerticalLayout();
    mainView.setImmediate(false);
    mainView.setWidth("-1px");
    mainView.setHeight("-1px");
    mainView.setMargin(false);
    mainLayout.addComponent(mainView);

    return mainLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildMenuLayout() {
    // common part: create layout
    menuLayout = new HorizontalLayout();
    menuLayout.setImmediate(false);
    menuLayout.setWidth("100.0%");
    menuLayout.setHeight("-1px");
    menuLayout.setMargin(false);
    menuLayout.setSpacing(true);

    // yearMenu
    yearMenu = new MenuBar();
    yearMenu.setImmediate(false);
    yearMenu.setWidth("-1px");
    yearMenu.setHeight("-1px");
    menuLayout.addComponent(yearMenu);

    // filterButton
    filterButton = new Button();
    filterButton.setCaption("Filtrowanie");
    filterButton.setImmediate(true);
    filterButton.setWidth("-1px");
    filterButton.setHeight("-1px");
    menuLayout.addComponent(filterButton);
    menuLayout.setComponentAlignment(filterButton, new Alignment(6));

    // cleanFilterButton
    cleanFilterButton = new Button();
    cleanFilterButton.setCaption("Wyczy��");
    cleanFilterButton.setImmediate(true);
    cleanFilterButton.setWidth("-1px");
    cleanFilterButton.setHeight("-1px");
    menuLayout.addComponent(cleanFilterButton);

    return menuLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildSearchLayout() {
    // common part: create layout
    searchLayout = new HorizontalLayout();
    searchLayout.setImmediate(false);
    searchLayout.setWidth("100.0%");
    searchLayout.setHeight("-1px");
    searchLayout.setMargin(false);
    searchLayout.setVisible(false);

    // category
    category = new ComboBox();
    category.setCaption("Kategoria");
    category.setImmediate(false);
    category.setWidth("-1px");
    category.setHeight("-1px");
    searchLayout.addComponent(category);

    // user
    user = new ComboBox();
    user.setCaption("U�ytkownik");
    user.setImmediate(false);
    user.setWidth("-1px");
    user.setHeight("-1px");
    searchLayout.addComponent(user);

    // formula
    formula = new TextField();
    formula.setCaption("Formu�a");
    formula.setImmediate(false);
    formula.setWidth("-1px");
    formula.setHeight("-1px");
    searchLayout.addComponent(formula);

    // comment
    comment = new ComboBox();
    comment.setCaption("Komentarz");
    comment.setImmediate(false);
    comment.setWidth("-1px");
    comment.setHeight("-1px");
    searchLayout.addComponent(comment);

    // searchButton
    searchButton = new Button();
    searchButton.setCaption("Zastosuj");
    searchButton.setImmediate(false);
    searchButton.setWidth("-1px");
    searchButton.setHeight("-1px");
    searchLayout.addComponent(searchButton);
    searchLayout.setComponentAlignment(searchButton, new Alignment(10));

    return searchLayout;
  }

}
