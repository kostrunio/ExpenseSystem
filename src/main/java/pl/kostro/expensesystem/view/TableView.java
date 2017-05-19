package pl.kostro.expensesystem.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.SpringMain;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.DateConverter;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.view.design.TableDesign;

@SuppressWarnings("serial")
public class TableView extends TableDesign {
  
  private ExpenseSheetService eshs;
  private ExpenseService es;

  private Logger logger = LogManager.getLogger();
  private Calendar calendar;
  private ExpenseSheet expenseSheet;
  private Button.ClickListener filterClick = new ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      User filterUser = null;
      if (userBox.getValue() instanceof UserLimit) {
        filterUser = ((UserLimit) userBox.getValue()).getUser();
      }
      List<Category> categories = new ArrayList<Category>();
      categories.add((Category) categoryBox.getValue());
      List<User> users = new ArrayList<User>();
      users.add((User) filterUser);
      expenseSheet.setFilter(new Filter(
          fromDateField.getValue(),
          toDateField.getValue(),
          categories,
          users,
          formulaField.getValue(),
          (String)commentBox.getValue()));
      refreshExpenses();
    }
  };
  private Button.ClickListener newClick = new ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      expenseForm.edit(new Expense());
    }
  };
  private SelectionEvent.SelectionListener gridSelect = new SelectionListener() {
    @Override
    public void select(SelectionEvent event) {
      if (expenseGrid.getSelectedRow() != null)
        expenseForm.edit((Expense)expenseGrid.getSelectedRow());
    }
  };
  
  public TableView() {
    ApplicationContext context = new AnnotationConfigApplicationContext(SpringMain.class);
    eshs = context.getBean(ExpenseSheetService.class);
    es = context.getBean(ExpenseService.class);
    logger.info("create");
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.calendar = VaadinSession.getCurrent().getAttribute(Calendar.class);
    setCaption();
    fromDateField.setDateFormat("yyyy-MM-dd");
    toDateField.setDateFormat("yyyy-MM-dd");
    expenseGrid.setContainerDataSource(new BeanItemContainer<Expense>(Expense.class));
    expenseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    expenseGrid.getColumn("date").setConverter(new DateConverter());

    expenseForm.prepare(expenseSheet, this);
    
    fromDateField.setValue(CalendarUtils.getFirstDay(calendar.getTime()));
    toDateField.setValue(CalendarUtils.getLastDay(calendar.getTime()));
    categoryBox.addItems(expenseSheet.getCategoryList());
    userBox.addItems(expenseSheet.getUserLimitList());
    commentBox.setNewItemsAllowed(true);
    commentBox.setFilteringMode(FilteringMode.CONTAINS);
    commentBox.addItems(eshs.getAllComments(expenseSheet));
    filterButton.addClickListener(filterClick);
    newExpenseButton.addClickListener(newClick);
    expenseGrid.addSelectionListener(gridSelect);
    
    if (expenseSheet.getFilter() != null) {
      if (expenseSheet.getFilter().getCategories() != null
          && expenseSheet.getFilter().getCategories().size() > 0)
        categoryBox.select(expenseSheet.getFilter().getCategories().get(0));
      if (expenseSheet.getFilter().getUsers() != null
          && expenseSheet.getFilter().getUsers().size() > 0)
        userBox.select(expenseSheet.getFilter().getUsers().get(0));
      if (expenseSheet.getFilter().getFormula() != null
          && !expenseSheet.getFilter().getFormula().isEmpty())
        formulaField.setValue(expenseSheet.getFilter().getFormula());
      if (expenseSheet.getFilter().getComment() != null
          && !expenseSheet.getFilter().getComment().isEmpty())
        commentBox.select(expenseSheet.getFilter().getComment());
      expenseSheet.getFilter().setDateFrom(fromDateField.getValue());
      expenseSheet.getFilter().setDateTo(toDateField.getValue());
    } else
      expenseSheet.setFilter(new Filter(
        fromDateField.getValue(),
        toDateField.getValue(),
        null,
        null,
        null,
        null));

    refreshExpenses();
  }
  
  private void setCaption() {
    fromDateField.setCaption(Msg.get("findPage.dateFrom"));
    toDateField.setCaption(Msg.get("findPage.dateTo"));
    categoryBox.setCaption(Msg.get("findPage.category"));
    userBox.setCaption(Msg.get("findPage.user"));
    formulaField.setCaption(Msg.get("findPage.formula"));
    commentBox.setCaption(Msg.get("findPage.comment"));
    newExpenseButton.setCaption(Msg.get("findPage.add"));
    expenseGrid.setColumns("date", "category", "user", "formula", "value", "comment");
    expenseGrid.getColumn("date").setHeaderCaption(Msg.get("findPage.date"));
    expenseGrid.getColumn("category").setHeaderCaption(Msg.get("findPage.category"));
    expenseGrid.getColumn("user").setHeaderCaption(Msg.get("findPage.user"));
    expenseGrid.getColumn("formula").setHeaderCaption(Msg.get("findPage.formula"));
    expenseGrid.getColumn("value").setHeaderCaption(Msg.get("findPage.value"));
    expenseGrid.getColumn("comment").setHeaderCaption(Msg.get("findPage.comment"));
  }
  
  public void refreshExpenses() {
    expenseGrid.setContainerDataSource(new BeanItemContainer<Expense>(
            Expense.class, es.findAllExpense(expenseSheet)));
    expenseGrid.recalculateColumnWidths();
    expenseGrid.sort("date", SortDirection.DESCENDING);
    expenseForm.setVisible(false);
  }
}
