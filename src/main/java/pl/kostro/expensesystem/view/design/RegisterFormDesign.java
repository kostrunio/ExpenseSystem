package pl.kostro.expensesystem.view.design;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

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
    addComponents(nameField, passwordField, rePasswordField, emailField, createButtons());
  }

  private Component createButtons() {
    CssLayout layout = new CssLayout();
    layout.setStyleName("buttons");
    saveButton.setStyleName("friendly");
    layout.addComponent(saveButton);
    return layout;
  }
}
