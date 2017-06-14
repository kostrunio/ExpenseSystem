package pl.kostro.expensesystem.components.grid;

import java.util.List;

import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.YearCategory;

public class LineChartGrid extends Grid {

  public LineChartGrid(List<YearCategory> yearCategoryList) {
    setColumns(Msg.get("grid.year"), UserSummaryService.getMonthsName());
    for (YearCategory yearCategory : yearCategoryList)
      addRow(yearCategory.getYear()+"", yearCategory.getMonthsSum());
  }
}
