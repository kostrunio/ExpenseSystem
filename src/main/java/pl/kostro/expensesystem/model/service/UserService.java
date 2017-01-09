package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.User;

public class UserService {

  public static User createUser(String name) {
    ExpenseEntityDao.begin();
    try {
      User user = new User();
      user.setName(name);
      ExpenseEntityDao.getEntityManager().persist(user);
      ExpenseEntityDao.commit();
      return user;
    } finally {
    }
  }

}
