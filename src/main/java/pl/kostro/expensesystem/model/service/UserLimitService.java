package pl.kostro.expensesystem.model.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.RealUserEntity;
import pl.kostro.expensesystem.model.UserEntity;
import pl.kostro.expensesystem.model.UserLimitEntity;
import pl.kostro.expensesystem.model.UserSummaryEntity;
import pl.kostro.expensesystem.model.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.model.repository.RealUserRepository;
import pl.kostro.expensesystem.model.repository.UserLimitRepository;

@Service
public class UserLimitService {
  
  @Autowired
  private RealUserRepository rur;
  @Autowired
  private UserLimitRepository ulr;
  @Autowired
  private ExpenseSheetRepository eshr;
  
  @Autowired
  private UserSummaryService uss;

  private static Logger logger = LogManager.getLogger();
  
  public void createUserLimit(ExpenseSheetEntity expenseSheet, UserEntity user) {
    LocalDateTime stopper = LocalDateTime.now();
    UserLimitEntity userLimit = ulr.save(new UserLimitEntity(user, expenseSheet.getUserLimitList().size()));
    expenseSheet.getUserLimitList().add(userLimit);
    expenseSheet = eshr.save(expenseSheet);

    if (user instanceof RealUserEntity) {
      RealUserEntity realUser = (RealUserEntity) user;
      realUser.getExpenseSheetList().add(expenseSheet);
      rur.save(realUser);
      logger.info("createUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public void merge(UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    ulr.save(userLimit);
    logger.info("merge for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeUserLimit(ExpenseSheetEntity expenseSheet, UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getUserLimitList().remove(userLimit);
    ulr.delete(userLimit);
    if (!(userLimit.getUser() instanceof RealUserEntity))
      rur.delete(userLimit.getUser().getId());
    logger.info("removeUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void decrypt(UserLimitEntity userLimit) {
    for (UserSummaryEntity userSummary : userLimit.getUserSummaryList())
      uss.decrypt(userSummary);
  }

  public void encrypt(UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    userLimit.setLimit(userLimit.getLimit(true), true);
    for (UserSummaryEntity userSummary : userLimit.getUserSummaryList())
      uss.encrypt(userSummary);
    ulr.save(userLimit);
    logger.info("encrypt for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  @Transactional
  public void fetchUserSummaryList(UserLimitEntity userLimit) {
    try {
      userLimit.getUserSummaryList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      UserLimitEntity attached = ulr.getOne(userLimit.getId());
      attached.getUserSummaryList().size();
      userLimit.setUserSummaryList(attached.getUserSummaryList());
      logger.info("fetchUserSummaryList for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

}
