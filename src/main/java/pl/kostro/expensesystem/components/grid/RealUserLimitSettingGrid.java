package pl.kostro.expensesystem.components.grid;

import java.math.BigDecimal;
import java.text.MessageFormat;

import com.vaadin.data.Binder;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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

  public RealUserLimitSettingGrid() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);

    TextField limitField = new TextField();
    TextField orderField = new TextField();
    
    Binder<UserLimit> binder = new Binder<>();
    Binder.Binding<UserLimit, String> limitBinder = binder.forField(limitField)
        .bind(userLimit -> userLimit.getLimit().toString(), (userLimit, value) -> userLimit.setLimit(new BigDecimal(value)));
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

    addSelectionListener(event -> deleteUserLimitButton.setEnabled(event.getAllSelectedItems().size() != 0));

    getEditor().setEnabled(true);
    getEditor().setSaveCaption(Msg.get("settingsPage.realUserSave"));
    getEditor().setCancelCaption(Msg.get("settingsPage.realUserCancel"));
    getEditor().addSaveListener(event -> {
        uls.merge(event.getBean());
        refreshValues();
      }
    );
  }

  public void setAddUserLimitButton(Button button) {
    addUserLimitButton = button;
    addUserLimitButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().addWindow(new AddRealUserWindow(RealUserLimitSettingGrid.this));
      }
    });
  }

  public void setDeleteUserLimitButton(Button button) {
    deleteUserLimitButton = button;
    deleteUserLimitButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        ConfirmDialog.show(getUI(), Msg.get("settingsPage.removeRealUserLabel"),
            MessageFormat.format(Msg.get("settingsPage.removeRealUserQuestion"),
                new Object[] { getItem().getUser().getName() }),
            Msg.get("settingsPage.removeRealUserYes"), Msg.get("settingsPage.removeRealUserNo"),
            new ConfirmDialog.Listener() {
              @Override
              public void onClose(ConfirmDialog dialog) {
                if (dialog.isConfirmed()) {
                  if (expenseSheet.getOwner().equals(getItem().getUser())) {
                    ShowNotification.removeOwnerProblem();
                    return;
                  }
                  uls.removeUserLimit(expenseSheet, getItem());
                  refreshValues();
                }
              }
            });
      }
    });
  }

  public void refreshValues() {
	  setItems(eshs.getUserLimitListRealUser(expenseSheet));
  }

  private UserLimit getItem() {
    return getSelectedItems().iterator().next();
  }
}
