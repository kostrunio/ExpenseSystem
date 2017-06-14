package pl.kostro.expensesystem.components.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.YearCategory;

public class ColumnChartGrid extends Grid {

  public ColumnChartGrid(List<YearCategory> yearCategoryList) {
    setColumns(Msg.get("grid.year"),
        UserSummaryService.getMonthsName()[0],
        UserSummaryService.getMonthsName()[1],
        UserSummaryService.getMonthsName()[2],
        UserSummaryService.getMonthsName()[3],
        UserSummaryService.getMonthsName()[4],
        UserSummaryService.getMonthsName()[5],
        UserSummaryService.getMonthsName()[6],
        UserSummaryService.getMonthsName()[7],
        UserSummaryService.getMonthsName()[8],
        UserSummaryService.getMonthsName()[9],
        UserSummaryService.getMonthsName()[10],
        UserSummaryService.getMonthsName()[11]);
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
