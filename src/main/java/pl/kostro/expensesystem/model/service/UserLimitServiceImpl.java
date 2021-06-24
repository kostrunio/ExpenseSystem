package pl.kostro.expensesystem.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.repository.UserLimitRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UserLimitServiceImpl implements UserLimitService {
  
  private UserLimitRepository repository;

  private Logger logger = LogManager.getLogger();

  @Autowired
  public UserLimitServiceImpl(UserLimitRepository repository) {
    this.repository = repository;
  }
  
  public UserLimitEntity create(UserEntity user, int orderId) {
    LocalDateTime stopper = LocalDateTime.now();
    UserLimitEntity userLimit = repository.save(new UserLimitEntity(user, orderId));
    logger.info("createUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return userLimit;
  }

  public void merge(UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.save(userLimit);
    logger.info("merge for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void remove(UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.delete(userLimit);
    logger.info("removeUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void decrypt(UserLimitEntity userLimit) {
    userLimit.getLimit();
  }

  public void encrypt(UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    userLimit.setLimit(userLimit.getLimit(true), true);
    repository.save(userLimit);
    logger.info("encrypt for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  @Transactional
  public void fetchUserSummaryList(UserLimitEntity userLimit) {
    try {
      userLimit.getUserSummaryList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      UserLimitEntity attached = repository.getOne(userLimit.getId());
      attached.getUserSummaryList().size();
      userLimit.setUserSummaryList(attached.getUserSummaryList());
      logger.info("fetchUserSummaryList for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

}
