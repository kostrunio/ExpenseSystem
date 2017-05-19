package pl.kostro.expensesystem.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.repository.UserRepository;

@Service
public class UserService {
  
  @Autowired
  private UserRepository ur;

  public User createUser(String name) {
    User user = new User();
    user.setName(name);
    ur.save(user);
    return user;
  }

}
