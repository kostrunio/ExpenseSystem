package pl.kostro.expensesystem.model.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;
import pl.kostro.expensesystem.model.repository.UserLimitRepository;
import pl.kostro.expensesystem.model.repository.UserSummaryRepository;
import pl.kostro.expensesystem.notification.ShowNotification;

@Service
public class UserSummaryService {
  
  @Autowired
  private UserLimitRepository ulr;
  @Autowired
  private UserSummaryRepository usr;
  
  @Autowired
  private UserLimitService uls;

  public UserSummary createUserSummary(UserLimit userLimit, Date date) {
    UserSummary userSummary = new UserSummary(date, userLimit.getLimit());
    usr.save(userSummary);
    userLimit.getUserSummaryList().add(userSummary);
    ulr.save(userLimit);
    return userSummary;
  }

  public UserSummary merge(UserSummary userSummary) {
    usr.save(userSummary);
    return userSummary;
  }

  public void decrypt(UserSummary userSummary) {
    userSummary.getLimit();
    userSummary.getSum();
  }

  public void encrypt(UserSummary userSummary) {
    userSummary.setLimit(userSummary.getLimit(true), true);
    userSummary.setSum(userSummary.getSum(true), true);
    usr.save(userSummary);
  }

  public BigDecimal calculateSum(UserLimit userLimit, Date date) {
    BigDecimal sum = new BigDecimal(0);
    for (UserSummary userSummary : userLimit.getUserSummaryList()) {
      if (!userSummary.getDate().after(date)) {
        sum = sum.add(userSummary.getLimit());
        sum = sum.subtract(userSummary.getSum());
      }
    }
    return sum;
  }

  public UserSummary findUserSummary(UserLimit userLimit, Date date) {
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      if (userSummary.getDate().getTime() == date.getTime())
        return userSummary;
    return createUserSummary(userLimit, date);
  }

  public void checkSummary(ExpenseSheet expenseSheet, Date date) {
    if (expenseSheet.getFilter() != null)
      return;
    for (UserLimit userLimit : expenseSheet.getUserLimitList()) {
      uls.fetchUserSummaryList(userLimit);
      UserSummary userSummary = findUserSummary(userLimit, date);
      BigDecimal exSummary = new BigDecimal(0);
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        exSummary = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      if (userSummary.getSum().doubleValue() != exSummary.doubleValue()) {
        ShowNotification.changeSummary(userLimit.getUser().getName(), userSummary.getSum(), exSummary);
        userSummary.setSum(exSummary);
        merge(userSummary);
      }
    }
  }

}
