package pl.kostro.expensesystem.model.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.business.ExpenseSheet;
import pl.kostro.expensesystem.business.User;
import pl.kostro.expensesystem.business.ExpenseSheet;
import pl.kostro.expensesystem.business.RealUser;
import pl.kostro.expensesystem.business.User;
import pl.kostro.expensesystem.business.UserLimit;
import pl.kostro.expensesystem.business.UserSummary;
import pl.kostro.expensesystem.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.RealUserEntity;
import pl.kostro.expensesystem.model.UserEntity;
import pl.kostro.expensesystem.model.UserLimitEntity;
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
  
  public void createUserLimit(ExpenseSheet expenseSheet, User user) {
    LocalDateTime stopper = LocalDateTime.now();
    UserEntity userEntity = new UserEntity();
    ExpenseSheetEntity expenseSheetEntity = new ExpenseSheetEntity();
//    UserLimitEntity userLimitEntity = ulr.save(new UserLimitEntity(userEntity, expenseSheetEntity.getUserLimitList().size()));
    UserLimit userLimit = new UserLimit(user, expenseSheet.getUserLimitList().size());
    expenseSheet.getUserLimitList().add(userLimit);
    expenseSheetEntity = eshr.save(expenseSheetEntity);

    if (user instanceof RealUser) {
      RealUser realUser = (RealUser) user;
      realUser.getExpenseSheetList().add(expenseSheet);
      RealUserEntity realUserEntity = new RealUserEntity();
      rur.save(realUserEntity);
      logger.info("createUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public void merge(UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    UserLimitEntity userLimitEntity = new UserLimitEntity();
    ulr.save(userLimitEntity);
    logger.info("merge for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getUserLimitList().remove(userLimit);
    UserLimitEntity userLimitEntity = new UserLimitEntity();
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
//      userLimit.setUserSummaryList(attached.getUserSummaryList());
      logger.info("fetchUserSummaryList for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

}
