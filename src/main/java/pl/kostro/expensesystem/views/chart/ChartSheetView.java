package pl.kostro.expensesystem.views.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.YearCategory;

import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.HighChartsData;
import at.downdrown.vaadinaddons.highchartsapi.model.data.base.DoubleData;
import at.downdrown.vaadinaddons.highchartsapi.model.series.ColumnChartSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.LineChartSeries;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class ChartSheetView extends CustomComponent {
  private static final long serialVersionUID = -7074536397686956050L;

  /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

  @AutoGenerated
  private VerticalLayout mainLayout;
  /**
   * The constructor should first build the main layout, set the
   * composition root and then do any custom initialization.
   *
   * The constructor will not be automatically regenerated by the
   * visual editor.
   */
  ExpenseSheet expenseSheet;
  
  public ChartSheetView() {
    buildMainLayout();
    setCompositionRoot(mainLayout);

    // TODO add user code here
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    expenseSheet.setFilter(new Filter(null, null, null, null));
    List<YearCategory> yearCategoryList = ExpenseSheetService.prepareYearCategoryList(expenseSheet);
    
    Axis xAxis = new Axis(Axis.AxisType.xAxis);
    for (String monthName : UserSummaryService.getMonthsName())
      xAxis.getCategories().add(monthName);
    
    ChartConfiguration lineConfiguration = new ChartConfiguration();
    lineConfiguration.setTitle(Msg.get("chart.tytle1"));
    lineConfiguration.setChartType(ChartType.LINE);
    lineConfiguration.setxAxis(xAxis);
    
    ChartConfiguration columnConfiguration = new ChartConfiguration();
    columnConfiguration.setTitle(Msg.get("chart.tytle2"));
    columnConfiguration.setChartType(ChartType.COLUMN);
    columnConfiguration.setxAxis(xAxis);

    for (YearCategory yearCategory : yearCategoryList) {
      BigDecimal sum = new BigDecimal(0);
      List<HighChartsData> monthValues1 = new ArrayList<HighChartsData>();
      List<HighChartsData> monthValues2 = new ArrayList<HighChartsData>();
      for (int m=0; m<=11; m++) {
        BigDecimal value = yearCategory.getMonthValue(m);
        if (value != null) {
          sum = sum.add(value);
          monthValues1.add(new DoubleData(sum.doubleValue()));
          monthValues2.add(new DoubleData(value.doubleValue()));
        }
      }
      LineChartSeries yearLine1 = new LineChartSeries(yearCategory.getYear()+"", monthValues1);
      ColumnChartSeries yearLine2 = new ColumnChartSeries(yearCategory.getYear()+"", monthValues2);
      lineConfiguration.getSeriesList().add(yearLine1);
      columnConfiguration.getSeriesList().add(yearLine2);
    }

    try {
       HighChart lineChart = HighChartFactory.renderChart(lineConfiguration);
       lineChart.setSizeFull();
       
       HighChart columnChart = HighChartFactory.renderChart(columnConfiguration);
       columnChart.setSizeFull();
       
       mainLayout.addComponent(lineChart);
       mainLayout.addComponent(columnChart);
    } catch (HighChartsException e) {
       e.printStackTrace();
    }
  }

  @AutoGenerated
  private VerticalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setImmediate(false);
    mainLayout.setWidth("100%");
    mainLayout.setHeight("100%");
    mainLayout.setMargin(false);
    
    // top-level component properties
    setWidth("100.0%");
    setHeight("200.0%");
    
    return mainLayout;
  }

}