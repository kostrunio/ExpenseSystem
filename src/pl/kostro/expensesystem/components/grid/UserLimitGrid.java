package pl.kostro.expensesystem.components.grid;

import java.util.List;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.UserLimitService;

import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;

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
  }

  public void setDeleteUserLimitButton(Button button) {
    deleteUserLimitButton = button;
  }
  
  public void filUserLimitGrid(List<UserLimit> userLimitList, boolean editName) {
    getContainerDataSource().removeAllItems();
    BeanItemContainer <UserLimit> container = new BeanItemContainer<UserLimit>(UserLimit.class, userLimitList);
    container.addNestedContainerBean("user");
    setContainerDataSource(container);
    recalculateColumnWidths();
    setColumns("user.name", "limit", "order");
    getColumn("user.name").setHeaderCaption(Msg.get("settingsPage.realUserName")).setEditable(editName);
    getColumn("limit").setHeaderCaption(Msg.get("settingsPage.realUserLimit"));
    getColumn("order").setHeaderCaption(Msg.get("settingsPage.realUserOrder"));
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
    
    addItemClickListener(new ItemClickListener() {
      private static final long serialVersionUID = 8360036685403681818L;

      @Override
      public void itemClick(ItemClickEvent event) {
        deleteUserLimitButton.setEnabled(getSelectedRow()!=null);
      }
    });
  }
  
  @SuppressWarnings("unchecked")
  private UserLimit getItem() {
    return ((BeanItem<UserLimit>)getContainerDataSource().getItem(getSelectedRow())).getBean();
  }
}
