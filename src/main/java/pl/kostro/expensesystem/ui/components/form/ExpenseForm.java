package pl.kostro.expensesystem.ui.components.form;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox.NewItemProvider;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.ui.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.ui.view.TableView;
import pl.kostro.expensesystem.ui.view.design.ExpenseFormDesign;
import pl.kostro.expensesystem.utils.calculator.Calculator;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.time.LocalDate;
import java.util.Optional;

public class ExpenseForm extends ExpenseFormDesign {
  
  private ExpenseService es;
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;
  
  private ExpenseSheetEntity expenseSheet;
  private ExpenseEntity expense;
  private TableView view;

  private Button.ClickListener saveClick = event -> {
    expense.setDate(dateField.getValue());
    expense.setCategory(categoryBox.getValue());
    expense.setUser(userBox.getValue().getUser());
    expense.setFormula(formulaField.getValue());
    if (commentBox.getValue() != null && !commentBox.getValue().isEmpty())
      expense.setComment(commentBox.getValue());
    expense.setNotify(notifyBox.getValue());
    expense.setExpenseSheet(expenseSheet);
    es.save(expense);
    if (!expenseSheet.getExpenseList().contains(expense))
      expenseSheet.getExpenseList().add(expense);
    view.refreshExpenses();
  };
  private Button.ClickListener duplicateClick = event -> {
    ExpenseEntity newExpense = new ExpenseEntity(expense.getDate(), expense.getFormula(), expense.getCategory(),
        expense.getUser(), expense.getComment(), expense.isNotify(), expense.getExpenseSheet());
    edit(newExpense);
    saveButton.setEnabled(false);
  };
  private Button.ClickListener removeClick = event -> {
    ConfirmDialog.show(getUI(), Msg.get("expensForm.removeLabel"), Msg.get("expensForm.removeQuestion"),
        Msg.get("expensForm.removeYes"), Msg.get("expensForm.removeNo"), dialog -> {
          if (dialog.isConfirmed()) {
            eshts.removeExpense(expense, expenseSheet);
            es.remove(expense);
            view.refreshExpenses();
          }
    });
  };
  private HasValue.ValueChangeListener verifyFormula = event -> verifyFormula(formulaField.getValue());
  private NewItemProvider<String> addComment = event -> Optional.of(event);

  public ExpenseForm() {
    es = AppCtxProvider.getBean(ExpenseService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
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

  private void configureComponents() {
    dateField.setDateFormat("yyyy-MM-dd");
    dateField.addValueChangeListener(verifyFormula);

    categoryBox.setItemCaptionGenerator(item -> item.getName());
    categoryBox.setEmptySelectionAllowed(false);
    categoryBox.addValueChangeListener(verifyFormula);

    userBox.setItemCaptionGenerator(item -> item.getUser().getName());
    userBox.setEmptySelectionAllowed(false);
    userBox.addValueChangeListener(verifyFormula);

    formulaField.focus();
    formulaField.addValueChangeListener(verifyFormula);

    commentBox.setNewItemProvider(addComment);
    commentBox.setEmptySelectionAllowed(true);
    commentBox.addValueChangeListener(verifyFormula);

    notifyBox.addValueChangeListener(verifyFormula);

    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(saveClick);

    duplicateButton.addClickListener(duplicateClick);

    removeButton.addClickListener(removeClick);

    setVisible(false);
  }

  public void prepare(ExpenseSheetEntity expenseSheet, TableView view) {
    this.expenseSheet = expenseSheet;
    this.view = view;
    categoryBox.setItems(expenseSheet.getCategoryList());
    userBox.setItems(expenseSheet.getUserLimitList());
    commentBox.setItems(eshts.getAllComments(expenseSheet));
  }

  private void verifyFormula(Object formula) {
    if (formula != null && !formula.toString().equals("") && Calculator.verifyAllowed(formula.toString()))
      saveButton.setEnabled(true);
    else
      saveButton.setEnabled(false);
  }

  public void edit(ExpenseEntity expense) {
    this.expense = expense;
    if (expense.getDate() != null)
      dateField.setValue(expense.getDate());
    else
      dateField.setValue(LocalDate.now());
    if (expense.getCategory() != null)
      categoryBox.setValue(expense.getCategory());
    else
      categoryBox.setValue(expenseSheet.getCategoryList().get(0));
    if (expense.getUser() != null)
      userBox.setValue(eshts.getUserLimitForUser(expense.getUser(), expenseSheet));
    else
      userBox.setValue(expenseSheet.getUserLimitList().get(0));
    formulaField.focus();
    if (expense.getFormula() != null)
      formulaField.setValue(expense.getFormula());
    else
      formulaField.setValue("");
    if (expense.getComment() != null)
      commentBox.setValue(expense.getComment());
    else
      commentBox.setValue("");
    if (expense.isNotify() || (expense.getDate() != null && expense.getDate().isAfter(LocalDate.now()))) {
      notifyBox.setValue(expense.isNotify());
      notifyBox.setVisible(true);
    }
    if (expense.getId() != null) {
      if (expense.getId() == 0) {
        duplicateButton.setEnabled(false);
        removeButton.setEnabled(false);
      } else
        duplicateButton.setEnabled(true);
      removeButton.setEnabled(true);
    } else {
      duplicateButton.setEnabled(false);
      removeButton.setEnabled(false);
    }
    setVisible(expense != null);
    saveButton.setEnabled(false);
  }

}
