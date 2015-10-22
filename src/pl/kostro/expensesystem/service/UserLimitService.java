package pl.kostro.expensesystem.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.UserLimit;

public class UserLimitService {

  public void removeUserLimit(int id) {
    UserLimit uL = findUserLimit(id);
    if (uL != null) {
      ExpenseEntityDao.getEntityManager().remove(uL);
    }
  }

  public UserLimit findUserLimit(int id) {
    return ExpenseEntityDao.getEntityManager().find(UserLimit.class, id);
  }
  
  public void createUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    ExpenseEntityDao.begin();
    try {
      userLimit = ExpenseEntityDao.getEntityManager().merge(userLimit);
      expenseSheet.getUserLimitList().add(userLimit);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);

      if (userLimit.getUser() instanceof RealUser) {
        RealUser persistUser = ExpenseEntityDao.getEntityManager().find(RealUser.class, userLimit.getUser().getId());
        persistUser.getExpenseSheetList().add(expenseSheet);
        ExpenseEntityDao.getEntityManager().merge(persistUser);
      }
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }

  public static void merge(UserLimit userLimit) {
    ExpenseEntityDao.begin();
    try {
      ExpenseEntityDao.getEntityManager().merge(userLimit);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }

}
