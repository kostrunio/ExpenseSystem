package pl.kostro.expensesystem.model.service;

import javax.persistence.EntityManager;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.User;

public class UserService {
  
  private EntityManager em;
  
  public UserService() {
    this.em = AdapterDB.getEntityManager();
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
  
  public User createUser(String name) {
    AdapterDB.begin(em);
    try {
      User user = new User();
      user.setName(name);
      em.persist(user);
      AdapterDB.commit(em);
      return user;
    } finally {
      AdapterDB.close(em);
    }
  }
}
