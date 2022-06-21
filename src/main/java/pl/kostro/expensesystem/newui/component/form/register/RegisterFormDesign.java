package pl.kostro.expensesystem.newui.component.form.register;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class RegisterFormDesign extends FormLayout {
  protected TextField nameField = new TextField();
  protected PasswordField passwordField = new PasswordField();
  protected PasswordField rePasswordField = new PasswordField();
  protected TextField emailField = new TextField();
  protected Button saveButton = new Button();

  public RegisterFormDesign() {
    setSizeFull();
    nameField.setRequiredIndicatorVisible(true);
    nameField.setWidth("15em");
    passwordField.setRequiredIndicatorVisible(true);
    passwordField.setWidth("15em");
    rePasswordField.setRequiredIndicatorVisible(true);
    rePasswordField.setWidth("15em");
    emailField.setRequiredIndicatorVisible(true);
    emailField.setWidth("15em");
    add(nameField, passwordField, rePasswordField, emailField, createButtons());
  }

  private Component createButtons() {
    FlexLayout layout = new FlexLayout();
    layout.setClassName("buttons");
    saveButton.setClassName("friendly");
    layout.add(saveButton);
    return layout;
  }
}
