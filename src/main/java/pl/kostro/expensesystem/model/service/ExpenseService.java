package pl.kostro.expensesystem.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

@Service
public class ExpenseService {
  
  @Autowired
  private ExpenseRepository er;
  
  @Autowired
  private ExpenseSheetService eshs;

  static Logger logger = LogManager.getLogger();

  public void createExpense(ExpenseSheet expenseSheet, Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expense.setExpenseSheet(expenseSheet);
    expense = er.save(expense);
    eshs.addExpense(expenseSheet, expense);
    logger.info("createExpense for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public Expense merge(Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expense = er.save(expense);
    logger.info("merge for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expense;
  }

  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getExpenseList().remove(expense);
    eshs.removeExpenseFromMap(expenseSheet, expense);
    er.delete(expense);
    logger.info("removeExpense for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
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
    logger.info("encrypt for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
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
    createExpense(expenseSheet, expense);
    logger.info("saveExpense for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public List<Expense> findExpensesToNotify() {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseList = null;
    try {
      expenseList = er.findExpensesToNotify(LocalDate.now());
    } catch (NoResultException e) {
    }
    logger.info("Found {} expenses to notify", expenseList.size());
    logger.info("findExpensesToNotify finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseList;
  }
}
