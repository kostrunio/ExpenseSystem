package pl.kostro.expensesystem.components.grid;

import java.math.BigDecimal;
import java.text.MessageFormat;

import com.vaadin.data.Binder;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.components.grid.EditorSaveListener;

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
  
  private SelectionListener<Category> itemSelected = event -> {
    moveUpCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
    moveDownCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
    deleteCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
  };
  private EditorSaveListener<Category> saveCategory = event -> cs.merge(event.getBean());
  private ClickListener addCategoryClicked = event -> UI.getCurrent().addWindow(new AddCategoryWindow(CategorySettingGrid.this));
  private ClickListener moveUpClicked = event -> {
    Category category = getItem();
    expenseSheet = eshs.moveCategoryUp(expenseSheet, category);
    refreshValues();
  };
  private ClickListener moveDownClick = event -> {
    Category category = getItem();
    expenseSheet = eshs.moveCategoryDown(expenseSheet, category);
    refreshValues();
  };
  private ClickListener deleteCategoryClick = event -> {
    ConfirmDialog.show(getUI(), Msg.get("settingsPage.removeCategoryLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeCategoryQuestion"), new Object[] { getItem().getName() }),
        Msg.get("settingsPage.removeCategoryYes"), Msg.get("settingsPage.removeCategoryNo"), dialog -> {
          if (dialog.isConfirmed()) {
            expenseSheet = cs.removeCategory(expenseSheet, getItem());
            refreshValues();
          }
        });
  };

  public CategorySettingGrid() {
    cs = AppCtxProvider.getBean(CategoryService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    
    ComboBox<BigDecimal> multiplierField = new ComboBox<>();
    multiplierField.setItems(new BigDecimal("-1"), new BigDecimal("1"));
    
    Binder<Category> binder = new Binder<>();
    Binder.Binding<Category, BigDecimal> multiplierBinder = binder.forField(multiplierField)
        .bind(Category::getMultiplier, Category::setMultiplier);
    
    addColumn(Category::getName)
      .setCaption(Msg.get("settingsPage.categoryName"))
      .setEditorComponent(new TextField(), Category::setName);
    addColumn(Category::getMultiplier)
      .setCaption(Msg.get("settingsPage.categoryMultiplier"))
      .setEditorBinding(multiplierBinder);

    addSelectionListener(itemSelected);
    
    getEditor().setEnabled(true);
    getEditor().setSaveCaption(Msg.get("settingsPage.categorySave"));
    getEditor().setCancelCaption(Msg.get("settingsPage.categoryCancel"));
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

  public void refreshValues() {
	  setItems(expenseSheet.getCategoryList());
  }

  private Category getItem() {
    return getSelectedItems().iterator().next();
  }
}
