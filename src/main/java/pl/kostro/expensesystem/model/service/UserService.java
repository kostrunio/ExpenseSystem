package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.repository.UserRepository;

public class UserService {
  
  private UserRepository ur;

  public User createUser(String name) {
    User user = new User();
    user.setName(name);
    ur.save(user);
    return user;
  }

}
