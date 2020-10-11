package pl.kostro.expensesystem.dto.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.dto.model.User;
import pl.kostro.expensesystem.db.model.UserEntity;
import pl.kostro.expensesystem.db.repository.UserRepository;

@Service
public class UserService {
  
  @Autowired
  private UserRepository ur;
  
  private static Logger logger = LogManager.getLogger();

  public User createUser(String name) {
    LocalDateTime stopper = LocalDateTime.now();
    UserEntity userEntity = new UserEntity();
    userEntity.setName(name);
    ur.save(userEntity);
    logger.info("createUser for {} finish: {} ms", userEntity, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    User user = new User(userEntity.getName());
    BeanUtils.copyProperties(userEntity, user);
    return user;
  }

}
