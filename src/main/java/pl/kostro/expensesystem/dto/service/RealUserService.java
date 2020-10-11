package pl.kostro.expensesystem.dto.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dao.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.dto.model.ExpenseSheet;
import pl.kostro.expensesystem.dto.model.RealUser;
import pl.kostro.expensesystem.dao.model.RealUserEntity;
import pl.kostro.expensesystem.dao.repository.RealUserRepository;

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
    RealUser realUser = new RealUser(name);
    messageDigest.update(password.getBytes());
    realUser.setPassword(new String(messageDigest.digest()));
    realUser.setEmail(email);
    RealUserEntity realUserEntity = new RealUserEntity();
    BeanUtils.copyProperties(realUser, realUserEntity);
    rur.save(realUserEntity);
    logger.info("createRealUser for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public void merge(RealUser realUser, boolean passwordChange) {
    LocalDateTime stopper = LocalDateTime.now();
    if (passwordChange) {
      messageDigest.update(realUser.getClearPassword().getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
    }
    RealUserEntity realUserEntity = new RealUserEntity();
    BeanUtils.copyProperties(realUser, realUserEntity);
    rur.save(realUserEntity);
    logger.info("merge for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUser refresh(RealUser realUser) {
    RealUserEntity realUserEntity = new RealUserEntity();
    realUserEntity = rur.findOne(realUserEntity.getId());
    return realUser;
  }

  public void setDefaultExpenseSheet(RealUser realUser, ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    realUser.setDefaultExpenseSheet(expenseSheet);
    RealUserEntity realUserEntity = new RealUserEntity();
    BeanUtils.copyProperties(realUser, realUserEntity);
    rur.save(realUserEntity);
    logger.info("setDefaultExpenseSheet for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUser getUserData(String userName, String password) {
    LocalDateTime stopper = LocalDateTime.now();
    messageDigest.update(password.getBytes());
    RealUserEntity realUserEntity = rur.findByNameAndPassword(userName, new String(messageDigest.digest()));
    RealUser realUser = new RealUser(realUserEntity.getName());
    if (realUser == null) return null; 
    realUser.setClearPassword(password);
    realUser.setLogDate(LocalDateTime.now());
    if (realUser.getPasswordByte() == null)
      realUser.setPasswordByte(messageDigest.digest());
    BeanUtils.copyProperties(realUser, realUserEntity);
    rur.save(realUserEntity);
    logger.info("getUserData for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public RealUser findRealUser(String userName) {
    LocalDateTime stopper = LocalDateTime.now();
    RealUser realUser = null;
    RealUserEntity realUserEntity = null;
    try {
      realUserEntity = rur.findByName(userName);
      BeanUtils.copyProperties(realUserEntity, realUser);
    } catch (NoResultException e) {
      return null;
    } finally {
      logger.info("findRealUser for {} finish: {} ms", userName, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
    return realUser;
  }

  @Transactional
  public void fetchExpenseSheetList(RealUser realUser) {
    try {
      realUser.getExpenseSheetList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      RealUserEntity attached = rur.getOne(realUser.getId());
      attached.getExpenseSheetList().size();
      realUser.getExpenseSheetList().clear();
      for (ExpenseSheetEntity expenseSheetEntity : attached.getExpenseSheetList()) {
        ExpenseSheet expenseSheet = new ExpenseSheet();
        BeanUtils.copyProperties(expenseSheetEntity, expenseSheet);
        realUser.getExpenseSheetList().add(expenseSheet);
      }
      logger.info("fetchExpenseSheetList for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }
}
