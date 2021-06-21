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

import pl.kostro.expensesystem.model.ExpenseEntity;
import pl.kostro.expensesystem.model.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {
  
  private ExpenseRepository repository;
  
  private Logger logger = LogManager.getLogger();

  @Autowired
  public ExpenseServiceImpl(ExpenseRepository repository) {
    this.repository = repository;
  }

  public void save(ExpenseEntity expense) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.save(expense);
    logger.info("save for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void remove(ExpenseEntity expense) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.delete(expense);
    logger.info("removeExpense for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void decrypt(ExpenseEntity expense) {
    expense.getFormula();
    expense.getComment();
  }

  public void encrypt(ExpenseEntity expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expense.setFormula(expense.getFormula());
    expense.setComment(expense.getComment());
    expense = repository.save(expense);
    logger.info("encrypt for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public List<ExpenseEntity> findExpensesToNotify() {
    LocalDateTime stopper = LocalDateTime.now();
    List<ExpenseEntity> expenseList = null;
    try {
      expenseList = repository.findExpensesToNotify(LocalDate.now());
    } catch (NoResultException e) {
    }
    logger.info("Found {} expenses to notify", expenseList.size());
    logger.info("findExpensesToNotify finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseList;
  }
}
