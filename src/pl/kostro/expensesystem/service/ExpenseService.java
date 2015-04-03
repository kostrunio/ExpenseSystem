package pl.kostro.expensesystem.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.thirdparty.guava.common.collect.ImmutableMap;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class ExpenseService {
  
  public void removeExpense(int id) {
    Expense ex = findExpense(id);
    if (ex != null) {
      ExpenseEntityDao.getEntityManager().remove(ex);
    }
  }

  public Expense findExpense(int id) {
    return ExpenseEntityDao.getEntityManager().find(Expense.class, id);
  }
  
  public List<Expense> findAllExpense(ExpenseSheet expenseSheet) {
    return ExpenseEntityDao.findByNamedQueryWithParameters("findAllExpense", ImmutableMap.of("expenseSheet", expenseSheet), Expense.class);
  }
  
  public Expense findFirstExpense(ExpenseSheet expenseSheet) {
	  Expense firstExpense = ExpenseEntityDao.findSingleByNamedQueryWithParameters("findFirstExpense", ImmutableMap.of("expenseSheet", expenseSheet), Expense.class);
	  if (firstExpense == null) {
		  firstExpense = new Expense();
		  firstExpense.setDate(new Date());
	  }
	  return firstExpense;
  }
  
  public List<Expense> findExpenseForDates(ExpenseSheet expenseSheet) {
    return ExpenseEntityDao.findByNamedQueryWithParameters("findExpenseByDates", ImmutableMap.of("expenseSheet", expenseSheet.getId(), "startDate", expenseSheet.getFirstDate(), "endDate", expenseSheet.getLastDate()), Expense.class);
  }
  
  public List<Expense> findExpenseByCategory(ExpenseSheet expenseSheet, Category category) {
    return ExpenseEntityDao.findByNamedQueryWithParameters("findExpenseByCategory", ImmutableMap.of("expenseSheet", expenseSheet.getId(), "category", category), Expense.class);
  }
  
  public void creteExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseSheetService expenseSheetService = new ExpenseSheetService();
    ExpenseEntityDao.begin();
    try {
      expense = ExpenseEntityDao.getEntityManager().merge(expense);
      expenseSheetService.addExpense(expenseSheet, expense);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }

  
  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseSheetService expenseSheetService = new ExpenseSheetService();
    ExpenseEntityDao.begin();
    try {
      expenseSheetService.removeExpense(expenseSheet, expense);
      ExpenseEntityDao.getEntityManager().remove(findExpense(expense.getId()));
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }

  public String getValueString(Expense expense) {
    return expense.getValue().toString();
  }

}
