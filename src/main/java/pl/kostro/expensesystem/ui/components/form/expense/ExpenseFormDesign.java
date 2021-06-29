package pl.kostro.expensesystem.ui.components.form.expense;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class ExpenseFormDesign extends FormLayout {
  protected HorizontalLayout actionsLayout = new HorizontalLayout();
  protected Button saveButton = new Button();
  protected Button duplicateButton = new Button();
  protected Button removeButton = new Button();
  protected DateField dateField = new DateField();
  protected ComboBox<CategoryEntity> categoryBox = new ComboBox<>();
  protected ComboBox<UserLimitEntity> userBox = new ComboBox<>();
  protected TextField formulaField = new TextField();
  protected ComboBox<java.lang.String> commentBox = new ComboBox<>();
  protected CheckBox notifyBox = new CheckBox();

  public ExpenseFormDesign() {
    setMargin(true);
    setSizeUndefined();
    notifyBox.setVisible(false);
    addComponents(createActionsLayout(), dateField, categoryBox, userBox, formulaField, commentBox, notifyBox);
  }

  private Component createActionsLayout() {
    saveButton.setEnabled(false);
    saveButton.setStyleName("friendly");
    duplicateButton.setIcon(VaadinIcons.COPY_O);
    duplicateButton.setStyleName("icon-only borderless");
    removeButton.setIcon(VaadinIcons.TRASH);
    removeButton.setStyleName("icon-only borderless");
    actionsLayout.addComponents(saveButton, duplicateButton, removeButton);
    return actionsLayout;
  }
}
