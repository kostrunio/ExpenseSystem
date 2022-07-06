package pl.kostro.expensesystem.newui.views.settingsPage;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

public class AddRealUserWindow extends Dialog {

  private Logger logger = LogManager.getLogger();
  private RealUserService rus;
  private ExpenseSheetService eshs;
  private UserLimitService uls;

  private final TextField nameField = new TextField(Msg.get("newRealUser.label"));
  private SettingsChangeListener listener;

  private ComponentEventListener<ClickEvent<Button>> cancelClicked = event -> close();
  private ComponentEventListener<ClickEvent<Button>> saveClicked = event -> {
    if (nameField.isEmpty()) {
      ShowNotification.fieldEmpty(nameField.getValue());
      return;
    }
    RealUserEntity realUser = rus.findRealUser(nameField.getValue());
    if (realUser == null) {
      ShowNotification.noSuchUser(nameField.getValue());
      return;
    }
    ExpenseSheetEntity expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
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
//    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    add(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();

    nameField.focus();
    nameField.setWidthFull();

    result.add(nameField);
    result.add(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setMargin(false);
//    footer.addClassName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidthFull();

    Button cancel = new Button(Msg.get("newRealUser.cancel"));
    cancel.addClickShortcut(Key.ESCAPE);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("newRealUser.save"));
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    save.addClickShortcut(Key.ENTER);
    save.addClickListener(saveClicked);

    footer.add(cancel, save);
    footer.setFlexGrow(1, cancel);
    footer.setAlignItems(FlexComponent.Alignment.START);
    return footer;
  }

}
