package pl.kostro.expensesystem.components.grid;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.YearCategory;

public class LineChartGrid extends Grid {

  public LineChartGrid(List<YearCategory> yearCategoryList) {
    List<String> columnList = new ArrayList<>();
    columnList.add(Msg.get("grid.year"));
    for (String monthName : UserSummaryService.getMonthsName())
      columnList.add(monthName);
    setColumns(columnList);
    List<String> rowList = new ArrayList<>();
    for (YearCategory yearCategory : yearCategoryList)
      addRow(yearCategory.getYear()+"",
          yearCategory.getMonthValue(0),
          yearCategory.getMonthValue(1),
          yearCategory.getMonthValue(2),
          yearCategory.getMonthValue(3),
          yearCategory.getMonthValue(4),
          yearCategory.getMonthValue(5),
          yearCategory.getMonthValue(6),
          yearCategory.getMonthValue(7),
          yearCategory.getMonthValue(8),
          yearCategory.getMonthValue(9),
          yearCategory.getMonthValue(10),
          yearCategory.getMonthValue(11));
  }
}
