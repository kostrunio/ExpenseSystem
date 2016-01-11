package pl.kostro.expensesystem.views.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
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

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ChartSheetView extends CustomComponent {
  private static final long serialVersionUID = -7074536397686956050L;

  private HorizontalLayout mainLayout;
  private VerticalLayout chartLayout;
  private Panel searchPanel;
  private VerticalLayout searchLayout;
  private OptionGroup categoryCombo;
  private OptionGroup userCombo;
  
  ExpenseSheet expenseSheet;
  
  public ChartSheetView() {
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    setCompositionRoot(buildMainLayout());

    if (expenseSheet.getFilter() == null)
      expenseSheet.setFilter(new Filter(null, null, null, null));
    showCharts();
    
  }
  
  private void showCharts() {
    chartLayout.removeAllComponents();
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
       
       chartLayout.addComponent(lineChart);
       chartLayout.addComponent(columnChart);
    } catch (HighChartsException e) {
       e.printStackTrace();
    }
  }

  private HorizontalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new HorizontalLayout();
    mainLayout.setSizeFull();
    Component chart = buildChartLayout();
    mainLayout.addComponent(chart);
    mainLayout.setExpandRatio(chart, 1);
    Component search = buildSearchLayout();
    mainLayout.addComponent(search);
//    mainLayout.setExpandRatio(search, 1);
    // top-level component properties
    setWidth("100%");
    setHeight("200%");
    
    return mainLayout;
  }
  
  private Component buildChartLayout() {
    chartLayout = new VerticalLayout();
    chartLayout.setHeight("100%");
    chartLayout.setWidth("85%");
    return chartLayout;
  }
  
  private Component buildSearchLayout() {
    searchLayout = new VerticalLayout();
    searchLayout.setSizeUndefined();
    searchLayout.setMargin(true);
    searchLayout.setSpacing(true);
    
    searchPanel = new Panel();
//    searchLayout.setVisible(false);
    searchPanel.setSizeUndefined();
    searchPanel.setContent(searchLayout);

    // categoryCombo
    categoryCombo = new OptionGroup(Msg.get("expense.category"));
    categoryCombo.setMultiSelect(true);
    categoryCombo.addItems(expenseSheet.getCategoryList());
    searchLayout.addComponent(categoryCombo);

    // userCombo
    userCombo = new OptionGroup(Msg.get("expense.user"));
    userCombo.setMultiSelect(true);
    userCombo.addItems(expenseSheet.getUserLimitList());
    searchLayout.addComponent(userCombo);

    // searchButton
    final Button searchButton = new Button();
    searchButton.setCaption(Msg.get("expense.search"));
    searchButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @SuppressWarnings("unchecked")
      @Override
      public void buttonClick(ClickEvent event) {
        User filterUser = null;
        String filterFormula = null;
        String filterComment = null;
        List<User> users = new ArrayList<User>();
        Set<UserLimit> setUserLimit = (Set<UserLimit>) userCombo.getValue();
        for(Iterator<UserLimit> iter = setUserLimit.iterator(); iter.hasNext();)
          users.add(iter.next().getUser());
        users.add((User) filterUser);
        
        List<Category> categories = new ArrayList<Category>();
        Set<Category> setCategory = (Set<Category>) categoryCombo.getValue();
        for(Iterator<Category> iter = setCategory.iterator(); iter.hasNext();)
          categories.add(iter.next());
        
        expenseSheet.setFilter(new Filter(categories, users, filterFormula, filterComment));
        showCharts();
      }
    });

    searchLayout.addComponent(searchButton);

    return searchPanel;
  }

}
