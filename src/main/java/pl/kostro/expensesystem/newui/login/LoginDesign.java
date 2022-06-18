package pl.kostro.expensesystem.newui.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kostro.expensesystem.ui.components.form.register.RegisterForm;

public class LoginDesign extends FlexLayout {
  protected VerticalLayout centeringLayout = new VerticalLayout();
  protected VerticalLayout loginPanel = new VerticalLayout();
  protected HorizontalLayout buttons = new HorizontalLayout();
  protected Button signIn = new Button();
  protected Button signUp = new Button();
  protected LoginForm loginForm = new LoginForm();
  protected RegisterForm registerForm = new RegisterForm();
  protected FlexLayout loginInformation = new FlexLayout();
  protected Label loginInfoText = new Label();

  public LoginDesign() {
    setClassName("login-screen");
//    setResponsive(true);
    setWidth("100%");
    add(createCenteringLayout(), createLoginInformation());
  }

  private Component createCenteringLayout() {
    centeringLayout.setClassName("centering-layout");
    centeringLayout.setSpacing(false);
    centeringLayout.setSizeUndefined();
    centeringLayout.setMargin(false);
    centeringLayout.add(createLoginPanel());
//    centeringLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    return centeringLayout;
  }

  private Component createLoginPanel() {
    loginPanel.setClassName("login-panel");
    loginPanel.setSpacing(false);
    loginPanel.setSizeUndefined();
    loginPanel.setMargin(false);
    loginForm.setClassName("login-form");
    registerForm.setVisible(false);
    registerForm.setStyleName("login-form");
    loginPanel.add(createButtons(), loginForm/*, registerForm*/);
    loginPanel.setAlignItems(Alignment.CENTER);
//    loginPanel.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
//    loginPanel.setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
    return loginPanel;
  }

  private Component createButtons() {
    buttons.setSpacing(false);
    buttons.setWidth("100%");
    signIn.setEnabled(false);
    signIn.setClassName("borderless-colored");
    signIn.setWidth("100%");
    signUp.setClassName("borderless");
    signUp.setWidth("100%");
    buttons.add(signIn, signUp);
    buttons.setAlignItems(Alignment.END);
    return buttons;
  }

  private Component createLoginInformation() {
    loginInformation.setClassName("login-information");
    loginInfoText.setWidth("100%");
//    loginInfoText.setContentMode(ContentMode.HTML);
    loginInformation.add(loginInfoText);
    return loginInformation;
  }
}
