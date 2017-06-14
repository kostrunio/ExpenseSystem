package pl.kostro.expensesystem.components.grid;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.YearCategory;

public class LineChartGrid extends Grid {

  public LineChartGrid(List<YearCategory> yearCategoryList) {
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
    
    for (YearCategory yearCategory : yearCategoryList)
      addRow(yearCategory.getYear()+"",
          yearCategory.getMonthValue(0).toString(),
          yearCategory.getMonthValue(1).toString(),
          yearCategory.getMonthValue(2).toString(),
          yearCategory.getMonthValue(3).toString(),
          yearCategory.getMonthValue(4).toString(),
          yearCategory.getMonthValue(5).toString(),
          yearCategory.getMonthValue(6).toString(),
          yearCategory.getMonthValue(7).toString(),
          yearCategory.getMonthValue(8).toString(),
          yearCategory.getMonthValue(9).toString(),
          yearCategory.getMonthValue(10).toString(),
          yearCategory.getMonthValue(11).toString());
  }
}
