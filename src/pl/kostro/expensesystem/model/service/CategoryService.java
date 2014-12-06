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
  
  public Category createCategory(String name, int orderId) {
    Category category = new Category();
    category.setName(name);
    category.setOrderId(orderId);
    em.persist(category);
    return category;
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
  
  public void createCategory(ExpenseSheet expenseSheet, Category category) {
    AdapterDB.begin(em);
    try {
      em.persist(category);
      expenseSheet.getCategoryList().add(category);
      expenseSheet = em.merge(expenseSheet);
      AdapterDB.commit(em);
    } finally {
      AdapterDB.close(em);
    }
  }


}
