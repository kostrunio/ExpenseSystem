package pl.kostro.expensesystem.ui.views.table;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import pl.kostro.expensesystem.ui.components.form.expense.ExpenseForm;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class TableDesign extends VerticalLayout {
  protected Panel actionPanel = new Panel();
  protected HorizontalLayout actionsLayout = new HorizontalLayout();
  protected DateField fromDateField = new DateField();
  protected DateField toDateField = new DateField();
  protected ComboBox<CategoryEntity> categoryBox = new ComboBox<>();
  protected ComboBox<UserLimitEntity> userBox = new ComboBox<>();
  protected TextField formulaField = new TextField();
  protected ComboBox<java.lang.String> commentBox = new ComboBox<>();
  protected Button newExpenseButton = new Button();
  protected HorizontalLayout workingLayout = new HorizontalLayout();
  protected Grid<ExpenseEntity> expenseGrid = new Grid<>();
  protected ExpenseForm expenseForm = new ExpenseForm();
  protected Button exportButton = new Button();

  public TableDesign() {
    setSizeFull();
    setMargin(false);
    addComponents(createActionPanel(), newExpenseButton, createWorkingLayout(), exportButton);
    setComponentAlignment(newExpenseButton, Alignment.TOP_RIGHT);
    setComponentAlignment(exportButton, Alignment.TOP_RIGHT);
  }

  private Component createActionPanel() {
    actionPanel.setSizeUndefined();
    actionsLayout.setSizeFull();
    actionsLayout.setMargin(true);
    fromDateField.setWidth("144px");
    toDateField.setWidth("144px");
    categoryBox.setWidth("144px");
    userBox.setWidth("144px");
    actionsLayout.addComponents(fromDateField, toDateField, categoryBox, userBox, formulaField, commentBox);
    actionPanel.setContent(actionsLayout);
    return actionPanel;
  }

  private Component createWorkingLayout() {
    workingLayout.setSizeFull();
    expenseGrid.setSizeFull();
    workingLayout.addComponents(expenseGrid, expenseForm);
    workingLayout.setExpandRatio(expenseGrid, 1);
    workingLayout.setComponentAlignment(expenseForm, Alignment.MIDDLE_RIGHT);
    return workingLayout;
  }
}

