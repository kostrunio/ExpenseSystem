package pl.kostro.expensesystem.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;

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
  
  public static void createUser(User user) {
    begin();
    try {
      entityManager.persist(user);
      commit();
    } finally {
      close();
    }
  }

  public static void createUser(RealUser loggedUser) {
    begin();
    try {
      entityManager.persist(loggedUser);
      commit();
    } finally {
      close();
    }
  }

  public static void createExpenseSheet(RealUser loggedUser, ExpenseSheet expenseSheet) {
    begin();
    try {
      UserLimit userLimit = new UserLimit(loggedUser, 0);
      entityManager.persist(userLimit);
      expenseSheet.getUserLimitList().add(userLimit);
      expenseSheet = entityManager.merge(expenseSheet);
      loggedUser.getExpenseSheetList().add(expenseSheet);
      loggedUser = entityManager.merge(loggedUser);
      commit();
    } finally {
      close();
    }
  }

  public static void createCategory(ExpenseSheet expenseSheet, Category category) {
    begin();
    try {
      entityManager.persist(category);
      expenseSheet.getCategoryList().add(category);
      expenseSheet = entityManager.merge(expenseSheet);
      commit();
    } finally {
      close();
    }
  }

  public static RealUser findRealUser(String userName) {
    begin();
    Query q = entityManager.createQuery("from RealUser where name = :name", RealUser.class);
    q.setParameter("name", userName);
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

  public static void createUserLimit(ExpenseSheet expenseSheet, UserLimit userLimit) {
    begin();
    try {
      userLimit = entityManager.merge(userLimit);
      expenseSheet.getUserLimitList().add(userLimit);
      expenseSheet = entityManager.merge(expenseSheet);

      if (userLimit.getUser() instanceof RealUser) {
        RealUser persistUser = entityManager.find(RealUser.class, userLimit.getUser().getId());
        persistUser.getExpenseSheetList().add(expenseSheet);
        entityManager.merge(persistUser);
      }
      commit();
    } finally {
      close();
    }
  }

  public static void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    begin();
    try {
      expenseSheet.removeExpense(expense);
      entityManager.remove(entityManager.find(Expense.class, expense.getId()));
      expenseSheet = entityManager.merge(expenseSheet);
      commit();
    } finally {
      close();
    }
  }

  public static void creteExpense(ExpenseSheet expenseSheet, Expense expense) {
    begin();
    try {
      expense = entityManager.merge(expense);
      expenseSheet.addExpense(expense);
      expenseSheet = entityManager.merge(expenseSheet);
      commit();
    } finally {
      close();
    }
  }
}
