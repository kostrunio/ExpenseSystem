package pl.kostro.expensesystem.views.settingsPage;

import java.text.MessageFormat;

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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Simple name editor Window.
 */
@SuppressWarnings("serial")
public class ExpenseSheetPasswordWindow extends Window {

  private final PasswordField nameField = new PasswordField();
  private ExpenseSheetSettingsChangeListener listener;

  public ExpenseSheetPasswordWindow(ExpenseSheetSettingsChangeListener listener) {
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

    nameField.setCaption(MessageFormat.format(Msg.get("expenseSheetPassord.label"), VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getName()));
    nameField.addStyleName("caption-on-left");
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

    Button cancel = new Button(Msg.get("expenseSheetPassord.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        close();
      }
    });

    Button save = new Button(Msg.get("expenseSheetPassord.save"));
    save.addStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        ExpenseSheet expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
        expenseSheet.setKey(nameField.getValue());
        if (expenseSheet.getUserLimitList().size() > 0) {
          try {
            expenseSheet.getUserLimitList().get(0).getLimit();
          } catch (NullPointerException e) {
            ShowNotification.badSheetPassword();
            expenseSheet.setKey(null);
            return;
          }
          listener.expenseSheetSettingsChange();
        }
        close();
      }
    });

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

  public interface ExpenseSheetEditListener {
    void expenseSheetNameEdited(TextField nameField);
  }

}
