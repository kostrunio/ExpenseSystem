package pl.kostro.expensesystem.ui.views.settingsPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.ui.ExpenseSystemUI;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.ui.notification.ShowNotification;

public class AddSheetWindow extends Window {

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetService eshs;
  private UserLimitService uls;
  private RealUserService rus;

  private final TextField nameField = new TextField(Msg.get("newSheet.label"));
  private final PasswordField passwordField = new PasswordField(Msg.get("newSheet.password"));
  private final PasswordField rePasswordField = new PasswordField(Msg.get("newSheet.repassword"));

  private ClickListener cancelClicked = event -> close();
  private ClickListener saveClicked = event -> {
    if (nameField.isEmpty()) {
      ShowNotification.fieldEmpty(nameField.getCaption());
      return;
    }
    if (passwordField.getValue().isEmpty()
        || rePasswordField.getValue().isEmpty()
        || !passwordField.getValue().equals(rePasswordField.getValue())) {
      ShowNotification.passwordProblem();
      return;
    }
    RealUserEntity loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    UserLimitEntity userLimit = uls.create(loggedUser, 0);
    ExpenseSheetEntity expenseSheet = eshs.create(nameField.getValue(), passwordField.getValue(), loggedUser, userLimit);
    loggedUser.getExpenseSheetList().add(expenseSheet);
    rus.merge(loggedUser, false);
    if (loggedUser.getDefaultExpenseSheet() == null)
      rus.setDefaultExpenseSheet(loggedUser, expenseSheet);
    VaadinSession.getCurrent().setAttribute(ExpenseSheetEntity.class, expenseSheet);
    ((ExpenseSystemUI)getUI()).getMainView().refresh();
    close();
  };

  public AddSheetWindow() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    rus = AppCtxProvider.getBean(RealUserService.class);
    logger.info("show");
    setModal(true);
    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    setContent(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();

    nameField.focus();
    
    result.addComponents(nameField, passwordField, rePasswordField);
    result.addComponent(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setMargin(false);
    footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("newSheet.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("newSheet.save"));
    save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(saveClicked);

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

}
