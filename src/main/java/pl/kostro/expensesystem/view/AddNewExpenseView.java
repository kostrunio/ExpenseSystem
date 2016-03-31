package pl.kostro.expensesystem.view;

import java.util.Calendar;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.ExpenseService;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Calculator;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AddNewExpenseView extends CustomComponent {

  @AutoGenerated
  private VerticalLayout mainLayout;

  @AutoGenerated
  private HorizontalLayout buttonLayout;

  @AutoGenerated
  private Button saveButton;

  @AutoGenerated
  private GridLayout expenseGrid;

  @AutoGenerated
  private ComboBox commentBox;

  @AutoGenerated
  private TextField formulaField;

  @AutoGenerated
  private ComboBox userBox;

  private DayView dayView;
  /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

  private ExpenseSheet expenseSheet;

  public AddNewExpenseView() {
    super();
  }

  public void setDayView(DayView dayView) {
    this.dayView = dayView;
  }

  public void buildAddNewExpense(final Expense expense, final Boolean modify, final Calendar calendar) {
    buildMainLayout();
    setCompositionRoot(mainLayout);

    // TODO add user code here
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    userBox.setNewItemsAllowed(false);
    userBox.setNullSelectionAllowed(false);
    userBox.addItems(expenseSheet.getUserLimitList());
    if (expense.getUser() != null)
      userBox.select(ExpenseSheetService.getUserLimitForUser(expenseSheet, expense.getUser()));
    else
      userBox.select(expenseSheet.getUserLimitList().get(0));
    userBox.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent event) {
        verifyFormula(formulaField.getValue());
      }

    });

    formulaField.focus();
    formulaField.setValue(expense.getFormula());
    formulaField.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent event) {
        verifyFormula(formulaField.getValue());
      }
    });

    commentBox.setNewItemsAllowed(true);
    commentBox.setNullSelectionAllowed(true);
    commentBox.setFilteringMode(FilteringMode.CONTAINS);
    commentBox.addItems(ExpenseSheetService.getCommentForCategory(expenseSheet, expense.getCategory()));
    commentBox.select(expense.getComment());
    commentBox.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent event) {
        verifyFormula(formulaField.getValue());
      }
    });

    saveButton.setEnabled(false);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        if (userBox.getValue() instanceof UserLimit) {
          ExpenseService.saveExpense(expenseSheet, expense, (UserLimit) userBox.getValue(), formulaField.getValue(),
              commentBox.getValue(), modify);
          dayView.refreshView(calendar, expense.getCategory());
        }
      }
    });
  }

  private void verifyFormula(Object formula) {
    if (formula != null && !formula.toString().equals("") && Calculator.verifyAllowed(formula.toString()))
      saveButton.setEnabled(true);
    else
      saveButton.setEnabled(false);
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
    setHeight("100.0%");

    // expenseGrid
    expenseGrid = buildExpenseGrid();
    mainLayout.addComponent(expenseGrid);

    // buttonLayout
    buttonLayout = buildButtonLayout();
    mainLayout.addComponent(buttonLayout);

    return mainLayout;
  }

  @AutoGenerated
  private GridLayout buildExpenseGrid() {
    // common part: create layout
    expenseGrid = new GridLayout();
    expenseGrid.setImmediate(false);
    expenseGrid.setWidth("-1px");
    expenseGrid.setHeight("-1px");
    expenseGrid.setMargin(false);
    expenseGrid.setSpacing(true);
    expenseGrid.setColumns(3);

    // user
    userBox = new ComboBox();
    userBox.setCaption(Msg.get("newExpense.user"));
    userBox.setImmediate(true);
    userBox.setWidth("-1px");
    userBox.setHeight("-1px");
    expenseGrid.addComponent(userBox, 0, 0);

    // formula
    formulaField = new TextField();
    formulaField.setCaption(Msg.get("newExpense.formula"));
    formulaField.setImmediate(true);
    formulaField.setWidth("-1px");
    formulaField.setHeight("-1px");
    expenseGrid.addComponent(formulaField, 1, 0);

    // comment
    commentBox = new ComboBox();
    commentBox.setCaption(Msg.get("newExpense.comment"));
    commentBox.setImmediate(true);
    commentBox.setWidth("-1px");
    commentBox.setHeight("-1px");
    expenseGrid.addComponent(commentBox, 2, 0);

    return expenseGrid;
  }

  @AutoGenerated
  private HorizontalLayout buildButtonLayout() {
    // common part: create layout
    buttonLayout = new HorizontalLayout();
    buttonLayout.setImmediate(false);
    buttonLayout.setWidth("-1px");
    buttonLayout.setHeight("-1px");
    buttonLayout.setMargin(true);

    // saveButton
    saveButton = new Button();
    saveButton.setCaption(Msg.get("newExpense.save"));
    saveButton.setImmediate(true);
    saveButton.setWidth("-1px");
    saveButton.setHeight("-1px");
    buttonLayout.addComponent(saveButton);

    return buttonLayout;
  }

}
