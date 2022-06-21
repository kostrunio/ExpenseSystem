package pl.kostro.expensesystem.newui.component.form.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import pl.kostro.expensesystem.utils.msg.Msg;

public class LoginForm extends com.vaadin.flow.component.login.LoginForm {

  public LoginForm() {
    LoginI18n i18n = LoginI18n.createDefault();
    LoginI18n.Form i18nForm = i18n.getForm();
    i18nForm.setTitle("");
    i18nForm.setUsername(Msg.get("loginPage.userName"));
    i18nForm.setPassword(Msg.get("loginPage.password"));
    i18nForm.setSubmit(Msg.get("loginPage.signIn"));
    i18n.setForm(i18nForm);

    setI18n(i18n);

    setForgotPasswordButtonVisible(false);

  }

  protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
//    this.loginButton = loginButton;
    FormLayout loginForm = new FormLayout();

    userNameField.setWidth(15, Unit.EM);
    loginForm.add(userNameField);

    passwordField.setWidth(15, Unit.EM);
    loginForm.add(passwordField);

    CssLayout buttons = new CssLayout();
    buttons.setStyleName("buttons");
//    loginForm.add(buttons);

    loginButton.addClassName(ValoTheme.BUTTON_FRIENDLY);
    loginButton.addClickShortcut(Key.ENTER);
    loginButton.setDisableOnClick(true);
//    buttons.addComponent(loginButton);

    return loginForm;
  }

}
