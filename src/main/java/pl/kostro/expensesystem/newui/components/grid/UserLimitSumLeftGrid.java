package pl.kostro.expensesystem.newui.components.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.server.VaadinSession;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.model.UserLimitSumLeft;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserLimitSumLeftGrid extends Grid<UserLimitSumLeft> {
  
  private UserSummaryService uss;
  private UserLimitService uls;
  private ExpenseSheetEntity expenseSheet;

  public UserLimitSumLeftGrid() {
    uss = AppCtxProvider.getBean(UserSummaryService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);;
    setAllRowsVisible(true);
    setSelectionMode(SelectionMode.NONE);
    addColumn(item -> item.getUserLimit().getUser().getName()).setHeader(Msg.get("userLimitTable.user"));
    addColumn(UserLimitSumLeft::getSum).setHeader(Msg.get("userLimitTable.sum"));
    addColumn(UserLimitSumLeft::getLeft).setHeader(Msg.get("userLimitTable.left"));
    setClassNameGenerator(item -> {
      double rows = getPageSize();
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
