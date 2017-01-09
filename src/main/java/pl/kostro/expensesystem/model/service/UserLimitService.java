package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;

public class UserLimitService {

  public static void createUserLimit(ExpenseSheet expenseSheet, User user) {
    ExpenseEntityDao.begin();
    try {
      UserLimit userLimit = ExpenseEntityDao.getEntityManager()
          .merge(new UserLimit(user, expenseSheet.getUserLimitList().size()));
      expenseSheet.getUserLimitList().add(userLimit);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);

      if (user instanceof RealUser) {
        ((RealUser) user).getExpenseSheetList().add(expenseSheet);
        ExpenseEntityDao.getEntityManager().merge(user);
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

  public static void decrypt(UserLimit userLimit) {
    userLimit.getLimit();
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      UserSummaryService.decrypt(userSummary);
  }

  public static void encrypt(UserLimit userLimit) {
    userLimit.setLimit(userLimit.getLimit(true), true);
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      UserSummaryService.encrypt(userSummary);
    ExpenseEntityDao.getEntityManager().merge(userLimit);
  }

}
