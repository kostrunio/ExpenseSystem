package pl.kostro.expensesystem.views.settingsPage;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.notification.ShowNotification;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Simple name editor Window.
 */
@SuppressWarnings("serial")
public class ExpenseSheetEditPasswordWindow extends Window {

  private final PasswordField oldPasswordField = new PasswordField(Msg.get("expenseSheet.oldPassword"));
  private final PasswordField newPasswordField = new PasswordField(Msg.get("expenseSheet.newPassword"));
  private final PasswordField reNewPasswordField = new PasswordField(Msg.get("expenseSheet.reNewPassword"));
  private ExpenseSheetPasswordChangeListener listener;

  public ExpenseSheetEditPasswordWindow(ExpenseSheetPasswordChangeListener listener) {
    this.listener = listener;
    setCaption(Msg.get("expenseSheet.edit"));
    setModal(true);
    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    addStyleName("edit-expensesheet");

    setContent(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();
    result.setMargin(true);
    result.setSpacing(true);

    oldPasswordField.addStyleName("caption-on-left");
    oldPasswordField.focus();
    
    newPasswordField.addStyleName("caption-on-left");
    reNewPasswordField.addStyleName("caption-on-left");

    result.addComponents(oldPasswordField, newPasswordField, reNewPasswordField);
    result.addComponent(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setSpacing(true);
    footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("expenseSheet.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        close();
      }
    });

    Button save = new Button(Msg.get("expenseSheet.save"));
    save.addStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        ExpenseSheet expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
        if (oldPasswordField.getValue().isEmpty()) {
          ShowNotification.fieldEmpty(oldPasswordField.getCaption());
          return;
        }
        if (newPasswordField.getValue().isEmpty()) {
          ShowNotification.fieldEmpty(newPasswordField.getCaption());
          return;
        }
        if (reNewPasswordField.getValue().isEmpty()) {
          ShowNotification.fieldEmpty(reNewPasswordField.getCaption());
          return;
        }
        if (!oldPasswordField.getValue().equals(expenseSheet.getKey())
            || !newPasswordField.getValue().equals(reNewPasswordField.getValue())) {
          ShowNotification.passwordProblem();
          return;
        }
        listener.expenseSheetPasswordChanged(newPasswordField.getValue());
        close();
      }
    });

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }
  
  public interface ExpenseSheetPasswordChangeListener {
    void expenseSheetPasswordChanged(String newPassword);
  }

}