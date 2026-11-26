package pl.kostro.expensesystem.ui.views.day;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class DayDesign extends VerticalLayout {
  protected HorizontalLayout navigationLayout = new HorizontalLayout();
  protected Button previousDayButton = new Button();
  protected DateField thisDateField = new DateField();
  protected Button nextDayButton = new Button();

  protected CssLayout dayLayout = new CssLayout();
  protected CssLayout categoryListPanel = new CssLayout();
  protected CssLayout categoryGrid = new CssLayout();
  protected Button backButton = new Button();

  protected CssLayout expenseListPanel = new CssLayout();
  protected VerticalLayout expenseListLayout = new VerticalLayout();
  protected Label categoryLabel = new Label();
  protected GridLayout expenseGrid = new GridLayout();

  protected HorizontalLayout addExpenseLayout = new HorizontalLayout();
  protected ComboBox<UserLimitEntity> userCombo = new ComboBox<>();
  protected TextField formulaField = new TextField();
  protected ComboBox<String> commentCombo = new ComboBox<>();
  protected CheckBox notifyCheckbox = new CheckBox("notify me");
  protected Button saveButton = new Button();

  public DayDesign() {
    setSizeFull();
    setSpacing(false);
    setMargin(false);
    addComponents(createaNavigationLayout(), createDayLayout());
    setComponentAlignment(navigationLayout, Alignment.MIDDLE_CENTER);
  }

  private Component createaNavigationLayout() {
    previousDayButton.setIcon(VaadinIcons.ARROW_LEFT);
    previousDayButton.setStyleName("icon-only borderless");
    nextDayButton.setIcon(VaadinIcons.ARROW_RIGHT);
    nextDayButton.setStyleName("icon-only borderless");

    navigationLayout.setMargin(true);
    navigationLayout.addComponents(previousDayButton, thisDateField, nextDayButton);
    navigationLayout.setComponentAlignment(previousDayButton, Alignment.MIDDLE_RIGHT);
    navigationLayout.setComponentAlignment(thisDateField, Alignment.MIDDLE_CENTER);
    return navigationLayout;
  }

  private Component createDayLayout() {
    dayLayout.setStyleName("day-layout");
    dayLayout.setWidth("100%");
    dayLayout.addComponents(createCategoryListPanel(), createExpenseListPanel());
    return dayLayout;
  }

  private Component createCategoryListPanel() {
    categoryListPanel.setWidth("50%");
    categoryListPanel.setStyleName("categorylist-panel");

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(false);
    layout.addComponents(createCategoryGrid(), backButton);

    categoryListPanel.addComponent(layout);
    return categoryListPanel;
  }

  private Component createCategoryGrid() {
    categoryGrid.setWidth("100%");
    categoryGrid.setStyleName("category-grid");
    return categoryGrid;
  }

  private Component createExpenseListPanel() {
    expenseListPanel.setWidth("50%");
    expenseListPanel.setStyleName("expenselist-panel");

    expenseListPanel.addComponents(createExpenseListLayout(), createLayout());
    return expenseListPanel;
  }

  private Component createExpenseListLayout() {
    expenseListLayout.setStyleName("expense-list");
    expenseListLayout.setSpacing(false);
    expenseListLayout.setMargin(false);
    expenseListLayout.addComponents(createCategoryLabel(), createExpenseGrid());
    expenseListLayout.setComponentAlignment(categoryLabel, Alignment.MIDDLE_CENTER);
    return expenseListLayout;
  }

  private Component createCategoryLabel() {
    categoryLabel.setStyleName("category-label");
    return categoryLabel;
  }

  private Component createExpenseGrid() {
    expenseGrid.setStyleName("expense-grid");
    expenseGrid.setSpacing(true);
    expenseGrid.setMargin(true);
    expenseGrid.setWidth("100%");
    return expenseGrid;
  }

  private Component createLayout() {
    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(false);
    layout.addComponents(createAddExpenseLayout(), createNotifyCheckbox(), createSaveButton());
    return layout;
  }

  private Component createAddExpenseLayout() {
    addExpenseLayout.setStyleName("add-expense");
    addExpenseLayout.addComponents(createUserCombo(), createFormulaField(), createCommentCombo());
    return addExpenseLayout;
  }

  private Component createUserCombo() {
    userCombo.setStyleName("add-expense-field");
    userCombo.setWidth("100%");
    return userCombo;
  }

  private Component createFormulaField() {
    formulaField.setStyleName("add-expense-field");
    formulaField.setWidth("100%");
    return formulaField;
  }

  private Component createCommentCombo() {
    commentCombo.setStyleName("add-expense-field");
    commentCombo.setWidth("100%");
    return categoryLabel;
  }

  private Component createNotifyCheckbox() {
    notifyCheckbox.setVisible(false);
    return notifyCheckbox;
  }

  private Component createSaveButton() {
    saveButton.setStyleName("friendly");
    return saveButton;
  }
}
