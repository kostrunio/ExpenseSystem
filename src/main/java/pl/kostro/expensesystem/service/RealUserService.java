package pl.kostro.expensesystem.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.NoResultException;

import com.google.gwt.thirdparty.guava.common.collect.ImmutableMap;

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

  public static void refresh(RealUser realUser) {
    ExpenseEntityDao.getEntityManager().refresh(realUser);
  }

  public void createRealUser(String name, String password, String email) {
    ExpenseEntityDao.begin();
    try {
      RealUser realUser = new RealUser();
      realUser.setName(name);
      messageDigest.update(password.getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
      realUser.setEmail(email);
      ExpenseEntityDao.getEntityManager().persist(realUser);
      ExpenseEntityDao.commit();
    } finally {
    }
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
      loggedUser = ExpenseEntityDao.findSingleByNamedQueryWithParameters("findLoggedUser", ImmutableMap.of("name", userName, "password", new String(messageDigest.digest())), RealUser.class);
      loggedUser.setKeyString(password);
      ExpenseEntityDao.commit();
    } catch (NoResultException e) {
    } finally {
    }
    return loggedUser;
  }

  public static RealUser findRealUser(String userName) {
    ExpenseEntityDao.begin();
    RealUser loggedUser = null;
    try {
      loggedUser = ExpenseEntityDao.findSingleByNamedQueryWithParameters("findUser", ImmutableMap.of("name", userName), RealUser.class);
      ExpenseEntityDao.commit();
    } catch (NoResultException e) {

    } finally {
    }
    return loggedUser;
  }

  public static void removeExpenseSheet(ExpenseSheet expenseSheet) {
    List<RealUser> realUsers = null;
    realUsers = ExpenseEntityDao.findByNamedQueryWithParameters("findUsersWithExpenseSheet", ImmutableMap.of("expenseSheet", expenseSheet), RealUser.class);
    if (realUsers != null)
      for (RealUser realUser : realUsers) {
        if (realUser.getDefaultExpenseSheet() != null && realUser.getDefaultExpenseSheet().equals(expenseSheet))
          realUser.setDefaultExpenseSheet(null);
        realUser.getExpenseSheetList().remove(expenseSheet);
      }
  }

}
