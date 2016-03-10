package pl.kostro.expensesystem.components.form;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.service.RealUserService;

@SuppressWarnings("serial")
public class RegisterForm extends FormLayout {

  public RegisterForm() {
    final TextField nameField = new TextField(Msg.get("registerPage.user"));
    nameField.setRequired(true);
    nameField.setWidth(15, Unit.EM);
    nameField.focus();
    addComponent(nameField);

    final PasswordField passwordField = new PasswordField(Msg.get("registerPage.password"));
    passwordField.setRequired(true);
    passwordField.setWidth(15, Unit.EM);
    addComponent(passwordField);

    final PasswordField rePasswordField = new PasswordField(Msg.get("registerPage.passwordRep"));
    rePasswordField.setRequired(true);
    rePasswordField.setWidth(15, Unit.EM);
    addComponent(rePasswordField);

    final TextField emailField = new TextField(Msg.get("registerPage.email"));
    emailField.setRequired(true);
    emailField.setWidth(15, Unit.EM);
    addComponent(emailField);

    CssLayout buttons = new CssLayout();
    buttons.setStyleName("buttons");
    addComponent(buttons);

    final Button saveButton = new Button(Msg.get("registerPage.save"));
    saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
    saveButton.setDisableOnClick(true);
    saveButton.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        if (!nameField.getValue().isEmpty())
          if (RealUserService.findRealUser(nameField.getValue()) != null) {
            ShowNotification.registerProblem(nameField.getValue());
            return;
          }
        if (!nameField.getValue().isEmpty() && !passwordField.getValue().isEmpty()
            && passwordField.getValue().equals(rePasswordField.getValue())) {
          RealUserService realUserService = new RealUserService();
          realUserService.createRealUser(nameField.getValue(), passwordField.getValue(), emailField.getValue());
          ShowNotification.registerOK();
          ((ExpenseSystemUI) getUI()).updateContent();
        } else {
          ShowNotification.registerProblem();
        }
      }
    });
    buttons.addComponent(saveButton);
  }
}
