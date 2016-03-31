package pl.kostro.expensesystem.view;

import java.util.Calendar;
import java.util.List;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.CategoryExpense;
import pl.kostro.expensesystem.utils.DateExpense;
import pl.kostro.expensesystem.view.design.DayDesign;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DayView extends DayDesign {

  private DayView thisView = this;
  private ExpenseSheet expenseSheet;
  private Calendar calendar;
  private ValueChangeListener dateChange = new ValueChangeListener() {
    @Override
    public void valueChange(ValueChangeEvent event) {
      calendar.setTime(thisDateField.getValue());
      removeAllComponents();
      addComponent(new DayView());
    }
  };
  private Button.ClickListener prevClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      calendar.add(java.util.Calendar.DAY_OF_MONTH, -1);
      removeAllComponents();
      addComponent(new DayView());
    }
  };
  private Button.ClickListener nextClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
      removeAllComponents();
      addComponent(new DayView());
    }
  };
  private Button.ClickListener backClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      removeAllComponents();
      addComponent(new MonthView());
    }
  };

  public DayView() {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.calendar = VaadinSession.getCurrent().getAttribute(Calendar.class);

    thisDateField.setValue(calendar.getTime());
    previousDayButton.addClickListener(prevClick);
    thisDateField.addValueChangeListener(dateChange);
    nextDayButton.addClickListener(nextClick);

    prepareExpensesLayout();
    backButton.addClickListener(backClick);

    categoryLayout.removeAllComponents();
    categoryLayout.addComponent(new CategoryExpenseView(calendar, expenseSheet.getCategoryList().get(0), thisView));
  }

  public void refreshView(Calendar calendar, Category category) {
    this.calendar = calendar;
    prepareExpensesLayout();
    if (category != null) {
      categoryLayout.removeAllComponents();
      categoryLayout.addComponent(new CategoryExpenseView(calendar, category, thisView));
    }
  }

  private void prepareExpensesLayout() {
    expensesGrid.removeAllComponents();
    List<Category> categoryList = expenseSheet.getCategoryList();
    expensesGrid.setColumns(5);
    expensesGrid.setRows(categoryList.size() / 5 + 1);

    for (int i = 0; i < categoryList.size(); i++) {
      VerticalLayout vertLay = new VerticalLayout();
      expensesGrid.addComponent(vertLay, i % 5, i / 5);

      Category category = categoryList.get(i);
      Label catLabel = new Label();
      catLabel.setImmediate(false);
      catLabel.setWidth("-1px");
      catLabel.setHeight("-1px");
      catLabel.setValue(category.getName());
      vertLay.addComponent(catLabel);

      HorizontalLayout expLay = new HorizontalLayout();
      vertLay.addComponent(expLay);
      Button expButton = new Button();
      DateExpense dateExpenseMap = ExpenseSheetService.getDateExpenseMap(expenseSheet, calendar.getTime());
      if (dateExpenseMap == null || dateExpenseMap.getCategoryExpenseMap().get(category) == null)
        expButton.setCaption("0");
      else {
        CategoryExpense categoryExpenseMap = dateExpenseMap.getCategoryExpenseMap().get(category);
        expButton.setCaption(categoryExpenseMap.getSumString());
      }
      expButton.setImmediate(true);
      expButton.setWidth("-1px");
      expButton.setHeight("-1px");
      expButton.setData(category);
      expButton.addClickListener(new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
          if (event.getButton().getData() instanceof Category) {
            Category category = (Category) event.getButton().getData();
            categoryLayout.removeAllComponents();
            categoryLayout.addComponent(new CategoryExpenseView(calendar, category, thisView));
          }
        }

      });
      expLay.addComponent(expButton);
    }
  }

}
