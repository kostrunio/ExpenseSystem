package pl.kostro.expensesystem.components.table;

import java.math.BigDecimal;
import java.util.Calendar;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.UserSummaryService;

import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class UserLimitTable extends Table {
  private ExpenseSheet expenseSheet;

  public UserLimitTable() {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);;
    setPageLength(expenseSheet.getUserLimitList().size());
    addContainerProperty(Msg.get("userLimitTable.user"), String.class, null);
    addContainerProperty(Msg.get("userLimitTable.sum"), BigDecimal.class, null);
    addContainerProperty(Msg.get("userLimitTable.left"), BigDecimal.class, null);
    setCellStyleGenerator(new Table.CellStyleGenerator() {
      @Override
      public String getStyle(Table source, Object itemId, Object propertyId) {
        int rows = source.getVisibleItemIds().size();
        int row = ((Integer) itemId).intValue();
        return "" + row % rows;
      }
    });
    alwaysRecalculateColumnWidths = true;
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
      row.getItemProperty(Msg.get("userLimitTable.user")).setValue(userLimit.getUser().getName());
      row.getItemProperty(Msg.get("userLimitTable.sum")).setValue(actSum);
      if (userLimit.isContinuousSummary()) {
        row.getItemProperty(Msg.get("userLimitTable.left")).setValue(UserSummaryService.calculateSum(userLimit, calendar.getTime()));
      } else {
        row.getItemProperty(Msg.get("userLimitTable.left")).setValue(userLimit.getLimit().subtract(actSum));
      }
    }
  }
}
