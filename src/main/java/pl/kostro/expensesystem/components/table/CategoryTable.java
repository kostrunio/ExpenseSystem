package pl.kostro.expensesystem.components.table;

import java.math.BigDecimal;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.CategoryExpense;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class CategoryTable extends Table {

  private static final long serialVersionUID = -6138656372402163480L;
  
  private ExpenseSheet expenseSheet;

  public CategoryTable(ExpenseSheet expenseSheet) {
    this.expenseSheet = expenseSheet;
    setPageLength(expenseSheet.getCategoryList().size());
    addContainerProperty(Msg.get("categoryTable.category"), String.class, null);
    addContainerProperty(Msg.get("categoryTable.sum"), BigDecimal.class, null);
  }
  
  @SuppressWarnings("unchecked")
  public void fulfill() {
    removeAllItems();
    for (Category category : expenseSheet.getCategoryList()) {
      CategoryExpense categoryExpense = ExpenseSheetService.getCategoryExpenseMap(expenseSheet, category);
      Object newItemId = addItem();
      Item row = getItem(newItemId);
      row.getItemProperty(Msg.get("categoryTable.category")).setValue(category.getName());
      row.getItemProperty(Msg.get("categoryTable.sum")).setValue(categoryExpense != null ? categoryExpense.getSum() : new BigDecimal(0));
    }
  }
}