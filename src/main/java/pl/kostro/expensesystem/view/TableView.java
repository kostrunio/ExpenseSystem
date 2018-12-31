package pl.kostro.expensesystem.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.HasValue;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox.NewItemHandler;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.components.grid.FooterRow;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.view.design.TableDesign;

@SuppressWarnings("serial")
public class TableView extends TableDesign {
  
  private ExpenseSheetService eshs;

  private Logger logger = LogManager.getLogger();
  private LocalDate date;
  private ExpenseSheet expenseSheet;
  private FooterRow footer = expenseGrid.prependFooterRow();
  private Column<Expense, LocalDate> dateColumn;
  private Column<Expense, Object> categoryColumn;
  private Column<Expense, String> formulaColumn;
  private Column<Expense, BigDecimal> valueColumn;
  @SuppressWarnings("rawtypes")
  private HasValue.ValueChangeListener filterChanged = event -> {
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
  };
  private ClickListener newClicked = event -> expenseForm.edit(new Expense());
  private SelectionListener<Expense> itemClicked = event -> {
    if (expenseGrid.getSelectedItems().size() != 0)
      expenseForm.edit(expenseGrid.getSelectedItems().iterator().next());
    else
      expenseForm.setVisible(false);
  };
  private NewItemHandler addComment = event -> commentBox.setValue(event);
  
  @SuppressWarnings("unchecked")
  public TableView() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    logger.info("create");
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class);
    setCaption();
    fromDateField.setDateFormat("yyyy-MM-dd");
    toDateField.setDateFormat("yyyy-MM-dd");
    expenseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

    expenseForm.prepare(expenseSheet, this);
    
    fromDateField.setValue(date.withDayOfMonth(1));
    fromDateField.addValueChangeListener(filterChanged);
    toDateField.setValue(date.withDayOfMonth(date.lengthOfMonth()));
    toDateField.addValueChangeListener(filterChanged);
    categoryBox.setItemCaptionGenerator(item -> item.getName());
    categoryBox.setItems(expenseSheet.getCategoryList());
    categoryBox.addValueChangeListener(filterChanged);
    userBox.setItemCaptionGenerator(item -> item.getUser().getName());
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(filterChanged);
    formulaField.addValueChangeListener(filterChanged);
    commentBox.setNewItemHandler(addComment);
    commentBox.setItems((itemCaption, filterText) -> itemCaption.contains(filterText), eshs.getAllComments(expenseSheet));
    commentBox.addValueChangeListener(filterChanged);
    newExpenseButton.addClickListener(newClicked);
    expenseGrid.addSelectionListener(itemClicked);
    
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
    dateColumn = expenseGrid.addColumn(Expense::getDate);
    dateColumn.setCaption(Msg.get("findPage.date"));
    categoryColumn = expenseGrid.addColumn(item -> item.getCategory().getName());
    categoryColumn.setCaption(Msg.get("findPage.category"));
    expenseGrid.addColumn(item -> item.getUser().getName()).setCaption(Msg.get("findPage.user"));
    formulaColumn = expenseGrid.addColumn(Expense::getFormula);
    formulaColumn.setCaption(Msg.get("findPage.formula"));
    valueColumn = expenseGrid.addColumn(Expense::getValue);
    valueColumn.setCaption(Msg.get("findPage.value"));
    expenseGrid.addColumn(Expense::getComment).setCaption(Msg.get("findPage.comment"));
    footer.getCell(dateColumn).setText(Msg.get("findPage.rows"));
    footer.getCell(formulaColumn).setText(Msg.get("findPage.sum"));
  }
  
  public void refreshExpenses() {
    logger.info("refreshExpenses");
    List<Expense> expensesList = eshs.findAllExpense(expenseSheet);
    expenseGrid.setItems(expensesList);
    footer.getCell(categoryColumn).setText(""+expensesList.size());
    footer.getCell(valueColumn).setHtml("<b>" + calcualteSum(expensesList) + "</b>");
    expenseGrid.sort(expenseGrid.getColumns().get(0), SortDirection.DESCENDING);
    expenseForm.setVisible(false);
  }

  private BigDecimal calcualteSum(List<Expense> expensesList) {
    BigDecimal result = new BigDecimal(0);
    for(Expense exp : expensesList) {
      result = result.add(exp.getValue());
    }
    return result;
  }
}
