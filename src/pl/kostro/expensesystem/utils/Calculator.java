package pl.kostro.expensesystem.utils;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
	public static boolean verifyAllowed(String str) {
		str = str.replaceAll(",", ".");
		if (str.matches("[0-9\\+\\-\\*\\/\\(\\)\\.]*")) {
			String[] open = str.split("\\(", -1);
			String[] close = str.split("\\)", -1);
			if (open.length == close.length)
				return true;
		}
		return false;
	}

	public static double getResult(String str) {
		if (str.equals(new String()))
			return 0;
		str = str.replaceAll(",", ".");
		if (str.indexOf("(") != -1) {
			int a = str.lastIndexOf("(");
			int b = str.indexOf(")", a);
			double middle = getResult(str.substring(a + 1, b));
			return getResult(str.substring(0, a) + Double.toString(middle)
					+ str.substring(b + 1));
		}
		double result = 0;
		String[] plus = str.split("\\+");
		if (plus.length > 1) {
			// there were some +
			if (plus[0].length() > 0)
				result = getResult(plus[0]);
			for (int i = 1; i < plus.length; i++) {
				result += getResult(plus[i]);
			}
			return result;
		} else {
			// no +
			List<String> minusList = new ArrayList<String>();
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
				result = getResult((String) minusList.get(0));
				for (int i = 1; i < minusList.size(); i++) {
					result -= getResult((String) minusList.get(i));
				}
				return result;
			} else {
				// no -
				String[] mult = ((String) minusList.get(0)).split("\\*");
				if (mult.length > 1) {
					// there were some *
					result = getResult(mult[0]);
					for (int i = 1; i < mult.length; i++) {
						result *= getResult(mult[i]);
					}
					return result;
				} else {
					// no *
					String[] div = mult[0].split("\\/");
					if (div.length > 1) {
						// there were some /
						result = getResult(div[0]);
						for (int i = 1; i < div.length; i++) {
							double dv = getResult(div[i]);
							if (dv == 0.0)
								throw new IllegalArgumentException(
										"divide by ZERO not allowed");
							result /= dv;
						}
						return result;
					} else {
						// no /
						str = str.replaceAll("--", new String());
						return Double.parseDouble(str);
					}
				}
			}
		}
	}
}
