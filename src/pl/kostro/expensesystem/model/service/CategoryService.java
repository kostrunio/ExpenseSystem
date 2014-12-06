package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class CategoryService {
  
  public void removeProfessor(int id) {
    Category emp = findCategory(id);
    if (emp != null) {
      AdapterDB.getEntityManager().remove(emp);
    }
  }

  public Category findCategory(int id) {
    return AdapterDB.getEntityManager().find(Category.class, id);
  }
  
  public void createCategory(ExpenseSheet expenseSheet, String name, int orderId) {
    AdapterDB.begin();
    try {
      Category category = new Category();
      category.setName(name);
      category.setOrderId(orderId);
      AdapterDB.getEntityManager().persist(category);
      expenseSheet.getCategoryList().add(category);
      expenseSheet = AdapterDB.getEntityManager().merge(expenseSheet);
      AdapterDB.commit();
    } finally {
      AdapterDB.close();
    }
  }


}
