package pl.kostro.expensesystem.newui.component.form.expense;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class ExpenseFormDesign extends FormLayout {
  protected HorizontalLayout actionsLayout = new HorizontalLayout();
  protected Button saveButton = new Button();
  protected Button duplicateButton = new Button();
  protected Button removeButton = new Button();
  protected DatePicker dateField = new DatePicker();
  protected ComboBox<CategoryEntity> categoryBox = new ComboBox<>();
  protected ComboBox<UserLimitEntity> userBox = new ComboBox<>();
  protected TextField formulaField = new TextField();
  protected ComboBox<String> commentBox = new ComboBox<>();
  protected Checkbox notifyBox = new Checkbox();

  public ExpenseFormDesign() {
//    setMargin(true);
    setSizeUndefined();
    notifyBox.setVisible(false);
    add(createActionsLayout(), dateField, categoryBox, userBox, formulaField, commentBox, notifyBox);
  }

  private Component createActionsLayout() {
    saveButton.setEnabled(false);
    saveButton.setClassName("friendly");
    duplicateButton.setIcon(VaadinIcon.COPY_O.create());
    duplicateButton.setClassName("icon-only borderless");
    removeButton.setIcon(VaadinIcon.TRASH.create());
    removeButton.setClassName("icon-only borderless");
    actionsLayout.add(saveButton, duplicateButton, removeButton);
    return actionsLayout;
  }
}
