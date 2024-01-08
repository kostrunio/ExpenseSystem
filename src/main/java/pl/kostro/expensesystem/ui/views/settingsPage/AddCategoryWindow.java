package pl.kostro.expensesystem.ui.views.settingsPage;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.service.CategoryService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.ui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.msg.Msg;

@Slf4j
public class AddCategoryWindow extends Window {

  private ExpenseSheetService echs;
  private CategoryService cs;

  private final TextField nameField = new TextField(Msg.get("newCategory.label"));
  private SettingsChangeListener listener;

  private final ClickListener cancelClicked = event -> close();
  private final ClickListener saveClicked = event -> {
    if (nameField.isEmpty()) {
      ShowNotification.fieldEmpty(nameField.getCaption());
      return;
    }
    ExpenseSheetEntity expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    CategoryEntity category = new CategoryEntity(nameField.getValue(), expenseSheet.getCategoryList().size());
    cs.save(category);
	echs.addCategory(category, expenseSheet);
    listener.refreshValues();
    close();
  };

  public AddCategoryWindow(SettingsChangeListener listener) {
    echs = AppCtxProvider.getBean(ExpenseSheetService.class);
    cs = AppCtxProvider.getBean(CategoryService.class);
    log.info("show");
    this.listener = listener;
    setModal(true);
    setClosable(false);
    setResizable(false);
    setWidth(300.0f, Unit.PIXELS);

    setContent(buildContent());
  }

  private Component buildContent() {
    VerticalLayout result = new VerticalLayout();

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

    Button cancel = new Button(Msg.get("newCategory.cancel"));
    cancel.setClickShortcut(KeyCode.ESCAPE, null);
    cancel.addClickListener(cancelClicked);

    Button save = new Button(Msg.get("newCategory.save"));
    save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    save.setClickShortcut(KeyCode.ENTER, null);
    save.addClickListener(saveClicked);

    footer.addComponents(cancel, save);
    footer.setExpandRatio(cancel, 1);
    footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
    return footer;
  }

}
