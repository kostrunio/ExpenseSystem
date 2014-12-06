package pl.kostro.expensesystem.model.service;

import java.util.Date;

import javax.persistence.EntityManager;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.utils.Calculator;

public class ExpenseService {
  
  private EntityManager em;
  
  public ExpenseService() {
    this.em = AdapterDB.getEntityManager();
  }
  
  public Expense createExpense(Date date, String formula, Category category, User user, String comment) {
    Expense expense = new Expense();
    expense.setDate(date);
    expense.setFormula(formula);
    expense.setCategory(category);
    expense.setUser(user);
    expense.setValue(Calculator.getResult(formula));
    expense.setComment(comment);
    em.persist(expense);
    return expense;
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
