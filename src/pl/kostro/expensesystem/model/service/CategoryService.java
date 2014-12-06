package pl.kostro.expensesystem.model.service;

import javax.persistence.EntityManager;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;

public class CategoryService {
  
  private EntityManager em;
  
  public CategoryService() {
    this.em = AdapterDB.getEntityManager();
  }
  
  public void removeProfessor(int id) {
    Category emp = findCategory(id);
    if (emp != null) {
      em.remove(emp);
    }
  }

  public Category findCategory(int id) {
    return em.find(Category.class, id);
  }
  
  public void createCategory(ExpenseSheet expenseSheet, String name, int orderId) {
    AdapterDB.begin(em);
    try {
      Category category = new Category();
      category.setName(name);
      category.setOrderId(orderId);
      em.persist(category);
      expenseSheet.getCategoryList().add(category);
      expenseSheet = em.merge(expenseSheet);
      AdapterDB.commit(em);
    } finally {
      AdapterDB.close(em);
    }
  }


}
