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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.model.service.UserService;
import pl.kostro.expensesystem.ui.notification.ShowNotification;

public class AddUserWindow extends Window {

  private Logger logger = LogManager.getLogger();
  private UserLimitService uls;
  private UserService us;
  private ExpenseSheetService eshs;

  private final TextField nameField = new TextField(Msg.get("newUser.label"));
  private SettingsChangeListener listener;

  private ClickListener cancelClicked = event -> close();
  private ClickListener saveClicked = event -> {
    if (nameField.isEmpty()) {
      ShowNotification.fieldEmpty(nameField.getCaption());
      return;
    }
    ExpenseSheetEntity expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    UserLimitEntity userLimit = uls.create(us.createAndSave(nameField.getValue()), expenseSheet.getUserLimitList().size());
    expenseSheet.getUserLimitList().add(userLimit);
    eshs.merge(expenseSheet);
    listener.refreshValues();
    close();
  };

  public AddUserWindow(SettingsChangeListener listener) {
    uls = AppCtxProvider.getBean(UserLimitService.class);
    us = AppCtxProvider.getBean(UserService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
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

    Button cancel = new Button(Msg.get("newUser.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("newUser.save"));
    save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(saveClicked);

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

}
