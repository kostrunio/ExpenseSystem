package pl.kostro.expensesystem.model.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import com.google.common.collect.ImmutableMap;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

public class RealUserService {

  static MessageDigest messageDigest;

  static {
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
    }
  }

  public static RealUser createRealUser(String name, String password, String email) {
    ExpenseEntityDao.begin();
    RealUser realUser = new RealUser();
    try {
      realUser.setName(name);
      messageDigest.update(password.getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
      realUser.setEmail(email);
      ExpenseEntityDao.getEntityManager().persist(realUser);
      ExpenseEntityDao.commit();
    } finally {
    }
    return realUser;
  }

  public static void merge(RealUser realUser, boolean passwordChange) {
    ExpenseEntityDao.begin();
    try {
      if (passwordChange) {
        messageDigest.update(realUser.getClearPassword().getBytes());
        realUser.setPassword(new String(messageDigest.digest()));
      }
      ExpenseEntityDao.getEntityManager().merge(realUser);
      ExpenseEntityDao.commit();
    } finally {
    }
  }

  public static void refresh(RealUser realUser) {
    ExpenseEntityDao.getEntityManager().refresh(realUser);
  }

  public static void setDefaultExpenseSheet(RealUser realUser, ExpenseSheet expenseSheet) {
    ExpenseEntityDao.begin();
    try {
      realUser.setDefaultExpenseSheet(expenseSheet);
      ExpenseEntityDao.getEntityManager().merge(realUser);
      ExpenseEntityDao.commit();
    } finally {
    }
  }

  public static RealUser getUserData(String userName, String password) {
    ExpenseEntityDao.begin();
    RealUser loggedUser = null;
    try {
      messageDigest.update(password.getBytes());
      loggedUser = ExpenseEntityDao.findSingleByNamedQueryWithParameters("findLoggedUser",
          ImmutableMap.of("name", userName, "password", new String(messageDigest.digest())), RealUser.class);
      loggedUser.setClearPassword(password);
      loggedUser.setLogDate(new Date());
      if (loggedUser.getPasswordByte() == null)
        loggedUser.setPasswordByte(messageDigest.digest());
      ExpenseEntityDao.getEntityManager().merge(loggedUser);
      ExpenseEntityDao.commit();
    } catch (Exception e) {
      ExpenseEntityDao.close();
      ExpenseEntityDao.begin();
    } finally {
    }
    return loggedUser;
  }

  public static RealUser findRealUser(String userName) {
    ExpenseEntityDao.begin();
    RealUser loggedUser = null;
    try {
      loggedUser = ExpenseEntityDao.findSingleByNamedQueryWithParameters("findUser", ImmutableMap.of("name", userName),
          RealUser.class);
      ExpenseEntityDao.commit();
    } catch (NoResultException e) {

    } finally {
    }
    return loggedUser;
  }

}
