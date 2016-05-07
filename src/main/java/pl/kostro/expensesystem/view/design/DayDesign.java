package pl.kostro.expensesystem.view.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class DayDesign extends VerticalLayout {
  protected HorizontalLayout navigationLayout;
  protected Button previousDayButton;
  protected DateField thisDateField;
  protected Button nextDayButton;
  protected CssLayout dayLayout;
  protected CssLayout categoryListPanel;
  protected CssLayout categoryGrid;
  protected Button backButton;
  protected CssLayout expenseListPanel;
  protected VerticalLayout expenseList;
  protected Label categoryLabel;
  protected GridLayout expenseGrid;
  protected HorizontalLayout addExpense;
  protected ComboBox userBox;
  protected TextField formulaField;
  protected ComboBox commentBox;
  protected CheckBox notifyBox;
  protected Button saveButton;

  public DayDesign() {
    Design.read(this);
  }
}
