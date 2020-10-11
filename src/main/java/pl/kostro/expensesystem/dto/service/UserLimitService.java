package pl.kostro.expensesystem.dto.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dao.model.*;
import pl.kostro.expensesystem.dto.model.*;
import pl.kostro.expensesystem.dao.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.dao.repository.RealUserRepository;
import pl.kostro.expensesystem.dao.repository.UserLimitRepository;

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
  
  public void createUserLimit(ExpenseSheet expenseSheet, User user) {
    LocalDateTime stopper = LocalDateTime.now();
    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);
    ExpenseSheetEntity expenseSheetEntity = new ExpenseSheetEntity();
    BeanUtils.copyProperties(expenseSheet, expenseSheetEntity);
    UserLimitEntity userLimitEntity = new UserLimitEntity(userEntity, expenseSheetEntity.getUserLimitList().size())
    userLimitEntity = ulr.save(userLimitEntity);
    UserLimit userLimit = new UserLimit();
    BeanUtils.copyProperties(userLimitEntity, userLimit);
    expenseSheet.getUserLimitList().add(userLimit);
    BeanUtils.copyProperties(expenseSheet, expenseSheetEntity);
    expenseSheetEntity = eshr.save(expenseSheetEntity);
    BeanUtils.copyProperties(expenseSheetEntity, expenseSheet);

    if (user instanceof RealUser) {
      RealUser realUser = (RealUser) user;
      realUser.getExpenseSheetList().add(expenseSheet);
      RealUserEntity realUserEntity = new RealUserEntity();
      BeanUtils.copyProperties(realUser, realUserEntity);
      rur.save(realUserEntity);
      logger.info("createUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public void merge(UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    UserLimitEntity userLimitEntity = new UserLimitEntity();
    BeanUtils.copyProperties(userLimit, userLimitEntity);
    ulr.save(userLimitEntity);
    logger.info("merge for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getUserLimitList().remove(userLimit);
    UserLimitEntity userLimitEntity = new UserLimitEntity();
    BeanUtils.copyProperties(userLimit, userLimitEntity);
    ulr.delete(userLimitEntity);
    if (!(userLimit.getUser() instanceof RealUser))
      rur.delete(userLimit.getUser().getId());
    logger.info("removeUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  @Transactional
  public void fetchUserSummaryList(UserLimit userLimit) {
    try {
      userLimit.getUserSummaryList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      UserLimitEntity attached = ulr.getOne(userLimit.getId());
      attached.getUserSummaryList().size();
      userLimit.getUserSummaryList().clear();
      for (UserSummaryEntity userSummaryEntity : attached.getUserSummaryList()) {
        UserSummary userSummary = new UserSummary();
        BeanUtils.copyProperties(userSummaryEntity, userSummary);
        userLimit.getUserSummaryList().add(userSummary);
      }
      logger.info("fetchUserSummaryList for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public static void decrypt(UserLimit userLimit) {
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      UserSummaryService.decrypt(userSummary);
  }

  public void encrypt(UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    userLimit.setLimit(userLimit.getLimit(true), true);
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      uss.encrypt(userSummary);
    ulr.save(userLimit);
    logger.info("encrypt for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
