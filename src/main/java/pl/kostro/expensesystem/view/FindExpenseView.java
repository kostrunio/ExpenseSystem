package pl.kostro.expensesystem.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.form.ExpenseForm;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.ExpenseService;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.UserSummaryService;
import pl.kostro.expensesystem.utils.Filter;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FindExpenseView extends CustomComponent {

  private VerticalLayout mainLayout;
  private Panel actionPanel;
  private HorizontalLayout actionsLayout;
  private PopupDateField fromDateField;
  private PopupDateField toDateField;
  private ComboBox categoryBox;
  private ComboBox userBox;
  private TextField formulaField;
  private ComboBox commentBox;
  private Button newExpenseButton;
  
  private HorizontalLayout horizontalLayout_2;
  private ExpenseForm expenseForm;
  private Grid expenseGrid;
  private Button filterButton;
  private Calendar calendar;
  private ExpenseSheet expenseSheet;
  
  public FindExpenseView(Calendar date) {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.calendar = date;
    setCompositionRoot(buildMainLayout());

    expenseForm.prepare(expenseSheet, this);
    
    fromDateField.setValue(UserSummaryService.getFirstDay(calendar.getTime()));
    toDateField.setValue(UserSummaryService.getLastDay(calendar.getTime()));
    categoryBox.addItems(expenseSheet.getCategoryList());
    userBox.addItems(expenseSheet.getUserLimitList());
    commentBox.setNewItemsAllowed(true);
    commentBox.setFilteringMode(FilteringMode.CONTAINS);
    commentBox.addItems(ExpenseSheetService.getAllComments(expenseSheet));
    
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
  
  public void refreshExpenses() {
    expenseGrid.setContainerDataSource(new BeanItemContainer<Expense>(
            Expense.class, ExpenseService.findAllExpense(expenseSheet)));
    expenseGrid.recalculateColumnWidths();
    expenseForm.setVisible(false);
  }

  private VerticalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setSizeFull();
    mainLayout.setSpacing(true);
    
    // top-level component properties
    setWidth("100.0%");
    setHeight("100.0%");
    
    // actionsLayout
    actionPanel = buildActionsPanel();
    mainLayout.addComponent(actionPanel);
    
    // horizontalLayout_2
    horizontalLayout_2 = buildHorizontalLayout_2();
    mainLayout.addComponent(horizontalLayout_2);
    
    return mainLayout;
  }

  private Panel buildActionsPanel() {
    // common part: create layout
    actionPanel = new Panel();
    actionsLayout = new HorizontalLayout();
    actionsLayout.setWidth("100.0%");
    actionsLayout.setHeightUndefined();
    actionsLayout.setMargin(true);
    actionsLayout.setSpacing(true);
    actionPanel.setContent(actionsLayout);
    
    // fromDateField
    fromDateField = new PopupDateField(Msg.get("findPage.dateFrom"));
    fromDateField.setDateFormat("yyyy-MM-dd");
    actionsLayout.addComponent(fromDateField);
    
    // toDateField
    toDateField = new PopupDateField(Msg.get("findPage.dateTo"));
    toDateField.setDateFormat("yyyy-MM-dd");
    actionsLayout.addComponent(toDateField);
    
    // categoryBox
    categoryBox = new ComboBox(Msg.get("findPage.category"));
    actionsLayout.addComponent(categoryBox);
    
    // userBox
    userBox = new ComboBox(Msg.get("findPage.user"));
    actionsLayout.addComponent(userBox);
    
    // formulaField
    formulaField = new TextField(Msg.get("findPage.formula"));
    actionsLayout.addComponent(formulaField);
    
    // commentBox
    commentBox = new ComboBox(Msg.get("findPage.comment"));
    actionsLayout.addComponent(commentBox);
    
    // filterButton
    filterButton = new Button(Msg.get("findPage.filter"));
    filterButton.addClickListener(new ClickListener() {
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
    });
    actionsLayout.addComponent(filterButton);
    actionsLayout.setComponentAlignment(filterButton, new Alignment(9));
    
    // newExpenseButton
    newExpenseButton = new Button(Msg.get("findPage.add"));
    newExpenseButton.setImmediate(true);
    newExpenseButton.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        expenseForm.edit(new Expense());
      }
    });
    actionsLayout.addComponent(newExpenseButton);
    actionsLayout.setComponentAlignment(newExpenseButton, new Alignment(10));
    
    return actionPanel;
  }

  private HorizontalLayout buildHorizontalLayout_2() {
    // common part: create layout
    horizontalLayout_2 = new HorizontalLayout();
    horizontalLayout_2.setSizeFull();
    
    // expenseGrid
    expenseGrid = new Grid();
    expenseGrid.setSizeFull();
    expenseGrid.setContainerDataSource(new BeanItemContainer<Expense>(Expense.class));
    expenseGrid.setColumnOrder(new Object[]{"date", "category", "user", "formula", "value", "comment"});
    expenseGrid.getColumn("date").setHeaderCaption(Msg.get("findPage.date"));
    expenseGrid.getColumn("category").setHeaderCaption(Msg.get("findPage.category"));
    expenseGrid.getColumn("user").setHeaderCaption(Msg.get("findPage.user"));
    expenseGrid.getColumn("formula").setHeaderCaption(Msg.get("findPage.formula"));
    expenseGrid.getColumn("value").setHeaderCaption(Msg.get("findPage.value"));
    expenseGrid.getColumn("comment").setHeaderCaption(Msg.get("findPage.comment"));
    expenseGrid.removeColumn("expenseSheet");
    expenseGrid.removeColumn("id");
    expenseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    expenseGrid.setImmediate(true);
    expenseGrid.addSelectionListener(new SelectionListener() {
      @Override
      public void select(SelectionEvent event) {
        expenseForm.edit((Expense)expenseGrid.getSelectedRow());
      }
    });
    horizontalLayout_2.addComponent(expenseGrid);
    
    // expenseForm
    expenseForm = new ExpenseForm();
    expenseForm.setSizeFull();
    horizontalLayout_2.addComponent(expenseForm);
    
    return horizontalLayout_2;
  }

}
