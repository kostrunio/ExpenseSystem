package pl.kostro.expensesystem.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Axis;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.SpringMain;
import pl.kostro.expensesystem.components.grid.ColumnChartGrid;
import pl.kostro.expensesystem.components.grid.LineChartGrid;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.utils.expense.YearCategory;
import pl.kostro.expensesystem.view.design.ChartDesign;

@SuppressWarnings("serial")
public class ChartView extends ChartDesign {

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetService eshs;
  private ExpenseSheet expenseSheet;
  private Button.ClickListener searchClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      refreshFilter();
    }
  };

  public ChartView() {
    ApplicationContext context = new AnnotationConfigApplicationContext(SpringMain.class);
    eshs = context.getBean(ExpenseSheetService.class);
    logger.info("create");
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    expenseSheet.setFilter(new Filter(null, null, null, null));
    setCaption();
    categoryCombo.addItems(expenseSheet.getCategoryList());
    categoryCombo.setMultiSelect(true);
    userCombo.addItems(expenseSheet.getUserLimitList());
    userCombo.setMultiSelect(true);
    searchButton.addClickListener(searchClick);
    refreshFilter();
  }

  private void setCaption() {
    categoryCombo.setCaption(Msg.get("expense.category"));
    userCombo.setCaption(Msg.get("expense.user"));
    searchButton.setCaption(Msg.get("expense.search"));
  }

  @SuppressWarnings("unchecked")
  private void refreshFilter() {
    String filterFormula = null;
    String filterComment = null;
    List<User> users = new ArrayList<User>();
    Set<UserLimit> setUserLimit = (Set<UserLimit>) userCombo.getValue();
    for (Iterator<UserLimit> iter = setUserLimit.iterator(); iter.hasNext();)
      users.add(iter.next().getUser());

    List<Category> categories = new ArrayList<Category>();
    Set<Category> setCategory = (Set<Category>) categoryCombo.getValue();
    for (Iterator<Category> iter = setCategory.iterator(); iter.hasNext();)
      categories.add(iter.next());

    expenseSheet.setFilter(new Filter(categories, users, filterFormula, filterComment));
    showCharts();
  }

  private void showCharts() {
    chartLayout.removeAllComponents();
    List<YearCategory> yearCategoryList = eshs.prepareYearCategoryList(expenseSheet);
    LineChartGrid lineGrid = new LineChartGrid(yearCategoryList);
    ColumnChartGrid columnGrid = new ColumnChartGrid(yearCategoryList);

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

    for (YearCategory yearCategory : yearCategoryList) {
      BigDecimal sum = new BigDecimal(0);
      List<BigDecimal> monthValues1 = new ArrayList<BigDecimal>();
      List<BigDecimal> monthValues2 = new ArrayList<BigDecimal>();
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
      lineGrid.addValues(yearCategory.getYear(), monthValues1);
      columnConfiguration.addSeries(yearLine2);
      columnGrid.addValues(yearCategory.getYear(), monthValues2);
    }

    chartLayout.addComponent(lineChart);
    chartLayout.addComponent(lineGrid);
    chartLayout.addComponent(columnChart);
    chartLayout.addComponent(columnGrid);
    
    lineGrid.setHeightByRows(yearCategoryList.size());
    columnGrid.setHeightByRows(yearCategoryList.size());
  }
}
