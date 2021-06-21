package pl.kostro.expensesystem.utils;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.kostro.expensesystem.dto.service.ExpenseSheetNotifyService;
import pl.kostro.expensesystem.model.ExpenseEntity;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.service.ExpenseService;

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
    Map<RealUser, Map<ExpenseSheet, List<ExpenseEntity>>> notifyMap = esns.prepareExpenseSheetNotify(expList);
    for (RealUser realUser : notifyMap.keySet()) {
      Map<ExpenseSheet, List<ExpenseEntity>> eSMap = notifyMap.get(realUser);
      for (ExpenseSheet eS : eSMap.keySet()) {
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