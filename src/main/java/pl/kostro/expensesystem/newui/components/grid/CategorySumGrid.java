package pl.kostro.expensesystem.newui.components.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.server.VaadinSession;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.model.CategoryExpense;
import pl.kostro.expensesystem.utils.transform.model.CategorySum;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CategorySumGrid extends Grid<CategorySum> {
  
  private ExpenseSheetTransformService eshts;
  
  private ExpenseSheetEntity expenseSheet;

  public CategorySumGrid() {
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    if (expenseSheet.getCategoryList().size() > 0)
      setAllRowsVisible(true);
    setSelectionMode(SelectionMode.NONE);
    addColumn(item -> item.getCategory().getName()).setHeader(Msg.get("categoryTable.category"));
    addColumn(CategorySum::getSum).setHeader(Msg.get("categoryTable.sum"));
  }
  
  public void fulfill() {
    List<CategorySum> values = new ArrayList<>();
    for (CategoryEntity category : expenseSheet.getCategoryList()) {
      CategoryExpense categoryExpense = eshts.getCategoryExpenseMap(expenseSheet, category);
      values.add(new CategorySum(category, categoryExpense != null ? categoryExpense.getSum() : new BigDecimal(0)));
    }
    setItems(values);
  }
}
