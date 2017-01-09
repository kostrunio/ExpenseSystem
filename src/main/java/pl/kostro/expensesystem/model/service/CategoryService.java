package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class CategoryService {

  public static void createCategory(ExpenseSheet expenseSheet, String name) {
    ExpenseEntityDao.begin();
    try {
      Category category = new Category(name, expenseSheet.getCategoryList().size());
      ExpenseEntityDao.getEntityManager().persist(category);
      expenseSheet.getCategoryList().add(category);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
    }
  }

  public static void merge(Category category) {
    ExpenseEntityDao.begin();
    try {
      ExpenseEntityDao.getEntityManager().merge(category);
      ExpenseEntityDao.commit();
    } finally {
    }
  }

  public static ExpenseSheet removeCategory(ExpenseSheet expenseSheet, Category category) {
    expenseSheet.getCategoryList().remove(category);
    int i = 0;
    for (Category cat : expenseSheet.getCategoryList())
      cat.setOrder(i++);
    ExpenseEntityDao.begin();
    try {
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.getEntityManager()
          .remove(ExpenseEntityDao.getEntityManager().find(Category.class, category.getId()));
      ExpenseEntityDao.commit();
    } finally {
    }
    return expenseSheet;
  }

  public static void decrypt(Category category) {
    category.getName();
  }

  public static void encrypt(Category category) {
    category.setName(category.getName(true), true);
    ExpenseEntityDao.getEntityManager().merge(category);
  }

}
