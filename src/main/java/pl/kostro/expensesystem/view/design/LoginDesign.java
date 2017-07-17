package pl.kostro.expensesystem.view.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;
import pl.kostro.expensesystem.components.form.LoginForm;
import pl.kostro.expensesystem.components.form.RegisterForm;

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
public class LoginDesign extends CssLayout {
  protected VerticalLayout centeringLayout;
  protected VerticalLayout loginPanel;
  protected HorizontalLayout buttons;
  protected Button signIn;
  protected Button signUp;
  protected LoginForm loginForm;
  protected RegisterForm registerForm;
  protected CssLayout loginInformation;
  protected Label loginInfoText;

  public LoginDesign() {
    Design.read(this);
  }
}
