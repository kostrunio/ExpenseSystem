package pl.kostro.expensesystem.views.mainPage;

import java.util.Calendar;

import org.vaadin.teemu.VaadinIcons;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.views.chart.ChartSheetView;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetPasswordWindow;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetSettingsChangeListener;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetSettingsView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

public class ExpenseView extends Panel implements View, ExpenseSheetSettingsChangeListener {

  private static final long serialVersionUID = -7668118300710655240L;
  private final VerticalLayout root;
  private Label titleLabel;
  private VerticalLayout expensePanels;

  private Calendar calendar = Calendar.getInstance();
  private ExpenseSheet expenseSheet;

  private HorizontalLayout searchLayout = new HorizontalLayout();
  final VerticalLayout mainView = new VerticalLayout();
  private ComboBox categoryCombo;
  private ComboBox userCombo;
  private TextField formulaField;
  private ComboBox commentCombo;
  private MenuBar monthMenu;

  public ExpenseView() {
    addStyleName(ValoTheme.PANEL_BORDERLESS);
    setSizeFull();
    root = new VerticalLayout();
    root.setSizeFull();
    root.setMargin(true);
    root.addStyleName("expense-view");
    setContent(root);
    Responsive.makeResponsive(root);
  }

  private void prepareView() {
    root.addComponent(buildHeader());
    Component content = buildContent();
    root.addComponent(content);
    root.setExpandRatio(content, 1);

    calendar.set(Calendar.DAY_OF_MONTH, 1);
    mainView.addComponent(new MonthView(calendar));
  }

  private Component buildHeader() {
    HorizontalLayout header = new HorizontalLayout();
    header.addStyleName("viewheader");
    header.setSpacing(true);

    titleLabel = new Label(expenseSheet.getName());
    titleLabel.setSizeUndefined();
    titleLabel.addStyleName(ValoTheme.LABEL_H1);
    titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
    header.addComponent(titleLabel);

    Component edit = buildEditButton();
    Component chart = buildChartButton();
    HorizontalLayout tools = new HorizontalLayout(edit, chart);
    tools.setSpacing(true);
    tools.addStyleName("toolbar");
    header.addComponent(tools);

    return header;
  }

  private Component buildEditButton() {
    Button result = new Button();
    result.setIcon(VaadinIcons.EDIT);
    result.addStyleName("icon-edit");
    result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    result.setDescription(Msg.get("expenseSheet.edit"));
    result.addClickListener(new ClickListener() {
      private static final long serialVersionUID = 1792451562271503948L;

      @Override
      public void buttonClick(final ClickEvent event) {
        root.removeAllComponents();
        root.addComponent(new ExpenseSheetSettingsView());
      }
  });
    return result;
  }
  
  private Component buildChartButton() {
    Button result = new Button();
    result.setIcon(VaadinIcons.CHART);
    result.addStyleName("icon-edit");
    result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    result.setDescription(Msg.get("expenseSheet.chart"));
    result.addClickListener(new ClickListener() {
      private static final long serialVersionUID = 1792451562271503948L;

      @Override
      public void buttonClick(final ClickEvent event) {
        root.removeAllComponents();
        root.addComponent(new ChartSheetView());
      }
  });
    return result;
  }
  
  private Component buildContent() {
    expensePanels = new VerticalLayout();
    expensePanels.setSpacing(true);
    expensePanels.addStyleName("expense-panels");
    Responsive.makeResponsive(expensePanels);

    expensePanels.addComponent(buildYearMenu());
    expensePanels.addComponent(buildMonthMenu());
    expensePanels.addComponent(buildSearchLayout());

    expensePanels.addComponent(mainView);

    return expensePanels;
  }

  private Component buildYearMenu() {
    MenuBar.Command yearCommand = new MenuBar.Command() {

      private static final long serialVersionUID = -5557223656934648266L;

      @Override
      public void menuSelected(MenuItem selectedItem) {
        UserSummaryService.setFirstDay(calendar, selectedItem.getText());
        mainView.removeAllComponents();
        mainView.addComponent(new MonthView(calendar));
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
    filterButton.setCaption(Msg.get("expense.filter"));
    filterButton.setImmediate(true);
    filterButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 5300096175827668413L;

      @Override
      public void buttonClick(ClickEvent event) {
        searchLayout.setVisible(!searchLayout.isVisible());
        if (searchLayout.isVisible()) {
          prepareSearchLayout();
        } else {
          expenseSheet.setFilter(null);
          mainView.removeAllComponents();
          mainView.addComponent(new MonthView(calendar));
        }
      }

    });

    menuLayout.addComponent(filterButton);
    menuLayout.setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);

    // findButton
    final Button findButton = new Button();
    findButton.setCaption(Msg.get("expense.find"));
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
          mainView.addComponent(new FindExpenseView(calendar));
        } else {
          expenseSheet.setFilter(null);
          yearMenu.setEnabled(true);
          monthMenu.setEnabled(true);
          filterButton.setEnabled(true);
          mainView.addComponent(new MonthView(calendar));
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
        mainView.addComponent(new MonthView(calendar));
      }

    };

    monthMenu = new MenuBar();
    monthMenu.setImmediate(false);
    for (String monthName : UserSummaryService.getMonthsName())
      if (!monthName.isEmpty())
        monthMenu.addItem(monthName, monthCommand);
    monthMenu.setSizeUndefined();

    return monthMenu;
  }

  private Component buildSearchLayout() {
    searchLayout.removeAllComponents();
    searchLayout.setMargin(true);
    searchLayout.setSpacing(true);
    searchLayout.setVisible(false);

    // categoryCombo
    categoryCombo = new ComboBox();
    categoryCombo.setCaption(Msg.get("expense.category"));
    searchLayout.addComponent(categoryCombo);

    // userCombo
    userCombo = new ComboBox();
    userCombo.setCaption(Msg.get("expense.user"));
    searchLayout.addComponent(userCombo);

    // formulaField
    formulaField = new TextField();
    formulaField.setCaption(Msg.get("expense.formula"));
    searchLayout.addComponent(formulaField);

    // commentCombo
    commentCombo = new ComboBox();
    commentCombo.setCaption(Msg.get("expense.comment"));
    searchLayout.addComponent(commentCombo);

    // searchButton
    final Button searchButton = new Button();
    searchButton.setCaption(Msg.get("expense.search"));
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
          filterFormula = formulaField.getValue().toString().replaceAll(",", ".");
        }
        if (commentCombo.getValue() != null) {
          filterComment = commentCombo.getValue().toString();
        }
        expenseSheet.setFilter(new Filter((Category) categoryCombo.getValue(), filterUser, filterFormula, filterComment));
        mainView.removeAllComponents();
        mainView.addComponent(new MonthView(calendar));
      }
    });

    searchLayout.addComponent(searchButton);
    searchLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);

    return searchLayout;
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
    VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, expenseSheet);
    if (expenseSheet.getKey() == null) {
      expenseSheet.setKey(loggedUser.getClearPassword());
      if (expenseSheet.getUserLimitList().size() > 0) {
        try {
          expenseSheet.getUserLimitList().get(0).getLimit();
        } catch (NullPointerException e) {
          expenseSheet.setKey(null);
          UI.getCurrent().addWindow(new ExpenseSheetPasswordWindow(ExpenseView.this));
          return;
        }
      }
    }
    if (!expenseSheet.getEncrypted())
      ExpenseSheetService.encrypt(expenseSheet);
    root.removeAllComponents();
    mainView.removeAllComponents();
    prepareView();
  }

  @Override
  public void expenseSheetSettingsChange() {
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    UI.getCurrent().getNavigator().navigateTo("expenseSheet/"+expenseSheet.getId());
  }

}
