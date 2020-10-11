package pl.kostro.expensesystem.dao.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.dao.model.ExpenseEntity;
import pl.kostro.expensesystem.dao.repository.ExpenseRepository;
import pl.kostro.expensesystem.dto.model.*;
import pl.kostro.expensesystem.utils.Converter;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseDao {
  
  @Autowired
  private ExpenseRepository er;
  
  public void save(Expense expense) {
    ExpenseEntity expenseEntity = Converter.toExpenseEntity(expense);
    er.save(expenseEntity);
    expense.setId(expenseEntity.getId());
  }

  public void merge(Expense expense) {
    ExpenseEntity expenseEntity = er.findOne(expense.getId());
    Converter.toExpenseEntity(expense, expenseEntity);
    er.save(expenseEntity);
  }

  public void delete(Expense expense) {
    er.delete(expense.getId());
  }


  public List<Expense> findExpensesToNotify(LocalDate now) {
    List<Expense> expenseList = new ArrayList<>();
    List<ExpenseEntity> expenseEntityList = null;
    try {
      expenseEntityList = er.findExpensesToNotify(now);
    } catch (NoResultException e) {
    }
    for (ExpenseEntity expenseEntity : expenseEntityList) {
      Expense expense = Converter.toExpense(expenseEntity);
      expenseList.add(expense);
    }
    return expenseList;
  }
}
