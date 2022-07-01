package pl.kostro.expensesystem.newui.components.grid;

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
import pl.kostro.expensesystem.newui.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.newui.views.settingsPage.AddUserWindow;
import pl.kostro.expensesystem.newui.views.settingsPage.SettingsChangeListener;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class UserLimitSettingGrid extends Grid<UserLimitEntity> implements SettingsChangeListener {
  
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;
  private UserLimitService uls;
  private Button addUserLimitButton;
  private Button deleteUserLimitButton;

  private TextField limitField = new TextField();
  private NumberField orderField = new NumberField();
  private Checkbox continuousField = new Checkbox();

  private ExpenseSheetEntity expenseSheet;
  
  private Binder<UserLimitEntity> binder = new Binder<>();

  private SelectionListener<Grid<UserLimitEntity>, UserLimitEntity> itemSelected = event -> deleteUserLimitButton.setEnabled(event.getAllSelectedItems().size() != 0);
  private EditorOpenListener<UserLimitEntity> editorOpen = event -> binder.setBean(event.getItem());
  private EditorSaveListener<UserLimitEntity> saveUserLimit = event -> {
    uls.merge(event.getItem());
    refreshValues();
  };
  private ComponentEventListener<ClickEvent<Button>> addUserClicked = event -> new AddUserWindow(UserLimitSettingGrid.this).open();
  private ComponentEventListener<ClickEvent<Button>> deleteUserClicked = event -> {
    ConfirmDialog.show(Msg.get("settingsPage.removeUserLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeUserQuestion"), new Object[] { getItem().getUser().getName() }),
        Msg.get("settingsPage.removeUserYes"),
        Msg.get("settingsPage.removeUserNo"),
        dialog -> {
          if (dialog.isConfirmed()) {
            ExpenseSheetEntity expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
            UserLimitEntity userLimit = getItem();
            expenseSheet.getUserLimitList().remove(userLimit);
            uls.remove(userLimit);
            refreshValues();
          }
          dialog.close();
        });
  };

  public UserLimitSettingGrid() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);

    binder.forField(limitField).bind(userLimit -> userLimit.getLimit().toString(), (userLimit, value) -> userLimit.setLimit(new BigDecimal(value.replaceAll(",", "."))));
    binder.forField(orderField).bind(userLimit -> (double)userLimit.getOrder(), (userLimit, value) -> userLimit.setOrder(value.intValue()));
    binder.forField(continuousField).bind(userLimit -> userLimit.isContinuousSummary(), (userLimit, value) -> userLimit.setContinuousSummary(value));
    
    addColumn(item -> item.getUser().getName())
      .setHeader(Msg.get("settingsPage.userName"));
//      .setEditorComponent(new TextField(), UserLimitEntity::setUser);
    addColumn(UserLimitEntity::getLimit)
      .setHeader(Msg.get("settingsPage.userLimit"))
      .setEditorComponent(limitField);
    addColumn(UserLimitEntity::getOrder)
      .setHeader(Msg.get("settingsPage.userOrder"))
      .setEditorComponent(orderField);
    addColumn(UserLimitEntity::isContinuousSummary)
      .setHeader(Msg.get("settingsPage.userContinuous"))
      .setEditorComponent(continuousField);

    addSelectionListener(itemSelected);

    getEditor().setBinder(binder);
    getEditor().setBuffered(true);
//    getEditor().setEnabled(true);
//    getEditor().setSaveCaption(Msg.get("settingsPage.userSave"));
//    getEditor().setCancelCaption(Msg.get("settingsPage.userCancel"));
    getEditor().addOpenListener(editorOpen);
    getEditor().addSaveListener(saveUserLimit);
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
	  setItems(eshts.getUserLimitListNotRealUser(expenseSheet));
  }

  private UserLimitEntity getItem() {
	  return getSelectedItems().iterator().next();
  }
}
