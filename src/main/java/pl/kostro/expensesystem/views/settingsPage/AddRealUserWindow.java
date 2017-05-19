package pl.kostro.expensesystem.views.settingsPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.SpringMain;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.notification.ShowNotification;

@SuppressWarnings("serial")
public class AddRealUserWindow extends Window {

  private Logger logger = LogManager.getLogger();
  private RealUserService rus;
  private UserLimitService uls;

  private final TextField nameField = new TextField(Msg.get("newRealUser.label"));
  private SettingsChangeListener listener;

  public AddRealUserWindow(SettingsChangeListener listener) {
    ApplicationContext context = new AnnotationConfigApplicationContext(SpringMain.class);
    rus = context.getBean(RealUserService.class);
    uls = context.getBean(UserLimitService.class);
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
    result.setMargin(true);
    result.setSpacing(true);

    nameField.focus();

    result.addComponent(nameField);
    result.addComponent(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setSpacing(true);
    footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("newRealUser.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        close();
      }
    });

    Button save = new Button(Msg.get("newRealUser.save"));
    save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        if (nameField.isEmpty()) {
          ShowNotification.fieldEmpty(nameField.getCaption());
          return;
        }
        RealUser realUser = rus.findRealUser(nameField.getValue());
        if (realUser == null) {
          ShowNotification.noSuchUser(nameField.getValue());
          return;
        }
        ExpenseSheet expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
        uls.createUserLimit(expenseSheet, realUser);
        listener.refreshValues();
        close();
      }
    });

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

}
