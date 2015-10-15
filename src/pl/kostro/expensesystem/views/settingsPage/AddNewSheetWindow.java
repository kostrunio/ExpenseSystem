package pl.kostro.expensesystem.views.settingsPage;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.service.ExpenseSheetService;

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

public class AddNewSheetWindow extends Window {

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
  private TextField newSheetField;
  /**
   * The constructor should first build the main layout, set the
   * composition root and then do any custom initialization.
   *
   * The constructor will not be automatically regenerated by the
   * visual editor.
   */
  public AddNewSheetWindow() {
    buildMainLayout();
    setContent(mainLayout);
    center();

    // TODO add user code here
    saveButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = -8606935359237092697L;

      @Override
      public void buttonClick(ClickEvent event) {
        if (newSheetField.isEmpty()) {
          ShowNotification.fieldEmpty("Nazwa nowego arkusza");
          return;
        }
        RealUser loggedUser = (RealUser) VaadinSession.getCurrent().getAttribute(RealUser.class.getName());
        ExpenseSheet expenseSheet = ExpenseSheetService.createExpenseSheet(loggedUser, newSheetField.getValue());
        UI.getCurrent().getNavigator().navigateTo("settings/" + expenseSheet.getName());
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
    
    // newSheetField
    newSheetField = new TextField();
    newSheetField.setCaption("Nazwa nowego arkusza");
    newSheetField.setImmediate(false);
    newSheetField.setWidth("-1px");
    newSheetField.setHeight("-1px");
    newSheetField.focus();
    mainLayout.addComponent(newSheetField);
    
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
    saveButton.setCaption("Zapisz");
    saveButton.setImmediate(false);
    saveButton.setWidth("-1px");
    saveButton.setHeight("-1px");
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
    buttonLayout.addComponent(saveButton);
    
    // cancelButton
    cancelButton = new Button();
    cancelButton.setCaption("Anuluj");
    cancelButton.setImmediate(false);
    cancelButton.setWidth("-1px");
    cancelButton.setHeight("-1px");
    buttonLayout.addComponent(cancelButton);
    
    return buttonLayout;
  }

}
