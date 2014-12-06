package pl.kostro.expensesystem.model.service;

import javax.persistence.EntityManager;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.UserLimit;

public class UserLimitService {

  private EntityManager em;
  
  public UserLimitService() {
    this.em = AdapterDB.getEntityManager();
  }
  
  public void removeProfessor(int id) {
    UserLimit emp = findUserLimit(id);
    if (emp != null) {
      em.remove(emp);
    }
  }

  public UserLimit findUserLimit(int id) {
    return em.find(UserLimit.class, id);
  }
  
  public void createUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    AdapterDB.begin(em);
    try {
      userLimit = em.merge(userLimit);
      expenseSheet.getUserLimitList().add(userLimit);
      expenseSheet = em.merge(expenseSheet);

      if (userLimit.getUser() instanceof RealUser) {
        RealUser persistUser = em.find(RealUser.class, userLimit.getUser().getId());
        persistUser.getExpenseSheetList().add(expenseSheet);
        em.merge(persistUser);
      }
      AdapterDB.commit(em);
    } finally {
      AdapterDB.close(em);
    }
  }

}
