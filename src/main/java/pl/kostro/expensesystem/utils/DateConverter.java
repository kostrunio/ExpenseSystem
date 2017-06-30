package pl.kostro.expensesystem.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.vaadin.v7.data.util.converter.StringToDateConverter;

@SuppressWarnings("serial")
public class DateConverter extends StringToDateConverter{

  @Override
  public DateFormat getFormat(Locale locale) {
    return new SimpleDateFormat("yyyy-MM-dd");
  }
}
