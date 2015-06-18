 package pl.kostro.expensesystem.views;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.service.RealUserService;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LoginPage extends VerticalLayout {

  private static final long serialVersionUID = 73520020905697667L;

	public LoginPage() {
	  setSizeFull();
	  
	  Component loginForm = buildLoginForm();
    addComponent(loginForm);
    setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
	}
	
	private Component buildLoginForm() {
    final VerticalLayout loginPanel = new VerticalLayout();
    loginPanel.setSizeUndefined();
    loginPanel.setSpacing(true);
    Responsive.makeResponsive(loginPanel);

    loginPanel.addComponent(buildLabels());
    loginPanel.addComponent(buildfieldsLayout());
    loginPanel.addComponent(buildOthButtons());
    return loginPanel;
	}
	
	private Component buildLabels() {
    CssLayout labels = new CssLayout();

    Label welcome = new Label("Witamy");
    welcome.setSizeUndefined();
    welcome.addStyleName(ValoTheme.LABEL_H4);
    welcome.addStyleName(ValoTheme.LABEL_COLORED);
    labels.addComponent(welcome);

    Label title = new Label("Expense System Application");
    title.setSizeUndefined();
    title.addStyleName(ValoTheme.LABEL_H3);
    title.addStyleName(ValoTheme.LABEL_LIGHT);
    labels.addComponent(title);
    return labels;
	}
	
	private Component buildfieldsLayout() {
    HorizontalLayout fieldsLayout = new HorizontalLayout();
    fieldsLayout.setSpacing(true);

    final TextField usernameField = new TextField("nazwa u¿ytkownika");
    usernameField.setIcon(FontAwesome.USER);
    usernameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    usernameField.focus();

    final PasswordField passwordField = new PasswordField("has³o");
    passwordField.setIcon(FontAwesome.LOCK);
    passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

    final Button signinButton = new Button("Zaloguj");
    signinButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
    signinButton.setClickShortcut(KeyCode.ENTER);
    signinButton.addClickListener(new ClickListener() {

      private static final long serialVersionUID = 5199438233052362164L;

        @Override
        public void buttonClick(final ClickEvent event) {
          RealUser loggedUser = RealUserService.getUserData(usernameField.getValue(), passwordField.getValue());
          if (loggedUser == null) {
            Notification notification = new Notification("B³¹d logowania");
            notification.setDescription("B³êdna nazwa u¿ytkownika lub has³o");
            notification.setStyleName(ValoTheme.NOTIFICATION_ERROR + " " + ValoTheme.NOTIFICATION_SMALL + " " + ValoTheme.NOTIFICATION_CLOSABLE);
            notification.setPosition(Position.BOTTOM_CENTER);
            notification.setDelayMsec(10000);
            notification.show(Page.getCurrent());
          } else
            VaadinSession.getCurrent().setAttribute(RealUser.class.getName(), loggedUser);
          ((ExpenseSystemUI)getUI()).updateContent();
        }
    });
    
    fieldsLayout.addComponents(usernameField, passwordField, signinButton);
    fieldsLayout.setComponentAlignment(signinButton, Alignment.BOTTOM_LEFT);

    return fieldsLayout;
	}

	private Component buildOthButtons() {
	  HorizontalLayout buttonsLayout = new HorizontalLayout();
	  final Button registerButton = new Button("Rejestracja");
	  registerButton.setStyleName(ValoTheme.BUTTON_SMALL);
	  buttonsLayout.addComponent(registerButton);
	  
	  registerButton.addClickListener(new Button.ClickListener() {
      
      private static final long serialVersionUID = 8249193265154727363L;

      @Override
      public void buttonClick(ClickEvent event) {
        ((ExpenseSystemUI)getUI()).updateContent(new RegisterPage());
      }
    });
	  return buttonsLayout;
	}
}
