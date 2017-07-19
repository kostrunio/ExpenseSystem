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
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.service.CategoryService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.views.settingsPage.AddCategoryWindow;
import pl.kostro.expensesystem.views.settingsPage.SettingsChangeListener;

@SuppressWarnings("serial")
public class CategorySettingGrid extends Grid<Category> implements SettingsChangeListener {
  private CategoryService cs;
  private ExpenseSheetService eshs;
  private ExpenseSheet expenseSheet;

  private Button addCategoryButton;
  private Button moveUpCategoryButton;
  private Button moveDownCategoryButton;
  private Button deleteCategoryButton;

  public CategorySettingGrid() {
    cs = AppCtxProvider.getBean(CategoryService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    addColumn(Category::getName).setCaption(Msg.get("settingsPage.categoryName"));
    addColumn(Category::getMultiplier).setCaption(Msg.get("settingsPage.categoryMultiplier"));

    addSelectionListener(event -> {
      moveUpCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
      moveDownCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
      deleteCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
    });
    
    getEditor().setEnabled(true);
    getEditor().setSaveCaption(Msg.get("settingsPage.categorySave"));
    getEditor().setCancelCaption(Msg.get("settingsPage.categoryCancel"));
    getEditor().addSaveListener(event -> cs.merge(event.getBean()));
  }

  public void setAddCategoryButton(Button button) {
    addCategoryButton = button;
    addCategoryButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().addWindow(new AddCategoryWindow(CategorySettingGrid.this));
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
	  setItems(expenseSheet.getCategoryList());
  }

  private Category getItem() {
    return getSelectedItems().iterator().next();
  }
}
