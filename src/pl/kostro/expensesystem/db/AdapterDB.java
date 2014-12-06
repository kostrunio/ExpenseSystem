package pl.kostro.expensesystem.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

}
