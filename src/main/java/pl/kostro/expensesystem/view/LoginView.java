package pl.kostro.expensesystem.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.view.design.LoginDesign;

@SuppressWarnings("serial")
public class LoginView extends LoginDesign {

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

  public LoginView() {
    setCaption();
    signIn.setDisableOnClick(true);
    signUp.setDisableOnClick(true);

    signIn.addClickListener(signInClick);
    signUp.addClickListener(signUpClick);
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
