package pl.kostro.expensesystem.service;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.RealUser;

public class RealUserService {
  
  public void removeRealUser(int id) {
    RealUser emp = findRealUser(id);
    if (emp != null) {
      ExpenseEntityDao.getEntityManager().remove(emp);
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
  
  public RealUser getUserData(String userName, String password) {
    ExpenseEntityDao.begin();
    Query q = ExpenseEntityDao.getEntityManager().createQuery("from RealUser where name = :name and password = :password", RealUser.class);
    q.setParameter("name", userName);
    q.setParameter("password", password);
    RealUser loggedUser = null;
    try {
      loggedUser = (RealUser) q.getSingleResult();
      ExpenseEntityDao.commit();
    } catch (NoResultException e) {

    } finally {
      ExpenseEntityDao.close();
    }
    return loggedUser;
  }
  
  public RealUser findRealUser(String userName) {
    ExpenseEntityDao.begin();
    Query q = ExpenseEntityDao.getEntityManager().createQuery("from RealUser where name = :name", RealUser.class);
    q.setParameter("name", userName);
    RealUser loggedUser = null;
    try {
      loggedUser = (RealUser) q.getSingleResult();
      ExpenseEntityDao.commit();
    } catch (NoResultException e) {

    } finally {
      ExpenseEntityDao.close();
    }
    return loggedUser;
  }

}
