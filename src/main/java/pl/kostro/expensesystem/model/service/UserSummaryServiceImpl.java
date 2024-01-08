package pl.kostro.expensesystem.model.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;
import pl.kostro.expensesystem.model.repository.UserSummaryRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
public class UserSummaryServiceImpl implements UserSummaryService {
  
  private final UserSummaryRepository repository;
  
  @Autowired
  public UserSummaryServiceImpl(UserSummaryRepository repository) {
    this.repository = repository;
  }

  public UserSummaryEntity create(UserLimitEntity userLimit, LocalDate date) {
    LocalDateTime stopper = LocalDateTime.now();
    UserSummaryEntity userSummary = new UserSummaryEntity(date, userLimit.getLimit());
    repository.save(userSummary);
    log.info("createUserSummary for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userSummary;
  }

  public UserSummaryEntity merge(UserSummaryEntity userSummary) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.save(userSummary);
    log.info("merge for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
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
    log.info("encrypt for {} finish: {} ms", userSummary, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public BigDecimal calculateSum(UserLimitEntity userLimit, LocalDate date) {
    BigDecimal sum = new BigDecimal(0);
    for (UserSummaryEntity userSummary : userLimit.getUserSummaryList()) {
      if (!userSummary.getDate().isAfter(date)) {
        sum = sum.add(userSummary.getLimit());
        sum = sum.subtract(userSummary.getSum());
      }
    }
    return sum;
  }

  public UserSummaryEntity findUserSummary(UserLimitEntity userLimit, LocalDate date) {
    Optional<UserSummaryEntity> result = userLimit.getUserSummaryList().parallelStream()
        .filter(us -> us.getDate().isEqual(date))
        .findFirst();
    return result.orElse(null);
  }

}
