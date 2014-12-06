package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class ExpenseService {
  
  public void removeExpense(int id) {
    Expense emp = findExpense(id);
    if (emp != null) {
      AdapterDB.getEntityManager().remove(emp);
    }
  }

  public Expense findExpense(int id) {
    return AdapterDB.getEntityManager().find(Expense.class, id);
  }
  
  public void creteExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseSheetService expenseSheetService = new ExpenseSheetService();
    AdapterDB.begin();
    try {
      expense = AdapterDB.getEntityManager().merge(expense);
      expenseSheetService.addExpense(expenseSheet, expense);
      expenseSheet = AdapterDB.getEntityManager().merge(expenseSheet);
      AdapterDB.commit();
    } finally {
      AdapterDB.close();
    }
  }

  
  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseSheetService expenseSheetService = new ExpenseSheetService();
    AdapterDB.begin();
    try {
      expenseSheetService.removeExpense(expenseSheet, expense);
      AdapterDB.getEntityManager().remove(findExpense(expense.getId()));
      expenseSheet = AdapterDB.getEntityManager().merge(expenseSheet);
      AdapterDB.commit();
    } finally {
      AdapterDB.close();
    }
  }

  public String getValueString(Expense expense) {
    return Double.toString(expense.getValue());
  }
}
