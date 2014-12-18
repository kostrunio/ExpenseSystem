package pl.kostro.expensesystem.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class CategoryService {
  
  public void removeProfessor(int id) {
    Category emp = findCategory(id);
    if (emp != null) {
      ExpenseEntityDao.getEntityManager().remove(emp);
    }
  }

  public Category findCategory(int id) {
    return ExpenseEntityDao.getEntityManager().find(Category.class, id);
  }
  
  public void createCategory(ExpenseSheet expenseSheet, String name, int orderId) {
    ExpenseEntityDao.begin();
    try {
      Category category = new Category();
      category.setName(name);
      category.setOrderId(orderId);
      category.setExpenseSheet(expenseSheet);
      ExpenseEntityDao.getEntityManager().persist(category);
      expenseSheet.getCategoryList().add(category);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }


}
