package pl.kostro.expensesystem.views;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.service.RealUserService;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class RegisterPage extends VerticalLayout {

	private static final long serialVersionUID = 6185954337600080624L;

	public RegisterPage() {
	  setSizeFull();
	  
	  Component registerForm = buildRegisterForm();
    addComponent(registerForm);
    setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
	}
	
	private Component buildRegisterForm() {
    final Panel registerPanel = new Panel(Msg.get("registerPage.label"));
    registerPanel.setSizeUndefined();

    final VerticalLayout contentLayout = new VerticalLayout();
    registerPanel.setContent(contentLayout);

    contentLayout.setSizeUndefined();
    contentLayout.setSpacing(true);
    contentLayout.setMargin(true);
    Responsive.makeResponsive(contentLayout);
    
    contentLayout.addComponent(buildFields());
    
    return registerPanel;
  }

  private Component buildFields() {
    VerticalLayout fields = new VerticalLayout();
    fields.setSpacing(true);
    fields.addStyleName("fields");
    
    final TextField nameField = new TextField(Msg.get("registerPage.user"));
    nameField.setRequired(true);
    nameField.setIcon(FontAwesome.USER);
    nameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    fields.addComponent(nameField);
    
    final PasswordField passwordField = new PasswordField(Msg.get("registerPage.password"));
    passwordField.setRequired(true);
    passwordField.setIcon(FontAwesome.LOCK);
    passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    fields.addComponent(passwordField);
    
    final PasswordField rePasswordField = new PasswordField(Msg.get("registerPage.passwordRep"));
    rePasswordField.setRequired(true);
    rePasswordField.setIcon(FontAwesome.LOCK);
    rePasswordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    fields.addComponent(rePasswordField);
    
    final TextField emailField = new TextField(Msg.get("registerPage.email"));
    emailField.setRequired(true);
    emailField.setIcon(FontAwesome.ENVELOPE);
    emailField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    fields.addComponent(emailField);
    
    final HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.setSpacing(true);
    fields.addComponent(buttonsLayout);
    
    final Button saveButton = new Button(Msg.get("registerPage.save"));
    saveButton.setStyleName(ValoTheme.BUTTON_SMALL);
    saveButton.setIcon(FontAwesome.SAVE);
    saveButton.addClickListener(new ClickListener() {
      private static final long serialVersionUID = -3113257276493397402L;

      @Override
      public void buttonClick(ClickEvent event) {
        if (!nameField.getValue().isEmpty())
          if (RealUserService.findRealUser(nameField.getValue()) != null) {
            ShowNotification.registerProblem(nameField.getValue());
          }
        if (!nameField.getValue().isEmpty() &&
            !passwordField.getValue().isEmpty() &&
            passwordField.getValue().equals(rePasswordField.getValue())) {
          RealUserService realUserService = new RealUserService();
          realUserService.createRealUser(nameField.getValue(), passwordField.getValue(), emailField.getValue());
          ((ExpenseSystemUI)getUI()).updateContent();
        } else {
          ShowNotification.registerProblem();
        }
      }
    });
    
    buttonsLayout.addComponent(saveButton);
    buttonsLayout.setComponentAlignment(saveButton, Alignment.MIDDLE_CENTER);
    
    final Button cancelButton = new Button(Msg.get("registerPage.cancel"));
    cancelButton.setStyleName(ValoTheme.BUTTON_SMALL);
    cancelButton.setIcon(FontAwesome.UNDO);
    cancelButton.addClickListener(new ClickListener() {
      private static final long serialVersionUID = -3113257276493397402L;

      @Override
      public void buttonClick(ClickEvent event) {
        ((ExpenseSystemUI)getUI()).updateContent();
      }
    });
    
    buttonsLayout.addComponent(cancelButton);
    buttonsLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
    
    return fields;
  }

}
