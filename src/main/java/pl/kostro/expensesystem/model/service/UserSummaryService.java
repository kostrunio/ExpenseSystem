package pl.kostro.expensesystem.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

  private static Logger logger = LogManager.getLogger();
  
  public UserSummary createUserSummary(UserLimit userLimit, LocalDate date) {
    LocalDateTime stopper = LocalDateTime.now();
    UserSummary userSummary = new UserSummary(date, userLimit.getLimit());
    usr.save(userSummary);
    userLimit.getUserSummaryList().add(userSummary);
    ulr.save(userLimit);
    logger.info("createUserSummary finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userSummary;
  }

  public UserSummary merge(UserSummary userSummary) {
    LocalDateTime stopper = LocalDateTime.now();
    usr.save(userSummary);
    logger.info("merge finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userSummary;
  }

  public void decrypt(UserSummary userSummary) {
    userSummary.getLimit();
    userSummary.getSum();
  }

  public void encrypt(UserSummary userSummary) {
    LocalDateTime stopper = LocalDateTime.now();
    userSummary.setLimit(userSummary.getLimit(true), true);
    userSummary.setSum(userSummary.getSum(true), true);
    usr.save(userSummary);
    logger.info("encrypt finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public BigDecimal calculateSum(UserLimit userLimit, LocalDate date) {
    BigDecimal sum = new BigDecimal(0);
    uls.fetchUserSummaryList(userLimit);
    for (UserSummary userSummary : userLimit.getUserSummaryList()) {
      if (!userSummary.getDate().isAfter(date)) {
        sum = sum.add(userSummary.getLimit());
        sum = sum.subtract(userSummary.getSum());
      }
    }
    return sum;
  }

  public UserSummary findUserSummary(UserLimit userLimit, LocalDate date) {
    Optional<UserSummary> result = userLimit.getUserSummaryList().stream()
        .filter(us -> us.getDate().isEqual(date))
        .findFirst();
    if (result.isPresent())
      return result.get();
    return createUserSummary(userLimit, date);
  }

  public void checkSummary(ExpenseSheet expenseSheet, LocalDate date) {
    LocalDateTime stopper = LocalDateTime.now();
    if (expenseSheet.getFilter() != null)
      return;
    for (UserLimit userLimit : expenseSheet.getUserLimitList()) {
      uls.fetchUserSummaryList(userLimit);
      UserSummary userSummary = findUserSummary(userLimit, date);
      BigDecimal exSummary = new BigDecimal(0);
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        exSummary = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      if (userSummary.getSum().compareTo(exSummary) != 0) {
        ShowNotification.changeSummary(userLimit.getUser().getName(), userSummary.getSum(), exSummary);
        userSummary.setSum(exSummary);
        merge(userSummary);
      }
    }
    logger.info("checkSummary finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

}
