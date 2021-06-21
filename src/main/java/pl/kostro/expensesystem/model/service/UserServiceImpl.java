package pl.kostro.expensesystem.model.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.UserEntity;
import pl.kostro.expensesystem.model.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository repository;
  
  private Logger logger = LogManager.getLogger();

  @Autowired
  public UserServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  public UserEntity createAndSave(String name) {
    LocalDateTime stopper = LocalDateTime.now();
    UserEntity user = new UserEntity();
    user.setName(name);
    repository.save(user);
    logger.info("createUser for {} finish: {} ms", user, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return user;
  }

}
