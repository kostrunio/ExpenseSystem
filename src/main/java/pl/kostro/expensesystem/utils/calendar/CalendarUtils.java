package pl.kostro.expensesystem.utils.calendar;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.Calendar;

public class CalendarUtils {

  private static String[] monthsName = new DateFormatSymbols().getMonths();

  public static String[] getMonthsName() {
    return monthsName;
  }

  public static LocalDate getFirstDay(LocalDate date) {
    return date.withDayOfMonth(1);
  }

  public static LocalDate getLastDay(LocalDate date) {
    return date.plusMonths(1).minusDays(1);
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
