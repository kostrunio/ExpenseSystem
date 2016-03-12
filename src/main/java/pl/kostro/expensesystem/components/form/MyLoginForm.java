package pl.kostro.expensesystem.components.form;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.service.RealUserService;

import com.ejt.vaadin.loginform.LoginForm;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MyLoginForm extends LoginForm {

  private Button loginButton;

  protected String getUserNameFieldCaption() {
    return Msg.get("loginPage.userName");
  }

  protected String getPasswordFieldCaption() {
    return Msg.get("loginPage.password");
  }

  protected String getLoginButtonCaption() {
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

  @Override
  protected void login(String userName, String password) {
    RealUser loggedUser = null;
    try {
      loggedUser = RealUserService.getUserData(userName, password);
      if (loggedUser == null) {
        ShowNotification.logonProblem();
        loginButton.setEnabled(true);
      } else {
        VaadinSession.getCurrent().setAttribute(RealUser.class, loggedUser);
      }
      ((ExpenseSystemUI) getUI()).updateContent();
    } catch (Exception e) {
      ShowNotification.dbProblem(e.getMessage());
      loginButton.setEnabled(true);
    }
  }

}
