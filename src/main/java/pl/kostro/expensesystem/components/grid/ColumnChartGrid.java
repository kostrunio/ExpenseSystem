package pl.kostro.expensesystem.components.grid;

import java.math.BigDecimal;
import java.util.List;

import com.vaadin.v7.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.utils.expense.YearCategory;

public class ColumnChartGrid extends Grid {

  public ColumnChartGrid(List<YearCategory> yearCategoryList) {
    setColumns(Msg.get("grid.year"),
        CalendarUtils.getMonthsName()[0],
        CalendarUtils.getMonthsName()[1],
        CalendarUtils.getMonthsName()[2],
        CalendarUtils.getMonthsName()[3],
        CalendarUtils.getMonthsName()[4],
        CalendarUtils.getMonthsName()[5],
        CalendarUtils.getMonthsName()[6],
        CalendarUtils.getMonthsName()[7],
        CalendarUtils.getMonthsName()[8],
        CalendarUtils.getMonthsName()[9],
        CalendarUtils.getMonthsName()[10],
        CalendarUtils.getMonthsName()[11]);
  }

  public void addValues(int year, List<BigDecimal> monthValues1) {
    addRow(year+"",
        monthValues1.get(0).toString(),
        monthValues1.get(1).toString(),
        monthValues1.get(2).toString(),
        monthValues1.get(3).toString(),
        monthValues1.get(4).toString(),
        monthValues1.get(5).toString(),
        monthValues1.get(6).toString(),
        monthValues1.get(7).toString(),
        monthValues1.get(8).toString(),
        monthValues1.get(9).toString(),
        monthValues1.get(10).toString(),
        monthValues1.get(11).toString());
  }
}
