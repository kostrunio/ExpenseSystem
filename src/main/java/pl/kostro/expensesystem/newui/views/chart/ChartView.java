package pl.kostro.expensesystem.newui.views.chart;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.newui.components.grid.ChartGrid;
import pl.kostro.expensesystem.newui.views.main.MainView;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.model.YearCategory;
import pl.kostro.expensesystem.utils.transform.model.YearValue;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Route(value = "chart", layout = MainView.class)
@PageTitle("Chart")
public class ChartView extends ChartDesign {

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetTransformService eshts;
  private ExpenseSheetEntity expenseSheet;
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<CheckboxGroup<CategoryEntity>, Set<CategoryEntity>>> categoryChanged = event -> refreshFilter();
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<CheckboxGroup<UserLimitEntity>, Set<UserLimitEntity>>> userChanged = event -> refreshFilter();

  public ChartView() {
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    logger.info("create");
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    expenseSheet.setFilter(new Filter(null, null, null, null));
    setCaption();
    categoryCombo.addValueChangeListener(categoryChanged);
    categoryCombo.setItemLabelGenerator(item -> item.getName());
    categoryCombo.setItems(expenseSheet.getCategoryList());
    userCombo.addValueChangeListener(userChanged);
    userCombo.setItems(expenseSheet.getUserLimitList());
    userCombo.setItemLabelGenerator(item -> item.getUser().getName());
    refreshFilter();
  }

  private void setCaption() {
    categoryCombo.setLabel(Msg.get("expense.category"));
    userCombo.setLabel(Msg.get("expense.user"));
  }

  private void refreshFilter() {
    List<UserEntity> users = new ArrayList<>();
    Set<UserLimitEntity> setUserLimit = userCombo.getValue();
    for (Iterator<UserLimitEntity> iter = setUserLimit.iterator(); iter.hasNext();)
      users.add(iter.next().getUser());

    List<CategoryEntity> categories = new ArrayList<>();
    Set<CategoryEntity> setCategory = categoryCombo.getValue();
    for (Iterator<CategoryEntity> iter = setCategory.iterator(); iter.hasNext();)
      categories.add(iter.next());

    expenseSheet.setFilter(new Filter(categories, users, null, null));
    showCharts();
  }

  private void showCharts() {
    chartLayout.removeAll();
    chartLayout2.removeAll();
    List<YearCategory> yearCategoryList = eshts.prepareYearCategoryList(expenseSheet);
    ChartGrid lineGrid = new ChartGrid(yearCategoryList);
    ChartGrid columnGrid = new ChartGrid(yearCategoryList);

    Chart lineChart = new Chart(ChartType.LINE);
    Configuration lineConfiguration = lineChart.getConfiguration();
    
    lineConfiguration.setTitle(Msg.get("chart.tytle1"));
    Axis xAxis1 = lineConfiguration.getxAxis();
    xAxis1.setCategories(CalendarUtils.getMonthsName());
    Tooltip tooltip = lineConfiguration.getTooltip();
    tooltip.setValueSuffix(Msg.get("chart.suffix"));
    tooltip.setShared(true);

    Chart columnChart = new Chart(ChartType.COLUMN);
    Configuration columnConfiguration = columnChart.getConfiguration();
    columnConfiguration.setTitle(Msg.get("chart.tytle2"));
    Axis xAxis2 = columnConfiguration.getxAxis();
    xAxis2.setCategories(CalendarUtils.getMonthsName());

    List<YearValue> lineGridData = new ArrayList<>();
    List<YearValue> columnGridData = new ArrayList<>();
    for (YearCategory yearCategory : yearCategoryList) {
      BigDecimal sum = new BigDecimal(0);
      List<BigDecimal> monthValues1 = new ArrayList<>();
      List<BigDecimal> monthValues2 = new ArrayList<>();
      for (int m = 0; m <= 11; m++) {
        BigDecimal value = yearCategory.getMonthValue(m);
        if (value != null) {
          sum = sum.add(value);
          monthValues1.add(sum);
          monthValues2.add(value);
        }
      }
      Series yearLine1 = new ListSeries(yearCategory.getYear() + "", monthValues1.toArray(new BigDecimal[0]));
      Series yearLine2 = new ListSeries(yearCategory.getYear() + "", monthValues2.toArray(new BigDecimal[0]));
      lineConfiguration.addSeries(yearLine1);
      lineGridData.add(new YearValue(yearCategory.getYear(), monthValues1));
      columnConfiguration.addSeries(yearLine2);
      columnGridData.add(new YearValue(yearCategory.getYear(), monthValues2));
    }
    
    lineGrid.setItems(lineGridData);
    lineGrid.setAllRowsVisible(true);
    columnGrid.setItems(columnGridData);
    columnGrid.setAllRowsVisible(true);

    lineChart.setWidth("1600px");
    chartLayout.add(lineChart);
    lineGrid.setWidth("1600px");
    chartLayout2.add(lineGrid);
    columnChart.setWidth("1600px");
    chartLayout2.add(columnChart);
    columnGrid.setWidth("1600px");
    chartLayout2.add(columnGrid);
    
    lineGrid.setAllRowsVisible(true);
    columnGrid.setAllRowsVisible(true);
  }
}
