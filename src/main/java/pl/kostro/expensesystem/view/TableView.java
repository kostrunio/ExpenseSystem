package pl.kostro.expensesystem.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.server.VaadinSession;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.view.design.TableDesign;

@SuppressWarnings("serial")
public class TableView extends TableDesign {
  
  private ExpenseSheetService eshs;
  private ExpenseService es;

  private Logger logger = LogManager.getLogger();
  private LocalDate calendar;
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
  
  public TableView() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    es = AppCtxProvider.getBean(ExpenseService.class);
    logger.info("create");
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.calendar = VaadinSession.getCurrent().getAttribute(LocalDate.class);
    setCaption();
    fromDateField.setDateFormat("yyyy-MM-dd");
    toDateField.setDateFormat("yyyy-MM-dd");
    expenseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

    expenseForm.prepare(expenseSheet, this);
    
    fromDateField.setValue(CalendarUtils.getFirstDay(calendar));
    toDateField.setValue(CalendarUtils.getLastDay(calendar));
    categoryBox.setItems(expenseSheet.getCategoryList());
    userBox.setItems(expenseSheet.getUserLimitList());
    commentBox.setNewItemHandler(event -> {});
    commentBox.setItems((itemCaption, filterText) -> itemCaption.contains(filterText), eshs.getAllComments(expenseSheet));
    filterButton.addClickListener(filterClick);
    newExpenseButton.addClickListener(newClick);
    expenseGrid.addSelectionListener(event -> {
      if (expenseGrid.getSelectedItems().size() != 0)
        expenseForm.edit(expenseGrid.getSelectedItems().iterator().next());
    });
    
    if (expenseSheet.getFilter() != null) {
      if (expenseSheet.getFilter().getCategories() != null
          && expenseSheet.getFilter().getCategories().size() > 0)
        categoryBox.setSelectedItem(expenseSheet.getFilter().getCategories().get(0));
      if (expenseSheet.getFilter().getUsers() != null
          && expenseSheet.getFilter().getUsers().size() > 0)
        userBox.setSelectedItem(eshs.getUserLimitForUser(expenseSheet, expenseSheet.getFilter().getUsers().get(0)));
      if (expenseSheet.getFilter().getFormula() != null
          && !expenseSheet.getFilter().getFormula().isEmpty())
        formulaField.setValue(expenseSheet.getFilter().getFormula());
      if (expenseSheet.getFilter().getComment() != null
          && !expenseSheet.getFilter().getComment().isEmpty())
        commentBox.setSelectedItem(expenseSheet.getFilter().getComment());
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
    expenseGrid.addColumn(Expense::getDate).setCaption(Msg.get("findPage.date"));
    expenseGrid.addColumn(Expense::getCategory).setCaption(Msg.get("findPage.category"));
    expenseGrid.addColumn(Expense::getUser).setCaption(Msg.get("findPage.user"));
    expenseGrid.addColumn(Expense::getFormula).setCaption(Msg.get("findPage.formula"));
    expenseGrid.addColumn(Expense::getValue).setCaption(Msg.get("findPage.value"));
    expenseGrid.addColumn(Expense::getComment).setCaption(Msg.get("findPage.comment"));
  }
  
  public void refreshExpenses() {
    expenseGrid.setItems(es.findAllExpense(expenseSheet));
    expenseGrid.sort("date", SortDirection.DESCENDING);
    expenseForm.setVisible(false);
  }
}
