package pl.kostro.expensesystem.components.form;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.service.RealUserService;

import com.ejt.vaadin.loginform.LoginForm;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class MyLoginForm extends LoginForm {
  private static final long serialVersionUID = 731185390322506461L;
  private TextField userNameField;
  private PasswordField passwordField;
  
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
    this.userNameField = userNameField;
    this.passwordField = passwordField;
    HorizontalLayout fieldsLayout = new HorizontalLayout();
    fieldsLayout.setSpacing(true);
    fieldsLayout.addStyleName("fields");

    userNameField.setIcon(FontAwesome.USER);
    userNameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    userNameField.focus();

    passwordField.setIcon(FontAwesome.LOCK);
    passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

    loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
    loginButton.setClickShortcut(KeyCode.ENTER);
    
    fieldsLayout.addComponents(userNameField, passwordField, loginButton);
    fieldsLayout.setComponentAlignment(loginButton, Alignment.BOTTOM_LEFT);

    return fieldsLayout;
  }
  
  @Override
  protected void login(String userName, String password) {
    RealUser loggedUser = null;
    try {
      loggedUser = RealUserService.getUserData(userNameField.getValue(), passwordField.getValue());
      if (loggedUser == null) {
        ShowNotification.logonProblem();
      } else {
        VaadinSession.getCurrent().setAttribute(RealUser.class, loggedUser);
      }
      ((ExpenseSystemUI)getUI()).updateContent();
    } catch (Exception e) {
      ShowNotification.dbProblem(e.getMessage());
    }
  }

}
