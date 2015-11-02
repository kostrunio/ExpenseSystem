package pl.kostro.expensesystem.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.utils.Filter;

public class ExpenseService {
  
  public static List<Expense> findAllExpense(ExpenseSheet expenseSheet) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for(Expense expense : expenseSheet.getExpenseList())
      if (Filter.matchFilter(expense, expenseSheet.getFilter()))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }
  
  public static Expense findFirstExpense(ExpenseSheet expenseSheet) {
    Expense firstExpense = new Expense();
    firstExpense.setDate(new Date());
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getDate().before(firstExpense.getDate()))
        firstExpense = expense;
	  return firstExpense;
  }
  
  public static List<Expense> findExpenseForDates(ExpenseSheet expenseSheet) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for(Expense expense : expenseSheet.getExpenseList())
      if (!expense.getDate().before(expenseSheet.getFirstDate())
          && !expense.getDate().after(expenseSheet.getLastDate()))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }
  
  public static List<Expense> findExpenseByCategory(ExpenseSheet expenseSheet, Category category) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for(Expense expense : expenseSheet.getExpenseList())
      if (expense.getCategory().equals(category))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }
  
  public static void creteExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseEntityDao.begin();
    try {
      expense = ExpenseEntityDao.getEntityManager().merge(expense);
      ExpenseSheetService.addExpense(expenseSheet, expense);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
    }
  }

  
  public static void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseEntityDao.begin();
    try {
      ExpenseSheetService.removeExpense(expenseSheet, expense);
      ExpenseEntityDao.getEntityManager().remove(expense);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
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
    }
    return expense;
  }

  public static void encrypt(Expense expense) {
    ExpenseEntityDao.begin();
    try {
      expense.setFormula(expense.getFormula());
      expense.setComment(expense.getComment());
      expense = ExpenseEntityDao.getEntityManager().merge(expense);
      ExpenseEntityDao.commit();
    } finally {
    }
  }

}
