package pl.kostro.expensesystem.newui.views.table;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.newui.components.form.expense.ExpenseForm;

public class TableDesign extends VerticalLayout {
  protected Div actionPanel = new Div();
  protected HorizontalLayout actionsLayout = new HorizontalLayout();
  protected DatePicker fromDateField = new DatePicker();
  protected DatePicker toDateField = new DatePicker();
  protected ComboBox<CategoryEntity> categoryBox = new ComboBox<>();
  protected ComboBox<UserLimitEntity> userBox = new ComboBox<>();
  protected TextField formulaField = new TextField();
  protected ComboBox<String> commentBox = new ComboBox<>();
  protected Button newExpenseButton = new Button();
  protected HorizontalLayout workingLayout = new HorizontalLayout();
  protected Grid<ExpenseEntity> expenseGrid = new Grid<>();
  protected ExpenseForm expenseForm = new ExpenseForm();
  protected Button exportButton = new Button();

  public TableDesign() {
    setSizeFull();
    setMargin(false);
    add(createActionPanel(), newExpenseButton, createWorkingLayout(), exportButton);
    expand(workingLayout);
    setAlignItems(Alignment.START);
//    setComponentAlignment(exportButton, Alignment.TOP_RIGHT);
  }

  private Component createActionPanel() {
//    actionPanel.setSizeUndefined();
    actionsLayout.setSizeFull();
    actionsLayout.setMargin(true);
    fromDateField.setWidth("144px");
    toDateField.setWidth("144px");
    categoryBox.setWidth("144px");
    userBox.setWidth("144px");
    actionsLayout.add(fromDateField, toDateField, categoryBox, userBox, formulaField, commentBox);
    actionPanel.add(actionsLayout);
    return actionPanel;
  }

  private Component createWorkingLayout() {
    workingLayout.setSizeFull();
    expenseGrid.setWidthFull();
    workingLayout.add(expenseGrid, expenseForm);
    workingLayout.expand(expenseGrid);
    workingLayout.setAlignItems(Alignment.END);
    return workingLayout;
  }
}

