package pl.kostro.expensesystem.newui.component.form.register;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.ui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.mail.SendEmail;
import pl.kostro.expensesystem.utils.msg.Msg;

public class RegisterForm extends RegisterFormDesign {
  
  private RealUserService rus;
  
  private ComponentEventListener<ClickEvent<Button>> saveListener = event -> {
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
      UI.getCurrent().getPage().reload();
    } else {
      ShowNotification.registerProblem();
      saveButton.setEnabled(true);
    }
  };

  public RegisterForm() {
    rus = AppCtxProvider.getBean(RealUserService.class);
    nameField.setLabel(Msg.get("registerPage.user"));
    nameField.focus();
    passwordField.setLabel(Msg.get("registerPage.password"));
    rePasswordField.setLabel(Msg.get("registerPage.passwordRep"));
    emailField.setLabel(Msg.get("registerPage.email"));

    saveButton.setText(Msg.get("registerPage.save"));
    saveButton.setDisableOnClick(true);
    saveButton.addClickListener(saveListener);
  }
}
