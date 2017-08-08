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
    logger.info("createRealUser finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public void merge(RealUser realUser, boolean passwordChange) {
    LocalDateTime stopper = LocalDateTime.now();
    if (passwordChange) {
      messageDigest.update(realUser.getClearPassword().getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
    }
    rur.save(realUser);
    logger.info("merge finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUser refresh(RealUser realUser) {
    return rur.findOne(realUser.getId());
  }

  public void setDefaultExpenseSheet(RealUser realUser, ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    realUser.setDefaultExpenseSheet(expenseSheet);
    rur.save(realUser);
    logger.info("setDefaultExpenseSheet finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUser getUserData(String userName, String password) {
    LocalDateTime stopper = LocalDateTime.now();
    messageDigest.update(password.getBytes());
    RealUser loggedUser = rur.findByNameAndPassword(userName, new String(messageDigest.digest()));
    if (loggedUser == null) return null; 
    loggedUser.setClearPassword(password);
    loggedUser.setLogDate(LocalDateTime.now());
    if (loggedUser.getPasswordByte() == null)
      loggedUser.setPasswordByte(messageDigest.digest());
    rur.save(loggedUser);
    logger.info("getUserData finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return loggedUser;
  }

  public RealUser findRealUser(String userName) {
    LocalDateTime stopper = LocalDateTime.now();
    RealUser loggedUser = null;
    try {
      loggedUser = rur.findByName(userName);
    } catch (NoResultException e) {

    }
    logger.info("findRealUser finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return loggedUser;
  }

  @Transactional
  public void fetchExpenseSheetList(RealUser realUser) {
    LocalDateTime stopper = LocalDateTime.now();
    try {
      realUser.getExpenseSheetList().size();
    } catch (LazyInitializationException e) {
      RealUser attached = rur.getOne(realUser.getId());
      attached.getExpenseSheetList().size();
      realUser.setExpenseSheetList(attached.getExpenseSheetList());
    }
    logger.info("fetchExpenseSheetList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
