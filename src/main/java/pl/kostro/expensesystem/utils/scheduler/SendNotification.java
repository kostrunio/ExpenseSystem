package pl.kostro.expensesystem.utils.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.utils.mail.SendEmail;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SendNotification {

  @Autowired
  private ExpenseService es;
  @Autowired
  private ExpenseSheetTransformService eshts;

  @Scheduled(cron = "0 0 0/2 * * *")
  public void process() {
    log.info("SendNotification - started");
    List<ExpenseEntity> expList = es.findExpensesToNotify();
    Map<RealUserEntity, Map<ExpenseSheetEntity, List<ExpenseEntity>>> notifyMap = eshts.prepareExpenseSheetNotify(expList);
    for (RealUserEntity realUser : notifyMap.keySet()) {
      Map<ExpenseSheetEntity, List<ExpenseEntity>> eSMap = notifyMap.get(realUser);
      for (ExpenseSheetEntity eS : eSMap.keySet()) {
        log.info("SendNotification: {}; {}; {}", realUser.getName(), eS.getName(), eSMap.get(eS).size());
        SendEmail.expenses(realUser, eS, eSMap.get(eS).size());
        for (ExpenseEntity e : eSMap.get(eS)) {
          e.setNotify(false);
          es.save(e);
        }
      }
    }
    log.info("SendNotification - finished");
  }
}