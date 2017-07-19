package pl.kostro.expensesystem.components.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.UserLimitSumLeft;

@SuppressWarnings("serial")
public class UserLimitSumLeftGrid extends Grid<UserLimitSumLeft> {
  
  private UserSummaryService uss;
  private ExpenseSheet expenseSheet;

  public UserLimitSumLeftGrid() {
    uss = AppCtxProvider.getBean(UserSummaryService.class);
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);;
//    setPageLength(expenseSheet.getUserLimitList().size());
    addColumn(UserLimitSumLeft::getUserLimit).setCaption(Msg.get("userLimitTable.user"));
    addColumn(UserLimitSumLeft::getSum).setCaption(Msg.get("userLimitTable.sum"));
    addColumn(UserLimitSumLeft::getLeft).setCaption(Msg.get("userLimitTable.left"));
/*    setCellStyleGenerator(new Table.CellStyleGenerator() {
      @Override
      public String getStyle(Table source, Object itemId, Object propertyId) {
        int rows = source.getVisibleItemIds().size();
        int row = ((Integer) itemId).intValue();
        return "" + row % rows;
      }
    });*/
  }
  
  public void fulfill(Calendar calendar) {
    List<UserLimitSumLeft> values = new ArrayList<>();
    for (UserLimit userLimit : expenseSheet.getUserLimitList()) {
      BigDecimal actSum;
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        actSum = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      else
        actSum = new BigDecimal(0);
      values.add(new UserLimitSumLeft(userLimit, actSum, userLimit.isContinuousSummary() ? uss.calculateSum(userLimit, calendar.getTime()) : userLimit.getLimit().subtract(actSum)));
    }
    setItems(values);
  }
}
