package pl.kostro.expensesystem.dto.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dto.model.Category;
import pl.kostro.expensesystem.dto.model.ExpenseSheet;
import pl.kostro.expensesystem.db.model.CategoryEntity;
import pl.kostro.expensesystem.db.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.db.repository.CategoryRepository;
import pl.kostro.expensesystem.db.repository.ExpenseSheetRepository;

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
    BeanUtils.copyProperties(category, categoryEntity);
    cr.save(categoryEntity);
    expenseSheet.getCategoryList().add(category);
    ExpenseSheetEntity expenseSheetEntity = new ExpenseSheetEntity();
    BeanUtils.copyProperties(expenseSheet, expenseSheetEntity);
    expenseSheetEntity = eshr.save(expenseSheetEntity);
    logger.info("createCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void merge(Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    CategoryEntity categoryEntity = new CategoryEntity();
    BeanUtils.copyProperties(category, categoryEntity);
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
    BeanUtils.copyProperties(expenseSheet, expenseSheetEntity);
    expenseSheetEntity = eshr.save(expenseSheetEntity);
    CategoryEntity categoryEntity = new CategoryEntity();
    BeanUtils.copyProperties(category, categoryEntity);
    cr.delete(categoryEntity);
    logger.info("removeCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public static void decrypt(Category category) {
    category.getName();
  }

  public void encrypt(Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    category.setName(category.getName(true), true);
    cr.save(category);
    logger.info("encrypt for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

}
