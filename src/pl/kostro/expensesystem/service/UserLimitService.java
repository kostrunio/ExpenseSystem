package pl.kostro.expensesystem.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.UserLimit;

public class UserLimitService {

  public static void removeUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    ExpenseEntityDao.begin();
    try {
      expenseSheet.getUserLimitList().remove(userLimit);
      ExpenseEntityDao.getEntityManager().remove(userLimit);
      if (!(userLimit.getUser() instanceof RealUser))
        ExpenseEntityDao.getEntityManager().remove(userLimit.getUser());
      ExpenseEntityDao.commit();
    } finally {
    }
  }

  public static void createUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
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
    }
  }

  public static void merge(UserLimit userLimit) {
    ExpenseEntityDao.begin();
    try {
      ExpenseEntityDao.getEntityManager().merge(userLimit);
      ExpenseEntityDao.commit();
    } finally {
    }
  }

}
