package pl.kostro.expensesystem.view.design;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.CategoryEntity;
import pl.kostro.expensesystem.model.UserLimit;

@SuppressWarnings("serial")
public class ExpenseDesign extends Panel {
  protected VerticalLayout root;
  
  protected HorizontalLayout header;
  protected Label titleLabel;
  protected HorizontalLayout tools;
  protected Button editButton;
  protected Button chartButton;
  
  protected VerticalLayout menusLayout;
  protected VerticalLayout expenseLayout;
  protected HorizontalLayout menuLayout;
  protected MenuBar yearMenu;
  protected Button filterButton;
  protected Button tableButton;
  protected Button userSummaryButton;
  
  protected MenuBar monthMenu;

  protected Panel searchPanel;
  protected HorizontalLayout searchLayout;
  protected ComboBox<CategoryEntity> categoryCombo;
  protected ComboBox<UserLimit> userCombo;
  protected TextField formulaField;
  protected ComboBox<String> commentCombo;

  protected VerticalLayout mainView;

  public ExpenseDesign() {
    addStyleName(ValoTheme.PANEL_BORDERLESS);
    setSizeFull();
    root = new VerticalLayout();
    root.setSizeUndefined();
    root.setSpacing(false);
    setContent(root);
  }

  protected Component buildHeader(String name) {
    header = new HorizontalLayout();
    header.setMargin(false);

    titleLabel = new Label(name);
    titleLabel.setSizeUndefined();
    titleLabel.addStyleName(ValoTheme.LABEL_H1);
    titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
    header.addComponent(titleLabel);

    tools = new HorizontalLayout(buildEditButton(), buildChartButton());
    tools.setMargin(false);
    header.addComponent(tools);

    return header;
  }

  private Component buildEditButton() {
    editButton = new Button();
    editButton.setIcon(VaadinIcons.EDIT);
    editButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    editButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    return editButton;
  }
  
  private Component buildChartButton() {
    chartButton = new Button();
    chartButton.setIcon(VaadinIcons.LINE_CHART);
    chartButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    chartButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    return chartButton;
  }
  
  protected Component buildContent() {
    expenseLayout = new VerticalLayout();
    expenseLayout.setMargin(false);
    expenseLayout.addComponent(buildMenus());

    mainView = new VerticalLayout();
    mainView.setMargin(false);
    mainView.setSpacing(false);
    expenseLayout.addComponent(mainView);

    return expenseLayout;
  }
  
  private Component buildMenus() {
    menusLayout = new VerticalLayout();
    menusLayout.addStyleName("menus-layout");
    menusLayout.setMargin(false);
    menusLayout.addComponent(buildYearMenu());
    menusLayout.addComponent(buildMonthMenu());
    menusLayout.addComponent(buildSearchLayout());
    return menusLayout;
  }
  
  private Component buildYearMenu() {
    menuLayout = new HorizontalLayout();
    menuLayout.setMargin(false);

    // yearMenu
    yearMenu = new MenuBar();
    menuLayout.addComponent(yearMenu);

    // filterButton
    filterButton = new Button();
    filterButton.setIcon(VaadinIcons.FILTER);
    filterButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    filterButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    filterButton.setCaption(Msg.get("expense.filter"));

    menuLayout.addComponent(filterButton);
    menuLayout.setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);

    // findButton
    tableButton = new Button();
    tableButton.setIcon(VaadinIcons.TABLE);
    tableButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    tableButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    tableButton.setCaption(Msg.get("expense.find"));

    menuLayout.addComponent(tableButton);
    menuLayout.setComponentAlignment(tableButton, Alignment.MIDDLE_RIGHT);

    // userSummaryBurron
    userSummaryButton = new Button();
    userSummaryButton.setIcon(VaadinIcons.FILE_TREE_SMALL);
    userSummaryButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    userSummaryButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    userSummaryButton.setCaption(Msg.get("expense.userSummary"));

    menuLayout.addComponent(userSummaryButton);
    menuLayout.setComponentAlignment(userSummaryButton, Alignment.MIDDLE_RIGHT);

    return menuLayout;
  }

  private Component buildMonthMenu() {
    monthMenu = new MenuBar();
    return monthMenu;
  }

  private Component buildSearchLayout() {
    searchPanel = new Panel();
    searchPanel.setVisible(false);
    searchLayout = new HorizontalLayout();
    searchLayout.setMargin(true);
    searchLayout.setWidthUndefined();
    searchPanel.setContent(searchLayout);

    // categoryCombo
    categoryCombo = new ComboBox<>();
    categoryCombo.setCaption(Msg.get("expense.category"));
    searchLayout.addComponent(categoryCombo);

    // userCombo
    userCombo = new ComboBox<>();
    userCombo.setCaption(Msg.get("expense.user"));
    searchLayout.addComponent(userCombo);

    // formulaField
    formulaField = new TextField();
    formulaField.setCaption(Msg.get("expense.formula"));
    searchLayout.addComponent(formulaField);

    // commentCombo
    commentCombo = new ComboBox<>();
    commentCombo.setCaption(Msg.get("expense.comment"));
    searchLayout.addComponent(commentCombo);

    return searchPanel;
  }
}
