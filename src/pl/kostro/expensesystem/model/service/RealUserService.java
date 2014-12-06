package pl.kostro.expensesystem.model.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.RealUser;

public class RealUserService {
  
  private EntityManager em;
  
  public RealUserService() {
    this.em = AdapterDB.getEntityManager();
  }
  
  public RealUser createRealUser(String name, String password, String email) {
    RealUser realUser = new RealUser();
    realUser.setName(name);
    realUser.setPassword(password);
    realUser.setEmail(email);
    em.persist(realUser);
    return realUser;
  }
  
  public void removeProfessor(int id) {
    RealUser emp = findRealUser(id);
    if (emp != null) {
      em.remove(emp);
    }
  }

  public RealUser findRealUser(int id) {
    return em.find(RealUser.class, id);
  }
  
  public RealUser getUserData(String userName, String password) {
    AdapterDB.begin(em);
    Query q = em.createQuery("from RealUser where name = :name and password = :password", RealUser.class);
    q.setParameter("name", userName);
    q.setParameter("password", password);
    RealUser loggedUser = null;
    try {
      loggedUser = (RealUser) q.getSingleResult();
      AdapterDB.commit(em);
    } catch (NoResultException e) {

    } finally {
      AdapterDB.close(em);
    }
    return loggedUser;
  }
  
  public RealUser findRealUser(String userName) {
    AdapterDB.begin(em);
    Query q = em.createQuery("from RealUser where name = :name", RealUser.class);
    q.setParameter("name", userName);
    RealUser loggedUser = null;
    try {
      loggedUser = (RealUser) q.getSingleResult();
      AdapterDB.commit(em);
    } catch (NoResultException e) {

    } finally {
      AdapterDB.close(em);
    }
    return loggedUser;
  }

}
