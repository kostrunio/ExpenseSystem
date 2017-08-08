package pl.kostro.expensesystem.components.form;

import java.time.LocalDate;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox.NewItemHandler;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Calculator;
import pl.kostro.expensesystem.view.TableView;
import pl.kostro.expensesystem.view.design.ExpenseFormDesign;

@SuppressWarnings("serial")
public class ExpenseForm extends ExpenseFormDesign {
  
  private ExpenseService es;
  private ExpenseSheetService eshs;
  
  private ExpenseSheet expenseSheet;
  private Expense expense;
  private TableView view;

  private Button.ClickListener saveClick = event -> {
    expense.setDate(dateField.getValue());
    expense.setCategory((Category) categoryBox.getValue());
    expense.setUser(((UserLimit) userBox.getValue()).getUser());
    expense.setFormula(formulaField.getValue());
    if (commentBox.getValue() != null && !commentBox.getValue().toString().isEmpty())
      expense.setComment(commentBox.getValue().toString());
    expense.setNotify(notifyBox.getValue());
    expense.setExpenseSheet(expenseSheet);
    expense = es.merge(expense);
    if (!expenseSheet.getExpenseList().contains(expense))
      expenseSheet.getExpenseList().add(expense);
    view.refreshExpenses();
  };
  private Button.ClickListener duplicateClick = event -> {
    Expense newExpense = new Expense(expense.getDate(), expense.getFormula(), expense.getCategory(),
        expense.getUser(), expense.getComment(), expense.isNotify(), expense.getExpenseSheet());
    edit(newExpense);
    saveButton.setEnabled(false);
  };
  private Button.ClickListener removeClick = event -> {
    ConfirmDialog.show(getUI(), Msg.get("expensForm.removeLabel"), Msg.get("expensForm.removeQuestion"),
        Msg.get("expensForm.removeYes"), Msg.get("expensForm.removeNo"), dialog -> {
          if (dialog.isConfirmed()) {
            es.removeExpense(expenseSheet, expense);
            view.refreshExpenses();
          }
    });
  };
  @SuppressWarnings("rawtypes")
  private HasValue.ValueChangeListener verifyFormula = event -> verifyFormula(formulaField.getValue());
  private NewItemHandler addNewComment = newItem -> {};

  public ExpenseForm() {
    es = AppCtxProvider.getBean(ExpenseService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    setCaption();
    configureComponents();
  }

  private void setCaption() {
    saveButton.setCaption(Msg.get("expensForm.save"));
    duplicateButton.setCaption(Msg.get("expensForm.duplicate"));
    dateField.setCaption(Msg.get("expensForm.date"));
    categoryBox.setCaption(Msg.get("expensForm.category"));
    userBox.setCaption(Msg.get("expensForm.user"));
    formulaField.setCaption(Msg.get("expensForm.formula"));
    commentBox.setCaption(Msg.get("expensForm.comment"));
    notifyBox.setCaption(Msg.get("expensForm.notify"));
  }

  @SuppressWarnings("unchecked")
  private void configureComponents() {
    dateField.setDateFormat("yyyy-MM-dd");
    dateField.addValueChangeListener(verifyFormula);

    categoryBox.setEmptySelectionAllowed(false);
    categoryBox.addValueChangeListener(verifyFormula);

    userBox.setEmptySelectionAllowed(false);
    userBox.addValueChangeListener(verifyFormula);

    formulaField.focus();
    formulaField.addValueChangeListener(verifyFormula);

    commentBox.setNewItemHandler(addNewComment);
    commentBox.setEmptySelectionAllowed(true);
    commentBox.addValueChangeListener(verifyFormula);

    notifyBox.addValueChangeListener(verifyFormula);

    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(saveClick);

    duplicateButton.addClickListener(duplicateClick);

    removeButton.addClickListener(removeClick);

    setVisible(false);
  }

  public void prepare(ExpenseSheet expenseSheet, TableView view) {
    this.expenseSheet = expenseSheet;
    this.view = view;
    categoryBox.setItems(expenseSheet.getCategoryList());
    userBox.setItems(expenseSheet.getUserLimitList());
    commentBox.setItems(eshs.getAllComments(expenseSheet));
  }

  private void verifyFormula(Object formula) {
    if (formula != null && !formula.toString().equals("") && Calculator.verifyAllowed(formula.toString()))
      saveButton.setEnabled(true);
    else
      saveButton.setEnabled(false);
  }

  public void edit(Expense expense) {
    this.expense = expense;
    if (expense.getId() != null) {
      dateField.setValue(expense.getDate());
      categoryBox.setValue(expense.getCategory());
      userBox.setValue(eshs.getUserLimitForUser(expenseSheet, expense.getUser()));
      formulaField.focus();
      formulaField.setValue(expense.getFormula());
      commentBox.setValue(expense.getComment());
      if (expense.isNotify() || expense.getDate().isAfter(LocalDate.now())) {
        notifyBox.setValue(expense.isNotify());
        notifyBox.setVisible(true);
      }
      if (expense.getId() == 0) {
        duplicateButton.setEnabled(false);
        removeButton.setEnabled(false);
      } else
        duplicateButton.setEnabled(true);
    } else {
      if (expense.getUser() != null)
        userBox.setValue(eshs.getUserLimitForUser(expenseSheet, expense.getUser()));
      else
        userBox.setValue(expenseSheet.getUserLimitList().get(0));
      duplicateButton.setEnabled(false);
    }
    setVisible(expense != null);
    saveButton.setEnabled(false);
  }

}
