package pl.kostro.expensesystem.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import pl.kostro.expensesystem.model.AbstractEntity;

public class ExpenseEntityDao {
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
  
  @SuppressWarnings("unchecked")
  public static <T extends AbstractEntity> List<T> findByNamedQueryWithParameters(final String queryName, final Map<String, ?> parameters, final Class<T> entityClass) {
    try {
      return createNamedQueryWithParameters(queryName, parameters, entityClass).getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T extends AbstractEntity> T findSingleByNamedQueryWithParameters(final String queryName, final Map<String, ?> parameters, final Class<T> entityClass) {
    try {
      return (T) createNamedQueryWithParameters(queryName, parameters, entityClass).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
  
  private static <T extends AbstractEntity> Query createNamedQueryWithParameters(final String queryName, final Map<String, ?> parameters, final Class<T> entityClass) {
    Query namedQuery = getEntityManager().createNamedQuery(queryName, entityClass);
    if (parameters != null) {
      for (Map.Entry<String, ?> entry : parameters.entrySet()) {
        namedQuery.setParameter(entry.getKey(), entry.getValue());
      }
    }
    return namedQuery;
  }

}
