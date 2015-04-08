package pl.kostro.expensesystem.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class CategoryService {
  
  public void removeCategory(int id) {
    Category cat = findCategory(id);
    if (cat != null) {
      ExpenseEntityDao.getEntityManager().remove(cat);
    }
  }

  public Category findCategory(int id) {
    return ExpenseEntityDao.getEntityManager().find(Category.class, id);
  }
  
  public void createCategory(ExpenseSheet expenseSheet, String name, int order) {
    ExpenseEntityDao.begin();
    try {
      Category category = new Category(name, order);
      ExpenseEntityDao.getEntityManager().persist(category);
      expenseSheet.getCategoryList().add(category);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }


}
