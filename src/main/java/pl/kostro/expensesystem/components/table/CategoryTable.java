package pl.kostro.expensesystem.components.table;

import java.math.BigDecimal;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.CategoryExpense;

import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class CategoryTable extends Table {
  private ExpenseSheet expenseSheet;

  public CategoryTable() {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);;
    setPageLength(expenseSheet.getCategoryList().size());
    addContainerProperty(Msg.get("categoryTable.category"), String.class, null);
    addContainerProperty(Msg.get("categoryTable.sum"), BigDecimal.class, null);
    alwaysRecalculateColumnWidths = true;
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
