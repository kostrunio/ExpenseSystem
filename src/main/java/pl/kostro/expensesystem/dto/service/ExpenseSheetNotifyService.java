package pl.kostro.expensesystem.dto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

public class ExpenseSheetNotifyService {

  public static Map<RealUser, Map<ExpenseSheet, List<Expense>>> prepareExpenseSheetNotify(List<Expense> expenseList) {
    Map<RealUser, Map<ExpenseSheet, List<Expense>>> rUMap = new HashMap<RealUser, Map<ExpenseSheet, List<Expense>>>();
    Map<ExpenseSheet, List<Expense>> eSMap;
    List<Expense> eList;
    for (Expense expense : expenseList) {
      if (!rUMap.containsKey(expense.getExpenseSheet().getOwner())) {
        eSMap = new HashMap<ExpenseSheet, List<Expense>>();
        rUMap.put(expense.getExpenseSheet().getOwner(), eSMap);
      } else
        eSMap = rUMap.get(expense.getExpenseSheet().getOwner());

      if (!eSMap.containsKey(expense.getExpenseSheet())) {
        eList = new ArrayList<Expense>();
        eSMap.put(expense.getExpenseSheet(), eList);
      } else
        eList = eSMap.get(expense.getExpenseSheet());

      eList.add(expense);
    }
    return rUMap;
  }
}
