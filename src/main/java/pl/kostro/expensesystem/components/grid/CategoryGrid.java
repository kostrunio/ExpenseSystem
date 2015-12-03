package pl.kostro.expensesystem.components.grid;

import java.text.MessageFormat;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.CategoryService;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.views.ExpenseMenu;
import pl.kostro.expensesystem.views.settingsPage.AddCategoryWindow;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetSettingsChangeListener;

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

public class CategoryGrid extends Grid {
  private static final long serialVersionUID = -1289032915572715567L;
  
  private ExpenseSheet expenseSheet;
  private ExpenseSheetSettingsChangeListener listener;
  
  private Button addCategoryButton;
  private Button moveUpCategoryButton;
  private Button moveDownCategoryButton;
  private Button deleteCategoryButton;
  
  public CategoryGrid(ExpenseSheetSettingsChangeListener listener) {
    this.listener = listener;
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    setImmediate(true);
    setEditorEnabled(true);
    setEditorSaveCaption(Msg.get("settingsPage.categorySave"));
    setEditorCancelCaption(Msg.get("settingsPage.categoryCancel"));
  }
  
  public void setAddCategoryButton(Button button) {
    addCategoryButton = button;
    addCategoryButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = 8671045294811634046L;

      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().addWindow(new AddCategoryWindow(listener));
      }
    });
  }

  public void setMoveUpCategoryButton(Button button) {
    moveUpCategoryButton = button;
    moveUpCategoryButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = -2706798565406087014L;

      @Override
      public void buttonClick(ClickEvent event) {
        expenseSheet = ExpenseSheetService.moveCategoryUp(expenseSheet, getItem());
        VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, expenseSheet);
        listener.expenseSheetSettingsChange();
      }
    });
  }

  public void setMoveDownCategoryButton(Button button) {
    moveDownCategoryButton = button;
    moveDownCategoryButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = 4157916927704412169L;

      @Override
      public void buttonClick(ClickEvent event) {
        expenseSheet = ExpenseSheetService.moveCategoryDown(expenseSheet, getItem());
        VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, expenseSheet);
        listener.expenseSheetSettingsChange();
      }
    });
  }

  public void setDeleteCategoryButton(Button button) {
    deleteCategoryButton = button;
    deleteCategoryButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = 8360036685403681818L;

      @Override
      public void buttonClick(ClickEvent event) {
        ConfirmDialog.show(getUI(),
            Msg.get("settingsPage.removeCategoryLabel"),
            MessageFormat.format(Msg.get("settingsPage.removeCategoryQuestion"), new Object[] {getItem().getName()}),
            Msg.get("settingsPage.removeCategoryYes"),
            Msg.get("settingsPage.removeCategoryNo"),
            new ConfirmDialog.Listener() {

          private static final long serialVersionUID = 3844318339125611876L;

          @Override
          public void onClose(ConfirmDialog dialog) {
            if (dialog.isConfirmed()) {
              expenseSheet = ExpenseSheetService.removeCategory(expenseSheet, getItem());
              VaadinSession.getCurrent().getAttribute(ExpenseMenu.class).refresh();
              listener.expenseSheetSettingsChange();
            }
          }
        });
      }
    });
  }
  
  public void filCategoryGrid() {
    getContainerDataSource().removeAllItems();
    BeanItemContainer <Category> container = new BeanItemContainer<Category>(Category.class, expenseSheet.getCategoryList());
    setContainerDataSource(container);
    recalculateColumnWidths();
    setColumns("name", "multiplier");
    getColumn("name").setHeaderCaption(Msg.get("settingsPage.categoryName"));
    getColumn("multiplier").setHeaderCaption(Msg.get("settingsPage.categoryMultiplier"));
    getEditorFieldGroup().addCommitHandler(new CommitHandler() {
      private static final long serialVersionUID = 7645963700451879164L;

      @Override
      public void preCommit(CommitEvent commitEvent) throws CommitException {}
      
      @SuppressWarnings("unchecked")
      @Override
      public void postCommit(CommitEvent commitEvent) throws CommitException {
        CategoryService.merge(((BeanItem<Category>)commitEvent.getFieldBinder().getItemDataSource()).getBean());
      }
    });
    
    addSelectionListener(new SelectionListener() {
      private static final long serialVersionUID = 8360036685403681818L;

      @Override
      public void select(SelectionEvent event) {
        moveUpCategoryButton.setEnabled(getSelectedRow()!=null);
        moveDownCategoryButton.setEnabled(getSelectedRow()!=null);
        deleteCategoryButton.setEnabled(getSelectedRow()!=null);
      }
    });
  }

  @SuppressWarnings("unchecked")
  private Category getItem() {
    return ((BeanItem<Category>)getContainerDataSource().getItem(getSelectedRow())).getBean();
  }
}
