package pl.kostro.expensesystem.ui.view.design;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AccountDesign extends VerticalLayout {
  protected Label titleLabel = new Label("Ustawienia konta");
  protected TextField usernameField = new TextField();
  protected Panel emailPanel = new Panel("Adres e-mail");
  protected TextField emailField = new TextField();
  protected TextField emailField2 = new TextField();
  protected Button saveEmailButton = new Button();
  protected Panel passwordPanel = new Panel();
  protected PasswordField oldPasswordField = new PasswordField();
  protected PasswordField newPasswordField = new PasswordField();
  protected PasswordField newPasswordField2 = new PasswordField();
  protected Button savePasswordButton = new Button();

  public AccountDesign() {
    setStyleName("account-layout");
    titleLabel.setStyleName("h1 no-margin colored");
    usernameField.setEnabled(false);
    usernameField.setStyleName("username-field");
    usernameField.setWidth("100%");
    usernameField.setReadOnly(true);
    addComponents(titleLabel, usernameField, createPanelsLayout());
  }

  private Component createPanelsLayout() {
    CssLayout layout = new CssLayout();
    layout.setWidth("100%");
    layout.addComponents(createEmailPanel(), createPasswordPanel());
    return layout;
  }

  private Component createEmailPanel() {
    emailPanel = new Panel();
    emailPanel.setStyleName("email-panel");
    VerticalLayout layout = new VerticalLayout();
    emailField.setWidth("100%");
    emailField2.setEnabled(false);
    emailField2.setWidth("100%");
    emailField2.setDescription("W celu zmiany adresu, wpisz nowy powyżej i tutaj.");
    emailField2.setPlaceholder("W celu zmiany adresu, wpisz nowy powyżej i tutaj.");
    saveEmailButton.setEnabled(false);
    saveEmailButton.setStyleName("friendly");
    saveEmailButton.setWidth("100%");
    layout.addComponents(emailField, emailField2, saveEmailButton);
    layout.setComponentAlignment(saveEmailButton, Alignment.BOTTOM_LEFT);
    emailPanel.setContent(layout);
    return emailPanel;
  }

  private Component createPasswordPanel() {
    passwordPanel = new Panel();
    passwordPanel.setStyleName("password-panel");
    VerticalLayout layout = new VerticalLayout();
    oldPasswordField.setWidth("100%");
    newPasswordField.setEnabled(false);
    newPasswordField.setWidth("100%");
    newPasswordField2.setEnabled(false);
    newPasswordField2.setWidth("100%");
    savePasswordButton.setEnabled(false);
    savePasswordButton.setStyleName("friendly");
    savePasswordButton.setWidth("100%");
    layout.addComponents(oldPasswordField, newPasswordField, newPasswordField2, savePasswordButton);
    layout.setComponentAlignment(savePasswordButton, Alignment.BOTTOM_LEFT);
    passwordPanel.setContent(layout);
    return passwordPanel;
  }
}
