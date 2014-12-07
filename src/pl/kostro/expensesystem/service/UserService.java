package pl.kostro.expensesystem.service;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.User;

public class UserService {
  
  public void removeProfessor(int id) {
    User emp = findUser(id);
    if (emp != null) {
      ExpenseEntityDao.getEntityManager().remove(emp);
    }
  }

  public User findUser(int id) {
    return ExpenseEntityDao.getEntityManager().find(User.class, id);
  }
  
  public User createUser(String name) {
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
