package pl.kostro.expensesystem.utils.calendar;

import pl.kostro.expensesystem.Msg;

public class CalendarUtils {

  private static String[] monthsName =
    {Msg.get("expenseSheet.january"),
      Msg.get("expenseSheet.february"),
      Msg.get("expenseSheet.march"),
      Msg.get("expenseSheet.april"),
      Msg.get("expenseSheet.may"),
      Msg.get("expenseSheet.june"),
      Msg.get("expenseSheet.july"),
      Msg.get("expenseSheet.august"),
      Msg.get("expenseSheet.september"),
      Msg.get("expenseSheet.october"),
      Msg.get("expenseSheet.november"),
      Msg.get("expenseSheet.december")};

  public static String[] getMonthsName() {
    return monthsName;
  }

  public static int getMonthNumber(String monthName) {
    for (int i = 0; i < getMonthsName().length; i++)
      if (monthName.equals(getMonthsName()[i]))
        return i+1;
    throw new IllegalArgumentException(monthName + "not known");
  }

  public static String getMonthName(int month) {
    if (month > monthsName.length)
      throw new IllegalArgumentException(month + "not known");
    return monthsName[month];
  }

}
