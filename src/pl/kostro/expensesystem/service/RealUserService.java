package pl.kostro.expensesystem.service;

import javax.persistence.NoResultException;

import com.google.gwt.thirdparty.guava.common.collect.ImmutableMap;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

public class RealUserService {
  
  public void removeRealUser(int id) {
    RealUser rU = findRealUser(id);
    if (rU != null) {
      ExpenseEntityDao.getEntityManager().remove(rU);
    }
  }

  public RealUser findRealUser(int id) {
    return ExpenseEntityDao.getEntityManager().find(RealUser.class, id);
  }
  
  public void createRealUser(String name, String password, String email) {
    ExpenseEntityDao.begin();
    try {
      RealUser realUser = new RealUser();
      realUser.setName(name);
      realUser.setPassword(password);
      realUser.setEmail(email);
      ExpenseEntityDao.getEntityManager().persist(realUser);
      ExpenseEntityDao.commit();
    } finally {
      ExpenseEntityDao.close();
    }
  }
  
  public void setDefaultExpenseSheet(RealUser realUser, ExpenseSheet expenseSheet) {
	  ExpenseEntityDao.begin();
	  realUser.setDefaultExpenseSheet(expenseSheet);
	  try {
		  RealUser loggedUser = findRealUser(realUser.getId());
		  loggedUser.setDefaultExpenseSheet(expenseSheet);
		  ExpenseEntityDao.getEntityManager().merge(loggedUser);
		  ExpenseEntityDao.commit();
	  } finally {
	      ExpenseEntityDao.close();
	  }
  }
  
  public RealUser getUserData(String userName, String password) {
    ExpenseEntityDao.begin();
    RealUser loggedUser = null;
    try {
      loggedUser = ExpenseEntityDao.findSingleByNamedQueryWithParameters("findLoggedUser", ImmutableMap.of("name", userName, "password", password), RealUser.class);
      ExpenseEntityDao.commit();
    } catch (NoResultException e) {

    } finally {
      ExpenseEntityDao.close();
    }
    return loggedUser;
  }
  
  public RealUser findRealUser(String userName) {
    ExpenseEntityDao.begin();
    RealUser loggedUser = null;
    try {
      loggedUser = ExpenseEntityDao.findSingleByNamedQueryWithParameters("findUser", ImmutableMap.of("name", userName), RealUser.class);
      ExpenseEntityDao.commit();
    } catch (NoResultException e) {

    } finally {
      ExpenseEntityDao.close();
    }
    return loggedUser;
  }

}
