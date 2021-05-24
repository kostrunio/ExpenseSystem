package pl.kostro.expensesystem.components.grid;

import java.util.List;

import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.utils.YearValue;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.utils.expense.YearCategory;

@SuppressWarnings("serial")
public class ChartGrid extends Grid<YearValue> {

  public ChartGrid(List<YearCategory> yearCategoryList) {
    addColumn(YearValue::getYear).setCaption(Msg.get("chart.year"));
    addColumn(YearValue::getJanuary).setCaption(CalendarUtils.getMonthsName()[0]);
    addColumn(YearValue::getFebruary).setCaption(CalendarUtils.getMonthsName()[1]);
    addColumn(YearValue::getMarch).setCaption(CalendarUtils.getMonthsName()[2]);
    addColumn(YearValue::getApril).setCaption(CalendarUtils.getMonthsName()[3]);
    addColumn(YearValue::getMay).setCaption(CalendarUtils.getMonthsName()[4]);
    addColumn(YearValue::getJune).setCaption(CalendarUtils.getMonthsName()[5]);
    addColumn(YearValue::getJuly).setCaption(CalendarUtils.getMonthsName()[6]);
    addColumn(YearValue::getAugust).setCaption(CalendarUtils.getMonthsName()[7]);
    addColumn(YearValue::getSeptember).setCaption(CalendarUtils.getMonthsName()[8]);
    addColumn(YearValue::getOctober).setCaption(CalendarUtils.getMonthsName()[9]);
    addColumn(YearValue::getNovember).setCaption(CalendarUtils.getMonthsName()[10]);
    addColumn(YearValue::getDecember).setCaption(CalendarUtils.getMonthsName()[11]);
  }
}
