package pl.kostro.expensesystem.model.service;

import javax.persistence.EntityManager;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.User;

public class UserService {
  
  private EntityManager em;
  
  public UserService() {
    this.em = AdapterDB.getEntityManager();
  }
  
  public User createUser(String name) {
    User user = new User();
    user.setName(name);
    em.persist(user);
    return user;
  }
  
  public void removeProfessor(int id) {
    User emp = findUser(id);
    if (emp != null) {
      em.remove(emp);
    }
  }

  public User findUser(int id) {
    return em.find(User.class, id);
  }
  
  public void createUser(User user) {
    AdapterDB.begin(em);
    try {
      em.persist(user);
      AdapterDB.commit(em);
    } finally {
      AdapterDB.close(em);
    }
  }
}
