package pl.kostro.expensesystem.newui.settings;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kostro.expensesystem.newui.component.grid.CategorySettingGrid;
import pl.kostro.expensesystem.newui.component.grid.RealUserLimitSettingGrid;
import pl.kostro.expensesystem.newui.component.grid.UserLimitSettingGrid;

public class SettingsDesign extends VerticalLayout {
  protected HorizontalLayout headerLayout = new HorizontalLayout();
  protected Label titleLabel = new Label();
  protected HorizontalLayout toolsLayout = new HorizontalLayout();
  protected Button editButton = new Button();
  protected Button passwordButton = new Button();
  protected Button removeButton = new Button();
  protected HorizontalLayout gridLayout = new HorizontalLayout();
  protected Div categoryPanel = new Div();
  protected VerticalLayout categoryLayout = new VerticalLayout();
  protected CategorySettingGrid categoryGrid = new CategorySettingGrid();
  protected HorizontalLayout categoryButtonLayout = new HorizontalLayout();
  protected Button addCategoryButton = new Button();
  protected Button moveUpCategoryButton = new Button();
  protected Button moveDownCategoryButton = new Button();
  protected Button deleteCategoryButton = new Button();
  protected Div realUserPanel = new Div();
  protected VerticalLayout realUserLayout = new VerticalLayout();
  protected RealUserLimitSettingGrid realUserGrid = new RealUserLimitSettingGrid();
  protected HorizontalLayout realUserButtonLayout = new HorizontalLayout();
  protected Button addRealUserButton = new Button();
  protected Button deleteRealUserButton = new Button();
  protected Div userPanel = new Div();
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
    editButton.setClassName("icon-only borderless");
    passwordButton.setIcon(VaadinIcon.LOCK.create());
    passwordButton.setClassName("icon-only borderless");
    removeButton.setIcon(VaadinIcon.TRASH.create());
    removeButton.setClassName("icon-only borderless");
    toolsLayout.add(editButton, passwordButton, removeButton);
    return toolsLayout;
  }

  private Component createGridLayout() {
    gridLayout.setSizeFull();
    gridLayout.add(createCategoryPanel(), createRealUserPanel(), createUserPanel());
    return gridLayout;
  }

  private Component createCategoryPanel() {
    categoryPanel.setWidth("355px");
    categoryPanel.setHeight("100%");
    categoryLayout.setSizeFull();
    categoryLayout.add(createCategoryGrid(), createCategoryButtonLayout());
    categoryPanel.add(categoryLayout);
    return categoryPanel;
  }

  private Component createCategoryGrid() {
    categoryGrid.setSizeFull();
    return categoryGrid;
  }

  private Component createCategoryButtonLayout() {
    categoryButtonLayout.setWidth("100%");
    addCategoryButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
    addCategoryButton.setClassName("icon-only borderless");
    moveUpCategoryButton.setIcon(VaadinIcon.ARROW_UP.create());
    moveUpCategoryButton.setClassName("icon-only borderless");
    moveDownCategoryButton.setIcon(VaadinIcon.ARROW_DOWN.create());
    moveDownCategoryButton.setClassName("icon-only borderless");
    deleteCategoryButton.setIcon(VaadinIcon.MINUS_SQUARE_O.create());
    deleteCategoryButton.setClassName("icon-only borderless");
    categoryButtonLayout.add(addCategoryButton, moveUpCategoryButton, moveDownCategoryButton, deleteCategoryButton);
    return categoryButtonLayout;
  }

  private Component createRealUserPanel() {
    realUserPanel.setWidth("355px");
    realUserPanel.setHeight("100%");
    realUserLayout.setSizeFull();
    realUserLayout.add(createRealUserGrid(), createRealUserButtonLayout());
    realUserPanel.add(realUserLayout);
    return realUserPanel;
  }

  private Component createRealUserGrid() {
    realUserGrid.setSizeFull();
    return realUserGrid;
  }

  private Component createRealUserButtonLayout() {
    realUserButtonLayout.setWidth("100%");
    addRealUserButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
    addRealUserButton.setClassName("icon-only borderless");
    deleteRealUserButton.setIcon(VaadinIcon.MINUS_SQUARE_O.create());
    deleteRealUserButton.setClassName("icon-only borderless");
    realUserButtonLayout.add(addRealUserButton, deleteRealUserButton);
    return realUserButtonLayout;
  }

  private Component createUserPanel() {
    userPanel.setWidth("355px");
    userPanel.setHeight("100%");
    userLayout.setSizeFull();
    userLayout.add(createUserGrid(), createUserButtonLayout());
    userPanel.add(userLayout);
    return userPanel;
  }

  private Component createUserGrid() {
    userGrid.setSizeFull();
    return userGrid;
  }

  private Component createUserButtonLayout() {
    userButtonLayout.setWidth("100%");
    addUserButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
    addUserButton.setClassName("icon-only borderless");
    deleteUserButton.setIcon(VaadinIcon.MINUS_SQUARE_O.create());
    deleteUserButton.setClassName("icon-only borderless");
    userButtonLayout.add(addUserButton, deleteUserButton);
    return userButtonLayout;
  }
}
