package pl.kostro.expensesystem.components.table;

import java.math.BigDecimal;

import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Table;

import pl.kostro.expensesystem.ApplicationContextProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;

@SuppressWarnings("serial")
public class CategoryTable extends Table {
  
  private ExpenseSheetService eshs;
  
  private ExpenseSheet expenseSheet;

  public CategoryTable() {
    eshs = ApplicationContextProvider.getBean(ExpenseSheetService.class);
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
      CategoryExpense categoryExpense = eshs.getCategoryExpenseMap(expenseSheet, category);
      Object newItemId = addItem();
      Item row = getItem(newItemId);
      row.getItemProperty(Msg.get("categoryTable.category")).setValue(category.getName());
      row.getItemProperty(Msg.get("categoryTable.sum")).setValue(categoryExpense != null ? categoryExpense.getSum() : new BigDecimal(0));
    }
  }
}
