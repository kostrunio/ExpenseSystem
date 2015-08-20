package pl.kostro.expensesystem.components.table;

import java.math.BigDecimal;
import java.util.Calendar;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.UserSummaryService;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class UserLimitTable extends Table {

  private static final long serialVersionUID = -8885139846860909089L;
  
  private ExpenseSheet expenseSheet;

  public UserLimitTable(ExpenseSheet expenseSheet) {
    this.expenseSheet = expenseSheet;
    setPageLength(expenseSheet.getUserLimitList().size());
    addContainerProperty("U¿ytkownik", String.class, null);
    addContainerProperty("Suma", BigDecimal.class, null);
    addContainerProperty("Zosta³o", BigDecimal.class, null);
    setCellStyleGenerator(new Table.CellStyleGenerator() {

      private static final long serialVersionUID = 446697675167120127L;

      @Override
      public String getStyle(Table source, Object itemId, Object propertyId) {
        int rows = source.getVisibleItemIds().size();
        int row = ((Integer) itemId).intValue();
        return "" + row % rows;
      }
    });
  }
  
  @SuppressWarnings("unchecked")
  public void fulfill(Calendar calendar) {
    removeAllItems();
    for (UserLimit userLimit : expenseSheet.getUserLimitList()) {
      BigDecimal actSum;
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        actSum = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      else
        actSum = new BigDecimal(0);
      Object newItemId = addItem();
      Item row = getItem(newItemId);
      row.getItemProperty("U¿ytkownik").setValue(userLimit.getUser().getName());
      row.getItemProperty("Suma").setValue(actSum);
      if (userLimit.isContinuousSummary()) {
        row.getItemProperty("Zosta³o").setValue(UserSummaryService.calculateSum(userLimit, calendar.getTime()));
      } else {
        row.getItemProperty("Zosta³o").setValue(userLimit.getLimit().subtract(actSum));
      }
    }
  }
}
