package pl.kostro.expensesystem.components.grid;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.SpringMain;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.views.settingsPage.AddUserWindow;
import pl.kostro.expensesystem.views.settingsPage.SettingsChangeListener;

@SuppressWarnings("serial")
public class UserLimitGrid extends Grid implements SettingsChangeListener {
  
  private ExpenseSheetService eshs;
  private UserLimitService uls;
  private Button addUserLimitButton;
  private Button deleteUserLimitButton;

  private ExpenseSheet expenseSheet;

  public UserLimitGrid() {
    ApplicationContext context = new AnnotationConfigApplicationContext(SpringMain.class);
    eshs = context.getBean(ExpenseSheetService.class);
    uls = context.getBean(UserLimitService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    setImmediate(true);

    setColumns("user.name", "limit", "order");
    getColumn("user.name").setHeaderCaption(Msg.get("settingsPage.userName"));
    getColumn("limit").setHeaderCaption(Msg.get("settingsPage.userLimit"));
    getColumn("order").setHeaderCaption(Msg.get("settingsPage.userOrder"));
    getEditorFieldGroup().addCommitHandler(new CommitHandler() {
      @Override
      public void preCommit(CommitEvent commitEvent) throws CommitException {
      }

      @SuppressWarnings("unchecked")
      @Override
      public void postCommit(CommitEvent commitEvent) throws CommitException {
        uls.merge(((BeanItem<UserLimit>) commitEvent.getFieldBinder().getItemDataSource()).getBean());
        refreshValues();
      }
    });

    addSelectionListener(new SelectionListener() {
      @Override
      public void select(SelectionEvent event) {
        deleteUserLimitButton.setEnabled(getSelectedRow() != null);
      }
    });

    setEditorEnabled(true);
    setEditorSaveCaption(Msg.get("settingsPage.userSave"));
    setEditorCancelCaption(Msg.get("settingsPage.userCancel"));
  }

  public void setAddUserLimitButton(Button button) {
    addUserLimitButton = button;
    addUserLimitButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().addWindow(new AddUserWindow(UserLimitGrid.this));
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
    List<UserLimit> userLimitList = eshs.getUserLimitListNotRealUser(expenseSheet);
    getContainerDataSource().removeAllItems();
    BeanItemContainer<UserLimit> container = new BeanItemContainer<UserLimit>(UserLimit.class, userLimitList);
    container.addNestedContainerBean("user");
    setContainerDataSource(container);
    recalculateColumnWidths();
  }

  @SuppressWarnings("unchecked")
  private UserLimit getItem() {
    return ((BeanItem<UserLimit>) getContainerDataSource().getItem(getSelectedRow())).getBean();
  }
}
