package pl.kostro.expensesystem.view.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;
import pl.kostro.expensesystem.components.form.ExpenseForm;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { … }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class TableDesign extends VerticalLayout {
  protected Panel actionPanel;
  protected HorizontalLayout actionsLayout;
  protected DateField fromDateField;
  protected DateField toDateField;
  protected ComboBox categoryBox;
  protected ComboBox userBox;
  protected TextField formulaField;
  protected ComboBox commentBox;
  protected Button filterButton;
  protected Button newExpenseButton;
  protected HorizontalLayout workingLayout;
  protected Grid expenseGrid;
  protected ExpenseForm expenseForm;

  public TableDesign() {
    Design.read(this);
  }
}
