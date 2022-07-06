package pl.kostro.expensesystem.newui.views.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.newui.notification.ShowNotification;

@Route("login")
@PageTitle("Login")
public class LoginView extends LoginDesign {
  
  private RealUserService rus;

  private Logger logger = LogManager.getLogger();

  private ComponentEventListener<ClickEvent<Button>> signInClick = event -> {
    signInEnable();
    registerForm.setVisible(false);
    loginForm.setVisible(true);
  };
  private ComponentEventListener<ClickEvent<Button>> signUpClick = event -> {
    signUpEnable();
    loginForm.setVisible(false);
    registerForm.setVisible(true);
  };
  private ComponentEventListener<AbstractLogin.LoginEvent> loginEvent = event -> {
    RealUserEntity loggedUser = null;
    try {
      loggedUser = rus.getUserData(event.getUsername(), event.getPassword());
      if (loggedUser == null) {
        ShowNotification.logonProblem();
        loginForm.setEnabled(true);
      } else {
        VaadinSession.getCurrent().setAttribute(RealUserEntity.class, loggedUser);
        UI.getCurrent().getPage().reload();
      }
    } catch (Exception e) {
      e.printStackTrace();
      ShowNotification.dbProblem(e.getMessage());
      loginForm.setEnabled(true);
    }
  };

  public LoginView() {
    rus = AppCtxProvider.getBean(RealUserService.class);
    logger.info("create");
    signIn.setDisableOnClick(true);
    signUp.setDisableOnClick(true);

    signIn.addClickListener(signInClick);
    signUp.addClickListener(signUpClick);
    
    loginForm.addLoginListener(loginEvent);
  }

  private void signInEnable() {
    signIn.removeClassName(ValoTheme.BUTTON_BORDERLESS);
    signIn.setClassName(ValoTheme.BUTTON_BORDERLESS_COLORED);

    signUp.setEnabled(true);
    signUp.removeClassName(ValoTheme.BUTTON_BORDERLESS_COLORED);
    signUp.setClassName(ValoTheme.BUTTON_BORDERLESS);
  }

  private void signUpEnable() {
    signUp.removeClassName(ValoTheme.BUTTON_BORDERLESS);
    signUp.setClassName(ValoTheme.BUTTON_BORDERLESS_COLORED);

    signIn.setEnabled(true);
    signIn.removeClassName(ValoTheme.BUTTON_BORDERLESS_COLORED);
    signIn.setClassName(ValoTheme.BUTTON_BORDERLESS);
  }
}
