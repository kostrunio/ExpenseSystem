package pl.kostro.expensesystem.dto.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dao.service.UserDao;
import pl.kostro.expensesystem.dao.service.UserLimitDao;
import pl.kostro.expensesystem.dto.model.*;
import pl.kostro.expensesystem.dao.repository.ExpenseSheetRepository;

@Service
public class UserLimitService {
  
  @Autowired
  private UserDao us;
  @Autowired
  private UserLimitDao uls;
  @Autowired
  private ExpenseSheetRepository eshr;
  
  @Autowired
  private UserSummaryService uss;

  private static Logger logger = LogManager.getLogger();
  
  public void createUserLimit(ExpenseSheet expenseSheet, User user) {
    LocalDateTime stopper = LocalDateTime.now();
    UserLimit userLimit = new UserLimit(user, expenseSheet.getUserLimitList().size());
    expenseSheet.getUserLimitList().add(userLimit);
    uls.save(userLimit);

    if (user instanceof RealUser) {
      RealUser realUser = (RealUser) user;
      realUser.getExpenseSheetList().add(expenseSheet);
      us.save(realUser);
      logger.info("createUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public void merge(UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    uls.merge(userLimit);
    logger.info("merge for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getUserLimitList().remove(userLimit);
    uls.delete(userLimit);
    if (!(userLimit.getUser() instanceof RealUser))
      us.delete(userLimit.getUser());
    logger.info("removeUserLimit for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  @Transactional
  public void fetchUserSummaryList(UserLimit userLimit) {
      LocalDateTime stopper = LocalDateTime.now();
      uls.fetchUserSummaryList(userLimit);
      logger.info("fetchUserSummaryList for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public static void decrypt(UserLimit userLimit) {
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      UserSummaryService.decrypt(userSummary);
  }

  public void encrypt(UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      uss.encrypt(userSummary);
    uls.merge(userLimit);
    logger.info("encrypt for {} finish: {} ms", userLimit, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
