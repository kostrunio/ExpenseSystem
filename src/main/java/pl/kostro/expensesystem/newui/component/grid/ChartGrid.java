package pl.kostro.expensesystem.newui.component.grid;

import com.vaadin.flow.component.grid.Grid;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.model.YearCategory;
import pl.kostro.expensesystem.utils.transform.model.YearValue;

import java.util.List;

public class ChartGrid extends Grid<YearValue> {

  public ChartGrid(List<YearCategory> yearCategoryList) {
    addColumn(YearValue::getYear).setHeader(Msg.get("chart.year"));
    addColumn(YearValue::getJanuary).setHeader(CalendarUtils.getMonthsName()[0]);
    addColumn(YearValue::getFebruary).setHeader(CalendarUtils.getMonthsName()[1]);
    addColumn(YearValue::getMarch).setHeader(CalendarUtils.getMonthsName()[2]);
    addColumn(YearValue::getApril).setHeader(CalendarUtils.getMonthsName()[3]);
    addColumn(YearValue::getMay).setHeader(CalendarUtils.getMonthsName()[4]);
    addColumn(YearValue::getJune).setHeader(CalendarUtils.getMonthsName()[5]);
    addColumn(YearValue::getJuly).setHeader(CalendarUtils.getMonthsName()[6]);
    addColumn(YearValue::getAugust).setHeader(CalendarUtils.getMonthsName()[7]);
    addColumn(YearValue::getSeptember).setHeader(CalendarUtils.getMonthsName()[8]);
    addColumn(YearValue::getOctober).setHeader(CalendarUtils.getMonthsName()[9]);
    addColumn(YearValue::getNovember).setHeader(CalendarUtils.getMonthsName()[10]);
    addColumn(YearValue::getDecember).setHeader(CalendarUtils.getMonthsName()[11]);
  }
}
