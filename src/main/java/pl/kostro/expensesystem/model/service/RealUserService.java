package pl.kostro.expensesystem.model.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.repository.RealUserRepository;

@Service
public class RealUserService {
  
  @Autowired
  private RealUserRepository rur;

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
    RealUser realUser = new RealUser();
    realUser.setName(name);
    messageDigest.update(password.getBytes());
    realUser.setPassword(new String(messageDigest.digest()));
    realUser.setEmail(email);
    rur.save(realUser);
    logger.info("createRealUser for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public void merge(RealUser realUser, boolean passwordChange) {
    LocalDateTime stopper = LocalDateTime.now();
    if (passwordChange) {
      messageDigest.update(realUser.getClearPassword().getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
    }
    rur.save(realUser);
    logger.info("merge for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUser refresh(RealUser realUser) {
    return rur.findOne(realUser.getId());
  }

  public void setDefaultExpenseSheet(RealUser realUser, ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    realUser.setDefaultExpenseSheet(expenseSheet);
    rur.save(realUser);
    logger.info("setDefaultExpenseSheet for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUser getUserData(String userName, String password) {
    LocalDateTime stopper = LocalDateTime.now();
    messageDigest.update(password.getBytes());
    RealUser realUser = rur.findByNameAndPassword(userName, new String(messageDigest.digest()));
    if (realUser == null) return null; 
    realUser.setClearPassword(password);
    realUser.setLogDate(LocalDateTime.now());
    if (realUser.getPasswordByte() == null)
      realUser.setPasswordByte(messageDigest.digest());
    rur.save(realUser);
    logger.info("getUserData for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public RealUser findRealUser(String userName) {
    LocalDateTime stopper = LocalDateTime.now();
    RealUser realUser = null;
    try {
      realUser = rur.findByName(userName);
    } catch (NoResultException e) {

    }
    logger.info("findRealUser for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  @Transactional
  public void fetchExpenseSheetList(RealUser realUser) {
    try {
      realUser.getExpenseSheetList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      RealUser attached = rur.getOne(realUser.getId());
      attached.getExpenseSheetList().size();
      realUser.setExpenseSheetList(attached.getExpenseSheetList());
      logger.info("fetchExpenseSheetList for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }
}
