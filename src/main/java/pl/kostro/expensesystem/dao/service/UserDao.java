package pl.kostro.expensesystem.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.dao.model.UserEntity;
import pl.kostro.expensesystem.dao.repository.UserRepository;
import pl.kostro.expensesystem.dto.model.User;
import pl.kostro.expensesystem.utils.Converter;

@Service
public class UserDao {
  
  @Autowired
  private UserRepository ur;
  
  public void save(User user) {
    UserEntity userEntity = Converter.toUserEntity(user);
    ur.save(userEntity);
    user.setId(userEntity.getId());
  }

  public void delete(User user) {
    ur.delete(user.getId());
  }
}
