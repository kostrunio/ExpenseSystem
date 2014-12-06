package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.UserLimit;

public class UserLimitService {

  public void removeProfessor(int id) {
    UserLimit emp = findUserLimit(id);
    if (emp != null) {
      AdapterDB.getEntityManager().remove(emp);
    }
  }

  public UserLimit findUserLimit(int id) {
    return AdapterDB.getEntityManager().find(UserLimit.class, id);
  }
  
  public void createUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    AdapterDB.begin();
    try {
      userLimit = AdapterDB.getEntityManager().merge(userLimit);
      expenseSheet.getUserLimitList().add(userLimit);
      expenseSheet = AdapterDB.getEntityManager().merge(expenseSheet);

      if (userLimit.getUser() instanceof RealUser) {
        RealUser persistUser = AdapterDB.getEntityManager().find(RealUser.class, userLimit.getUser().getId());
        persistUser.getExpenseSheetList().add(expenseSheet);
        AdapterDB.getEntityManager().merge(persistUser);
      }
      AdapterDB.commit();
    } finally {
      AdapterDB.close();
    }
  }

}
