package pl.kostro.expensesystem.model.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.repository.UserLimitRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class UserLimitServiceImpl implements UserLimitService {
  
  private final UserLimitRepository repository;

  @Autowired
  public UserLimitServiceImpl(UserLimitRepository repository) {
    this.repository = repository;
  }
  
  public UserLimitEntity create(UserEntity user, int orderId) {
    LocalDateTime stopper = LocalDateTime.now();
    UserLimitEntity userLimit = repository.save(new UserLimitEntity(user, orderId));
    log.info("createUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userLimit;
  }

  public void merge(UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.save(userLimit);
    log.info("merge for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void remove(UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.delete(userLimit);
    log.info("removeUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  @Transactional
  public void fetchUserSummaryList(UserLimitEntity userLimit) {
    try {
      userLimit.getUserSummaryList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      UserLimitEntity attached = repository.getReferenceById(userLimit.getId());
      attached.getUserSummaryList().size();
      userLimit.setUserSummaryList(attached.getUserSummaryList());
      log.info("fetchUserSummaryList for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  void decrypt(UserLimitEntity userLimit) {
    userLimit.getLimit();
  }

  void encrypt(UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    userLimit.setLimit(userLimit.getLimit(true), true);
    repository.save(userLimit);
    log.info("encrypt for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

}
