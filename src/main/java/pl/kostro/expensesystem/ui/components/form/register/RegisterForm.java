package pl.kostro.expensesystem.ui.components.form.register;

import com.vaadin.ui.Button.ClickListener;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.ui.ExpenseSystemUI;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.ui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.mail.SendEmail;

public class RegisterForm extends RegisterFormDesign {
  
  private RealUserService rus;
  
  private ClickListener saveListener = event -> {
    if (!nameField.getValue().isEmpty())
      if (rus.findRealUser(nameField.getValue()) != null) {
        ShowNotification.registerProblem(nameField.getValue());
        return;
      }
    if (!nameField.getValue().isEmpty() && !passwordField.getValue().isEmpty()
        && passwordField.getValue().equals(rePasswordField.getValue())) {
      RealUserEntity realUser = rus.create(nameField.getValue(), passwordField.getValue(), emailField.getValue());
      new Thread(() -> SendEmail.welcome(realUser)).start();
      ShowNotification.registerOK();
      ((ExpenseSystemUI) getUI()).updateContent();
    } else {
      ShowNotification.registerProblem();
      saveButton.setEnabled(true);
    }
  };

  public RegisterForm() {
    rus = AppCtxProvider.getBean(RealUserService.class);
    nameField.setCaption(Msg.get("registerPage.user"));
    nameField.focus();
    passwordField.setCaption(Msg.get("registerPage.password"));
    rePasswordField.setCaption(Msg.get("registerPage.passwordRep"));
    emailField.setCaption(Msg.get("registerPage.email"));

    saveButton.setCaption(Msg.get("registerPage.save"));
    saveButton.setDisableOnClick(true);
    saveButton.addClickListener(saveListener);
  }
}
