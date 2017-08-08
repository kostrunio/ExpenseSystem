package pl.kostro.expensesystem.views.settingsPage;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.notification.ShowNotification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Simple name editor Window.
 */
@SuppressWarnings("serial")
public class ExpenseSheetEditWindow extends Window {

  private Logger logger = LogManager.getLogger();
  private final TextField nameField = new TextField(Msg.get("expenseSheet.name"));
  private ExpenseSheetEditListener listener;

  private ClickListener cancelClicked = event -> close();
  private ClickListener saveClicked = event -> {
    if (nameField.getValue().isEmpty()) {
      ShowNotification.fieldEmpty(nameField.getCaption());
      return;
    }
    listener.expenseSheetNameEdited(nameField);
    close();
  };

  public ExpenseSheetEditWindow(ExpenseSheetEditListener listener, ExpenseSheet expenseSheet) {
    logger.info("show");
    this.listener = listener;
    setCaption(Msg.get("expenseSheet.edit"));
    setModal(true);
    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    setContent(buildContent(expenseSheet.getName()));
  }

  private Component buildContent(final String currentName) {
    VerticalLayout result = new VerticalLayout();

    nameField.setValue(currentName);
    nameField.focus();

    result.addComponent(nameField);
    result.addComponent(buildFooter());

    return result;
  }

  private Component buildFooter() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setMargin(false);
    footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
    footer.setWidth(100.0f, Unit.PERCENTAGE);

    Button cancel = new Button(Msg.get("expenseSheet.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("expenseSheet.save"));
    save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(saveClicked);

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }
  
  public interface ExpenseSheetEditListener {
    void expenseSheetNameEdited(TextField nameField);
  }

}
