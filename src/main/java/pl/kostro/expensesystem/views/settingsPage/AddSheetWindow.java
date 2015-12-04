package pl.kostro.expensesystem.views.settingsPage;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.views.ExpenseMenu;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AddSheetWindow extends Window {

  private final TextField nameField = new TextField(Msg.get("newSheet.label"));
  private final PasswordField passwordField = new PasswordField(Msg.get("newSheet.password"));
  private final PasswordField rePasswordField = new PasswordField(Msg.get("newSheet.repassword"));
  private ExpenseSheetSettingsChangeListener listener;

  public AddSheetWindow(ExpenseSheetSettingsChangeListener listener) {
    this.listener = listener;
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

    nameField.addStyleName("caption-on-left");
    nameField.focus();
    
    passwordField.addStyleName("caption-on-left");
    rePasswordField.addStyleName("caption-on-left");

    result.addComponents(nameField, passwordField, rePasswordField);
    result.addComponent(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setSpacing(true);
    footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("newSheet.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        close();
      }
    });

    Button save = new Button(Msg.get("newSheet.save"));
    save.addStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
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
        RealUser loggedUser = VaadinSession.getCurrent().getAttribute(RealUser.class);
        ExpenseSheet expenseSheet = ExpenseSheetService.createExpenseSheet(loggedUser, nameField.getValue(), passwordField.getValue());
        VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, expenseSheet);
        VaadinSession.getCurrent().getAttribute(ExpenseMenu.class).refresh();
        listener.expenseSheetSettingsChange();
        close();
      }
    });

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

}
