package pl.kostro.expensesystem.components.grid;

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
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.notification.ShowNotification;
import pl.kostro.expensesystem.views.settingsPage.AddRealUserWindow;
import pl.kostro.expensesystem.views.settingsPage.SettingsChangeListener;

@SuppressWarnings("serial")
public class RealUserLimitSettingGrid extends Grid<UserLimit> implements SettingsChangeListener {
  
  private ExpenseSheetService eshs;
  private UserLimitService uls;
  private Button addUserLimitButton;
  private Button deleteUserLimitButton;

  private ExpenseSheet expenseSheet;
  
  private Binder<UserLimit> binder = new Binder<>();

  private SelectionListener<UserLimit> itemSelected = event -> deleteUserLimitButton.setEnabled(event.getAllSelectedItems().size() != 0);
  private EditorOpenListener<UserLimit> editorOpen = event -> binder.setBean(event.getBean());
  private EditorSaveListener<UserLimit> saveRealUserLimit = event -> {
    uls.merge(event.getBean());
    refreshValues();
  };
  private ClickListener addUserClicked = event -> UI.getCurrent().addWindow(new AddRealUserWindow(RealUserLimitSettingGrid.this));
  private ClickListener deleteUserClicked = event -> {
    ConfirmDialog.show(getUI(), Msg.get("settingsPage.removeRealUserLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeRealUserQuestion"),
            new Object[] { getItem().getUser().getName() }),
        Msg.get("settingsPage.removeRealUserYes"), Msg.get("settingsPage.removeRealUserNo"), dialog -> {
          if (dialog.isConfirmed()) {
            if (expenseSheet.getOwner().equals(getItem().getUser())) {
              ShowNotification.removeOwnerProblem();
              return;
            }
            uls.removeUserLimit(expenseSheet, getItem());
            refreshValues();
          }
        });
  };

  public RealUserLimitSettingGrid() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);

    TextField limitField = new TextField();
    TextField orderField = new TextField();
    
    Binder.Binding<UserLimit, String> limitBinder = binder.forField(limitField)
        .bind(userLimit -> userLimit.getLimit().toString(), (userLimit, value) -> userLimit.setLimit(new BigDecimal(value.replaceAll(",", "."))));
    Binder.Binding<UserLimit, String> orderBinder = binder.forField(orderField)
        .bind(userLimit -> userLimit.getOrder()+"", (userLimit, value) -> userLimit.setOrder(Integer.parseInt(value)));
    
    addColumn(item -> item.getUser().getName())
      .setCaption(Msg.get("settingsPage.realUserName"))
      .setEditorComponent(new TextField(), UserLimit::setUser)
      .setEditable(false);
    addColumn(UserLimit::getLimit)
      .setCaption(Msg.get("settingsPage.realUserLimit"))
      .setEditorBinding(limitBinder);
    addColumn(UserLimit::getOrder)
      .setCaption(Msg.get("settingsPage.realUserOrder"))
      .setEditorBinding(orderBinder);

    addSelectionListener(itemSelected);

    getEditor().setEnabled(true);
    getEditor().setSaveCaption(Msg.get("settingsPage.realUserSave"));
    getEditor().setCancelCaption(Msg.get("settingsPage.realUserCancel"));
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
	  setItems(eshs.getUserLimitListRealUser(expenseSheet));
  }

  private UserLimit getItem() {
    return getSelectedItems().iterator().next();
  }
}
