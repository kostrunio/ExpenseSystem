package pl.kostro.expensesystem.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class CategoryService {
  
  public static void removeCategory(int id) {
    Category cat = findCategory(id);
    if (cat != null) {
      ExpenseEntityDao.getEntityManager().remove(cat);
    }
  }

  public static Category findCategory(int id) {
    return ExpenseEntityDao.getEntityManager().find(Category.class, id);
  }
  
  public static void createCategory(ExpenseSheet expenseSheet, String name) {
    ExpenseEntityDao.begin();
    try {
      Category category = new Category(name, expenseSheet.getCategoryList().size());
      ExpenseEntityDao.getEntityManager().persist(category);
      expenseSheet.getCategoryList().add(category);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }


}
