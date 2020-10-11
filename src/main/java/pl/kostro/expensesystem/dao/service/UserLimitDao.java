package pl.kostro.expensesystem.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.dao.model.*;
import pl.kostro.expensesystem.dao.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.dao.repository.RealUserRepository;
import pl.kostro.expensesystem.dao.repository.UserLimitRepository;
import pl.kostro.expensesystem.dto.model.*;
import pl.kostro.expensesystem.utils.Converter;

import javax.transaction.Transactional;

@Service
public class UserLimitDao {
  
  @Autowired
  private RealUserRepository rur;
  @Autowired
  private UserLimitRepository ulr;
  @Autowired
  private ExpenseSheetRepository eshr;
  
  @Autowired
  private UserSummaryDao uss;

  public void save(UserLimit user) {

  }

  public void merge(UserLimit userLimit) {
    UserLimitEntity userLimitEntity = ulr.findOne(userLimit.getId());
    Converter.toUserLimitEntity(userLimit, userLimitEntity);
    ulr.save(userLimitEntity);
  }

  public void delete(UserLimit userLimit) {
    ulr.delete(userLimit.getId());
  }

  @Transactional
  public void fetchUserSummaryList(UserLimit userLimit) {
    UserLimitEntity userLimitEntity = ulr.getOne(userLimit.getId());
    userLimitEntity.getUserSummaryList().size();
    Converter.toUserLimit(userLimitEntity);
  }

}
