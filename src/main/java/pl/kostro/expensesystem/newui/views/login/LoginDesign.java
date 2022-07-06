package pl.kostro.expensesystem.newui.views.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kostro.expensesystem.newui.components.form.login.LoginForm;
import pl.kostro.expensesystem.newui.components.form.register.RegisterForm;
import pl.kostro.expensesystem.utils.msg.Msg;

public class LoginDesign extends FlexLayout {
  protected FlexLayout centeringLayout = new FlexLayout();
  protected VerticalLayout loginPanel = new VerticalLayout();
  protected HorizontalLayout buttons = new HorizontalLayout();
  protected Button signIn = new Button();
  protected Button signUp = new Button();
  protected LoginForm loginForm = new LoginForm();
  protected RegisterForm registerForm = new RegisterForm();
  protected VerticalLayout loginInformation = new VerticalLayout();

  protected H1 loginInfoHeader = new H1();
  protected Span loginInfoText = new Span();

  public LoginDesign() {
    setSizeFull();
    getStyle().set("background-image", "url('img/ship.jpg')");
//    setClassName("login-design");
    setCaption();
    add(createLoginInformation(), createCenteringLayout());
    expand(centeringLayout);
  }

  private void setCaption() {
    signIn.setText(Msg.get("loginPage.signin"));
    signUp.setText(Msg.get("loginPage.signup"));
    loginInfoHeader.setText("New Expense System");
    loginInfoText.setText(Msg.get("loginPage.welcome"));
  }

  private Component createCenteringLayout() {
    centeringLayout.setSizeFull();
    centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    centeringLayout.setAlignItems(Alignment.CENTER);
//    centeringLayout.setClassName("centering-layout");
    centeringLayout.add(createLoginPanel());
    return centeringLayout;
  }

  private Component createLoginPanel() {
    loginPanel.getStyle().set("background", "white");
    loginPanel.setSizeUndefined();
//    loginPanel.setClassName("login-panel");
//    loginPanel.setSpacing(false);
//    loginPanel.setSizeUndefined();
//    loginPanel.setMargin(false);
//    loginForm.setClassName("login-form");
    registerForm.setVisible(false);
//    registerForm.setSizeUndefined();
//    registerForm.setClassName("register-form");
    loginPanel.add(createButtons(), loginForm, registerForm);
    loginPanel.setAlignItems(Alignment.CENTER);
    return loginPanel;
  }

  private Component createButtons() {
//    buttons.setSpacing(false);
//    buttons.setWidth("100%");
    signIn.setEnabled(false);
//    signIn.setClassName("borderless-colored");
//    signIn.setWidth("100%");
//    signUp.setClassName("borderless");
//    signUp.setWidth("100%");
    buttons.add(signIn, signUp);
    buttons.setAlignItems(Alignment.STRETCH);
    return buttons;
  }

  private Component createLoginInformation() {
    loginInformation.getStyle().set("background-color", "rgba(96, 160, 234, 0.7)");
//    loginInformation.setClassName("login-information");
    loginInformation.setWidth("300px");
//    loginInfoHeader.setWidth("100%");
//    loginInfoText.setWidth("100%");

    loginInformation.add(loginInfoHeader, loginInfoText);
    return loginInformation;
  }
}
