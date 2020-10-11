package pl.kostro.expensesystem.dao.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.dao.model.UserSummaryEntity;
import pl.kostro.expensesystem.dao.repository.UserSummaryRepository;
import pl.kostro.expensesystem.dto.model.UserSummary;
import pl.kostro.expensesystem.utils.Converter;

@Service
public class UserSummaryDao {
  
  @Autowired
  private UserSummaryRepository usr;
  
  private static Logger logger = LogManager.getLogger();
  
  public void save(UserSummary userSummary) {
    UserSummaryEntity userSummaryEntity = Converter.toUserSummaryEntity(userSummary);
    usr.save(userSummaryEntity);
    userSummary.setId(userSummaryEntity.getId());
  }

  public void merge(UserSummary userSummary) {
    UserSummaryEntity userSummaryEntity = usr.findOne(userSummary.getId());
    Converter.toUserSummaryEntity(userSummary, userSummaryEntity);
    usr.save(userSummaryEntity);
  }

  public void delete(UserSummary userSummary) {
    usr.delete(userSummary.getId());
  }
}
