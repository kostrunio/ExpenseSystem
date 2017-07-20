package pl.kostro.expensesystem.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    LocalDateTime stopper = LocalDateTime.now();
    expense = er.save(expense);
    eshs.addExpense(expenseSheet, expense);
    expenseSheet = eshr.save(expenseSheet);
    logger.info("creteExpense finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public Expense merge(Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expense = er.save(expense);
    logger.info("merge finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expense;
  }

  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getExpenseList().remove(expense);
    eshs.removeExpenseFromMap(expenseSheet, expense);
    er.delete(expense);
    expenseSheet = eshr.save(expenseSheet);
    logger.info("removeExpense finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }


  public static void decrypt(Expense expense) {
    expense.getFormula();
    expense.getComment();
  }

  public void encrypt(Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expense.setFormula(expense.getFormula(true), true);
    expense.setComment(expense.getComment(true), true);
    expense = er.save(expense);
    logger.info("encrypt finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public List<Expense> findAllExpense(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (Filter.matchFilter(expense, expenseSheet.getFilter()))
        expenseListToReturn.add(expense);
    logger.info("findAllExpense finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseListToReturn;
  }

  public Expense findFirstExpense(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    Expense firstExpense = new Expense();
    firstExpense.setDate(LocalDate.now());
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getDate().isBefore(firstExpense.getDate()))
        firstExpense = expense;
    logger.info("findFirstExpense finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return firstExpense;
  }

  public List<Expense> findExpenseForDates(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (!expense.getDate().isBefore(expenseSheet.getFirstDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
          && !expense.getDate().isAfter(expenseSheet.getLastDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
        expenseListToReturn.add(expense);
    logger.info("findExpenseForDates finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseListToReturn;
  }

  public List<Expense> findExpenseByCategory(ExpenseSheet expenseSheet, Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getCategory().equals(category))
        expenseListToReturn.add(expense);
    logger.info("findExpenseByCategory finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
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
    LocalDateTime stopper = LocalDateTime.now();
    if (modify)
      removeExpense(expenseSheet, expense);
    expense.setUser(userLimit.getUser());
    expense.setFormula(formula.startsWith("=") ? formula.substring(1) : formula);
    if (comment != null)
      expense.setComment(comment.toString());
    expense.setNotify(notify);
    creteExpense(expenseSheet, expense);
    logger.info("saveExpense finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public List<Expense> findExpensesToNotify() {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseList = null;
    LocalDate date = LocalDate.now();
    try {
      expenseList = er.findExpensesToNotify(date);
    } catch (NoResultException e) {
    }
    logger.info("Found {} expenses to notify", expenseList.size());
    logger.info("findExpensesToNotify finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseList;
  }
}
