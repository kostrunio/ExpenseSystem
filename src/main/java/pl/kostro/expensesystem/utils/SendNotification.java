package pl.kostro.expensesystem.utils;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import pl.kostro.expensesystem.dto.service.ExpenseSheetNotifyService;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.service.ExpenseService;

public class SendNotification implements Job {

  @Override
  public void execute(final JobExecutionContext ctx) throws JobExecutionException {
    System.out.println("SendNotification - started");
    Map<RealUser, Map<ExpenseSheet, List<Expense>>> notifyMap = ExpenseSheetNotifyService.prepareExpenseSheetNotify(ExpenseService.findExpensesToNotify());
    for (RealUser realUser : notifyMap.keySet()) {
      Map<ExpenseSheet, List<Expense>> eSMap = notifyMap.get(realUser);
      for (ExpenseSheet eS : eSMap.keySet()) {
        System.out.println("SendNotification: " + realUser.getName() + "; " + eS.getName() + "; " + eSMap.get(eS));
      }
    }
    System.out.println("SendNotification - finished");
  }

}