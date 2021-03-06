package pl.kostro.expensesystem.ui.components.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.transform.model.CategorySum;
import pl.kostro.expensesystem.utils.transform.model.CategoryExpense;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

public class CategorySumGrid extends Grid<CategorySum> {
  
  private ExpenseSheetTransformService eshts;
  
  private ExpenseSheetEntity expenseSheet;

  public CategorySumGrid() {
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    if (expenseSheet.getCategoryList().size() > 0)
      setHeightByRows(expenseSheet.getCategoryList().size());
    setSelectionMode(SelectionMode.NONE);
    addColumn(item -> item.getCategory().getName()).setCaption(Msg.get("categoryTable.category"));
    addColumn(CategorySum::getSum).setCaption(Msg.get("categoryTable.sum"));
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
