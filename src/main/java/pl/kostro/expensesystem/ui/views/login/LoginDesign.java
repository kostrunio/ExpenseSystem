package pl.kostro.expensesystem.ui.views.login;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import pl.kostro.expensesystem.ui.components.form.login.LoginForm;
import pl.kostro.expensesystem.ui.components.form.register.RegisterForm;

public class LoginDesign extends CssLayout {
  protected VerticalLayout centeringLayout = new VerticalLayout();
  protected VerticalLayout loginPanel = new VerticalLayout();
  protected HorizontalLayout buttons = new HorizontalLayout();
  protected Button signIn = new Button();
  protected Button signUp = new Button();
  protected LoginForm loginForm = new LoginForm();
  protected RegisterForm registerForm = new RegisterForm();
  protected CssLayout loginInformation = new CssLayout();
  protected Label loginInfoText = new Label();

  public LoginDesign() {
    setStyleName("login-screen");
    setResponsive(true);
    setWidth("100%");
    addComponents(createCenteringLayout(), createLoginInformation());
  }

  private Component createCenteringLayout() {
    centeringLayout.setPrimaryStyleName("centering-layout");
    centeringLayout.setSpacing(false);
    centeringLayout.setSizeUndefined();
    centeringLayout.setMargin(false);
    centeringLayout.addComponent(createLoginPanel());
    centeringLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    return centeringLayout;
  }

  private Component createLoginPanel() {
    loginPanel.setStyleName("login-panel");
    loginPanel.setSpacing(false);
    loginPanel.setSizeUndefined();
    loginPanel.setMargin(false);
    loginForm.setStyleName("login-form");
    registerForm.setVisible(false);
    registerForm.setStyleName("login-form");
    loginPanel.addComponents(createButtons(), loginForm, registerForm);
    loginPanel.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    loginPanel.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    loginPanel.setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
    return loginPanel;
  }

  private Component createButtons() {
    buttons.setSpacing(false);
    buttons.setWidth("100%");
    signIn.setEnabled(false);
    signIn.setStyleName("borderless-colored");
    signIn.setWidth("100%");
    signUp.setStyleName("borderless");
    signUp.setWidth("100%");
    buttons.addComponents(signIn, signUp);
    buttons.setComponentAlignment(signUp, Alignment.MIDDLE_RIGHT);
    return buttons;
  }

  private Component createLoginInformation() {
    loginInformation.setStyleName("login-information");
    loginInfoText.setWidth("100%");
    loginInfoText.setContentMode(ContentMode.HTML);
    loginInformation.addComponent(loginInfoText);
    return loginInformation;
  }
}
