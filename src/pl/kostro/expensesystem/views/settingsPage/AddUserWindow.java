package pl.kostro.expensesystem.views.settingsPage;

import java.math.BigDecimal;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.service.UserLimitService;
import pl.kostro.expensesystem.service.UserService;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Window;

public class AddUserWindow extends Window {

  /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

  private static final long serialVersionUID = 3714489654451677620L;

  @AutoGenerated
  private VerticalLayout mainLayout;
  @AutoGenerated
  private HorizontalLayout buttonLayout;
  @AutoGenerated
  private Button cancelButton;
  @AutoGenerated
  private Button saveButton;
  @AutoGenerated
  private TextField newUserField;
  /**
   * The constructor should first build the main layout, set the
   * composition root and then do any custom initialization.
   *
   * The constructor will not be automatically regenerated by the
   * visual editor.
   */
  public AddUserWindow() {
    buildMainLayout();
    setContent(mainLayout);
    center();

    // TODO add user code here
    saveButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = -8606935359237092697L;

      @Override
      public void buttonClick(ClickEvent event) {
        if (newUserField.isEmpty()) {
          ShowNotification.fieldEmpty(newUserField.getCaption());
          return;
        }
        ExpenseSheet expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
        UserLimitService.createUserLimit(expenseSheet, new UserLimit(UserService.createUser(newUserField.getValue()), new BigDecimal(0)));
        UI.getCurrent().getNavigator().navigateTo("settings");
        close();
      }
    });
    
    cancelButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = -8871409583806958661L;

      @Override
      public void buttonClick(ClickEvent event) {
        close();
      }
    });
  }

  @AutoGenerated
  private VerticalLayout buildMainLayout() {
    setModal(true);
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setImmediate(false);
    mainLayout.setWidth("100%");
    mainLayout.setHeight("100%");
    mainLayout.setMargin(true);
    
    // top-level component properties
    setWidth("200px");
    setHeight("200px");
    
    // newCategoryField
    newUserField = new TextField();
    newUserField.setCaption(Msg.get("newUser.label"));
    newUserField.setImmediate(false);
    newUserField.setWidth("-1px");
    newUserField.setHeight("-1px");
    newUserField.focus();
    mainLayout.addComponent(newUserField);
    
    // buttonLayout
    buttonLayout = buildButtonLayout();
    mainLayout.addComponent(buttonLayout);
    
    return mainLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildButtonLayout() {
    // common part: create layout
    buttonLayout = new HorizontalLayout();
    buttonLayout.setImmediate(false);
    buttonLayout.setWidth("-1px");
    buttonLayout.setHeight("-1px");
    buttonLayout.setSpacing(true);
    
    // saveButton
    saveButton = new Button();
    saveButton.setCaption(Msg.get("newUser.save"));
    saveButton.setImmediate(false);
    saveButton.setWidth("-1px");
    saveButton.setHeight("-1px");
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
    buttonLayout.addComponent(saveButton);
    
    // cancelButton
    cancelButton = new Button();
    cancelButton.setCaption(Msg.get("newUser.cancel"));
    cancelButton.setImmediate(false);
    cancelButton.setWidth("-1px");
    cancelButton.setHeight("-1px");
    buttonLayout.addComponent(cancelButton);
    
    return buttonLayout;
  }

}