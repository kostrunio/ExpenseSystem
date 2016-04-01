package pl.kostro.expensesystem.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.ExpenseService;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.view.design.TableDesign;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;

@SuppressWarnings("serial")
public class TableView extends TableDesign {

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
      expenseForm.edit((Expense)expenseGrid.getSelectedRow());
    }
  };
  
  public TableView() {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.calendar = VaadinSession.getCurrent().getAttribute(Calendar.class);
    setCaption();
    expenseGrid.setContainerDataSource(new BeanItemContainer<Expense>(Expense.class));
    expenseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

    expenseForm.prepare(expenseSheet, this);
    
    fromDateField.setValue(UserSummaryService.getFirstDay(calendar.getTime()));
    toDateField.setValue(UserSummaryService.getLastDay(calendar.getTime()));
    categoryBox.addItems(expenseSheet.getCategoryList());
    userBox.addItems(expenseSheet.getUserLimitList());
    commentBox.setNewItemsAllowed(true);
    commentBox.setFilteringMode(FilteringMode.CONTAINS);
    commentBox.addItems(ExpenseSheetService.getAllComments(expenseSheet));
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
            Expense.class, ExpenseService.findAllExpense(expenseSheet)));
    expenseGrid.recalculateColumnWidths();
    expenseForm.setVisible(false);
  }
}
