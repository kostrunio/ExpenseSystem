package pl.kostro.expensesystem.dto.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dao.service.CategoryDao;
import pl.kostro.expensesystem.dao.service.ExpenseSheetDao;
import pl.kostro.expensesystem.dto.model.Category;
import pl.kostro.expensesystem.dto.model.ExpenseSheet;

@Service
public class CategoryService {
  
  @Autowired
  private CategoryDao cs;
  @Autowired
  private ExpenseSheetDao eshs;

  private static Logger logger = LogManager.getLogger();
  
  public void createCategory(ExpenseSheet expenseSheet, String name) {
    LocalDateTime stopper = LocalDateTime.now();
    Category category = new Category(name, expenseSheet.getCategoryList().size());
    expenseSheet.getCategoryList().add(category);
    cs.save(category);
    logger.info("createCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void merge(Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    cs.merge(category);
    logger.info("merge for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public ExpenseSheet removeCategory(ExpenseSheet expenseSheet, Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getCategoryList().remove(category);
    int i = 0;
    for (Category cat : expenseSheet.getCategoryList())
      cat.setOrder(i++);
    cs.delete(category);
    logger.info("removeCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public static void decrypt(Category category) {
    category.getName();
  }

  public void encrypt(Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    cs.merge(category);
    logger.info("encrypt for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

}
