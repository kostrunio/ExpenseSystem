package pl.kostro.expensesystem.model.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
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
    Date stopper = new Date();
    Category category = new Category(name, expenseSheet.getCategoryList().size());
    cr.save(category);
    expenseSheet.getCategoryList().add(category);
    expenseSheet = eshr.save(expenseSheet);
    logger.info("createCategory finish: {} ms", new Date().getTime() - stopper.getTime());
  }

  public void merge(Category category) {
    Date stopper = new Date();
    cr.save(category);
    logger.info("merge finish: {} ms", new Date().getTime() - stopper.getTime());
  }

  public ExpenseSheet removeCategory(ExpenseSheet expenseSheet, Category category) {
    Date stopper = new Date();
    expenseSheet.getCategoryList().remove(category);
    int i = 0;
    for (Category cat : expenseSheet.getCategoryList())
      cat.setOrder(i++);
    expenseSheet = eshr.save(expenseSheet);
    cr.delete(category);
    logger.info("removeCategory finish: {} ms", new Date().getTime() - stopper.getTime());
    return expenseSheet;
  }

  public void decrypt(Category category) {
    category.getName();
  }

  public void encrypt(Category category) {
    Date stopper = new Date();
    category.setName(category.getName(true), true);
    cr.save(category);
    logger.info("encrypt finish: {} ms", new Date().getTime() - stopper.getTime());
  }

}
