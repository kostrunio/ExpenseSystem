package pl.kostro.expensesystem.dao.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.dao.model.*;
import pl.kostro.expensesystem.dao.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.dao.repository.RealUserRepository;
import pl.kostro.expensesystem.dao.repository.UserLimitRepository;
import pl.kostro.expensesystem.dto.model.*;
import pl.kostro.expensesystem.utils.Converter;

import javax.transaction.Transactional;

@Service
public class ExpenseSheetDao {
  
  @Autowired
  private ExpenseSheetRepository eshr;
  @Autowired
  private UserLimitRepository ulr;
  @Autowired
  private RealUserRepository rur;
  
  @Autowired
  private CategoryDao cs;
  @Autowired
  private ExpenseDao es;
  @Autowired
  private RealUserDao rus;
  @Autowired
  private UserLimitDao uls;

  private static Logger logger = LogManager.getLogger();

  public void save(ExpenseSheet expenseSheet) {
    ExpenseSheetEntity expenseSheetEntity = Converter.toExpenseSheetEntity(expenseSheet);
    eshr.save(expenseSheetEntity);
    expenseSheet.setId(expenseSheetEntity.getId());
  }

  public void merge(ExpenseSheet expenseSheet) {
    ExpenseSheetEntity expenseSheetEntity = eshr.findOne(expenseSheet.getId());
    Converter.toExpenseSheetEntity(expenseSheet, expenseSheetEntity);
    eshr.save(expenseSheetEntity);
  }

  public void delete(ExpenseSheet expenseSheet) {
    eshr.delete(expenseSheet.getId());
  }

  @Transactional
  public void fetchCategoryList(ExpenseSheet expenseSheet) {
    ExpenseSheetEntity expenseSheetEntity = eshr.getOne(expenseSheet.getId());
    expenseSheetEntity.getCategoryList().size();
    expenseSheet.getCategoryList().clear();
    for (CategoryEntity categoryEntity : expenseSheetEntity.getCategoryList())
      expenseSheet.getCategoryList().add(Converter.toCategory(categoryEntity));
  }

  @Transactional
  public void fetchExpenseList(ExpenseSheet expenseSheet) {
    ExpenseSheetEntity expenseSheetEntity = eshr.getOne(expenseSheet.getId());
    expenseSheetEntity.getExpenseList().size();
    expenseSheet.getExpenseList().clear();
    for (ExpenseEntity expenseEntity : expenseSheetEntity.getExpenseList())
      expenseSheet.getExpenseList().add(Converter.toExpense(expenseEntity));
  }

  @Transactional
  public void fetchUserLimitList(ExpenseSheet expenseSheet) {
    ExpenseSheetEntity expenseSheetEntity = eshr.getOne(expenseSheet.getId());
    expenseSheetEntity.getUserLimitList().size();
    expenseSheet.getUserLimitList().clear();
    for (UserLimitEntity userLimitEntity : expenseSheetEntity.getUserLimitList())
      expenseSheet.getUserLimitList().add(Converter.toUserLimit(userLimitEntity));
  }
}
