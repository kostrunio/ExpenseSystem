package pl.kostro.expensesystem.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.dao.model.ExpenseEntity;
import pl.kostro.expensesystem.dao.model.RealUserEntity;
import pl.kostro.expensesystem.dao.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.dao.repository.RealUserRepository;
import pl.kostro.expensesystem.dto.model.Expense;
import pl.kostro.expensesystem.dto.model.ExpenseSheet;
import pl.kostro.expensesystem.dto.model.RealUser;
import pl.kostro.expensesystem.dto.model.User;
import pl.kostro.expensesystem.utils.Converter;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RealUserDao {
  
  @Autowired
  private RealUserRepository rur;
  @Autowired
  private ExpenseSheetRepository eshr;

  public void save(RealUser realUser) {
    RealUserEntity realUserEntity = Converter.toRealUserEntity(realUser);
    rur.save(realUserEntity);
    realUser.setId(realUserEntity.getId());
  }

  public void merge(RealUser realUser) {
    RealUserEntity realUserEntity = rur.findOne(realUser.getId());
    Converter.toRealUserEntity(realUser, realUserEntity);
    rur.save(realUserEntity);
  }

  public void refresh(RealUser realUser) {
    Converter.toRealUser(rur.findOne(realUser.getId()), realUser);
  }

  public RealUser findByNameAndPassword(String userName, byte[] bytePassword) {
    RealUserEntity realUserEntity = rur.findByNameAndPassword(userName, new String(bytePassword));
    if (realUserEntity == null) return null;
    realUserEntity.setLogDate(LocalDateTime.now());
    if (realUserEntity.getPasswordByte() == null)
      realUserEntity.setPasswordByte(bytePassword);
    rur.save(realUserEntity);
    return Converter.toRealUser(realUserEntity);
  }

  public RealUser findByName(String userName) {
    RealUser realUser = new RealUser();
    try {
      RealUserEntity realUserEntity = rur.findByName(userName);
      Converter.toRealUser(realUserEntity, realUser);
    } catch (NoResultException e) {
      return null;
    }
    return realUser;
  }

  @Transactional
  public void fetchExpenseSheetList(RealUser realUser) {
    RealUserEntity realUserEntity = rur.getOne(realUser.getId());
    realUserEntity.getExpenseSheetList().size();
    Converter.toRealUser(realUserEntity, realUser);
  }

  public List<RealUser> findUsersWithExpenseSheet(ExpenseSheet expenseSheet) {
    List<RealUser> realUserList = new ArrayList<>();
    List<RealUserEntity> realUserEntityList = null;
    try {
      realUserEntityList = rur.findUsersWithExpenseSheet(eshr.findOne(expenseSheet.getId()));
    } catch (NoResultException e) {
    }
    for (RealUserEntity realUserEntity : realUserEntityList) {
      RealUser realUser = Converter.toRealUser(realUserEntity);
      realUserList.add(realUser);
    }
    return realUserList;
  }

  public void delete(User user) {
    rur.delete(user.getId());
  }
}
