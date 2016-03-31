package pl.kostro.expensesystem.view.design;

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.Msg;

@SuppressWarnings("serial")
public class AddNewExpenseDesign extends VerticalLayout {

  protected GridLayout expenseGrid;
  protected ComboBox userBox;
  protected TextField formulaField;
  protected ComboBox commentBox;

  protected HorizontalLayout buttonLayout;
  protected Button saveButton;

  public AddNewExpenseDesign() {
    setSizeFull();

    // expenseGrid
    expenseGrid = buildExpenseGrid();
    addComponent(expenseGrid);

    // buttonLayout
    buttonLayout = buildButtonLayout();
    addComponent(buttonLayout);
  }

  private GridLayout buildExpenseGrid() {
    expenseGrid = new GridLayout();
    expenseGrid.setSpacing(true);
    expenseGrid.setColumns(3);

    // user
    userBox = new ComboBox();
    userBox.setCaption(Msg.get("newExpense.user"));
    userBox.setImmediate(true);
    userBox.setNewItemsAllowed(false);
    userBox.setNullSelectionAllowed(false);
    expenseGrid.addComponent(userBox, 0, 0);

    // formula
    formulaField = new TextField();
    formulaField.setCaption(Msg.get("newExpense.formula"));
    formulaField.setImmediate(true);
    formulaField.focus();
    expenseGrid.addComponent(formulaField, 1, 0);

    // comment
    commentBox = new ComboBox();
    commentBox.setCaption(Msg.get("newExpense.comment"));
    commentBox.setImmediate(true);
    commentBox.setNewItemsAllowed(true);
    commentBox.setNullSelectionAllowed(true);
    commentBox.setFilteringMode(FilteringMode.CONTAINS);
    expenseGrid.addComponent(commentBox, 2, 0);

    return expenseGrid;
  }

  private HorizontalLayout buildButtonLayout() {
    // common part: create layout
    buttonLayout = new HorizontalLayout();
    buttonLayout.setMargin(true);

    // saveButton
    saveButton = new Button();
    saveButton.setCaption(Msg.get("newExpense.save"));
    saveButton.setImmediate(true);
    saveButton.setEnabled(false);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    buttonLayout.addComponent(saveButton);

    return buttonLayout;
  }
}
