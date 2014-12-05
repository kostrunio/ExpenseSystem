package pl.kostro.expensesystem.components.mainPageComponents;

import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.db.AdapterDB;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.utils.Calculator;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class AddNewExpense extends CustomComponent {

  @AutoGenerated
  private VerticalLayout mainLayout;

  @AutoGenerated
  private HorizontalLayout buttonLayout;

  @AutoGenerated
  private Button backButton;

  @AutoGenerated
  private Button saveButton;

  @AutoGenerated
  private GridLayout expenseGrid;

  @AutoGenerated
  private ComboBox comment;

  @AutoGenerated
  private TextField formula;

  @AutoGenerated
  private ComboBox user;

  private static final long serialVersionUID = 665074948023076492L;

  /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

  /**
   * The constructor should first build the main layout, set the composition
   * root and then do any custom initialization.
   * 
   * The constructor will not be automatically regenerated by the visual editor.
   * 
   * @param date
   * @param expenseSheet
   * @param category
   */
  public AddNewExpense(final ExpenseSheet expenseSheet, final Expense expense, final Boolean modify) {
    buildMainLayout();
    setCompositionRoot(mainLayout);

    // TODO add user code here
    user.setNewItemsAllowed(false);
    user.setNullSelectionAllowed(false);
    user.addItems(expenseSheet.getUserLimitList());
    if (expense.getUser() != null)
      user.select(expenseSheet.getUserLimitForUser(expense.getUser()));
    else
      user.select(expenseSheet.getDefaultUserLimit());
    user.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = -7382627003346137188L;

      @Override
      public void valueChange(ValueChangeEvent event) {
        saveButton.setEnabled(true);
      }

    });

    formula.setValue(expense.getFormula());
    formula.addValueChangeListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = -7382627003346137188L;

      @Override
      public void valueChange(ValueChangeEvent event) {
        if (event.getProperty().getValue() != null && !event.getProperty().getValue().toString().equals(new String()))
          if (Calculator.verifyAllowed(event.getProperty().getValue().toString()))
            saveButton.setEnabled(true);
          else
            saveButton.setEnabled(false);
      }
    });

    comment.setNewItemsAllowed(true);
    comment.setNullSelectionAllowed(true);
    comment.addItems(expenseSheet.getCommentForCategory(expense.getCategory()));
    comment.select(expense.getComment());
    comment.addValueChangeListener(new Property.ValueChangeListener() {


      private static final long serialVersionUID = -2428143441766112539L;

      @Override
      public void valueChange(ValueChangeEvent event) {
        saveButton.setEnabled(true);
      }

    });

    saveButton.setEnabled(false);
    saveButton.setClickShortcut(KeyCode.ENTER);
    saveButton.addStyleName(Reindeer.BUTTON_DEFAULT);
    saveButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(ClickEvent event) {
        if (user.getValue() instanceof UserLimit) {
          UserLimit userLimit = (UserLimit) user.getValue();
          if (modify)
            AdapterDB.removeExpense(expenseSheet, expense);
          expense.setUser(userLimit.getUser());
          expense.setFormula(formula.getValue());
          if (comment.getValue() != null)
            expense.setComment(comment.getValue().toString());
          AdapterDB.creteExpense(expenseSheet, expense);
          setCompositionRoot(new CategoryExpenseView(expenseSheet, expense.getDate(), expense.getCategory()));
        }
      }
    });
    backButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(ClickEvent event) {
        if (saveButton.isEnabled()) {
          ConfirmDialog.show(getUI(), "Zapisanie zmian", "Czy chesz opu�ci� stron� bez zapisywania?", "Tak, porzu� zmiany", "Nie", new ConfirmDialog.Listener() {

            private static final long serialVersionUID = 3844318339125611876L;

            @Override
            public void onClose(ConfirmDialog dialog) {
              if (dialog.isConfirmed()) {
                setCompositionRoot(new CategoryExpenseView(expenseSheet, expense.getDate(), expense.getCategory()));
              }
            }
          });
        } else {
          setCompositionRoot(new CategoryExpenseView(expenseSheet, expense.getDate(), expense.getCategory()));
        }
      }
    });
  }

  @AutoGenerated
  private VerticalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setImmediate(false);
    mainLayout.setWidth("100%");
    mainLayout.setHeight("100%");
    mainLayout.setMargin(true);

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
    expenseGrid.setMargin(true);
    expenseGrid.setSpacing(true);
    expenseGrid.setColumns(3);

    // user
    user = new ComboBox();
    user.setCaption("U�ytkownik");
    user.setImmediate(true);
    user.setWidth("-1px");
    user.setHeight("-1px");
    expenseGrid.addComponent(user, 0, 0);

    // formula
    formula = new TextField();
    formula.setCaption("Formu�a");
    formula.setImmediate(true);
    formula.setWidth("-1px");
    formula.setHeight("-1px");
    expenseGrid.addComponent(formula, 1, 0);

    // comment
    comment = new ComboBox();
    comment.setCaption("komentarz");
    comment.setImmediate(true);
    comment.setWidth("-1px");
    comment.setHeight("-1px");
    expenseGrid.addComponent(comment, 2, 0);

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
    buttonLayout.setSpacing(true);

    // saveButton
    saveButton = new Button();
    saveButton.setCaption("Zapisz");
    saveButton.setImmediate(true);
    saveButton.setWidth("-1px");
    saveButton.setHeight("-1px");
    buttonLayout.addComponent(saveButton);

    // backButton
    backButton = new Button();
    backButton.setCaption("Powr�t");
    backButton.setImmediate(true);
    backButton.setWidth("-1px");
    backButton.setHeight("-1px");
    buttonLayout.addComponent(backButton);

    return buttonLayout;
  }

}
