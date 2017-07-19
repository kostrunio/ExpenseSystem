package pl.kostro.expensesystem.components.grid;

import java.text.MessageFormat;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.views.settingsPage.AddUserWindow;
import pl.kostro.expensesystem.views.settingsPage.SettingsChangeListener;

@SuppressWarnings("serial")
public class UserLimitSetingGrid extends Grid<UserLimit> implements SettingsChangeListener {
  
  private ExpenseSheetService eshs;
  private UserLimitService uls;
  private Button addUserLimitButton;
  private Button deleteUserLimitButton;

  private ExpenseSheet expenseSheet;

  public UserLimitSetingGrid() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    uls = AppCtxProvider.getBean(UserLimitService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);

    addColumn(item -> item.getUser().getName()).setCaption(Msg.get("settingsPage.userName"));
    addColumn(UserLimit::getLimit).setCaption(Msg.get("settingsPage.userLimit"));
    addColumn(UserLimit::getOrder).setCaption(Msg.get("settingsPage.userOrder"));

    addSelectionListener(event -> deleteUserLimitButton.setEnabled(event.getAllSelectedItems().size() != 0));

    getEditor().setEnabled(true);
    getEditor().setSaveCaption(Msg.get("settingsPage.userSave"));
    getEditor().setCancelCaption(Msg.get("settingsPage.userCancel"));
    getEditor().addSaveListener(event -> {
      uls.merge(event.getBean());
      refreshValues();
    });
  }

  public void setAddUserLimitButton(Button button) {
    addUserLimitButton = button;
    addUserLimitButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().addWindow(new AddUserWindow(UserLimitSetingGrid.this));
      }
    });
  }

  public void setDeleteUserLimitButton(Button button) {
    deleteUserLimitButton = button;
    deleteUserLimitButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        ConfirmDialog.show(getUI(), Msg.get("settingsPage.removeUserLabel"),
            MessageFormat.format(Msg.get("settingsPage.removeUserQuestion"),
                new Object[] { getItem().getUser().getName() }),
            Msg.get("settingsPage.removeUserYes"), Msg.get("settingsPage.removeUserNo"), new ConfirmDialog.Listener() {
              @Override
              public void onClose(ConfirmDialog dialog) {
                if (dialog.isConfirmed()) {
                  ExpenseSheet expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
                  uls.removeUserLimit(expenseSheet, getItem());
                  refreshValues();
                }
              }
            });
      }
    });
  }

  public void refreshValues() {
	  setItems(eshs.getUserLimitListNotRealUser(expenseSheet));
  }

  private UserLimit getItem() {
	  return getSelectedItems().iterator().next();
  }
}
