package pl.kostro.expensesystem.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.ExpenseService;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.DateExpense;
import pl.kostro.expensesystem.view.design.CategoryExpenseDesign;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class CategoryExpenseView extends CategoryExpenseDesign {

  private ExpenseSheet expenseSheet;
  private Calendar calendar;
  private Category category;
  private DayView dayView;

  public CategoryExpenseView(Calendar calendar, Category category, DayView dayView) {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.calendar = calendar;
    this.category = category;
    this.dayView = dayView;

    categoryLabel.setValue(category.getName());
    addNewExpense.setDayView(dayView);

    prepareExpenseGrid();
    addNewExpense.buildAddNewExpense(ExpenseService.prepareNewExpense(expenseSheet, calendar.getTime(), category,
        expenseSheet.getUserLimitList().get(0).getUser()), false);
  }

  private void prepareExpenseGrid() {
    expenseGrid.removeAllComponents();

    List<Expense> expenseList;
    DateExpense dateExpenseMap = ExpenseSheetService.getDateExpenseMap(expenseSheet, calendar.getTime());
    if (dateExpenseMap == null || dateExpenseMap.getCategoryExpenseMap().get(category) == null)
      expenseList = new ArrayList<Expense>();
    else {
      expenseList = dateExpenseMap.getCategoryExpenseMap().get(category).getExpenseList();
    }
    expenseGrid.setColumns(4);
    expenseGrid.setRows(expenseList.size() == 0 ? 1 : expenseList.size());

    for (int i = 0; i < expenseList.size(); i++) {
      Expense expense = expenseList.get(i);
      TextField user = new TextField();
      user.setEnabled(false);
      user.setValue(expense.getUser().getName());
      expenseGrid.addComponent(user, 0, i);

      Button valueButton = new Button(ExpenseService.getValueString(expense));
      valueButton.setImmediate(true);
      valueButton.setWidth("-1px");
      valueButton.setHeight("-1px");
      valueButton.setData(expense);
      expenseGrid.addComponent(valueButton, 1, i);

      valueButton.addClickListener(new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
          if (event.getButton().getData() instanceof Expense) {
            addNewExpense.buildAddNewExpense((Expense) event.getButton().getData(), true);
          }
        }
      });

      TextField comment = new TextField();
      comment.setEnabled(false);
      comment.setNullRepresentation("");
      comment.setValue(expense.getComment());
      expenseGrid.addComponent(comment, 2, i);

      Button removeButton = new Button();
      removeButton.setCaption("X");
      removeButton.setImmediate(true);
      removeButton.setWidth("-1px");
      removeButton.setHeight("-1px");
      removeButton.setData(expense);
      expenseGrid.addComponent(removeButton, 3, i);

      removeButton.addClickListener(new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
          if (event.getButton().getData() instanceof Expense) {
            final Expense expense = (Expense) event.getButton().getData();
            ConfirmDialog.show(getUI(), Msg.get("category.removeLabel"), Msg.get("category.removeQuestion"),
                Msg.get("category.removeYes"), Msg.get("category.removeNo"), new ConfirmDialog.Listener() {
                  @Override
                  public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                      ExpenseService.removeExpense(expenseSheet, expense);
                      dayView.refreshView(calendar, category);
                    }
                  }
                });
          }
        }
      });
    }
  }

}
