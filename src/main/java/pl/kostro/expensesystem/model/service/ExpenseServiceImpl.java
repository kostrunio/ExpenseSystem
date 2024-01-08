package pl.kostro.expensesystem.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.repository.ExpenseRepository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService {
  
  private final ExpenseRepository repository;
  
  public void save(ExpenseEntity expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expense.setUpdateDate(LocalDate.now());
    ExpenseEntity saved = repository.save(expense);
    expense.setId(saved.getId());
    log.info("save for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void remove(ExpenseEntity expense) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.delete(expense);
    log.info("removeExpense for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public List<ExpenseEntity> findExpensesToNotify() {
    LocalDateTime stopper = LocalDateTime.now();
    List<ExpenseEntity> expenseList = new ArrayList<>();
    try {
      expenseList = repository.findExpensesToNotify(LocalDate.now());
    } catch (NoResultException e) {
      //if no expenses found expenseList is 0
    }
    log.info("Found {} expenses to notify", expenseList.size());
    log.info("findExpensesToNotify finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseList;
  }

  void decrypt(ExpenseEntity expense) {
    expense.getFormula();
    expense.getComment();
  }

  void encrypt(ExpenseEntity expense) {
    LocalDateTime stopper = LocalDateTime.now();
    expense.setFormula(expense.getFormula());
    expense.setComment(expense.getComment());
    expense = repository.save(expense);
    log.info("encrypt for {} finish: {} ms", expense, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
