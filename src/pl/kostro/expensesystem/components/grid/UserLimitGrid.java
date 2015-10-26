package pl.kostro.expensesystem.components.grid;

import java.text.MessageFormat;
import java.util.List;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.UserLimitService;
import pl.kostro.expensesystem.views.settingsPage.AddUserWindow;

import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

public class UserLimitGrid extends Grid {
  private static final long serialVersionUID = 5378642683850471251L;

  private Button addUserLimitButton;
  private Button deleteUserLimitButton;
  
  public UserLimitGrid() {
    setImmediate(true);
    setEditorEnabled(true);
    setEditorSaveCaption(Msg.get("settingsPage.userSave"));
    setEditorCancelCaption(Msg.get("settingsPage.userCancel"));
  }

  public void setAddUserLimitButton(Button button) {
    addUserLimitButton = button;
    addUserLimitButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = -7924703303108271795L;

      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().addWindow(new AddUserWindow());
      }
    });
  }

  public void setDeleteUserLimitButton(Button button) {
    deleteUserLimitButton = button;
    deleteUserLimitButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = 8360036685403681818L;

      @Override
      public void buttonClick(ClickEvent event) {
        ConfirmDialog.show(getUI(),
            Msg.get("settingsPage.removeUserLabel"),
            MessageFormat.format(Msg.get("settingsPage.removeUserQuestion"), new Object[] {getItem().getUser().getName()}),
            Msg.get("settingsPage.removeUserYes"),
            Msg.get("settingsPage.removeUserNo"),
            new ConfirmDialog.Listener() {

          private static final long serialVersionUID = 3844318339125611876L;

          @Override
          public void onClose(ConfirmDialog dialog) {
            if (dialog.isConfirmed()) {
              ExpenseSheet expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
              UserLimitService.removeUserLimit(expenseSheet, getItem());
              UI.getCurrent().getNavigator().navigateTo("settings");
            }
          }
        });
      }
    });
  }
  
  public void filUserLimitGrid(List<UserLimit> userLimitList) {
    getContainerDataSource().removeAllItems();
    BeanItemContainer <UserLimit> container = new BeanItemContainer<UserLimit>(UserLimit.class, userLimitList);
    container.addNestedContainerBean("user");
    setContainerDataSource(container);
    recalculateColumnWidths();
    setColumns("user.name", "limit", "order");
    getColumn("user.name").setHeaderCaption(Msg.get("settingsPage.userName"));
    getColumn("limit").setHeaderCaption(Msg.get("settingsPage.userLimit"));
    getColumn("order").setHeaderCaption(Msg.get("settingsPage.userOrder"));
    getEditorFieldGroup().addCommitHandler(new CommitHandler() {
      private static final long serialVersionUID = 7645963700451879164L;

      @Override
      public void preCommit(CommitEvent commitEvent) throws CommitException {}
      
      @SuppressWarnings("unchecked")
      @Override
      public void postCommit(CommitEvent commitEvent) throws CommitException {
        UserLimitService.merge(((BeanItem<UserLimit>)commitEvent.getFieldBinder().getItemDataSource()).getBean());
      }
    });
    
    addSelectionListener(new SelectionListener() {
      private static final long serialVersionUID = 8360036685403681818L;

      @Override
      public void select(SelectionEvent event) {
        deleteUserLimitButton.setEnabled(getSelectedRow()!=null);
      }
    });
  }
  
  @SuppressWarnings("unchecked")
  private UserLimit getItem() {
    return ((BeanItem<UserLimit>)getContainerDataSource().getItem(getSelectedRow())).getBean();
  }
}
