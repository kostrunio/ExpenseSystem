package pl.kostro.expensesystem.utils.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Calculator {

  public static boolean verifyAllowed(String str) {
    if (str.startsWith("="))
      str = str.substring(1);
    str = str.replaceAll(",", ".");
    if (str.matches("[0-9\\+\\-\\*\\/\\(\\)\\.]*")) {
      String[] open = str.split("\\(", -1);
      String[] close = str.split("\\)", -1);
      return open.length == close.length;
    }
    return false;
  }

  public static BigDecimal getOperationResult(String str) {
    if (str.startsWith("="))
      str = str.substring(1);
    if (str.isEmpty())
      return new BigDecimal(0);
    str = str.replaceAll(",", ".");
    if (str.contains("(")) {
      int a = str.lastIndexOf("(");
      int b = str.indexOf(")", a);
      BigDecimal middle = getOperationResult(str.substring(a + 1, b));
      return getOperationResult(str.substring(0, a) + middle + str.substring(b + 1));
    }
    BigDecimal result = new BigDecimal(0);
    String[] plus = str.split("\\+");
    if (plus.length > 1) {
      // there were some +
      if (!plus[0].isEmpty())
        result = getOperationResult(plus[0]);
      for (int i = 1; i < plus.length; i++) {
        result = result.add(getOperationResult(plus[i]));
      }
      return result;
    } else {
      // no +
      List<String> minusList = new ArrayList<>();
      char mem = ';';
      int index = 0;
      String ex = plus[0];
      for (int i = 0; i < ex.length(); i++) {
        char c = ex.charAt(i);
        if (c == '-' && String.valueOf(mem).matches("[0-9]")) {
          minusList.add(ex.substring(index, i));
          index = i + 1;
        }
        mem = c;
        if (i == ex.length() - 1) {
          minusList.add(ex.substring(index));
        }
      }
      if (minusList.size() > 1) {
        // there were some -
        result = getOperationResult(minusList.get(0));
        for (int i = 1; i < minusList.size(); i++) {
          result = result.subtract(getOperationResult(minusList.get(i)));
        }
        return result;
      } else {
        // no -
        String[] mult = (minusList.get(0)).split("\\*");
        if (mult.length > 1) {
          // there were some *
          result = getOperationResult(mult[0]);
          for (int i = 1; i < mult.length; i++) {
            result = result.multiply(getOperationResult(mult[i]));
          }
          return result;
        } else {
          // no *
          String[] div = mult[0].split("\\/");
          if (div.length > 1) {
            // there were some /
            result = getOperationResult(div[0]);
            for (int i = 1; i < div.length; i++) {
              BigDecimal dv = getOperationResult(div[i]);
              if (dv.equals(new BigDecimal(0)))
                throw new IllegalArgumentException("divide by ZERO not allowed");
              result = result.divide(dv, 2, RoundingMode.HALF_UP);
            }
            return result;
          } else {
            // no /
            return new BigDecimal(str);
          }
        }
      }
    }
  }

  public static BigDecimal getResult(String str) {
    return getOperationResult(str).setScale(2, RoundingMode.HALF_UP);
  }

}
