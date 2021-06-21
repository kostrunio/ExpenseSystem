package pl.kostro.expensesystem.utils.scheduler;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.kostro.expensesystem.model.dto.service.ExpenseSheetNotifyService;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.utils.mail.SendEmail;

@Component
public class SendNotification {

  private static Logger logger = LogManager.getLogger();
  @Autowired
  private ExpenseService es;
  private ExpenseSheetNotifyService esns = new ExpenseSheetNotifyService();

  @Scheduled(cron = "0 0 0/2 * * *")
  public void process() {
    logger.info("SendNotification - started");
    List<ExpenseEntity> expList = es.findExpensesToNotify();
    Map<RealUserEntity, Map<ExpenseSheetEntity, List<ExpenseEntity>>> notifyMap = esns.prepareExpenseSheetNotify(expList);
    for (RealUserEntity realUser : notifyMap.keySet()) {
      Map<ExpenseSheetEntity, List<ExpenseEntity>> eSMap = notifyMap.get(realUser);
      for (ExpenseSheetEntity eS : eSMap.keySet()) {
        logger.info("SendNotification: {}; {}; {}", realUser.getName(), eS.getName(), eSMap.get(eS).size());
        SendEmail.expenses(realUser, eS, eSMap.get(eS).size());
        for (ExpenseEntity e : eSMap.get(eS)) {
          e.setNotify(false);
          es.save(e);
        }
      }
    }
    logger.info("SendNotification - finished");
  }
}