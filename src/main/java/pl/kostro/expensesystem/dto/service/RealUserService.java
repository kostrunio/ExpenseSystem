package pl.kostro.expensesystem.dto.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dao.service.RealUserDao;
import pl.kostro.expensesystem.dto.model.ExpenseSheet;
import pl.kostro.expensesystem.dto.model.RealUser;

@Service
public class RealUserService {
  
  @Autowired
  private RealUserDao rus;

  private static Logger logger = LogManager.getLogger();
  
  static MessageDigest messageDigest;

  static {
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
    }
  }

  public RealUser createRealUser(String name, String password, String email) {
    LocalDateTime stopper = LocalDateTime.now();
    RealUser realUser = new RealUser(name);
    messageDigest.update(password.getBytes());
    realUser.setPassword(new String(messageDigest.digest()));
    realUser.setEmail(email);
    rus.save(realUser);
    logger.info("createRealUser for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public void merge(RealUser realUser, boolean passwordChange) {
    LocalDateTime stopper = LocalDateTime.now();
    if (passwordChange) {
      messageDigest.update(realUser.getClearPassword().getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
    }
    rus.merge(realUser);
    logger.info("merge for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void refresh(RealUser realUser) {
    LocalDateTime stopper = LocalDateTime.now();
    rus.refresh(realUser);
    logger.info("refresh for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void setDefaultExpenseSheet(RealUser realUser, ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    realUser.setDefaultExpenseSheet(expenseSheet);
    rus.save(realUser);
    logger.info("setDefaultExpenseSheet for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUser getUserData(String userName, String password) {
    LocalDateTime stopper = LocalDateTime.now();
    messageDigest.update(password.getBytes());
    RealUser realUser = rus.findByNameAndPassword(userName, messageDigest.digest());
    if (realUser == null) return null;
    realUser.setClearPassword(password);
    logger.info("getUserData for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public RealUser findRealUser(String userName) {
    LocalDateTime stopper = LocalDateTime.now();
    RealUser realUser = rus.findByName(userName);
    logger.info("findRealUser for {} finish: {} ms", userName, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public void fetchExpenseSheetList(RealUser realUser) {
    LocalDateTime stopper = LocalDateTime.now();
    rus.fetchExpenseSheetList(realUser);
    logger.info("fetchExpenseSheetList for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
