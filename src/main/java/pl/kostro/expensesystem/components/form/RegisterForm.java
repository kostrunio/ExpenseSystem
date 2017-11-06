package pl.kostro.expensesystem.components.form;

import com.vaadin.ui.Button.ClickListener;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.utils.SendEmail;
import pl.kostro.expensesystem.view.design.RegisterFormDesign;

@SuppressWarnings("serial")
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
      RealUser realUser = rus.createRealUser(nameField.getValue(), passwordField.getValue(), emailField.getValue());
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
