package pl.kostro.expensesystem.dto.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dao.service.UserDao;
import pl.kostro.expensesystem.dto.model.User;

@Service
public class UserService {
  
  @Autowired
  private UserDao us;
  
  private static Logger logger = LogManager.getLogger();

  public User createUser(String name) {
    LocalDateTime stopper = LocalDateTime.now();
    User user = new User(name);
    us.save(user);
    logger.info("createUser for {} finish: {} ms", user, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return user;
  }

}
