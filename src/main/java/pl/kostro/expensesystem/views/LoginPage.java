 package pl.kostro.expensesystem.views;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.service.RealUserService;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
    loginPanel.addStyleName("login-panel");
    loginPanel.addComponent(buildLabels());
    loginPanel.addComponent(buildfieldsLayout());
    loginPanel.addComponent(buildOthButtons());
    return loginPanel;
	}
	
	private Component buildLabels() {
    CssLayout labelsLayout = new CssLayout();
    labelsLayout.addStyleName("labels");

    Label welcome = new Label(Msg.get("loginPage.welcome"));
    welcome.setSizeUndefined();
    welcome.addStyleName(ValoTheme.LABEL_H4);
    welcome.addStyleName(ValoTheme.LABEL_COLORED);
    labelsLayout.addComponent(welcome);

    Label title = new Label(Msg.get("loginPage.title"));
    title.setSizeUndefined();
    title.addStyleName(ValoTheme.LABEL_H3);
    title.addStyleName(ValoTheme.LABEL_LIGHT);
    labelsLayout.addComponent(title);
    return labelsLayout;
	}
	
	private Component buildfieldsLayout() {
    HorizontalLayout fieldsLayout = new HorizontalLayout();
    fieldsLayout.setSpacing(true);
    fieldsLayout.addStyleName("fields");

    final TextField userNameField = new TextField(Msg.get("loginPage.userName"));
    userNameField.setIcon(FontAwesome.USER);
    userNameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    userNameField.focus();

    final PasswordField passwordField = new PasswordField(Msg.get("loginPage.password"));
    passwordField.setIcon(FontAwesome.LOCK);
    passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

    final Button signinButton = new Button(Msg.get("loginPage.signIn"));
    signinButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
    signinButton.setClickShortcut(KeyCode.ENTER);
    signinButton.addClickListener(new ClickListener() {

      private static final long serialVersionUID = 5199438233052362164L;

        @Override
        public void buttonClick(final ClickEvent event) {
          RealUser loggedUser = null;
          try {
            loggedUser = RealUserService.getUserData(userNameField.getValue(), passwordField.getValue());
            if (loggedUser == null) {
              ShowNotification.logonProblem();
            } else {
              VaadinSession.getCurrent().setAttribute(RealUser.class, loggedUser);
            }
            ((ExpenseSystemUI)getUI()).updateContent();
          } catch (Exception e) {
            ShowNotification.dbProblem(e.getMessage());
          }
        }
    });
    
    fieldsLayout.addComponents(userNameField, passwordField, signinButton);
    fieldsLayout.setComponentAlignment(signinButton, Alignment.BOTTOM_LEFT);

    return fieldsLayout;
	}

	private Component buildOthButtons() {
	  HorizontalLayout buttonsLayout = new HorizontalLayout();
	  final Button registerButton = new Button(Msg.get("loginPage.register"));
	  registerButton.setStyleName(ValoTheme.BUTTON_SMALL);
	  registerButton.setIcon(FontAwesome.PLUS_SQUARE);
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