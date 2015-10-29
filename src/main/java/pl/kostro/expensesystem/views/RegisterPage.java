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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
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
    Responsive.makeResponsive(registerPanel);
    final VerticalLayout contentLayout = new VerticalLayout();
    contentLayout.setSizeUndefined();
    contentLayout.setSpacing(true);
    contentLayout.setMargin(true);
    Responsive.makeResponsive(contentLayout);
    registerPanel.setContent(contentLayout);
    
    final FormLayout registerForm = new FormLayout();
    contentLayout.addComponent(registerForm);
    
    final TextField nameField = new TextField(Msg.get("registerPage.user"));
    nameField.setRequired(true);
    nameField.setIcon(FontAwesome.USER);
    registerForm.addComponent(nameField);
    
    final TextField passwordField = new TextField(Msg.get("registerPage.password"));
    passwordField.setRequired(true);
    passwordField.setIcon(FontAwesome.LOCK);
    registerForm.addComponent(passwordField);
    
    final TextField rePasswordField = new TextField(Msg.get("registerPage.passwordRep"));
    rePasswordField.setRequired(true);
    rePasswordField.setIcon(FontAwesome.LOCK);
    registerForm.addComponent(rePasswordField);
    
    final TextField emailField = new TextField(Msg.get("registerPage.email"));
    emailField.setRequired(true);
    registerForm.addComponent(emailField);
    
    final HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.setSpacing(true);
    registerForm.addComponent(buttonsLayout);
    
    final Button saveButton = new Button(Msg.get("registerPage.save"));
    saveButton.setStyleName(ValoTheme.BUTTON_SMALL);
    saveButton.setIcon(FontAwesome.SAVE);
    saveButton.addClickListener(new ClickListener() {
      private static final long serialVersionUID = -3113257276493397402L;

      @Override
      public void buttonClick(ClickEvent event) {
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
    return registerPanel;
  }

}
