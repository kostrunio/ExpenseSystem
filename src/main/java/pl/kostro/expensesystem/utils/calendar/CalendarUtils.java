package pl.kostro.expensesystem.utils.calendar;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarUtils {

  private static String[] monthsName = new DateFormatSymbols().getMonths();

  public static String[] getMonthsName() {
    return monthsName;
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

  public static String getMonthName(int month) {
    if (month > monthsName.length)
      throw new IllegalArgumentException(month + "not known");
    return monthsName[month];
  }

}
