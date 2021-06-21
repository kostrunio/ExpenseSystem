package pl.kostro.expensesystem.dto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.kostro.expensesystem.model.ExpenseEntity;
import pl.kostro.expensesystem.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.RealUserEntity;

public class ExpenseSheetNotifyService {

  public Map<RealUserEntity, Map<ExpenseSheetEntity, List<ExpenseEntity>>> prepareExpenseSheetNotify(List<ExpenseEntity> expenseList) {
    Map<RealUserEntity, Map<ExpenseSheetEntity, List<ExpenseEntity>>> rUMap = new HashMap<RealUserEntity, Map<ExpenseSheetEntity, List<ExpenseEntity>>>();
    Map<ExpenseSheetEntity, List<ExpenseEntity>> eSMap;
    List<ExpenseEntity> eList;
    for (ExpenseEntity expense : expenseList) {
      if (!rUMap.containsKey(expense.getExpenseSheet().getOwner())) {
        eSMap = new HashMap<ExpenseSheetEntity, List<ExpenseEntity>>();
        rUMap.put(expense.getExpenseSheet().getOwner(), eSMap);
      } else
        eSMap = rUMap.get(expense.getExpenseSheet().getOwner());

      if (!eSMap.containsKey(expense.getExpenseSheet())) {
        eList = new ArrayList<ExpenseEntity>();
        eSMap.put(expense.getExpenseSheet(), eList);
      } else
        eList = eSMap.get(expense.getExpenseSheet());

      eList.add(expense);
    }
    return rUMap;
  }
}
