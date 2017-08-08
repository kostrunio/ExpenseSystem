package pl.kostro.expensesystem.model.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;
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
    UserLimit userLimit = ulr.save(new UserLimit(user, expenseSheet.getUserLimitList().size()));
    expenseSheet.getUserLimitList().add(userLimit);
    expenseSheet = eshr.save(expenseSheet);

    if (user instanceof RealUser) {
      RealUser realUser = (RealUser) user;
      realUser.getExpenseSheetList().add(expenseSheet);
      rur.save(realUser);
      logger.info("createUserLimit finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public void merge(UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    ulr.save(userLimit);
    logger.info("merge finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getUserLimitList().remove(userLimit);
    ulr.delete(userLimit);
    if (!(userLimit.getUser() instanceof RealUser))
      rur.delete(userLimit.getUser().getId());
    logger.info("removeUserLimit finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void decrypt(UserLimit userLimit) {
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      uss.decrypt(userSummary);
  }

  public void encrypt(UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    userLimit.setLimit(userLimit.getLimit(true), true);
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      uss.encrypt(userSummary);
    ulr.save(userLimit);
    logger.info("encrypt finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  @Transactional
  public void fetchUserSummaryList(UserLimit userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    try {
      userLimit.getUserSummaryList().size();
    } catch (LazyInitializationException e) {
      UserLimit attached = ulr.getOne(userLimit.getId());
      attached.getUserSummaryList().size();
      userLimit.setUserSummaryList(attached.getUserSummaryList());
    }
    logger.info("fetchUserSummaryList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

}
