package pl.kostro.expensesystem.service;

import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.User;

public class UserService {
  
  public void removeProfessor(int id) {
    User emp = findUser(id);
    if (emp != null) {
      AdapterDB.getEntityManager().remove(emp);
    }
  }

  public User findUser(int id) {
    return AdapterDB.getEntityManager().find(User.class, id);
  }
  
  public User createUser(String name) {
    AdapterDB.begin();
    try {
      User user = new User();
      user.setName(name);
      AdapterDB.getEntityManager().persist(user);
      AdapterDB.commit();
      return user;
    } finally {
      AdapterDB.close();
    }
  }
}
