package pl.kostro.expensesystem.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.User;

public class UserService {
  
  public static void removeUser(int id) {
    User u = findUser(id);
    if (u != null) {
      ExpenseEntityDao.getEntityManager().remove(u);
    }
  }

  public static User findUser(int id) {
    return ExpenseEntityDao.getEntityManager().find(User.class, id);
  }
  
  public static User createUser(String name) {
    ExpenseEntityDao.begin();
    try {
      User user = new User();
      user.setName(name);
      ExpenseEntityDao.getEntityManager().persist(user);
      ExpenseEntityDao.commit();
      return user;
    } finally {
      ExpenseEntityDao.close();
    }
  }
}
