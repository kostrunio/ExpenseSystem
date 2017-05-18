package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;
import pl.kostro.expensesystem.model.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.model.repository.RealUserRepository;
import pl.kostro.expensesystem.model.repository.UserLimitRepository;

public class UserLimitService {
  
  private RealUserRepository rur;
  private UserLimitRepository ulr;
  private ExpenseSheetRepository eshr;
  
  private UserSummaryService uss;

  public void createUserLimit(ExpenseSheet expenseSheet, User user) {
    UserLimit userLimit = ulr.save(new UserLimit(user, expenseSheet.getUserLimitList().size()));
    expenseSheet.getUserLimitList().add(userLimit);
    expenseSheet = eshr.save(expenseSheet);

    if (user instanceof RealUser) {
      RealUser realUser = (RealUser) user;
      realUser.getExpenseSheetList().add(expenseSheet);
      rur.save(realUser);
    }
  }

  public void merge(UserLimit userLimit) {
    ulr.save(userLimit);
  }

  public void removeUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    expenseSheet.getUserLimitList().remove(userLimit);
    ulr.delete(userLimit);
    if (!(userLimit.getUser() instanceof RealUser))
      rur.delete(userLimit.getUser().getId());
  }

  public void decrypt(UserLimit userLimit) {
    userLimit.getLimit();
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      uss.decrypt(userSummary);
  }

  public void encrypt(UserLimit userLimit) {
    userLimit.setLimit(userLimit.getLimit(true), true);
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      uss.encrypt(userSummary);
    ulr.save(userLimit);
  }

}
