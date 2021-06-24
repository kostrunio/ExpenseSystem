package pl.kostro.expensesystem.ui.view.settingsPage;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.ui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.text.MessageFormat;

public class ExpenseSheetPasswordWindow extends Window {

  private Logger logger = LogManager.getLogger();
  private final PasswordField passwordField = new PasswordField();

  private ClickListener cancelClicked = event -> close();
  private ClickListener saveClicked = event -> {
    ExpenseSheetEntity expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    expenseSheet.setSecretKey(passwordField.getValue());
    if (expenseSheet.getUserLimitList().size() > 0) {
      try {
        expenseSheet.getUserLimitList().get(0).getLimit();
      } catch (NullPointerException e) {
        ShowNotification.badSheetPassword();
        expenseSheet.setSecretKey(null);
        return;
      }
    }
    close();
  };

  public ExpenseSheetPasswordWindow() {
    logger.info("show");
    setModal(true);
    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    setContent(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();

    passwordField.setCaption(MessageFormat.format(Msg.get("expenseSheetPassord.label"), VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class).getName()));
    passwordField.focus();

    result.addComponent(passwordField);
    result.addComponent(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setMargin(false);
    footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("expenseSheetPassord.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("expenseSheetPassord.save"));
    save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(saveClicked);

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

}
