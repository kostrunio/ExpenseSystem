package pl.kostro.expensesystem.newui.views.settingsPage;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.newui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.msg.Msg;

public class ExpenseSheetEditPasswordWindow extends Dialog {

  private Logger logger = LogManager.getLogger();
  private final PasswordField oldPasswordField = new PasswordField(Msg.get("expenseSheet.oldPassword"));
  private final PasswordField newPasswordField = new PasswordField(Msg.get("expenseSheet.newPassword"));
  private final PasswordField reNewPasswordField = new PasswordField(Msg.get("expenseSheet.reNewPassword"));
  private ExpenseSheetPasswordChangeListener listener;

  private ComponentEventListener<ClickEvent<Button>> cancelClicked = event -> close();
  private ComponentEventListener<ClickEvent<Button>> saveClicked = event -> {
    ExpenseSheetEntity expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    if (oldPasswordField.getValue().isEmpty()) {
      ShowNotification.fieldEmpty(oldPasswordField.getValue());
      return;
    }
    if (newPasswordField.getValue().isEmpty()) {
      ShowNotification.fieldEmpty(newPasswordField.getValue());
      return;
    }
    if (reNewPasswordField.getValue().isEmpty()) {
      ShowNotification.fieldEmpty(reNewPasswordField.getValue());
      return;
    }
    if (!oldPasswordField.getValue().equals(expenseSheet.getKey())
        || !newPasswordField.getValue().equals(reNewPasswordField.getValue())) {
      ShowNotification.passwordProblem();
      return;
    }
    listener.expenseSheetPasswordChanged(newPasswordField.getValue());
    close();
  };

  public ExpenseSheetEditPasswordWindow(ExpenseSheetPasswordChangeListener listener) {
    logger.info("show");
    this.listener = listener;
//    setHeaderTitle(Msg.get("expenseSheet.edit"));
    setModal(true);
//    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    add(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();

    oldPasswordField.focus();
    oldPasswordField.setWidthFull();
    newPasswordField.setWidthFull();
    reNewPasswordField.setWidthFull();
    
    result.add(oldPasswordField, newPasswordField, reNewPasswordField);
    result.add(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setMargin(false);
//    footer.addClassName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("expenseSheet.cancel"));
    cancel.addClickShortcut(Key.ESCAPE);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("expenseSheet.save"));
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    save.addClickShortcut(Key.ENTER);
    save.addClickListener(saveClicked);

    footer.add(cancel, save);
    footer.setFlexGrow(1, cancel);
    footer.setAlignItems(FlexComponent.Alignment.START);
    return footer;
  }

}
