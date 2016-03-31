package pl.kostro.expensesystem.components.form;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.ExpenseService;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Calculator;
import pl.kostro.expensesystem.view.TableView;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ExpenseForm extends FormLayout {
  Button saveButton = new Button(Msg.get("expensForm.save"));
  Button duplicateButton = new Button(Msg.get("expensForm.duplicate"));
  DateField dateField = new DateField(Msg.get("expensForm.date"));
  ComboBox categoryBox = new ComboBox(Msg.get("expensForm.category"));
  ComboBox userBox = new ComboBox(Msg.get("expensForm.user"));
  TextField formulaField = new TextField(Msg.get("expensForm.formula"));
  ComboBox commentBox = new ComboBox(Msg.get("expensForm.comment"));
  
  ExpenseSheet expenseSheet;
  Expense expense;
  TableView view;
  
  public ExpenseForm() {}
  
  public void prepare(ExpenseSheet expenseSheet, TableView view) {
    this.expenseSheet = expenseSheet;
    this.view = view;
    configureComponents();
    buildLayout();
  }
  

  private void configureComponents() {
    dateField.setDateFormat("yyyy-MM-dd");
    dateField.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent event) {
        verifyFormula(formulaField.getValue());
      }
    });
    
    categoryBox.setNewItemsAllowed(false);
    categoryBox.setNullSelectionAllowed(false);
    categoryBox.addItems(expenseSheet.getCategoryList());
    categoryBox.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent event) {
        verifyFormula(formulaField.getValue());
      }
    });
    
    userBox.setNewItemsAllowed(false);
    userBox.setNullSelectionAllowed(false);
    userBox.addItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent event) {
        verifyFormula(formulaField.getValue());
      }
    });
    
    formulaField.focus();
    formulaField.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent event) {
        verifyFormula(formulaField.getValue());
      }
    });

    commentBox.setNewItemsAllowed(true);
    commentBox.setNullSelectionAllowed(true);
    commentBox.addItems(ExpenseSheetService.getAllComments(expenseSheet));
    commentBox.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent event) {
        verifyFormula(formulaField.getValue());
      }
    });
    
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    saveButton.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        expense.setDate(dateField.getValue());
        expense.setCategory((Category)categoryBox.getValue());
        expense.setUser(((UserLimit)userBox.getValue()).getUser());
        expense.setFormula(formulaField.getValue());
        if (commentBox.getValue() != null && !commentBox.getValue().toString().isEmpty())
          expense.setComment(commentBox.getValue().toString());
        expense.setExpenseSheet(expenseSheet);
        expense = ExpenseService.merge(expense);
        if (!expenseSheet.getExpenseList().contains(expense))
          expenseSheet.getExpenseList().add(expense);
        view.refreshExpenses();
      }
    });
    
    duplicateButton.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        Expense newExpense = new Expense(
            expense.getDate(),
            expense.getFormula(),
            expense.getCategory(),
            expense.getUser(),
            expense.getComment(),
            expense.getExpenseSheet());
        edit(newExpense);
        saveButton.setEnabled(false);
      }
    });
    
    setVisible(false);
  }
  
  private void verifyFormula(Object formula) {
    if (formula != null
        && !formula.toString().equals("")
        && Calculator.verifyAllowed(formula.toString()))
      saveButton.setEnabled(true);
    else
      saveButton.setEnabled(false);
  }
  
  private void buildLayout() {
    setSizeUndefined();
    setMargin(true);

    HorizontalLayout actions = new HorizontalLayout(saveButton, duplicateButton);
    actions.setSpacing(true);

    addComponents(actions, dateField, categoryBox, userBox, formulaField, commentBox);
  }
  
  public void edit(Expense expense) {
    this.expense = expense;
    if (expense != null) {
      dateField.setValue(expense.getDate());
      categoryBox.setValue(expense.getCategory());
      userBox.setValue(ExpenseSheetService.getUserLimitForUser(expenseSheet, expense.getUser()));
      formulaField.setValue(expense.getFormula());
      commentBox.setValue(expense.getComment());
      formulaField.focus();
      if (expense.getId() == 0)
        duplicateButton.setEnabled(false);
      else
        duplicateButton.setEnabled(true);
    } else {
      userBox.select(expenseSheet.getUserLimitList().get(0));
      duplicateButton.setEnabled(false);
    }
    setVisible(expense != null);
    saveButton.setEnabled(false);
  }
  
}
