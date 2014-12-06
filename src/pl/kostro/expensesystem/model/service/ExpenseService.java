package pl.kostro.expensesystem.model.service;

import javax.persistence.EntityManager;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class ExpenseService {
  
  private EntityManager em;
  
  public ExpenseService() {
    this.em = AdapterDB.getEntityManager();
  }
  
  public void removeExpense(int id) {
    Expense emp = findExpense(id);
    if (emp != null) {
      em.remove(emp);
    }
  }

  public Expense findExpense(int id) {
    return em.find(Expense.class, id);
  }
  
  public void creteExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseSheetService expenseSheetService = new ExpenseSheetService();
    AdapterDB.begin(em);
    try {
      expense = em.merge(expense);
      expenseSheetService.addExpense(expenseSheet, expense);
      expenseSheet = em.merge(expenseSheet);
      AdapterDB.commit(em);
    } finally {
      AdapterDB.close(em);
    }
  }

  
  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseSheetService expenseSheetService = new ExpenseSheetService();
    AdapterDB.begin(em);
    try {
      expenseSheetService.removeExpense(expenseSheet, expense);
      em.remove(em.find(Expense.class, expense.getId()));
      expenseSheet = em.merge(expenseSheet);
      AdapterDB.commit(em);
    } finally {
      AdapterDB.close(em);
    }
  }

  public String getValueString(Expense expense) {
    return Double.toString(expense.getValue());
  }
}
