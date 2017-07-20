package pl.kostro.expensesystem.view.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.v7.ui.DateField;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.UserLimit;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.Design;

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
public class ExpenseFormDesign extends FormLayout {
  protected HorizontalLayout actionsLayout;
  protected Button saveButton;
  protected Button duplicateButton;
  protected Button removeButton;
  protected DateField dateField;
  protected ComboBox<Category> categoryBox;
  protected ComboBox<UserLimit> userBox;
  protected TextField formulaField;
  protected ComboBox<String> commentBox;
  protected CheckBox notifyBox;

  public ExpenseFormDesign() {
    Design.read(this);
  }
}
