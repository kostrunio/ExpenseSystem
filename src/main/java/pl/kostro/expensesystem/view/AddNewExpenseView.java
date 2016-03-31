package pl.kostro.expensesystem.view;

import java.util.Calendar;

import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.ExpenseService;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Calculator;
import pl.kostro.expensesystem.view.design.AddNewExpenseDesign;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class AddNewExpenseView extends AddNewExpenseDesign {

  private DayView dayView;
  private ExpenseSheet expenseSheet;
  private Expense expense;
  private Boolean modify;
  private Calendar calendar;
  private Property.ValueChangeListener valueChange = new Property.ValueChangeListener() {
    @Override
    public void valueChange(ValueChangeEvent event) {
      verifyFormula(formulaField.getValue());
    }
  };
  private Button.ClickListener saveClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      if (userBox.getValue() instanceof UserLimit) {
        ExpenseService.saveExpense(expenseSheet, expense, (UserLimit) userBox.getValue(), formulaField.getValue(),
            commentBox.getValue(), modify);
        dayView.refreshView(calendar, expense.getCategory());
      }
    }
  };

  public void setDayView(DayView dayView) {
    this.dayView = dayView;
  }

  public void buildAddNewExpense(Expense expense, Boolean modify) {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.expense = expense;
    this.modify = modify;
    this.calendar = VaadinSession.getCurrent().getAttribute(Calendar.class);
    userBox.addItems(expenseSheet.getUserLimitList());
    if (expense.getUser() != null)
      userBox.select(ExpenseSheetService.getUserLimitForUser(expenseSheet, expense.getUser()));
    else
      userBox.select(expenseSheet.getUserLimitList().get(0));
    userBox.addValueChangeListener(valueChange);

    formulaField.setValue(expense.getFormula());
    formulaField.addValueChangeListener(valueChange);

    commentBox.addItems(ExpenseSheetService.getCommentForCategory(expenseSheet, expense.getCategory()));
    commentBox.select(expense.getComment());
    commentBox.addValueChangeListener(valueChange);

    saveButton.addClickListener(saveClick);
  }

  private void verifyFormula(Object formula) {
    if (formula != null && !formula.toString().equals("") && Calculator.verifyAllowed(formula.toString()))
      saveButton.setEnabled(true);
    else
      saveButton.setEnabled(false);
  }

}
