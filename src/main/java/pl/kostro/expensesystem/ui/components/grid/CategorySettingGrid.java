package pl.kostro.expensesystem.ui.components.grid;

import com.vaadin.data.Binder;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.components.grid.EditorOpenListener;
import com.vaadin.ui.components.grid.EditorSaveListener;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.service.CategoryService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.ui.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.ui.view.settingsPage.AddCategoryWindow;
import pl.kostro.expensesystem.ui.view.settingsPage.SettingsChangeListener;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class CategorySettingGrid extends Grid<CategoryEntity> implements SettingsChangeListener {

  private CategoryService cs;
  private ExpenseSheetService eshs;
  private ExpenseSheetEntity expenseSheet;
  
  private Binder<CategoryEntity> binder = new Binder<>();

  private Button addCategoryButton;
  private Button moveUpCategoryButton;
  private Button moveDownCategoryButton;
  private Button deleteCategoryButton;
  
  private SelectionListener<CategoryEntity> itemSelected = event -> {
    moveUpCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
    moveDownCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
    deleteCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
  };
  private EditorOpenListener<CategoryEntity> editorOpen = event -> binder.setBean(event.getBean());
  private EditorSaveListener<CategoryEntity> saveCategory = event -> cs.save(event.getBean());
  private ClickListener addCategoryClicked = event -> UI.getCurrent().addWindow(new AddCategoryWindow(CategorySettingGrid.this));
  private ClickListener moveUpClicked = event -> {
    expenseSheet = eshs.moveCategoryUp(expenseSheet, getItem());
    refreshValues();
  };
  private ClickListener moveDownClick = event -> {
    expenseSheet = eshs.moveCategoryDown(expenseSheet, getItem());
    refreshValues();
  };
  private ClickListener deleteCategoryClick = event -> {
    ConfirmDialog.show(getUI(), Msg.get("settingsPage.removeCategoryLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeCategoryQuestion"), new Object[] { getItem().getName() }),
        Msg.get("settingsPage.removeCategoryYes"), Msg.get("settingsPage.removeCategoryNo"), dialog -> {
          if (dialog.isConfirmed()) {
            CategoryEntity category = getItem();
            eshs.removeCategory(category, expenseSheet);
            cs.remove(category);
            refreshValues();
          }
        });
  };

  public CategorySettingGrid() {
    cs = AppCtxProvider.getBean(CategoryService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    
    ComboBox<BigDecimal> multiplierField = new ComboBox<>();
    multiplierField.setEmptySelectionAllowed(false);
    multiplierField.setItems(new BigDecimal("-1"), new BigDecimal("1"));
    
    Binder.Binding<CategoryEntity, BigDecimal> multiplierBinder = binder.bind(multiplierField, CategoryEntity::getMultiplier, CategoryEntity::setMultiplier);
    
    addColumn(CategoryEntity::getName)
      .setCaption(Msg.get("settingsPage.categoryName"))
      .setEditorComponent(new TextField(), CategoryEntity::setName);
    addColumn(CategoryEntity::getMultiplier)
      .setCaption(Msg.get("settingsPage.categoryMultiplier"))
      .setEditorBinding(multiplierBinder);

    addSelectionListener(itemSelected);
    
    getEditor().setEnabled(true);
    getEditor().setSaveCaption(Msg.get("settingsPage.categorySave"));
    getEditor().setCancelCaption(Msg.get("settingsPage.categoryCancel"));
    getEditor().addOpenListener(editorOpen);
    getEditor().addSaveListener(saveCategory);
  }

  public void setAddCategoryButton(Button button) {
    addCategoryButton = button;
    addCategoryButton.addClickListener(addCategoryClicked);
  }

  public void setMoveUpCategoryButton(Button button) {
    moveUpCategoryButton = button;
    moveUpCategoryButton.addClickListener(moveUpClicked);
  }

  public void setMoveDownCategoryButton(Button button) {
    moveDownCategoryButton = button;
    moveDownCategoryButton.addClickListener(moveDownClick);
  }

  public void setDeleteCategoryButton(Button button) {
    deleteCategoryButton = button;
    deleteCategoryButton.addClickListener(deleteCategoryClick);
  }

  @Override
  public void refreshValues() {
	  setItems(expenseSheet.getCategoryList());
  }

  private CategoryEntity getItem() {
    return getSelectedItems().iterator().next();
  }
}
