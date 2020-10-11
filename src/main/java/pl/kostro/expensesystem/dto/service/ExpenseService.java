package pl.kostro.expensesystem.dto.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dao.service.ExpenseDao;
import pl.kostro.expensesystem.dto.model.Category;
import pl.kostro.expensesystem.dto.model.Expense;
import pl.kostro.expensesystem.dto.model.ExpenseSheet;
import pl.kostro.expensesystem.dto.model.User;
import pl.kostro.expensesystem.dto.model.UserLimit;

@Service
public class ExpenseService {
  
  @Autowired
  private ExpenseDao es;
  
  @Autowired
  private ExpenseSheetService eshs;

  static Logger logger = LogManager.getLogger();

  public void createExpense(ExpenseSheet expenseSheet, Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expense.setExpenseSheet(expenseSheet);
    eshs.addExpense(expenseSheet, expense);
    es.save(expense);
    logger.info("createExpense for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public Expense merge(Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    es.merge(expense);
    logger.info("merge for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expense;
  }

  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getExpenseList().remove(expense);
    eshs.removeExpenseFromMap(expenseSheet, expense);
    es.delete(expense);
    logger.info("removeExpense for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public String getValueString(Expense expense) {
    return expense.getValue().toString();
  }

  public Expense prepareNewExpense(ExpenseSheet expenseSheet, LocalDate date, Category category, User user) {
    return new Expense(date, "", category, user, "", date.isAfter(LocalDate.now()) ? true : false, expenseSheet);
  }

  public void saveExpense(ExpenseSheet expenseSheet, Expense expense, UserLimit userLimit, String formula,
                          String comment, Boolean notify, Boolean modify) {
    LocalDateTime stopper = LocalDateTime.now();
    if (modify)
      removeExpense(expenseSheet, expense);
    expense.setUser(userLimit.getUser());
    expense.setFormula(formula.startsWith("=") ? formula.substring(1) : formula);
    if (comment != null)
      expense.setComment(comment);
    expense.setNotify(notify);
    createExpense(expenseSheet, expense);
    logger.info("saveExpense for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public List<Expense> findExpensesToNotify() {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseList = es.findExpensesToNotify(LocalDate.now());
    logger.info("Found {} expenses to notify", expenseList.size());
    logger.info("findExpensesToNotify finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseList;
  }

  public static void decrypt(Expense expense) {
    expense.getFormula();
    expense.getComment();
  }

  public void encrypt(Expense expense) {
    LocalDateTime stopper = LocalDateTime.now();
    es.merge(expense);
    logger.info("encrypt for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
