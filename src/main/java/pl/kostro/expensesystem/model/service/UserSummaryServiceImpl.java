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
import pl.kostro.expensesystem.model.UserSummaryEntity;
import pl.kostro.expensesystem.model.repository.UserLimitRepository;
import pl.kostro.expensesystem.model.repository.UserSummaryRepository;
import pl.kostro.expensesystem.notification.ShowNotification;

@Service
public class UserSummaryServiceImpl implements UserSummaryService {
  
  @Autowired
  private UserLimitRepository ulr;
  private UserSummaryRepository repository;
  
  private Logger logger = LogManager.getLogger();

  @Autowired
  public UserSummaryServiceImpl(UserSummaryRepository repository) {
    this.repository = repository;
  }

  public UserSummaryEntity createUserSummary(UserLimit userLimit, LocalDate date) {
    LocalDateTime stopper = LocalDateTime.now();
    UserSummaryEntity userSummary = new UserSummaryEntity(date, userLimit.getLimit());
    repository.save(userSummary);
    userLimit.getUserSummaryList().add(userSummary);
    ulr.save(userLimit);
    logger.info("createUserSummary for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userSummary;
  }

  public UserSummaryEntity merge(UserSummaryEntity userSummary) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.save(userSummary);
    logger.info("merge for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userSummary;
  }

  public void decrypt(UserSummaryEntity userSummary) {
    userSummary.getLimit();
    userSummary.getSum();
  }

  public void encrypt(UserSummaryEntity userSummary) {
    LocalDateTime stopper = LocalDateTime.now();
    userSummary.setLimit(userSummary.getLimit(true), true);
    userSummary.setSum(userSummary.getSum(true), true);
    repository.save(userSummary);
    logger.info("encrypt for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public BigDecimal calculateSum(UserLimit userLimit, LocalDate date) {
    BigDecimal sum = new BigDecimal(0);
    for (UserSummaryEntity userSummary : userLimit.getUserSummaryList()) {
      if (!userSummary.getDate().isAfter(date)) {
        sum = sum.add(userSummary.getLimit());
        sum = sum.subtract(userSummary.getSum());
      }
    }
    return sum;
  }

  public UserSummaryEntity findUserSummary(UserLimit userLimit, LocalDate date) {
    Optional<UserSummaryEntity> result = userLimit.getUserSummaryList().parallelStream()
        .filter(us -> us.getDate().isEqual(date))
        .findFirst();
    if (result.isPresent())
      return result.get();
    return createUserSummary(userLimit, date);
  }

}
