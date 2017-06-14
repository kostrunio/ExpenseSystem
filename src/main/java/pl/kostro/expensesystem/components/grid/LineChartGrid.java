package pl.kostro.expensesystem.components.grid;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.YearCategory;

public class LineChartGrid extends Grid {

  public LineChartGrid(List<YearCategory> yearCategoryList) {
    setColumns(Msg.get("grid.year"), UserSummaryService.getMonthsName());
    List<String> rowList = new ArrayList<>();
    for (YearCategory yearCategory : yearCategoryList) {
      rowList.add(yearCategory.getYear()+"");
      rowList.addAll(yearCategory.getMonthsSum());
      addRow(rowList);
    }
  }
}
