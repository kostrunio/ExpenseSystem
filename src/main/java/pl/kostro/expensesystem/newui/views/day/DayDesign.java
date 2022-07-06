package pl.kostro.expensesystem.newui.views.day;

import com.vaadin.componentfactory.gridlayout.GridLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.newui.components.button.Button;

public class DayDesign extends VerticalLayout {
  protected HorizontalLayout navigationLayout = new HorizontalLayout();
  protected Button previousDayButton = new Button();
  protected DatePicker thisDateField = new DatePicker();
  protected Button nextDayButton = new Button();
  protected FlexLayout dayLayout = new FlexLayout();
  protected FlexLayout categoryListPanel = new FlexLayout();
  protected FlexLayout categoryGrid = new FlexLayout();
  protected Button backButton = new Button();
  protected Div expenseListPanel = new Div();
  protected VerticalLayout expenseList = new VerticalLayout();
  protected Label categoryLabel = new Label();
  protected GridLayout expenseGrid = new GridLayout(1,1);
  protected HorizontalLayout addExpense = new HorizontalLayout();
  protected ComboBox<UserLimitEntity> userBox = new ComboBox<>();
  protected TextField formulaField = new TextField();
  protected ComboBox<String> commentBox = new ComboBox<>();
  protected Checkbox notifyBox = new Checkbox("notify me");
  protected Button saveButton = new Button();

  public DayDesign() {
    setSizeFull();
    setSpacing(false);
    setMargin(false);
    add(createNavigationLayout(), createDayLayout());
    setAlignItems(Alignment.CENTER);
  }

  private Component createNavigationLayout() {
    navigationLayout.setMargin(true);
    previousDayButton.setIcon(VaadinIcon.ARROW_LEFT.create());
    previousDayButton.addThemeVariants(ButtonVariant.LUMO_ICON);
    nextDayButton.setIcon(VaadinIcon.ARROW_RIGHT.create());
    nextDayButton.addThemeVariants(ButtonVariant.LUMO_ICON);
    navigationLayout.add(previousDayButton, thisDateField, nextDayButton);
    navigationLayout.setAlignItems(Alignment.END);
//    navigationLayout.setComponentAlignment(thisDateField, Alignment.MIDDLE_CENTER);
    return navigationLayout;
  }

  private Component createDayLayout() {
    dayLayout.setClassName("day-layout");
    dayLayout.setWidth("100%");
    dayLayout.add(createCategoryListPanel(), createExpenseListPanel());
    return dayLayout;
  }

  private Component createCategoryListPanel() {
    categoryListPanel.setWidth("50%");
    categoryListPanel.setClassName("categorylist-panel");
    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(false);
    categoryGrid.setWidth("100%");
    categoryGrid.setClassName("category-grid");
    categoryGrid.getStyle().set("flex-wrap", "wrap");
    layout.add(categoryGrid, backButton);
    categoryListPanel.add(layout);
    return categoryListPanel;
  }

  private Component createExpenseListPanel() {
    expenseListPanel.setWidth("50%");
    expenseListPanel.setClassName("expenselist-panel");
    notifyBox.setVisible(false);
    saveButton.setClassName("friendly");
    expenseList.setClassName("expense-list");
    expenseList.setSpacing(false);
    expenseList.setMargin(false);
    categoryLabel.setClassName("category-label");
    expenseGrid.setClassName("expense-grid");
    expenseGrid.setSpacing(true);
    expenseGrid.setMargin(true);
    expenseGrid.setWidth("100%");
    expenseList.add(categoryLabel, expenseGrid);
    expenseList.setAlignItems(Alignment.CENTER);
    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(false);
    layout.add(createAddExpense(), notifyBox, saveButton);
    expenseListPanel.add(expenseList, layout);
    return expenseListPanel;
  }

  private Component createAddExpense() {
    addExpense.setClassName("add-expense");
    userBox.setClassName("add-expense-field");
    userBox.setWidth("100%");
    formulaField.setClassName("add-expense-field");
    formulaField.setWidth("100%");
    commentBox.setClassName("add-expense-field");
    commentBox.setWidth("100%");
    addExpense.add(userBox, formulaField, commentBox);
    return addExpense;
  }
}
