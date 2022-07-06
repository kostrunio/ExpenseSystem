package pl.kostro.expensesystem.newui.components.form.register;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import pl.kostro.expensesystem.utils.msg.Msg;

public class RegisterFormDesign extends FormLayout {
  protected TextField nameField = new TextField();
  protected PasswordField passwordField = new PasswordField();
  protected PasswordField rePasswordField = new PasswordField();
  protected TextField emailField = new TextField();
  protected Button saveButton = new Button();

  public RegisterFormDesign() {
    setMaxWidth("300px");
    nameField.setLabel(Msg.get("registerPage.user"));
    nameField.setRequiredIndicatorVisible(true);
    nameField.setWidth("15em");
    passwordField.setLabel(Msg.get("registerPage.password"));
    passwordField.setRequiredIndicatorVisible(true);
    passwordField.setWidth("15em");
    rePasswordField.setLabel(Msg.get("registerPage.passwordRep"));
    rePasswordField.setRequiredIndicatorVisible(true);
    rePasswordField.setWidth("15em");
    emailField.setLabel(Msg.get("registerPage.email"));
    emailField.setRequiredIndicatorVisible(true);
    emailField.setWidth("15em");
    add(nameField, passwordField, rePasswordField, emailField, createButtons());
//    addFormItem(nameField, Msg.get("registerPage.user"));
//    addFormItem(passwordField, Msg.get("registerPage.password"));
//    addFormItem(rePasswordField, Msg.get("registerPage.passwordRep"));
//    addFormItem(emailField, Msg.get("registerPage.email"));

  }

  private Component createButtons() {
    FlexLayout layout = new FlexLayout();
//    layout.setClassName("buttons");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.setText(Msg.get("registerPage.save"));
    layout.add(saveButton);
    return layout;
  }
}
