package pl.kostro.expensesystem.newui.components.form.login;

import com.vaadin.flow.component.login.LoginI18n;
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

}
