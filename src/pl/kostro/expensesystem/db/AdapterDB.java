package pl.kostro.expensesystem.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import pl.kostro.expensesystem.model.RealUser;

public class AdapterDB {
  private static EntityManagerFactory entityManagerFactory;
  private static EntityManager entityManager;
  
  public static EntityManagerFactory getEntityManagerFactory() {
    if (entityManagerFactory == null)
      entityManagerFactory = Persistence.createEntityManagerFactory("expenseSystem");
    return entityManagerFactory;
  }
  
  public static EntityManager getEntityManager() {
    if (entityManager == null || !entityManager.isOpen())
      entityManager = getEntityManagerFactory().createEntityManager();
    return entityManager;
  }
  
  public static void begin() {
    getEntityManager().getTransaction().begin();
  }
  
  public static void commit() {
    getEntityManager().getTransaction().commit();
  }
  
  public static void close() {
    getEntityManager().close();
  }
  
  public static RealUser getUserData(String userName, String password) {
    begin();
    Query q = entityManager.createQuery("from RealUser where name = :name and password = :password", RealUser.class);
    q.setParameter("name", userName);
    q.setParameter("password", password);
    RealUser loggedUser = null;
    try {
      loggedUser = (RealUser) q.getSingleResult();
      commit();
    } catch (NoResultException e) {
      
    } finally {
      close();
    }
    return loggedUser;
  }

  public static void createUser(RealUser loggedUser) {
    begin();
    try {
      entityManager.persist(loggedUser);
      commit();
    } catch (NoResultException e) {
      
    } finally {
      close();
    }
  }
}
