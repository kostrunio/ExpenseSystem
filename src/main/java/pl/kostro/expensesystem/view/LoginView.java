package pl.kostro.expensesystem.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.RealUserEntity;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.view.design.LoginDesign;

public class LoginView extends LoginDesign {
  
  private RealUserService rus;

  private Logger logger = LogManager.getLogger();

  private Button.ClickListener signInClick = event -> {
    signInEnable();
    registerForm.setVisible(false);
    loginForm.setVisible(true);
  };
  private Button.ClickListener signUpClick = event -> {
    signUpEnable();
    loginForm.setVisible(false);
    registerForm.setVisible(true);
  };
  private LoginForm.LoginListener loginEvent = event -> {
    RealUserEntity loggedUser = null;
    try {
      loggedUser = rus.getUserData(event.getLoginParameter("username"), event.getLoginParameter("password"));
      if (loggedUser == null) {
        ShowNotification.logonProblem();
        loginForm.getLoginButton().setEnabled(true);
      } else {
        VaadinSession.getCurrent().setAttribute(RealUserEntity.class, loggedUser);
        ((ExpenseSystemUI) getUI()).updateContent();
      }
    } catch (Exception e) {
      e.printStackTrace();
      ShowNotification.dbProblem(e.getMessage());
      loginForm.getLoginButton().setEnabled(true);
    }
  };

  public LoginView() {
    rus = AppCtxProvider.getBean(RealUserService.class);
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
