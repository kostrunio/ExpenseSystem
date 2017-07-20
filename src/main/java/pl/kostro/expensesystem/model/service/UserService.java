package pl.kostro.expensesystem.model.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.repository.UserRepository;

@Service
public class UserService {
  
  @Autowired
  private UserRepository ur;
  
  private static Logger logger = LogManager.getLogger();

  public User createUser(String name) {
    LocalDateTime stopper = LocalDateTime.now();
    User user = new User();
    user.setName(name);
    ur.save(user);
    logger.info("createUser finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return user;
  }

}
