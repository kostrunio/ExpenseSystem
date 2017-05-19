package pl.kostro.expensesystem.model.service;

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

  public void createCategory(ExpenseSheet expenseSheet, String name) {
    Category category = new Category(name, expenseSheet.getCategoryList().size());
    cr.save(category);
    expenseSheet.getCategoryList().add(category);
    expenseSheet = eshr.save(expenseSheet);
  }

  public void merge(Category category) {
    cr.save(category);
  }

  public ExpenseSheet removeCategory(ExpenseSheet expenseSheet, Category category) {
    expenseSheet.getCategoryList().remove(category);
    int i = 0;
    for (Category cat : expenseSheet.getCategoryList())
      cat.setOrder(i++);
    expenseSheet = eshr.save(expenseSheet);
    cr.delete(category);
    return expenseSheet;
  }

  public void decrypt(Category category) {
    category.getName();
  }

  public void encrypt(Category category) {
    category.setName(category.getName(true), true);
    cr.save(category);
  }

}
