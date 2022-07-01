package pl.kostro.expensesystem.newui.views.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class AccountDesign extends VerticalLayout {
  protected Label titleLabel = new Label("Ustawienia konta");
  protected TextField usernameField = new TextField();
  protected VerticalLayout emailPanel = new VerticalLayout();
  protected Label emailLabel = new Label();
  protected TextField emailField = new TextField();
  protected TextField emailField2 = new TextField();
  protected Button saveEmailButton = new Button();
  protected VerticalLayout passwordPanel = new VerticalLayout();
  protected Label passwordLabel = new Label();
  protected PasswordField oldPasswordField = new PasswordField();
  protected PasswordField newPasswordField = new PasswordField();
  protected PasswordField newPasswordField2 = new PasswordField();
  protected Button savePasswordButton = new Button();

  public AccountDesign() {
    setClassName("account-layout");
    titleLabel.setClassName("h1 no-margin colored");
    usernameField.setEnabled(false);
    usernameField.setClassName("username-field");
    usernameField.setWidth("100%");
    usernameField.setReadOnly(true);
    add(titleLabel, usernameField, createPanelsLayout());
  }

  private Component createPanelsLayout() {
    FlexLayout layout = new FlexLayout();
    layout.setWidth("100%");
    layout.add(createEmailPanel(), createPasswordPanel());
    return layout;
  }

  private Component createEmailPanel() {
    emailPanel.setClassName("email-panel");
    VerticalLayout layout = new VerticalLayout();
    emailField.setWidth("100%");
    emailField2.setEnabled(false);
    emailField2.setWidth("100%");
    emailField2.setHelperText("W celu zmiany adresu, wpisz nowy powyżej i tutaj.");
    emailField2.setPlaceholder("W celu zmiany adresu, wpisz nowy powyżej i tutaj.");
    saveEmailButton.setEnabled(false);
    saveEmailButton.setClassName("friendly");
    saveEmailButton.setWidth("100%");
    layout.add(emailField, emailField2, saveEmailButton);
    layout.setAlignItems(Alignment.END);
    emailPanel.add(emailLabel, layout);
    return emailPanel;
  }

  private Component createPasswordPanel() {
    passwordPanel.setClassName("password-panel");
    VerticalLayout layout = new VerticalLayout();
    oldPasswordField.setWidth("100%");
    newPasswordField.setEnabled(false);
    newPasswordField.setWidth("100%");
    newPasswordField2.setEnabled(false);
    newPasswordField2.setWidth("100%");
    savePasswordButton.setEnabled(false);
    savePasswordButton.setClassName("friendly");
    savePasswordButton.setWidth("100%");
    layout.add(oldPasswordField, newPasswordField, newPasswordField2, savePasswordButton);
    layout.setAlignItems(Alignment.END);
    passwordPanel.add(passwordLabel, layout);
    return passwordPanel;
  }
}
