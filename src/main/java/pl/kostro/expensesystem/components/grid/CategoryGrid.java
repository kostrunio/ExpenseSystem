package pl.kostro.expensesystem.components.grid;

import java.text.MessageFormat;

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

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.CategoryService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.views.settingsPage.AddCategoryWindow;
import pl.kostro.expensesystem.views.settingsPage.SettingsChangeListener;

@SuppressWarnings("serial")
public class CategoryGrid extends Grid implements SettingsChangeListener {
  private CategoryService cs;
  private ExpenseSheetService eshs;
  private ExpenseSheet expenseSheet;

  private Button addCategoryButton;
  private Button moveUpCategoryButton;
  private Button moveDownCategoryButton;
  private Button deleteCategoryButton;

  public CategoryGrid() {
    cs = AppCtxProvider.getBean(CategoryService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    setImmediate(true);
    setColumns("name", "multiplier");
    getColumn("name").setHeaderCaption(Msg.get("settingsPage.categoryName"));
    getColumn("multiplier").setHeaderCaption(Msg.get("settingsPage.categoryMultiplier"));

    setEditorEnabled(true);
    setEditorSaveCaption(Msg.get("settingsPage.categorySave"));
    setEditorCancelCaption(Msg.get("settingsPage.categoryCancel"));

    getEditorFieldGroup().addCommitHandler(new CommitHandler() {
      @Override
      public void preCommit(CommitEvent commitEvent) throws CommitException {
      }

      @SuppressWarnings("unchecked")
      @Override
      public void postCommit(CommitEvent commitEvent) throws CommitException {
        cs.merge(((BeanItem<Category>) commitEvent.getFieldBinder().getItemDataSource()).getBean());
      }
    });

    addSelectionListener(new SelectionListener() {
      @Override
      public void select(SelectionEvent event) {
        moveUpCategoryButton.setEnabled(getSelectedRow() != null);
        moveDownCategoryButton.setEnabled(getSelectedRow() != null);
        deleteCategoryButton.setEnabled(getSelectedRow() != null);
      }
    });
  }

  public void setAddCategoryButton(Button button) {
    addCategoryButton = button;
    addCategoryButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().addWindow(new AddCategoryWindow(CategoryGrid.this));
      }
    });
  }

  public void setMoveUpCategoryButton(Button button) {
    moveUpCategoryButton = button;
    moveUpCategoryButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        Category category = getItem();
        expenseSheet = eshs.moveCategoryUp(expenseSheet, category);
        refreshValues();
      }
    });
  }

  public void setMoveDownCategoryButton(Button button) {
    moveDownCategoryButton = button;
    moveDownCategoryButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        Category category = getItem();
        expenseSheet = eshs.moveCategoryDown(expenseSheet, category);
        refreshValues();
      }
    });
  }

  public void setDeleteCategoryButton(Button button) {
    deleteCategoryButton = button;
    deleteCategoryButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        ConfirmDialog.show(getUI(), Msg.get("settingsPage.removeCategoryLabel"),
            MessageFormat.format(Msg.get("settingsPage.removeCategoryQuestion"), new Object[] { getItem().getName() }),
            Msg.get("settingsPage.removeCategoryYes"), Msg.get("settingsPage.removeCategoryNo"),
            new ConfirmDialog.Listener() {
              @Override
              public void onClose(ConfirmDialog dialog) {
                if (dialog.isConfirmed()) {
                  expenseSheet = cs.removeCategory(expenseSheet, getItem());
                  refreshValues();
                }
              }
            });
      }
    });
  }

  public void refreshValues() {
    getContainerDataSource().removeAllItems();
    BeanItemContainer<Category> container = new BeanItemContainer<Category>(Category.class,
        expenseSheet.getCategoryList());
    setContainerDataSource(container);
  }

  @SuppressWarnings("unchecked")
  private Category getItem() {
    return ((BeanItem<Category>) getContainerDataSource().getItem(getSelectedRow())).getBean();
  }
}
