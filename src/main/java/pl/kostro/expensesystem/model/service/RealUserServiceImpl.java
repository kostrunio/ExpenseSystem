package pl.kostro.expensesystem.model.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.RealUserEntity;
import pl.kostro.expensesystem.model.repository.RealUserRepository;

@Service
public class RealUserServiceImpl implements RealUserService {

  private RealUserRepository repository;

  private Logger logger = LogManager.getLogger();
  
  static MessageDigest messageDigest;

  static {
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
    }
  }

  @Autowired
  public RealUserServiceImpl(RealUserRepository repository) {
    this.repository = repository;
  }

  public RealUserEntity createRealUser(String name, String password, String email) {
    LocalDateTime stopper = LocalDateTime.now();
    RealUserEntity realUser = new RealUserEntity();
    realUser.setName(name);
    messageDigest.update(password.getBytes());
    realUser.setPassword(new String(messageDigest.digest()));
    realUser.setEmail(email);
    repository.save(realUser);
    logger.info("createRealUser for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public void merge(RealUserEntity realUser, boolean passwordChange) {
    LocalDateTime stopper = LocalDateTime.now();
    if (passwordChange) {
      messageDigest.update(realUser.getClearPassword().getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
    }
    repository.save(realUser);
    logger.info("merge for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUserEntity refresh(RealUserEntity realUser) {
    return repository.findOne(realUser.getId());
  }

  public void setDefaultExpenseSheet(RealUserEntity realUser, ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    realUser.setDefaultExpenseSheet(expenseSheet);
    repository.save(realUser);
    logger.info("setDefaultExpenseSheet for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public RealUserEntity getUserData(String userName, String password) {
    LocalDateTime stopper = LocalDateTime.now();
    messageDigest.update(password.getBytes());
    RealUserEntity realUser = repository.findByNameAndPassword(userName, new String(messageDigest.digest()));
    if (realUser == null) return null; 
    realUser.setClearPassword(password);
    realUser.setLogDate(LocalDateTime.now());
    if (realUser.getPasswordByte() == null)
      realUser.setPasswordByte(messageDigest.digest());
    repository.save(realUser);
    logger.info("getUserData for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  public RealUserEntity findRealUser(String userName) {
    LocalDateTime stopper = LocalDateTime.now();
    RealUserEntity realUser = null;
    try {
      realUser = repository.findByName(userName);
    } catch (NoResultException e) {

    }
    logger.info("findRealUser for {} finish: {} ms", userName, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return realUser;
  }

  @Transactional
  public void fetchExpenseSheetList(RealUserEntity realUser) {
    try {
      realUser.getExpenseSheetList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      RealUserEntity attached = repository.getOne(realUser.getId());
      attached.getExpenseSheetList().size();
      realUser.setExpenseSheetList(attached.getExpenseSheetList());
      logger.info("fetchExpenseSheetList for {} finish: {} ms", realUser, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public void removeExpenseSheetFromUsers(ExpenseSheetEntity expenseSheet) {
    List<RealUserEntity> realUsers = repository.findUsersWithExpenseSheet(expenseSheet);
    if (realUsers != null) {
      for (RealUserEntity realUser : realUsers) {
        if (realUser.getDefaultExpenseSheet() != null && realUser.getDefaultExpenseSheet().equals(expenseSheet))
          realUser.setDefaultExpenseSheet(null);
        realUser.getExpenseSheetList().remove(expenseSheet);
      }
    }
  }
}
