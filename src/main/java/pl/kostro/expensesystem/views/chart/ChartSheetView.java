package pl.kostro.expensesystem.views.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ChartSheetView extends CustomComponent {
  private static final long serialVersionUID = -7074536397686956050L;

  private VerticalLayout mainLayout;
  
  private Panel searchPanel = new Panel();
  private VerticalLayout searchLayout = new VerticalLayout();
  private OptionGroup categoryCombo;
  private OptionGroup userCombo;
  
  ExpenseSheet expenseSheet;
  
  public ChartSheetView() {
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    setCompositionRoot(buildMainLayout());

    if (expenseSheet.getFilter() == null)
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

  private VerticalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setSizeFull();
    mainLayout.addComponent(buildSearchLayout());
    // top-level component properties
    setWidth("100.0%");
    setHeight("200.0%");
    
    return mainLayout;
  }
  
  private Component buildSearchLayout() {
    searchPanel.setVisible(false);
    searchPanel.setContent(searchLayout);
    searchLayout.removeAllComponents();
    searchLayout.setMargin(true);
    searchLayout.setSpacing(true);

    // categoryCombo
    categoryCombo = new OptionGroup(Msg.get("expense.category"));
    categoryCombo.setStyleName("v-select-optiongroup-horizontal");
    categoryCombo.setMultiSelect(true);
    categoryCombo.addItems(expenseSheet.getCategoryList());
    searchLayout.addComponent(categoryCombo);

    // userCombo
    userCombo = new OptionGroup(Msg.get("expense.user"));
    userCombo.setStyleName("v-select-optiongroup-horizontal");
    userCombo.setMultiSelect(true);
    userCombo.addItems(expenseSheet.getUserLimitList());
    searchLayout.addComponent(userCombo);

    // searchButton
    final Button searchButton = new Button();
    searchButton.setCaption(Msg.get("expense.search"));
    searchButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(ClickEvent event) {
        User filterUser = null;
        String filterFormula = null;
        String filterComment = null;
        if (userCombo.getValue() instanceof UserLimit) {
          filterUser = ((UserLimit) userCombo.getValue()).getUser();
        }
        List<Category> categories = new ArrayList<Category>();
        categories.add((Category) categoryCombo.getValue());
        List<User> users = new ArrayList<User>();
        users.add((User) filterUser);
        expenseSheet.setFilter(new Filter(categories, users, filterFormula, filterComment));
      }
    });

    searchLayout.addComponent(searchButton);
    searchLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);

    return searchPanel;
  }

}
