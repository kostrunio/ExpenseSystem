package pl.kostro.expensesystem.ui.components.grid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Grid;
import com.vaadin.ui.StyleGenerator;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.expense.UserLimitSumLeft;

public class UserLimitSumLeftGrid extends Grid<UserLimitSumLeft> {
  
  private UserSummaryService uss;
  private UserLimitService uls;
  private ExpenseSheetEntity expenseSheet;

  public UserLimitSumLeftGrid() {
    uss = AppCtxProvider.getBean(UserSummaryService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);;
    setHeightByRows(expenseSheet.getUserLimitList().size());
    setSelectionMode(SelectionMode.NONE);
    addColumn(item -> item.getUserLimit().getUser().getName()).setCaption(Msg.get("userLimitTable.user"));
    addColumn(UserLimitSumLeft::getSum).setCaption(Msg.get("userLimitTable.sum"));
    addColumn(UserLimitSumLeft::getLeft).setCaption(Msg.get("userLimitTable.left"));
    setStyleGenerator((StyleGenerator<UserLimitSumLeft>) item -> {
      double rows = getHeightByRows();
      int row = item.getUserLimit().getOrder();
      return "" + row % rows;
    });
  }
  
  public void fulfill(LocalDate date) {
    List<UserLimitSumLeft> values = new ArrayList<>();
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList()) {
      BigDecimal actSum;
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        actSum = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      else
        actSum = new BigDecimal(0);
      if (userLimit.isContinuousSummary()) {
        uls.fetchUserSummaryList(userLimit);
        values.add(new UserLimitSumLeft(userLimit, actSum, uss.calculateSum(userLimit, date)));
      } else {
        values.add(new UserLimitSumLeft(userLimit, actSum, userLimit.getLimit().subtract(actSum)));
      }
    }
    setItems(values);
  }
}
