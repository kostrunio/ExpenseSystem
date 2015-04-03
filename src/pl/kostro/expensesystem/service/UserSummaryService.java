package pl.kostro.expensesystem.service;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;

public class UserSummaryService {
  
  private static String[] monthsName = new DateFormatSymbols().getMonths();
  
  public static String[] getMonthsName() {
    return monthsName;
  }

  public static void removeUserSummary(int id) {
    UserSummary uS = findUserSummary(id);
    if (uS != null) {
      ExpenseEntityDao.getEntityManager().remove(uS);
    }
  }

  public static UserSummary findUserSummary(int id) {
    return ExpenseEntityDao.getEntityManager().find(UserSummary.class, id);
  }

  public static UserSummary createUserSummary(UserLimit userLimit, Date date) {
    ExpenseEntityDao.begin();
    try {
      UserSummary userSummary = new UserSummary(userLimit, date, userLimit.getLimit());
      ExpenseEntityDao.getEntityManager().persist(userSummary);
      userLimit.getUserSummaryList().add(userSummary);
      ExpenseEntityDao.getEntityManager().merge(userLimit);
      ExpenseEntityDao.commit();
      return userSummary;
    } finally {
      ExpenseEntityDao.close();
    }
  }

  public static UserSummary save(UserSummary userSummary) {
    ExpenseEntityDao.begin();
    try {
      ExpenseEntityDao.getEntityManager().merge(userSummary);
      ExpenseEntityDao.commit();
      return userSummary;
    } finally {
      ExpenseEntityDao.close();
    }
  }

  public static Date getFirstDay(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
    return calendar.getTime();
  }

  public static Date getLastDay(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
    calendar.add(java.util.Calendar.MONTH, 1);
    calendar.add(java.util.Calendar.DAY_OF_MONTH, -1);
    return calendar.getTime();
  }
  
  public static void setFirstDay(Calendar calendar, int monthNumber) {
    calendar.set(Calendar.MONTH, monthNumber);
    setFirstDay(calendar);
  }

  public static void setFirstDay(Calendar calendar, String year) {
    calendar.set(Calendar.YEAR, Integer.parseInt(year));
    setFirstDay(calendar);
  }

  public static void setFirstDay(Calendar calendar) {
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
  }
  
  public static int getMonthNumber(String monthName) {
    for (int i = 0; i < getMonthsName().length; i++)
      if (monthName.equals(getMonthsName()[i]))
        return i;
    throw new IllegalArgumentException(monthName + "not known");
  }

  public static BigDecimal calculateSum(UserLimit userLimit, Date date) {
    BigDecimal sum = new BigDecimal(0);
    for (UserSummary userSummary : userLimit.getUserSummaryList()) {
      if (!userSummary.getDate().after(date)) {
        sum = sum.add(userSummary.getLimit());
        sum = sum.subtract(userSummary.getExSummary());
      }
    }
    return sum;
  }

  public static UserSummary findUserSummary(UserLimit userLimit, Date date) {
    for (UserSummary userSummary : userLimit.getUserSummaryList())
      if (userSummary.getDate().equals(date))
        return userSummary;
    return createUserSummary(userLimit, date);
  }

  public static void checkSummary(ExpenseSheet expenseSheet, Date date) {
    for (UserLimit userLimit : expenseSheet.getUserLimitList()) {
      UserSummary userSummary = findUserSummary(userLimit, date);
      BigDecimal exSummary = new BigDecimal(0);
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        exSummary = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      if (!userSummary.getExSummary().equals(exSummary)) {
        userSummary.setExSummary(exSummary);
        save(userSummary);
      }
    }
  }

}
