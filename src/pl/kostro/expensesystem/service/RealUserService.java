package pl.kostro.expensesystem.service;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.RealUser;

public class RealUserService {
  
  public void removeRealUser(int id) {
    RealUser emp = findRealUser(id);
    if (emp != null) {
      AdapterDB.getEntityManager().remove(emp);
    }
  }

  public RealUser findRealUser(int id) {
    return AdapterDB.getEntityManager().find(RealUser.class, id);
  }
  
  public void createRealUser(String name, String password, String email) {
    AdapterDB.begin();
    try {
      RealUser realUser = new RealUser();
      realUser.setName(name);
      realUser.setPassword(password);
      realUser.setEmail(email);
      AdapterDB.getEntityManager().persist(realUser);
      AdapterDB.commit();
    } finally {
      AdapterDB.close();
    }
  }
  
  public RealUser getUserData(String userName, String password) {
    AdapterDB.begin();
    Query q = AdapterDB.getEntityManager().createQuery("from RealUser where name = :name and password = :password", RealUser.class);
    q.setParameter("name", userName);
    q.setParameter("password", password);
    RealUser loggedUser = null;
    try {
      loggedUser = (RealUser) q.getSingleResult();
      AdapterDB.commit();
    } catch (NoResultException e) {

    } finally {
      AdapterDB.close();
    }
    return loggedUser;
  }
  
  public RealUser findRealUser(String userName) {
    AdapterDB.begin();
    Query q = AdapterDB.getEntityManager().createQuery("from RealUser where name = :name", RealUser.class);
    q.setParameter("name", userName);
    RealUser loggedUser = null;
    try {
      loggedUser = (RealUser) q.getSingleResult();
      AdapterDB.commit();
    } catch (NoResultException e) {

    } finally {
      AdapterDB.close();
    }
    return loggedUser;
  }

}
