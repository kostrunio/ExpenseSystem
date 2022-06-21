package pl.kostro.expensesystem.newui.component.grid;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.EditorOpenListener;
import com.vaadin.flow.component.grid.editor.EditorSaveListener;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.server.VaadinSession;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.newui.dialog.ConfirmDialog;
import pl.kostro.expensesystem.newui.notification.ShowNotification;
import pl.kostro.expensesystem.newui.settingsPage.AddRealUserWindow;
import pl.kostro.expensesystem.newui.settingsPage.SettingsChangeListener;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class RealUserLimitSettingGrid extends Grid<UserLimitEntity> implements SettingsChangeListener {

  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;
  private UserLimitService uls;
  private Button addUserLimitButton;
  private Button deleteUserLimitButton;

  TextField limitField = new TextField();
  NumberField orderField = new NumberField();
  Checkbox continuousField = new Checkbox();

  private ExpenseSheetEntity expenseSheet;
  
  private Binder<UserLimitEntity> binder = new Binder<>();

  private SelectionListener<Grid<UserLimitEntity>, UserLimitEntity> itemSelected = event -> deleteUserLimitButton.setEnabled(event.getAllSelectedItems().size() != 0);
  private EditorOpenListener<UserLimitEntity> editorOpen = event -> binder.setBean(event.getItem());
  private EditorSaveListener<UserLimitEntity> saveRealUserLimit = event -> {
    uls.merge(event.getItem());
    refreshValues();
  };
  private ComponentEventListener<ClickEvent<Button>> addUserClicked = event -> new AddRealUserWindow(RealUserLimitSettingGrid.this).open();
  private ComponentEventListener<ClickEvent<Button>> deleteUserClicked = event -> {
    ConfirmDialog.show(Msg.get("settingsPage.removeRealUserLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeRealUserQuestion"), new Object[] { getItem().getUser().getName() }),
        Msg.get("settingsPage.removeRealUserYes"),
        Msg.get("settingsPage.removeRealUserNo"),
        dialog -> {
          if (dialog.isConfirmed()) {
            if (expenseSheet.getOwner().equals(getItem().getUser())) {
              ShowNotification.removeOwnerProblem();
              return;
            }
            UserLimitEntity userLimit = getItem();
            expenseSheet.getUserLimitList().remove(userLimit);
            uls.remove(userLimit);
            refreshValues();
          }
        });
  };

  public RealUserLimitSettingGrid() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);

    binder.forField(limitField).bind(userLimit -> userLimit.getLimit().toString(), (userLimit, value) -> userLimit.setLimit(new BigDecimal(value)));
    binder.forField(orderField).bind(userLimit -> (double)userLimit.getOrder(), (userLimit, value) -> userLimit.setOrder(value.intValue()));
    binder.forField(continuousField).bind(userLimit -> userLimit.isContinuousSummary(), (userLimit, value) -> userLimit.setContinuousSummary(value));
    
    addColumn(item -> item.getUser().getName())
      .setHeader(Msg.get("settingsPage.realUserName"));
//      .setEditorComponent(nameField);
    addColumn(UserLimitEntity::getLimit)
      .setHeader(Msg.get("settingsPage.realUserLimit"))
      .setEditorComponent(limitField);
    addColumn(UserLimitEntity::getOrder)
      .setHeader(Msg.get("settingsPage.realUserOrder"))
      .setEditorComponent(orderField);
    addColumn(UserLimitEntity::isContinuousSummary)
      .setHeader(Msg.get("settingsPage.realUserContinuous"))
      .setEditorComponent(continuousField);

    addSelectionListener(itemSelected);

    getEditor().setBinder(binder);
    getEditor().setBuffered(true);
//    getEditor().setEnabled(true);
//    getEditor().setSaveCaption(Msg.get("settingsPage.realUserSave"));
//    getEditor().setCancelCaption(Msg.get("settingsPage.realUserCancel"));
    getEditor().addOpenListener(editorOpen);
    getEditor().addSaveListener(saveRealUserLimit);
  }

  public void setAddUserLimitButton(Button button) {
    addUserLimitButton = button;
    addUserLimitButton.addClickListener(addUserClicked);
  }

  public void setDeleteUserLimitButton(Button button) {
    deleteUserLimitButton = button;
    deleteUserLimitButton.addClickListener(deleteUserClicked);
  }

  public void refreshValues() {
	  setItems(eshts.getUserLimitListRealUser(expenseSheet));
  }

  private UserLimitEntity getItem() {
    return getSelectedItems().iterator().next();
  }
}
