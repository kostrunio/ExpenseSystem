package pl.kostro.expensesystem.newui.views.settingsPage;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.newui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.msg.Msg;

public class AddSheetWindow extends Dialog {

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetService eshs;
  private UserLimitService uls;
  private RealUserService rus;

  private final TextField nameField = new TextField(Msg.get("newSheet.label"));
  private final PasswordField passwordField = new PasswordField(Msg.get("newSheet.password"));
  private final PasswordField rePasswordField = new PasswordField(Msg.get("newSheet.repassword"));

  private ComponentEventListener<ClickEvent<Button>> cancelClicked = event -> close();
  private ComponentEventListener<ClickEvent<Button>> saveClicked = event -> {
    if (nameField.isEmpty()) {
      ShowNotification.fieldEmpty(nameField.getValue());
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
    close();
//    ((ExpenseSystemUI)getUI()).getMainView().refresh();
    UI.getCurrent().getPage().reload();
  };

  public AddSheetWindow() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    rus = AppCtxProvider.getBean(RealUserService.class);
    logger.info("show");
    setModal(true);
//    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    add(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();

    nameField.focus();
    nameField.setWidthFull();
    passwordField.setWidthFull();
    rePasswordField.setWidthFull();
    
    result.add(nameField, passwordField, rePasswordField);
    result.add(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setWidthFull();

    Button cancel = new Button(Msg.get("newSheet.cancel"));
    cancel.addClickShortcut(Key.ESCAPE);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("newSheet.save"));
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    save.addClickShortcut(Key.ENTER);
    save.addClickListener(saveClicked);

    footer.add(cancel, save);
    footer.setFlexGrow(1, cancel);
    footer.setAlignItems(FlexComponent.Alignment.START);
    return footer;
  }

}
