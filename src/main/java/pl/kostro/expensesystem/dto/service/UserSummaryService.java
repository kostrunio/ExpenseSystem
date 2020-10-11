package pl.kostro.expensesystem.dto.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dao.service.UserLimitDao;
import pl.kostro.expensesystem.dao.service.UserSummaryDao;
import pl.kostro.expensesystem.dto.model.ExpenseSheet;
import pl.kostro.expensesystem.dto.model.UserLimit;
import pl.kostro.expensesystem.dto.model.UserSummary;
import pl.kostro.expensesystem.notification.ShowNotification;

@Service
public class UserSummaryService {
  
  @Autowired
  private UserLimitDao uld;
  @Autowired
  private UserSummaryDao uss;
  
  @Autowired
  private UserLimitService uls;

  private static Logger logger = LogManager.getLogger();
  
  public UserSummary createUserSummary(UserLimit userLimit, LocalDate date) {
    LocalDateTime stopper = LocalDateTime.now();
    UserSummary userSummary = new UserSummary(date, userLimit.getLimit());
    userLimit.getUserSummaryList().add(userSummary);
    uss.save(userSummary);
    logger.info("createUserSummary for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userSummary;
  }

  public UserSummary merge(UserSummary userSummary) {
    LocalDateTime stopper = LocalDateTime.now();
    uss.merge(userSummary);
    logger.info("merge for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userSummary;
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
      logger.debug("checkSummary for: {} at {}", userLimit, date);
      UserSummary userSummary = findUserSummary(userLimit, date);
      BigDecimal exSummary = new BigDecimal(0);
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        exSummary = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      logger.debug("exSummary: {}; userSummary: {}", exSummary, userSummary.getSum());
      if (userSummary.getSum().compareTo(exSummary) != 0) {
        ShowNotification.changeSummary(userLimit.getUser().getName(), userSummary.getSum(), exSummary);
        userSummary.setSum(exSummary);
        merge(userSummary);
      }
    }
    logger.info("checkSummary for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public static void decrypt(UserSummary userSummary) {
    userSummary.getLimit();
    userSummary.getSum();
  }

  public void encrypt(UserSummary userSummary) {
    LocalDateTime stopper = LocalDateTime.now();
    uss.merge(userSummary);
    logger.info("encrypt for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
