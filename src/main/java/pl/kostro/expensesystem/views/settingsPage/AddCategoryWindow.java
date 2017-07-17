package pl.kostro.expensesystem.views.settingsPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.CategoryService;
import pl.kostro.expensesystem.notification.ShowNotification;

@SuppressWarnings("serial")
public class AddCategoryWindow extends Window {

  private Logger logger = LogManager.getLogger();
  private CategoryService cs;

  private final TextField nameField = new TextField(Msg.get("newCategory.label"));
  private SettingsChangeListener listener;

  public AddCategoryWindow(SettingsChangeListener listener) {
    cs = AppCtxProvider.getBean(CategoryService.class);
    logger.info("show");
    this.listener = listener;
    setModal(true);
    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    setContent(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();
    result.setMargin(true);
    result.setSpacing(true);

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

    Button cancel = new Button(Msg.get("newCategory.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        close();
      }
    });

    Button save = new Button(Msg.get("newCategory.save"));
    save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        if (nameField.isEmpty()) {
          ShowNotification.fieldEmpty(nameField.getCaption());
          return;
        }
        ExpenseSheet expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
        cs.createCategory(expenseSheet, nameField.getValue());
        listener.refreshValues();
        close();
      }
    });

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

}
