package pl.kostro.expensesystem.model.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    Date stopper = new Date();
    RealUser realUser = new RealUser();
    realUser.setName(name);
    messageDigest.update(password.getBytes());
    realUser.setPassword(new String(messageDigest.digest()));
    realUser.setEmail(email);
    rur.save(realUser);
    logger.info("createRealUser finish: {} ms", new Date().getTime() - stopper.getTime());
    return realUser;
  }

  public void merge(RealUser realUser, boolean passwordChange) {
    Date stopper = new Date();
    if (passwordChange) {
      messageDigest.update(realUser.getClearPassword().getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
    }
    rur.save(realUser);
    logger.info("merge finish: {} ms", new Date().getTime() - stopper.getTime());
  }

  public RealUser refresh(RealUser realUser) {
    return rur.findOne(realUser.getId());
  }

  public void setDefaultExpenseSheet(RealUser realUser, ExpenseSheet expenseSheet) {
    Date stopper = new Date();
    realUser.setDefaultExpenseSheet(expenseSheet);
    rur.save(realUser);
    logger.info("setDefaultExpenseSheet finish: {} ms", new Date().getTime() - stopper.getTime());
  }

  public RealUser getUserData(String userName, String password) {
    Date stopper = new Date();
    messageDigest.update(password.getBytes());
    RealUser loggedUser = rur.findByNameAndPassword(userName, new String(messageDigest.digest()));
    if (loggedUser == null) return null; 
    loggedUser.setClearPassword(password);
    loggedUser.setLogDate(new Date());
    if (loggedUser.getPasswordByte() == null)
      loggedUser.setPasswordByte(messageDigest.digest());
    rur.save(loggedUser);
    logger.info("getUserData finish: {} ms", new Date().getTime() - stopper.getTime());
    return loggedUser;
  }

  public RealUser findRealUser(String userName) {
    Date stopper = new Date();
    RealUser loggedUser = null;
    try {
      loggedUser = rur.findByName(userName);
    } catch (NoResultException e) {

    }
    logger.info("findRealUser finish: {} ms", new Date().getTime() - stopper.getTime());
    return loggedUser;
  }

  public void fetchExpenseSheetList(RealUser realUser) {
    Date stopper = new Date();
    realUser.setExpenseSheetList(rur.fetchExpenseSheetList(realUser));
    logger.info("fetchExpenseSheetList finish: {} ms", new Date().getTime() - stopper.getTime());
  }
}
