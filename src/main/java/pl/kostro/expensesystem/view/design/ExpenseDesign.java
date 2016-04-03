package pl.kostro.expensesystem.view.design;

import com.vaadin.server.FontAwesome;
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

@SuppressWarnings("serial")
public class ExpenseDesign extends Panel {
  protected VerticalLayout root;
  
  protected HorizontalLayout header;
  protected Label titleLabel;
  protected HorizontalLayout tools;
  protected Button editButton;
  protected Button chartButton;
  
  protected VerticalLayout expensePanels;
  protected HorizontalLayout menuLayout;
  protected MenuBar yearMenu;
  protected Button filterButton;
  protected Button tableButton;
  
  protected MenuBar monthMenu;

  protected Panel searchPanel;
  protected HorizontalLayout searchLayout;
  protected ComboBox categoryCombo;
  protected ComboBox userCombo;
  protected TextField formulaField;
  protected ComboBox commentCombo;
  protected Button searchButton;

  protected VerticalLayout mainView;

  public ExpenseDesign() {
    addStyleName(ValoTheme.PANEL_BORDERLESS);
    setSizeFull();
    root = new VerticalLayout();
    root.setSizeUndefined();
    root.setMargin(true);
    setContent(root);
  }

  protected Component buildHeader(String name) {
    header = new HorizontalLayout();
    header.setSpacing(true);

    titleLabel = new Label(name);
    titleLabel.setSizeUndefined();
    titleLabel.addStyleName(ValoTheme.LABEL_H1);
    titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
    header.addComponent(titleLabel);

    tools = new HorizontalLayout(buildEditButton(), buildChartButton());
    tools.setSpacing(true);
    header.addComponent(tools);

    return header;
  }

  private Component buildEditButton() {
    editButton = new Button();
    editButton.setIcon(FontAwesome.EDIT);
    editButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    editButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    return editButton;
  }
  
  private Component buildChartButton() {
    chartButton = new Button();
    chartButton.setIcon(FontAwesome.LINE_CHART);
    chartButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    chartButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    return chartButton;
  }
  
  protected Component buildContent() {
    expensePanels = new VerticalLayout();
    expensePanels.setSpacing(true);

    expensePanels.addComponent(buildYearMenu());
    expensePanels.addComponent(buildMonthMenu());
    expensePanels.addComponent(buildSearchLayout());

    mainView = new VerticalLayout();
    expensePanels.addComponent(mainView);

    return expensePanels;
  }
  
  private Component buildYearMenu() {
    menuLayout = new HorizontalLayout();
    menuLayout.setImmediate(false);
    menuLayout.setSpacing(true);

    // yearMenu
    yearMenu = new MenuBar();
    yearMenu.setImmediate(false);

    menuLayout.addComponent(yearMenu);

    // filterButton
    filterButton = new Button();
    filterButton.setIcon(FontAwesome.FILTER);
    filterButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    filterButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    filterButton.setCaption(Msg.get("expense.filter"));
    filterButton.setImmediate(true);

    menuLayout.addComponent(filterButton);
    menuLayout.setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);

    // findButton
    tableButton = new Button();
    tableButton.setIcon(FontAwesome.TABLE);
    tableButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    tableButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    tableButton.setCaption(Msg.get("expense.find"));
    tableButton.setImmediate(true);

    menuLayout.addComponent(tableButton);
    menuLayout.setComponentAlignment(tableButton, Alignment.MIDDLE_RIGHT);

    return menuLayout;
  }

  private Component buildMonthMenu() {
    monthMenu = new MenuBar();
    monthMenu.setImmediate(false);
    monthMenu.setSizeUndefined();
    return monthMenu;
  }

  private Component buildSearchLayout() {
    searchPanel = new Panel();
    searchPanel.setVisible(false);
    searchLayout = new HorizontalLayout();
    searchLayout.setWidthUndefined();
    searchLayout.setMargin(true);
    searchLayout.setSpacing(true);
    searchPanel.setContent(searchLayout);

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
    searchButton = new Button();
    searchButton.setCaption(Msg.get("expense.search"));

    searchLayout.addComponent(searchButton);
    searchLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);

    return searchPanel;
  }
}
