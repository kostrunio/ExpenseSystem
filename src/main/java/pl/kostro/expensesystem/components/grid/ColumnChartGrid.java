package pl.kostro.expensesystem.components.grid;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.YearCategory;

public class ColumnChartGrid extends Grid {

  public ColumnChartGrid(List<YearCategory> yearCategoryList) {
    List<String> columnList = new ArrayList<>();
    columnList.add(Msg.get("grid.year"));
    for (String monthName : UserSummaryService.getMonthsName())
      columnList.add(monthName);
    setColumns(columnList);
    List<String> rowList = new ArrayList<>();
    for (YearCategory yearCategory : yearCategoryList) {
      rowList.add(yearCategory.getYear()+"");
      rowList.addAll(yearCategory.getMonths());
      addRow(rowList);
    }
  }
}
