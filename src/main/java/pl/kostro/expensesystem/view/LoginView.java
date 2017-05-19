package pl.kostro.expensesystem.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.SpringMain;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.view.design.LoginDesign;

@SuppressWarnings("serial")
public class LoginView extends LoginDesign {
  
  private RealUserService rus;

  private Logger logger = LogManager.getLogger();

  private Button.ClickListener signInClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      signInEnable();
      registerForm.setVisible(false);
      loginForm.setVisible(true);
    }
  };
  private Button.ClickListener signUpClick = new Button.ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      signUpEnable();
      loginForm.setVisible(false);
      registerForm.setVisible(true);
    }
  };
  private LoginForm.LoginListener loginEvent = new LoginForm.LoginListener() {
    @Override
    public void onLogin(LoginEvent event) {
      RealUser loggedUser = null;
      try {
        loggedUser = rus.getUserData(event.getLoginParameter("username"), event.getLoginParameter("password"));
        if (loggedUser == null) {
          ShowNotification.logonProblem();
          loginForm.getLoginButton().setEnabled(true);
        } else {
          VaadinSession.getCurrent().setAttribute(RealUser.class, loggedUser);
          ((ExpenseSystemUI) getUI()).updateContent();
        }
      } catch (Exception e) {
        e.printStackTrace();
        ShowNotification.dbProblem(e.getMessage());
        loginForm.getLoginButton().setEnabled(true);
      }
    }
  };

  public LoginView() {
    ApplicationContext context = new AnnotationConfigApplicationContext(SpringMain.class);
    rus = context.getBean(RealUserService.class);
    logger.info("create");
    setCaption();
    signIn.setDisableOnClick(true);
    signUp.setDisableOnClick(true);

    signIn.addClickListener(signInClick);
    signUp.addClickListener(signUpClick);
    
    loginForm.addLoginListener(loginEvent);
  }

  private void setCaption() {
    signIn.setCaption(Msg.get("loginPage.signin"));
    signUp.setCaption(Msg.get("loginPage.signup"));
    loginInfoText.setValue(Msg.get("loginPage.welcome"));
  }

  private void signInEnable() {
    signIn.removeStyleName(ValoTheme.BUTTON_BORDERLESS);
    signIn.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

    signUp.setEnabled(true);
    signUp.removeStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
    signUp.setStyleName(ValoTheme.BUTTON_BORDERLESS);
  }

  private void signUpEnable() {
    signUp.removeStyleName(ValoTheme.BUTTON_BORDERLESS);
    signUp.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

    signIn.setEnabled(true);
    signIn.removeStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
    signIn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
  }
}
