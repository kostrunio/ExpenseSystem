package pl.kostro.expensesystem.components.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.CategorySum;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;

@SuppressWarnings("serial")
public class CategorySumGrid extends Grid<CategorySum> {
  
  private ExpenseSheetService eshs;
  
  private ExpenseSheet expenseSheet;

  public CategorySumGrid() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    if (expenseSheet.getCategoryList().size() > 0)
      setHeightByRows(expenseSheet.getCategoryList().size());
    setSelectionMode(SelectionMode.NONE);
    addColumn(item -> item.getCategory().getName()).setCaption(Msg.get("categoryTable.category"));
    addColumn(CategorySum::getSum).setCaption(Msg.get("categoryTable.sum"));
  }
  
  public void fulfill() {
    List<CategorySum> values = new ArrayList<>();
    for (Category category : expenseSheet.getCategoryList()) {
      CategoryExpense categoryExpense = eshs.getCategoryExpenseMap(expenseSheet, category);
      values.add(new CategorySum(category, categoryExpense != null ? categoryExpense.getSum() : new BigDecimal(0)));
    }
    setItems(values);
  }
}
