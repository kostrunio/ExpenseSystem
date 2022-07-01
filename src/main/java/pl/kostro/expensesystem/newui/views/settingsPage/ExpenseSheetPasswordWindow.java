package pl.kostro.expensesystem.newui.views.settingsPage;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.ui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.text.MessageFormat;

public class ExpenseSheetPasswordWindow extends Dialog {

  private Logger logger = LogManager.getLogger();
  private final PasswordField passwordField = new PasswordField();

  private ComponentEventListener<ClickEvent<Button>> cancelClicked = event -> close();
  private ComponentEventListener<ClickEvent<Button>> saveClicked = event -> {
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
//    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    add(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();

    passwordField.setLabel(MessageFormat.format(Msg.get("expenseSheetPassord.label"), VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class).getName()));
    passwordField.focus();

    result.add(passwordField);
    result.add(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setMargin(false);
    footer.addClassName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("expenseSheetPassord.cancel"));
    cancel.addClickShortcut(Key.ESCAPE);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("expenseSheetPassord.save"));
    save.addClassName(ValoTheme.BUTTON_FRIENDLY);
    save.addClickShortcut(Key.ENTER);
    save.addClickListener(saveClicked);

    footer.add(cancel, save);
    footer.setFlexGrow(1, cancel);
    footer.setAlignItems(FlexComponent.Alignment.START);
    return footer;
  }

}
