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
  protected VerticalLayout expenseList = new VerticalLayout();
  protected Label categoryLabel = new Label();
  protected GridLayout expenseGrid = new GridLayout();
  protected HorizontalLayout addExpense = new HorizontalLayout();
  protected ComboBox<UserLimitEntity> userBox = new ComboBox<>();
  protected TextField formulaField = new TextField();
  protected ComboBox<java.lang.String> commentBox = new ComboBox<>();
  protected CheckBox notifyBox = new CheckBox("notify me");
  protected Button saveButton = new Button();

  public DayDesign() {
    setSizeFull();
    setSpacing(false);
    setMargin(false);
    addComponents(createaNavigationLayout(), createDayLayout());
    setComponentAlignment(navigationLayout, Alignment.MIDDLE_CENTER);
  }

  private Component createaNavigationLayout() {
    navigationLayout.setMargin(true);
    previousDayButton.setIcon(VaadinIcons.ARROW_LEFT);
    previousDayButton.setStyleName("icon-only borderless");
    nextDayButton.setIcon(VaadinIcons.ARROW_RIGHT);
    nextDayButton.setStyleName("icon-only borderless");
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
    categoryGrid.setWidth("100%");
    categoryGrid.setStyleName("category-grid");
    layout.addComponents(categoryGrid, backButton);
    categoryListPanel.addComponent(layout);
    return categoryListPanel;
  }

  private Component createExpenseListPanel() {
    expenseListPanel.setWidth("50%");
    expenseListPanel.setStyleName("expenselist-panel");
    notifyBox.setVisible(false);
    saveButton.setStyleName("friendly");
    expenseList.setStyleName("expense-list");
    expenseList.setSpacing(false);
    expenseList.setMargin(false);
    categoryLabel.setStyleName("category-label");
    expenseGrid.setStyleName("expense-grid");
    expenseGrid.setSpacing(true);
    expenseGrid.setMargin(true);
    expenseGrid.setWidth("100%");
    expenseList.addComponents(categoryLabel, expenseGrid);
    expenseList.setComponentAlignment(categoryLabel, Alignment.MIDDLE_CENTER);
    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(false);
    layout.addComponents(createAddExpense(), notifyBox, saveButton);
    expenseListPanel.addComponents(expenseList, layout);
    return expenseListPanel;
  }

  private Component createAddExpense() {
    addExpense.setStyleName("add-expense");
    userBox.setStyleName("add-expense-field");
    userBox.setWidth("100%");
    formulaField.setStyleName("add-expense-field");
    formulaField.setWidth("100%");
    commentBox.setStyleName("add-expense-field");
    commentBox.setWidth("100%");
    addExpense.addComponents(userBox, formulaField, commentBox);
    return addExpense;
  }
}
