package pl.kostro.expensesystem.model.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.CategoryEntity;
import pl.kostro.expensesystem.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.repository.CategoryRepository;
import pl.kostro.expensesystem.model.repository.ExpenseSheetRepository;

@Service
public class CategoryService {
  
  @Autowired
  private CategoryRepository cr;
  @Autowired
  private ExpenseSheetRepository eshr;

  private static Logger logger = LogManager.getLogger();
  
  public void createCategory(ExpenseSheetEntity expenseSheet, String name) {
    LocalDateTime stopper = LocalDateTime.now();
    CategoryEntity category = new CategoryEntity(name, expenseSheet.getCategoryList().size());
    cr.save(category);
    expenseSheet.getCategoryList().add(category);
    expenseSheet = eshr.save(expenseSheet);
    logger.info("createCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void merge(CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    cr.save(category);
    logger.info("merge for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public ExpenseSheetEntity removeCategory(ExpenseSheetEntity expenseSheet, CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getCategoryList().remove(category);
    int i = 0;
    for (CategoryEntity cat : expenseSheet.getCategoryList())
      cat.setOrder(i++);
    expenseSheet = eshr.save(expenseSheet);
    cr.delete(category);
    logger.info("removeCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public void decrypt(CategoryEntity category) {
    category.getName();
  }

  public void encrypt(CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    category.setName(category.getName(true), true);
    cr.save(category);
    logger.info("encrypt for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

}
