package pl.kostro.expensesystem.newui.views.settings;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kostro.expensesystem.newui.components.grid.CategorySettingGrid;
import pl.kostro.expensesystem.newui.components.grid.RealUserLimitSettingGrid;
import pl.kostro.expensesystem.newui.components.grid.UserLimitSettingGrid;

public class SettingsDesign extends VerticalLayout {
  protected HorizontalLayout headerLayout = new HorizontalLayout();
  protected Label titleLabel = new Label();
  protected HorizontalLayout toolsLayout = new HorizontalLayout();
  protected Button editButton = new Button();
  protected Button passwordButton = new Button();
  protected Button removeButton = new Button();
  protected HorizontalLayout gridLayout = new HorizontalLayout();
  protected Label categoryLabel = new Label();
  protected VerticalLayout categoryLayout = new VerticalLayout();
  protected CategorySettingGrid categoryGrid = new CategorySettingGrid();
  protected HorizontalLayout categoryButtonLayout = new HorizontalLayout();
  protected Button addCategoryButton = new Button();
  protected Button moveUpCategoryButton = new Button();
  protected Button moveDownCategoryButton = new Button();
  protected Button deleteCategoryButton = new Button();
  protected Label realUserLabel = new Label();
  protected VerticalLayout realUserLayout = new VerticalLayout();
  protected RealUserLimitSettingGrid realUserGrid = new RealUserLimitSettingGrid();
  protected HorizontalLayout realUserButtonLayout = new HorizontalLayout();
  protected Button addRealUserButton = new Button();
  protected Button deleteRealUserButton = new Button();
  protected Label userLabel = new Label();
  protected VerticalLayout userLayout = new VerticalLayout();
  protected UserLimitSettingGrid userGrid = new UserLimitSettingGrid();
  protected HorizontalLayout userButtonLayout = new HorizontalLayout();
  protected Button addUserButton = new Button();
  protected Button deleteUserButton = new Button();
  protected Button backButton = new Button();

  public SettingsDesign() {
    setSpacing(false);
    setSizeFull();
    setMargin(false);
    add(createHeaderLayout(), createGridLayout(), backButton);
  }

  private Component createHeaderLayout() {
    titleLabel.setClassName("h1 no-margin");
    headerLayout.add(titleLabel, createToolsLayout());
    return headerLayout;
  }

  private Component createToolsLayout() {
    editButton.setIcon(VaadinIcon.EDIT.create());
    editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    passwordButton.setIcon(VaadinIcon.LOCK.create());
    passwordButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    removeButton.setIcon(VaadinIcon.TRASH.create());
    removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    toolsLayout.add(editButton, passwordButton, removeButton);
    return toolsLayout;
  }

  private Component createGridLayout() {
    gridLayout.setSizeFull();
    gridLayout.add(createCategoryPanel(), createRealUserPanel(), createUserPanel());
    return gridLayout;
  }

  private Component createCategoryPanel() {
    categoryLayout.setWidth("500px");
    categoryLayout.setHeightFull();
    categoryLayout.add(categoryLabel, createCategoryGrid(), createCategoryButtonLayout());
    return categoryLayout;
  }

  private Component createCategoryGrid() {
    categoryGrid.setWidthFull();
    return categoryGrid;
  }

  private Component createCategoryButtonLayout() {
    categoryButtonLayout.setWidthFull();
    addCategoryButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
    addCategoryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    moveUpCategoryButton.setIcon(VaadinIcon.ARROW_UP.create());
    moveUpCategoryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    moveDownCategoryButton.setIcon(VaadinIcon.ARROW_DOWN.create());
    moveDownCategoryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    deleteCategoryButton.setIcon(VaadinIcon.MINUS_SQUARE_O.create());
    deleteCategoryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    categoryButtonLayout.add(addCategoryButton, moveUpCategoryButton, moveDownCategoryButton, deleteCategoryButton);
    return categoryButtonLayout;
  }

  private Component createRealUserPanel() {
    realUserLayout.setWidth("450px");
    realUserLayout.setHeightFull();
    realUserLayout.add(realUserLabel, createRealUserGrid(), createRealUserButtonLayout());
    return realUserLayout;
  }

  private Component createRealUserGrid() {
    realUserGrid.setWidthFull();
    return realUserGrid;
  }

  private Component createRealUserButtonLayout() {
    realUserButtonLayout.setWidthFull();
    addRealUserButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
    addRealUserButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    deleteRealUserButton.setIcon(VaadinIcon.MINUS_SQUARE_O.create());
    deleteRealUserButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    realUserButtonLayout.add(addRealUserButton, deleteRealUserButton);
    return realUserButtonLayout;
  }

  private Component createUserPanel() {
    userLayout.setWidth("450px");
    userLayout.setHeightFull();
    userLayout.add(userLabel, createUserGrid(), createUserButtonLayout());
    return userLayout;
  }

  private Component createUserGrid() {
    userGrid.setWidthFull();
    return userGrid;
  }

  private Component createUserButtonLayout() {
    userButtonLayout.setWidthFull();
    addUserButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
    addUserButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    deleteUserButton.setIcon(VaadinIcon.MINUS_SQUARE_O.create());
    deleteUserButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
    userButtonLayout.add(addUserButton, deleteUserButton);
    return userButtonLayout;
  }
}
