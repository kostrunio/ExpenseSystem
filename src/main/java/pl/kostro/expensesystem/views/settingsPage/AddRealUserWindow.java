package pl.kostro.expensesystem.views.settingsPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUserEntity;
import pl.kostro.expensesystem.model.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.notification.ShowNotification;

public class AddRealUserWindow extends Window {

  private Logger logger = LogManager.getLogger();
  private RealUserService rus;
  private ExpenseSheetService eshs;
  private UserLimitService uls;

  private final TextField nameField = new TextField(Msg.get("newRealUser.label"));
  private SettingsChangeListener listener;

  private ClickListener cancelClicked = event -> close();
  private ClickListener saveClicked = event -> {
    if (nameField.isEmpty()) {
      ShowNotification.fieldEmpty(nameField.getCaption());
      return;
    }
    RealUserEntity realUser = rus.findRealUser(nameField.getValue());
    if (realUser == null) {
      ShowNotification.noSuchUser(nameField.getValue());
      return;
    }
    ExpenseSheet expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    UserLimitEntity userLimit = uls.create(realUser, expenseSheet.getUserLimitList().size());
    expenseSheet.getUserLimitList().add(userLimit);
    eshs.merge(expenseSheet);
    realUser.getExpenseSheetList().add(expenseSheet);
    rus.merge(realUser, false);
    listener.refreshValues();
    close();
  };

  public AddRealUserWindow(SettingsChangeListener listener) {
    rus = AppCtxProvider.getBean(RealUserService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    logger.info("show");
    this.listener = listener;
    setModal(true);
    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    setContent(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();

    nameField.focus();

    result.addComponent(nameField);
    result.addComponent(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setMargin(false);
    footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("newRealUser.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("newRealUser.save"));
    save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(saveClicked);

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

}
