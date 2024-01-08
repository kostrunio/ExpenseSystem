package pl.kostro.expensesystem.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  
  public UserEntity createAndSave(String name) {
    LocalDateTime stopper = LocalDateTime.now();
    UserEntity user = new UserEntity();
    user.setName(name);
    repository.save(user);
    log.info("createUser for {} finish: {} ms", user, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return user;
  }

}
