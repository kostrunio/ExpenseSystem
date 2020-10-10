package pl.kostro.expensesystem.model.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.business.Category;
import pl.kostro.expensesystem.business.ExpenseSheet;
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
  
  public void createCategory(ExpenseSheet expenseSheet, String name) {
    LocalDateTime stopper = LocalDateTime.now();
    Category category = new Category(name, expenseSheet.getCategoryList().size());
    CategoryEntity categoryEntity = new CategoryEntity();
    cr.save(categoryEntity);
    expenseSheet.getCategoryList().add(category);
    ExpenseSheetEntity expenseSheetEntity = new ExpenseSheetEntity();
    expenseSheetEntity = eshr.save(expenseSheetEntity);
    logger.info("createCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void merge(Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    CategoryEntity categoryEntity = new CategoryEntity();
    cr.save(categoryEntity);
    logger.info("merge for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public ExpenseSheet removeCategory(ExpenseSheet expenseSheet, Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getCategoryList().remove(category);
    int i = 0;
    for (Category cat : expenseSheet.getCategoryList())
      cat.setOrder(i++);
    ExpenseSheetEntity expenseSheetEntity = new ExpenseSheetEntity();
    expenseSheetEntity = eshr.save(expenseSheetEntity);
    CategoryEntity categoryEntity = new CategoryEntity();
    cr.delete(categoryEntity);
    logger.info("removeCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

}
