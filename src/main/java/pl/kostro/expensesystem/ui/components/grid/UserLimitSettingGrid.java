package pl.kostro.expensesystem.ui.components.grid;

import java.math.BigDecimal;
import java.text.MessageFormat;

import com.vaadin.data.Binder;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.components.grid.EditorOpenListener;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.ui.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.ui.views.settingsPage.AddUserWindow;
import pl.kostro.expensesystem.ui.views.settingsPage.SettingsChangeListener;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

public class UserLimitSettingGrid extends Grid<UserLimitEntity> implements SettingsChangeListener {
  
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;
  private UserLimitService uls;
  private Button addUserLimitButton;
  private Button deleteUserLimitButton;

  private TextField limitField = new TextField();
  private TextField orderField = new TextField();
  private TextField continuousField = new TextField();

  private ExpenseSheetEntity expenseSheet;
  
  private Binder<UserLimitEntity> binder = new Binder<>();

  private SelectionListener<UserLimitEntity> itemSelected = event -> deleteUserLimitButton.setEnabled(event.getAllSelectedItems().size() != 0);
  private EditorOpenListener<UserLimitEntity> editorOpen = event -> binder.setBean(event.getBean());
  private EditorSaveListener<UserLimitEntity> saveUserLimit = event -> {
    uls.merge(event.getBean());
    refreshValues();
  };
  private ClickListener addUserClicked = event -> UI.getCurrent().addWindow(new AddUserWindow(UserLimitSettingGrid.this));
  private ClickListener deleteUserClicked = event -> {
    ConfirmDialog.show(getUI(), Msg.get("settingsPage.removeUserLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeUserQuestion"),
            new Object[] { getItem().getUser().getName() }),
        Msg.get("settingsPage.removeUserYes"), Msg.get("settingsPage.removeUserNo"), dialog -> {
          if (dialog.isConfirmed()) {
            ExpenseSheetEntity expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
            UserLimitEntity userLimit = getItem();
            expenseSheet.getUserLimitList().remove(userLimit);
            uls.remove(userLimit);
            refreshValues();
          }
        });
  };

  public UserLimitSettingGrid() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);

    Binder.Binding<UserLimitEntity, String> limitBinder = binder.forField(limitField)
        .bind(userLimit -> userLimit.getLimit().toString(), (userLimit, value) -> userLimit.setLimit(new BigDecimal(value.replaceAll(",", "."))));
    Binder.Binding<UserLimitEntity, String> orderBinder = binder.forField(orderField)
        .bind(userLimit -> userLimit.getOrder()+"", (userLimit, value) -> userLimit.setOrder(Integer.parseInt(value)));
    Binder.Binding<UserLimitEntity, String> continuousBinder = binder.forField(continuousField)
        .bind(userLimit -> userLimit.isContinuousSummary()+"", (userLimit, value) -> userLimit.setContinuousSummary(Boolean.parseBoolean(value)));
    
    addColumn(item -> item.getUser().getName())
      .setCaption(Msg.get("settingsPage.userName"))
      .setEditorComponent(new TextField(), UserLimitEntity::setUser);
    addColumn(UserLimitEntity::getLimit)
      .setCaption(Msg.get("settingsPage.userLimit"))
      .setEditorBinding(limitBinder);
    addColumn(UserLimitEntity::getOrder)
      .setCaption(Msg.get("settingsPage.userOrder"))
      .setEditorBinding(orderBinder);
    addColumn(UserLimitEntity::isContinuousSummary)
      .setCaption(Msg.get("settingsPage.userContinuous"))
      .setEditorBinding(continuousBinder);

    addSelectionListener(itemSelected);

    getEditor().setEnabled(true);
    getEditor().setSaveCaption(Msg.get("settingsPage.userSave"));
    getEditor().setCancelCaption(Msg.get("settingsPage.userCancel"));
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
