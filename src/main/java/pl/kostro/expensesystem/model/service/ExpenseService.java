package pl.kostro.expensesystem.model.service;

import java.time.LocalDate;
import java.time.ZoneId;
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
  
  @Autowired
  private ExpenseSheetService eshs;

  private static Logger logger = LogManager.getLogger();

  public void creteExpense(ExpenseSheet expenseSheet, Expense expense) {
    Date stopper = new Date();
    expense = er.save(expense);
    eshs.addExpense(expenseSheet, expense);
    expenseSheet = eshr.save(expenseSheet);
    logger.info("creteExpense finish: {} ms", new Date().getTime() - stopper.getTime());
  }

  public Expense merge(Expense expense) {
    Date stopper = new Date();
    expense = er.save(expense);
    logger.info("merge finish: {} ms", new Date().getTime() - stopper.getTime());
    return expense;
  }

  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    Date stopper = new Date();
    expenseSheet.getExpenseList().remove(expense);
    eshs.removeExpenseFromMap(expenseSheet, expense);
    er.delete(expense);
    expenseSheet = eshr.save(expenseSheet);
    logger.info("removeExpense finish: {} ms", new Date().getTime() - stopper.getTime());
  }


  public static void decrypt(Expense expense) {
    expense.getFormula();
    expense.getComment();
  }

  public void encrypt(Expense expense) {
    Date stopper = new Date();
    expense.setFormula(expense.getFormula(true), true);
    expense.setComment(expense.getComment(true), true);
    expense = er.save(expense);
    logger.info("encrypt finish: {} ms", new Date().getTime() - stopper.getTime());
  }

  public List<Expense> findAllExpense(ExpenseSheet expenseSheet) {
    Date stopper = new Date();
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (Filter.matchFilter(expense, expenseSheet.getFilter()))
        expenseListToReturn.add(expense);
    logger.info("findAllExpense finish: {} ms", new Date().getTime() - stopper.getTime());
    return expenseListToReturn;
  }

  public Expense findFirstExpense(ExpenseSheet expenseSheet) {
    Date stopper = new Date();
    Expense firstExpense = new Expense();
    firstExpense.setDate(LocalDate.now());
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getDate().isBefore(firstExpense.getDate()))
        firstExpense = expense;
    logger.info("findFirstExpense finish: {} ms", new Date().getTime() - stopper.getTime());
    return firstExpense;
  }

  public List<Expense> findExpenseForDates(ExpenseSheet expenseSheet) {
    Date stopper = new Date();
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (!expense.getDate().isBefore(expenseSheet.getFirstDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
          && !expense.getDate().isAfter(expenseSheet.getLastDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
        expenseListToReturn.add(expense);
    logger.info("findExpenseForDates finish: {} ms", new Date().getTime() - stopper.getTime());
    return expenseListToReturn;
  }

  public List<Expense> findExpenseByCategory(ExpenseSheet expenseSheet, Category category) {
    Date stopper = new Date();
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getCategory().equals(category))
        expenseListToReturn.add(expense);
    logger.info("findExpenseByCategory finish: {} ms", new Date().getTime() - stopper.getTime());
    return expenseListToReturn;
  }

  public String getValueString(Expense expense) {
    return expense.getValue().toString();
  }

  public Expense prepareNewExpense(ExpenseSheet expenseSheet, LocalDate date, Category category, User user) {
    return new Expense(date, "", category, user, "", date.isAfter(LocalDate.now()) ? true : false, expenseSheet);
  }

  public void saveExpense(ExpenseSheet expenseSheet, Expense expense, UserLimit userLimit, String formula,
      Object comment, Boolean notify, Boolean modify) {
    Date stopper = new Date();
    if (modify)
      removeExpense(expenseSheet, expense);
    expense.setUser(userLimit.getUser());
    expense.setFormula(formula.startsWith("=") ? formula.substring(1) : formula);
    if (comment != null)
      expense.setComment(comment.toString());
    expense.setNotify(notify);
    creteExpense(expenseSheet, expense);
    logger.info("saveExpense finish: {} ms", new Date().getTime() - stopper.getTime());
  }

  public List<Expense> findExpensesToNotify() {
    Date stopper = new Date();
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
    logger.info("findExpensesToNotify finish: {} ms", new Date().getTime() - stopper.getTime());
    return expenseList;
  }
}
