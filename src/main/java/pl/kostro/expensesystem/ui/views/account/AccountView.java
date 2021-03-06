package pl.kostro.expensesystem.ui.views.account;

import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button.ClickListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.ui.ExpenseSystemUI;
import pl.kostro.expensesystem.ui.notification.ShowNotification;
import pl.kostro.expensesystem.ui.views.main.MainView;
import pl.kostro.expensesystem.utils.msg.Msg;

public class AccountView extends AccountDesign implements View {

  private Logger logger = LogManager.getLogger();
  private RealUserService rus;
  private RealUserEntity loggedUser;

  private ClickListener saveEmailClick = event -> {
    if (emailField.getValue().trim().equals(emailField2.getValue().trim())) {
      loggedUser.setEmail(emailField.getValue().trim());
      rus.merge(loggedUser, false);
      ShowNotification.changeEmailOK();
      emailField2.setValue("");
      emailField2.setEnabled(false);
      saveEmailButton.setEnabled(false);
    }
  };
  private ClickListener savePasswordClick = event -> {
    if (newPasswordField.getValue().trim().equals(newPasswordField2.getValue().trim())) {
      loggedUser.setClearPassword(newPasswordField.getValue().trim());
      rus.merge(loggedUser, true);
      ShowNotification.changePasswordOK();
      oldPasswordField.setValue("");
      oldPasswordField.setEnabled(false);
      newPasswordField.setValue("");
      newPasswordField.setEnabled(false);
      newPasswordField2.setValue("");
      newPasswordField2.setEnabled(false);
      savePasswordButton.setEnabled(false);
    }
  };
  private ValueChangeListener<String> emailChanged = event -> emailField2.setEnabled(true);
  private ValueChangeListener<String> email2Changed = event -> saveEmailButton.setEnabled(emailField.getValue().trim().equals(emailField2.getValue().trim()));
  private ValueChangeListener<String> oldPasswordChanged = event -> {
    newPasswordField.setEnabled(oldPasswordField.getValue().trim().equals(loggedUser.getClearPassword()));
    newPasswordField2.setEnabled(oldPasswordField.getValue().trim().equals(loggedUser.getClearPassword()));
  };
  private ValueChangeListener<String> newPasswordChanged = event -> savePasswordButton.setEnabled(newPasswordField.getValue().trim().equals(newPasswordField2.getValue().trim()));
  private ValueChangeListener<String> newPassword2Changed = event -> savePasswordButton.setEnabled(newPasswordField.getValue().trim().equals(newPasswordField2.getValue().trim()));

  public AccountView() {
    rus = AppCtxProvider.getBean(RealUserService.class);
    setCaption();
    emailField.addValueChangeListener(emailChanged);
    emailField2.addValueChangeListener(email2Changed);
    saveEmailButton.addClickListener(saveEmailClick);
    oldPasswordField.addValueChangeListener(oldPasswordChanged);
    newPasswordField.addValueChangeListener(newPasswordChanged);
    newPasswordField2.addValueChangeListener(newPassword2Changed);
    savePasswordButton.addClickListener(savePasswordClick);
  }

  private void setCaption() {
    usernameField.setCaption(Msg.get("account.userName"));
    emailPanel.setCaption(Msg.get("account.emailPanel"));
    emailField.setCaption(Msg.get("account.emailField"));
    emailField2.setCaption(Msg.get("account.emailField2"));
    saveEmailButton.setCaption(Msg.get("account.saveEmailButton"));
    passwordPanel.setCaption(Msg.get("account.passwordPanel"));
    oldPasswordField.setCaption(Msg.get("account.oldPasswordField"));
    newPasswordField.setCaption(Msg.get("account.newPasswordField"));
    newPasswordField2.setCaption(Msg.get("account.newPasswordField2"));
    savePasswordButton.setCaption(Msg.get("account.savePasswordButton"));
  }

  @Override
  public void enter(ViewChangeEvent event) {
    logger.info("Enter");
    loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    MainView menuView = ((ExpenseSystemUI) getUI()).getMainView();
    menuView.setActiveView("account");
    usernameField.setReadOnly(false);
    usernameField.setValue(loggedUser.getName());
    usernameField.setReadOnly(true);
    emailField.setValue(loggedUser.getEmail());
  }

}
