package pl.kostro.expensesystem.view.design;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.form.ExpenseForm;
import pl.kostro.expensesystem.model.Expense;

@SuppressWarnings("serial")
public class TableDesign extends VerticalLayout {
  protected Panel actionPanel;
  protected HorizontalLayout actionsLayout;
  protected PopupDateField fromDateField;
  protected PopupDateField toDateField;
  protected ComboBox categoryBox;
  protected ComboBox userBox;
  protected TextField formulaField;
  protected ComboBox commentBox;
  protected Button newExpenseButton;
  
  protected HorizontalLayout horizontalLayout_2;
  protected ExpenseForm expenseForm;
  protected Grid expenseGrid;
  protected Button filterButton;
  
  public TableDesign() {
    setSizeFull();
    setSpacing(true);
    
    // actionsLayout
    actionPanel = buildActionsPanel();
    addComponent(actionPanel);
    
    // horizontalLayout_2
    horizontalLayout_2 = buildHorizontalLayout_2();
    addComponent(horizontalLayout_2);
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
    fromDateField = new PopupDateField();
    fromDateField.setCaption(Msg.get("findPage.dateFrom"));
    fromDateField.setDateFormat("yyyy-MM-dd");
    actionsLayout.addComponent(fromDateField);
    
    // toDateField
    toDateField = new PopupDateField();
    toDateField.setCaption(Msg.get("findPage.dateTo"));
    toDateField.setDateFormat("yyyy-MM-dd");
    actionsLayout.addComponent(toDateField);
    
    // categoryBox
    categoryBox = new ComboBox();
    categoryBox.setCaption(Msg.get("findPage.category"));
    actionsLayout.addComponent(categoryBox);
    
    // userBox
    userBox = new ComboBox();
    userBox.setCaption(Msg.get("findPage.user"));
    actionsLayout.addComponent(userBox);
    
    // formulaField
    formulaField = new TextField();
    formulaField.setCaption(Msg.get("findPage.formula"));
    actionsLayout.addComponent(formulaField);
    
    // commentBox
    commentBox = new ComboBox();
    commentBox.setCaption(Msg.get("findPage.comment"));
    actionsLayout.addComponent(commentBox);
    
    // filterButton
    filterButton = new Button();
    filterButton.setCaption(Msg.get("findPage.filter"));
    actionsLayout.addComponent(filterButton);
    actionsLayout.setComponentAlignment(filterButton, new Alignment(9));
    
    // newExpenseButton
    newExpenseButton = new Button();
    newExpenseButton.setCaption(Msg.get("findPage.add"));
    newExpenseButton.setImmediate(true);
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
    horizontalLayout_2.addComponent(expenseGrid);
    
    // expenseForm
    expenseForm = new ExpenseForm();
    expenseForm.setSizeFull();
    horizontalLayout_2.addComponent(expenseForm);
    
    return horizontalLayout_2;
  }
}
