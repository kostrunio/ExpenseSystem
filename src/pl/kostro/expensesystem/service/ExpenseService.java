package pl.kostro.expensesystem.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.thirdparty.guava.common.collect.ImmutableMap;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.utils.Filter;

public class ExpenseService {
  
  public void removeExpense(int id) {
    Expense ex = findExpense(id);
    if (ex != null) {
      ExpenseEntityDao.getEntityManager().remove(ex);
    }
  }

  public static Expense findExpense(int id) {
    return ExpenseEntityDao.getEntityManager().find(Expense.class, id);
  }
  
  public static List<Expense> findAllExpense(ExpenseSheet expenseSheet) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for(Expense expense : ExpenseEntityDao.findByNamedQueryWithParameters(
        "findAllExpense",
        ImmutableMap.of("expenseSheet", expenseSheet),
        Expense.class)) {
      if (Filter.matchFilter(expense, expenseSheet.getFilter()))
        expenseListToReturn.add(expense);
    }
    return expenseListToReturn;
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
  
  public static void creteExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseEntityDao.begin();
    try {
      expense = ExpenseEntityDao.getEntityManager().merge(expense);
      ExpenseSheetService.addExpense(expenseSheet, expense);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }

  
  public static void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseEntityDao.begin();
    try {
      ExpenseSheetService.removeExpense(expenseSheet, expense);
      ExpenseEntityDao.getEntityManager().remove(findExpense(expense.getId()));
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }

  public static String getValueString(Expense expense) {
    return expense.getValue().toString();
  }

  public static Expense prepareNewExpense(ExpenseSheet expenseSheet, Date date, Category category, User user) {
    return new Expense(date, "", category, user, "", expenseSheet);
  }

  public static void saveExpense(ExpenseSheet expenseSheet, Expense expense, UserLimit userLimit, String formula, Object comment, Boolean modify) {
    if (modify)
      ExpenseService.removeExpense(expenseSheet, expense);
    expense.setUser(userLimit.getUser());
    expense.setFormula(formula.startsWith("=")?formula.substring(1):formula);
    if (comment != null)
      expense.setComment(comment.toString());
    ExpenseService.creteExpense(expenseSheet, expense);
  }
  
  public static Expense merge(Expense expense) {
    ExpenseEntityDao.begin();
    try {
      expense = ExpenseEntityDao.getEntityManager().merge(expense);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
    return expense;
  }

}
