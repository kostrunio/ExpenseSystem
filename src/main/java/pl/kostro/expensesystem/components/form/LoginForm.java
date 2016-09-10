package pl.kostro.expensesystem.components.form;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.Msg;

@SuppressWarnings("serial")
public class LoginForm extends com.vaadin.ui.LoginForm {

  private Button loginButton;

  @Override
  public String getUsernameCaption() {
    return Msg.get("loginPage.userName");
  }

  @Override
  public String getPasswordCaption() {
    return Msg.get("loginPage.password");
  }

  @Override
  public String getLoginButtonCaption() {
    return Msg.get("loginPage.signIn");
  }

  @Override
  protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
    this.loginButton = loginButton;
    FormLayout loginForm = new FormLayout();

    userNameField.setWidth(15, Unit.EM);
    loginForm.addComponent(userNameField);

    passwordField.setWidth(15, Unit.EM);
    loginForm.addComponent(passwordField);

    CssLayout buttons = new CssLayout();
    buttons.setStyleName("buttons");
    loginForm.addComponent(buttons);

    loginButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    loginButton.setClickShortcut(KeyCode.ENTER);
    loginButton.setDisableOnClick(true);
    buttons.addComponent(loginButton);

    return loginForm;
  }

  public Button getLoginButton() {
    return loginButton;
  }
}
