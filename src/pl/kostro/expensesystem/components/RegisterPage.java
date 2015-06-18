package pl.kostro.expensesystem.components;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.service.RealUserService;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class RegisterPage extends VerticalLayout {

	private static final long serialVersionUID = 6185954337600080624L;

	public RegisterPage() {
	  setWidthUndefined();
	  Component registerForm = buildRegisterForm();
    addComponent(registerForm);
    setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
	}
	
	private Component buildRegisterForm() {
    final Panel registerPanel = new Panel("Rejestracja nowego u¿ytkownika");
    final VerticalLayout contentLayout = new VerticalLayout();
    contentLayout.setSizeUndefined();
    contentLayout.setSpacing(true);
    contentLayout.setMargin(true);
    registerPanel.setContent(contentLayout);
    
    final FormLayout registerForm = new FormLayout();
    contentLayout.addComponent(registerForm);
    
    final TextField nameField = new TextField("nazwa u¿ytkownika");
    nameField.setRequired(true);
    registerForm.addComponent(nameField);
    
    final TextField passwordField = new TextField("has³o");
    passwordField.setRequired(true);
    registerForm.addComponent(passwordField);
    
    final TextField rePasswordField = new TextField("powtórz has³o");
    rePasswordField.setRequired(true);
    registerForm.addComponent(rePasswordField);
    
    final TextField emailField = new TextField("e-mail");
    emailField.setRequired(true);
    registerForm.addComponent(emailField);
    
    final Button saveButton = new Button("Zapisz");
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
          Notification notification = new Notification("B³êdne has³o");
          notification.setDescription("Podane has³a do siebie nie pasuj¹");
          notification.setStyleName("error register-help");
          notification.setPosition(Position.BOTTOM_CENTER);
          notification.setDelayMsec(10000);
          notification.show(Page.getCurrent());
        }
      }
    });
    
    registerForm.addComponent(saveButton);
    
    final Button cancelButton = new Button("Powrót");
    cancelButton.addClickListener(new ClickListener() {
      private static final long serialVersionUID = -3113257276493397402L;

      @Override
      public void buttonClick(ClickEvent event) {
        ((ExpenseSystemUI)getUI()).updateContent();
      }
    });
    
    registerForm.addComponent(cancelButton);
    return registerPanel;
  }

}
