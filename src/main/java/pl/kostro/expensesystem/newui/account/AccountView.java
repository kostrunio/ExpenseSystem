package pl.kostro.expensesystem.newui.account;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.newui.main.MainView;
import pl.kostro.expensesystem.newui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.msg.Msg;

@Route(value = "account", layout = MainView.class)
@PageTitle("Account")
public class AccountView extends AccountDesign implements BeforeEnterObserver {

  private Logger logger = LogManager.getLogger();
  private RealUserService rus;
  private RealUserEntity loggedUser;

  private ComponentEventListener<ClickEvent<Button>> saveEmailClick = event -> {
    if (emailField.getValue().trim().equals(emailField2.getValue().trim())) {
      loggedUser.setEmail(emailField.getValue().trim());
      rus.merge(loggedUser, false);
      ShowNotification.changeEmailOK();
      emailField2.setValue("");
      emailField2.setEnabled(false);
      saveEmailButton.setEnabled(false);
    }
  };
  private ComponentEventListener<ClickEvent<Button>> savePasswordClick = event -> {
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
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<TextField, String>> emailChanged = event -> emailField2.setEnabled(true);
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<TextField, String>> email2Changed = event -> saveEmailButton.setEnabled(emailField.getValue().trim().equals(emailField2.getValue().trim()));
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<PasswordField, String>> oldPasswordChanged = event -> {
    newPasswordField.setEnabled(oldPasswordField.getValue().trim().equals(loggedUser.getClearPassword()));
    newPasswordField2.setEnabled(oldPasswordField.getValue().trim().equals(loggedUser.getClearPassword()));
  };
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<PasswordField, String>> newPasswordChanged = event -> savePasswordButton.setEnabled(newPasswordField.getValue().trim().equals(newPasswordField2.getValue().trim()));
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<PasswordField, String>> newPassword2Changed = event -> savePasswordButton.setEnabled(newPasswordField.getValue().trim().equals(newPasswordField2.getValue().trim()));

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
    usernameField.setLabel(Msg.get("account.userName"));
    emailLabel.setText(Msg.get("account.emailPanel"));
    emailField.setLabel(Msg.get("account.emailField"));
    emailField2.setLabel(Msg.get("account.emailField2"));
    saveEmailButton.setText(Msg.get("account.saveEmailButton"));
    passwordLabel.setText(Msg.get("account.passwordPanel"));
    oldPasswordField.setLabel(Msg.get("account.oldPasswordField"));
    newPasswordField.setLabel(Msg.get("account.newPasswordField"));
    newPasswordField2.setLabel(Msg.get("account.newPasswordField2"));
    savePasswordButton.setText(Msg.get("account.savePasswordButton"));
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    logger.info("Enter");
    loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
//    MainView menuView = ((ExpenseSystemUI) getUI()).getMainView();
//    menuView.setActiveView("account");
    usernameField.setReadOnly(false);
    usernameField.setValue(loggedUser.getName());
    usernameField.setReadOnly(true);
    emailField.setValue(loggedUser.getEmail());
  }

}
