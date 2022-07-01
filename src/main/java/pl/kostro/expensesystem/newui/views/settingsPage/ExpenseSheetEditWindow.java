package pl.kostro.expensesystem.newui.views.settingsPage;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.newui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.msg.Msg;

public class ExpenseSheetEditWindow extends Dialog {

  private Logger logger = LogManager.getLogger();
  private final TextField nameField = new TextField(Msg.get("expenseSheet.name"));
  private ExpenseSheetEditListener listener;

  private ComponentEventListener<ClickEvent<Button>> cancelClicked = event -> close();
  private ComponentEventListener<ClickEvent<Button>> saveClicked = event -> {
    if (nameField.getValue().isEmpty()) {
      ShowNotification.fieldEmpty(nameField.getValue());
      return;
    }
    listener.expenseSheetNameEdited(nameField.getValue());
    close();
  };

  public ExpenseSheetEditWindow(ExpenseSheetEditListener listener, ExpenseSheetEntity expenseSheet) {
    logger.info("show");
    this.listener = listener;
    setHeaderTitle(Msg.get("expenseSheet.edit"));
    setModal(true);
//    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    add(buildContent(expenseSheet.getName()));
  }

  private Component buildContent(final String currentName) {
    VerticalLayout result = new VerticalLayout();

    nameField.setValue(currentName);
    nameField.focus();

    result.add(nameField);
    result.add(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setMargin(false);
    footer.addClassName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("expenseSheet.cancel"));
    cancel.addClickShortcut(Key.ESCAPE);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("expenseSheet.save"));
    save.addClassName(ValoTheme.BUTTON_FRIENDLY);
    save.addClickShortcut(Key.ENTER);
    save.addClickListener(saveClicked);

    footer.add(cancel, save);
    footer.setFlexGrow(1, cancel);
    footer.setAlignItems(FlexComponent.Alignment.START);
    return footer;
  }

}
