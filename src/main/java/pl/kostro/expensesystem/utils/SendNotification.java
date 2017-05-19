package pl.kostro.expensesystem.utils;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import pl.kostro.expensesystem.dto.service.ExpenseSheetNotifyService;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.service.ExpenseService;

public class SendNotification implements Job {

  private static Logger logger = LogManager.getLogger();
  private ExpenseService es;
  private ExpenseSheetNotifyService esns = new ExpenseSheetNotifyService();

  @Override
  public void execute(final JobExecutionContext ctx) throws JobExecutionException {
    process();
  }

  public void process() {
    logger.info("SendNotification - started");
    List<Expense> expList = es.findExpensesToNotify();
    Map<RealUser, Map<ExpenseSheet, List<Expense>>> notifyMap = esns.prepareExpenseSheetNotify(expList);
    for (RealUser realUser : notifyMap.keySet()) {
      Map<ExpenseSheet, List<Expense>> eSMap = notifyMap.get(realUser);
      for (ExpenseSheet eS : eSMap.keySet()) {
        logger.info("SendNotification: {}; {}; {}", realUser.getName(), eS.getName(), eSMap.get(eS).size());
        SendEmail.expenses(realUser, eS, eSMap.get(eS).size());
        for (Expense e : eSMap.get(eS)) {
          e.setNotify(false);
          es.merge(e);
        }
      }
    }
    logger.info("SendNotification - finished");
  }
}