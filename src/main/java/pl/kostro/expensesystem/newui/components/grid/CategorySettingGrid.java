package pl.kostro.expensesystem.newui.components.grid;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.EditorOpenListener;
import com.vaadin.flow.component.grid.editor.EditorSaveListener;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.server.VaadinSession;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.service.CategoryService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.newui.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.newui.views.settingsPage.AddCategoryWindow;
import pl.kostro.expensesystem.newui.views.settingsPage.SettingsChangeListener;
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

  TextField nameField = new TextField();
  ComboBox<BigDecimal> multiplierField = new ComboBox<>();
  
  private SelectionListener<Grid<CategoryEntity>, CategoryEntity> itemSelected = event -> {
    moveUpCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
    moveDownCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
    deleteCategoryButton.setEnabled(event.getAllSelectedItems().size() != 0);
  };
  private EditorOpenListener<CategoryEntity> editorOpen = event -> binder.setBean(event.getItem());
  private EditorSaveListener<CategoryEntity> saveCategory = event -> cs.save(event.getItem());
  private ComponentEventListener<ClickEvent<Button>> addCategoryClicked = event -> new AddCategoryWindow(CategorySettingGrid.this).open();
  private ComponentEventListener<ClickEvent<Button>> moveUpClicked = event -> {
    expenseSheet = eshs.moveCategoryUp(expenseSheet, getItem());
    refreshValues();
  };
  private ComponentEventListener<ClickEvent<Button>> moveDownClick = event -> {
    expenseSheet = eshs.moveCategoryDown(expenseSheet, getItem());
    refreshValues();
  };
  private ComponentEventListener<ClickEvent<Button>> deleteCategoryClick = event -> {
    ConfirmDialog.show(Msg.get("settingsPage.removeCategoryLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeCategoryQuestion"), new Object[] { getItem().getName() }),
        Msg.get("settingsPage.removeCategoryYes"),
        Msg.get("settingsPage.removeCategoryNo"),
        dialog -> {
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

//    multiplierField.setEmptySelectionAllowed(false);
    multiplierField.setItems(new BigDecimal("-1"), new BigDecimal("1"));

    binder.forField(nameField).bind(CategoryEntity::getName, CategoryEntity::setName);
    binder.forField(multiplierField).bind(CategoryEntity::getMultiplier, CategoryEntity::setMultiplier);

    Button saveButton = new Button(Msg.get("settingsPage.categorySave"), e -> getEditor().save());
    Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> getEditor().cancel());
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
    HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
    actions.setPadding(false);

    addColumn(CategoryEntity::getName).setHeader(Msg.get("settingsPage.categoryName")).setEditorComponent(nameField);
    addColumn(CategoryEntity::getMultiplier).setHeader(Msg.get("settingsPage.categoryMultiplier")).setEditorComponent(multiplierField);
    addComponentColumn(category -> {
      Button editButton = new Button(Msg.get("settingsPage.categoryEdit"));
      editButton.addClickListener(e -> getEditor().editItem(category));
      return editButton;
    }).setEditorComponent(actions);

    addSelectionListener(itemSelected);

    getEditor().setBinder(binder);
    getEditor().setBuffered(true);

//    getEditor().setEnabled(true);
//    getEditor().setSaveCaption();
//    getEditor().setCancelCaption(Msg.get("settingsPage.categoryCancel"));
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
