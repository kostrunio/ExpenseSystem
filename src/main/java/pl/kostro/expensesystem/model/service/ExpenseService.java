package pl.kostro.expensesystem.model.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.utils.Filter;

public class ExpenseService {

  private static Logger logger = LogManager.getLogger();

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

  public static Expense merge(Expense expense) {
    ExpenseEntityDao.begin();
    try {
      expense = ExpenseEntityDao.getEntityManager().merge(expense);
      ExpenseEntityDao.commit();
    } finally {
    }
    return expense;
  }

  public static void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    ExpenseEntityDao.begin();
    try {
      expenseSheet.getExpenseList().remove(expense);
      ExpenseSheetService.removeExpenseFromMap(expenseSheet, expense);
      ExpenseEntityDao.getEntityManager().remove(expense);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
    }
  }


  public static void decrypt(Expense expense) {
    expense.getFormula();
    expense.getComment();
  }

  public static void encrypt(Expense expense) {
    expense.setFormula(expense.getFormula(true), true);
    expense.setComment(expense.getComment(true), true);
    expense = ExpenseEntityDao.getEntityManager().merge(expense);
  }

  public static List<Expense> findAllExpense(ExpenseSheet expenseSheet) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
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
    for (Expense expense : expenseSheet.getExpenseList())
      if (!expense.getDate().before(expenseSheet.getFirstDate())
          && !expense.getDate().after(expenseSheet.getLastDate()))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }

  public static List<Expense> findExpenseByCategory(ExpenseSheet expenseSheet, Category category) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getCategory().equals(category))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }

  public static String getValueString(Expense expense) {
    return expense.getValue().toString();
  }

  public static Expense prepareNewExpense(ExpenseSheet expenseSheet, Date date, Category category, User user) {
    return new Expense(date, "", category, user, "", date.after(new Date()) ? true : false, expenseSheet);
  }

  public static void saveExpense(ExpenseSheet expenseSheet, Expense expense, UserLimit userLimit, String formula,
      Object comment, Boolean notify, Boolean modify) {
    if (modify)
      ExpenseService.removeExpense(expenseSheet, expense);
    expense.setUser(userLimit.getUser());
    expense.setFormula(formula.startsWith("=") ? formula.substring(1) : formula);
    if (comment != null)
      expense.setComment(comment.toString());
    expense.setNotify(notify);
    ExpenseService.creteExpense(expenseSheet, expense);
  }

  public static List<Expense> findExpensesToNotify() {
    ExpenseEntityDao.begin();
    List<Expense> expenseList = null;
    Calendar date = Calendar.getInstance();
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    try {
      expenseList = ExpenseEntityDao.findByNamedQueryWithParameters("findExpensesToNotify",
          ImmutableMap.of("date", date.getTime()), Expense.class);
      ExpenseEntityDao.commit();
    } catch (NoResultException e) {

    } finally {
      ExpenseEntityDao.close();
    }
    logger.info("Found {} expenses to notify", expenseList.size());
    return expenseList;
  }
}
