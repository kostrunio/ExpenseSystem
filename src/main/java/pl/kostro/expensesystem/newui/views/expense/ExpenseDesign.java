package pl.kostro.expensesystem.newui.views.expense;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.utils.msg.Msg;

public class ExpenseDesign extends Div {
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

  protected Div searchPanel;
  protected HorizontalLayout searchLayout;
  protected ComboBox<CategoryEntity> categoryCombo;
  protected ComboBox<UserLimitEntity> userCombo;
  protected TextField formulaField;
  protected ComboBox<String> commentCombo;

  protected VerticalLayout mainView;

  public ExpenseDesign() {
//    setClassName(ValoTheme.PANEL_BORDERLESS);
    setSizeFull();
    root = new VerticalLayout();
    root.setSizeUndefined();
    root.setSpacing(false);
    add(root);
  }

  protected Component buildHeader(String name) {
    header = new HorizontalLayout();
    header.setMargin(false);

    titleLabel = new Label(name);
    titleLabel.setSizeUndefined();
//    titleLabel.addClassName(ValoTheme.LABEL_H1);
//    titleLabel.addClassName(ValoTheme.LABEL_NO_MARGIN);
    header.add(titleLabel);

    tools = new HorizontalLayout(buildEditButton(), buildChartButton());
    tools.setMargin(false);
    header.add(tools);

    return header;
  }

  private Component buildEditButton() {
    editButton = new Button();
    editButton.setIcon(VaadinIcon.EDIT.create());
    editButton.addThemeVariants(ButtonVariant.LUMO_ICON);
    return editButton;
  }
  
  private Component buildChartButton() {
    chartButton = new Button();
    chartButton.setIcon(VaadinIcon.LINE_CHART.create());
    chartButton.addThemeVariants(ButtonVariant.LUMO_ICON);
    return chartButton;
  }
  
  protected Component buildContent() {
    expenseLayout = new VerticalLayout();
    expenseLayout.setMargin(false);
    expenseLayout.add(buildMenus());

    mainView = new VerticalLayout();
    mainView.setMargin(false);
    mainView.setSpacing(false);
    expenseLayout.add(mainView);

    return expenseLayout;
  }
  
  private Component buildMenus() {
    menusLayout = new VerticalLayout();
    menusLayout.addClassName("menus-layout");
    menusLayout.setMargin(false);
    menusLayout.add(buildYearMenu());
    menusLayout.add(buildMonthMenu());
    menusLayout.add(buildSearchLayout());
    return menusLayout;
  }
  
  private Component buildYearMenu() {
    menuLayout = new HorizontalLayout();
    menuLayout.setMargin(false);

    // yearMenu
    yearMenu = new MenuBar();
    menuLayout.add(yearMenu);

    // filterButton
    filterButton = new Button();
    filterButton.setIcon(VaadinIcon.FILTER.create());
    filterButton.addThemeVariants(ButtonVariant.LUMO_ICON);
//    filterButton.setText(Msg.get("expense.filter"));

    menuLayout.add(filterButton);
    menuLayout.setAlignItems(FlexComponent.Alignment.END);

    // findButton
    tableButton = new Button();
    tableButton.setIcon(VaadinIcon.TABLE.create());
    tableButton.addThemeVariants(ButtonVariant.LUMO_ICON);
//    tableButton.setText(Msg.get("expense.find"));

    menuLayout.add(tableButton);
    menuLayout.setAlignItems(FlexComponent.Alignment.END);

    // userSummaryBurron
    userSummaryButton = new Button();
    userSummaryButton.setIcon(VaadinIcon.FILE_TREE_SMALL.create());
    userSummaryButton.addThemeVariants(ButtonVariant.LUMO_ICON);
//    userSummaryButton.setText(Msg.get("expense.userSummary"));

    menuLayout.add(userSummaryButton);
    menuLayout.setAlignItems(FlexComponent.Alignment.END);

    return menuLayout;
  }

  private Component buildMonthMenu() {
    monthMenu = new MenuBar();
    return monthMenu;
  }

  private Component buildSearchLayout() {
    searchPanel = new Div();
    searchPanel.setVisible(false);
    searchLayout = new HorizontalLayout();
    searchLayout.setMargin(true);
//    searchLayout.setWidthUndefined();
    searchPanel.add(searchLayout);

    // categoryCombo
    categoryCombo = new ComboBox<>();
    categoryCombo.setLabel(Msg.get("expense.category"));
    searchLayout.add(categoryCombo);

    // userCombo
    userCombo = new ComboBox<>();
    userCombo.setLabel(Msg.get("expense.user"));
    searchLayout.add(userCombo);

    // formulaField
    formulaField = new TextField();
    formulaField.setLabel(Msg.get("expense.formula"));
    searchLayout.add(formulaField);

    // commentCombo
    commentCombo = new ComboBox<>();
    commentCombo.setLabel(Msg.get("expense.comment"));
    searchLayout.add(commentCombo);

    return searchPanel;
  }
}
