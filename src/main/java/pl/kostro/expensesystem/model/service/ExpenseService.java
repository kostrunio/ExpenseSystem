package pl.kostro.expensesystem.model.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.repository.ExpenseRepository;
import pl.kostro.expensesystem.model.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.utils.Filter;

@Service
public class ExpenseService {
  
  @Autowired
  private ExpenseRepository er;
  @Autowired
  private ExpenseSheetRepository eshr;
  
  private ExpenseSheetService eshs;

  private static Logger logger = LogManager.getLogger();

  public void creteExpense(ExpenseSheet expenseSheet, Expense expense) {
    expense = er.save(expense);
    eshs.addExpense(expenseSheet, expense);
    expenseSheet = eshr.save(expenseSheet);
  }

  public Expense merge(Expense expense) {
    expense = er.save(expense);
    return expense;
  }

  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    expenseSheet.getExpenseList().remove(expense);
    eshs.removeExpenseFromMap(expenseSheet, expense);
    er.delete(expense);
    expenseSheet = eshr.save(expenseSheet);
  }


  public static void decrypt(Expense expense) {
    expense.getFormula();
    expense.getComment();
  }

  public void encrypt(Expense expense) {
    expense.setFormula(expense.getFormula(true), true);
    expense.setComment(expense.getComment(true), true);
    expense = er.save(expense);
  }

  public List<Expense> findAllExpense(ExpenseSheet expenseSheet) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (Filter.matchFilter(expense, expenseSheet.getFilter()))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }

  public Expense findFirstExpense(ExpenseSheet expenseSheet) {
    Expense firstExpense = new Expense();
    firstExpense.setDate(new Date());
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getDate().before(firstExpense.getDate()))
        firstExpense = expense;
    return firstExpense;
  }

  public List<Expense> findExpenseForDates(ExpenseSheet expenseSheet) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (!expense.getDate().before(expenseSheet.getFirstDate())
          && !expense.getDate().after(expenseSheet.getLastDate()))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }

  public List<Expense> findExpenseByCategory(ExpenseSheet expenseSheet, Category category) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getCategory().equals(category))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }

  public String getValueString(Expense expense) {
    return expense.getValue().toString();
  }

  public Expense prepareNewExpense(ExpenseSheet expenseSheet, Date date, Category category, User user) {
    return new Expense(date, "", category, user, "", date.after(new Date()) ? true : false, expenseSheet);
  }

  public void saveExpense(ExpenseSheet expenseSheet, Expense expense, UserLimit userLimit, String formula,
      Object comment, Boolean notify, Boolean modify) {
    if (modify)
      removeExpense(expenseSheet, expense);
    expense.setUser(userLimit.getUser());
    expense.setFormula(formula.startsWith("=") ? formula.substring(1) : formula);
    if (comment != null)
      expense.setComment(comment.toString());
    expense.setNotify(notify);
    creteExpense(expenseSheet, expense);
  }

  public List<Expense> findExpensesToNotify() {
    List<Expense> expenseList = null;
    Calendar date = Calendar.getInstance();
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    try {
      expenseList = er.findExpensesToNotify(date.getTime());
    } catch (NoResultException e) {
    }
    logger.info("Found {} expenses to notify", expenseList.size());
    return expenseList;
  }
}
