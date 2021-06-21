package pl.kostro.expensesystem.ui.view.design;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import pl.kostro.expensesystem.ui.components.grid.CategorySettingGrid;
import pl.kostro.expensesystem.ui.components.grid.RealUserLimitSettingGrid;
import pl.kostro.expensesystem.ui.components.grid.UserLimitSettingGrid;

public class SettingsDesign extends VerticalLayout {
  protected HorizontalLayout headerLayout = new HorizontalLayout();
  protected Label titleLabel = new Label();
  protected HorizontalLayout toolsLayout = new HorizontalLayout();
  protected Button editButton = new Button();
  protected Button passwordButton = new Button();
  protected Button removeButton = new Button();
  protected HorizontalLayout gridLayout = new HorizontalLayout();
  protected Panel categoryPanel = new Panel();
  protected VerticalLayout categoryLayout = new VerticalLayout();
  protected CategorySettingGrid categoryGrid = new CategorySettingGrid();
  protected HorizontalLayout categoryButtonLayout = new HorizontalLayout();
  protected Button addCategoryButton = new Button();
  protected Button moveUpCategoryButton = new Button();
  protected Button moveDownCategoryButton = new Button();
  protected Button deleteCategoryButton = new Button();
  protected Panel realUserPanel = new Panel();
  protected VerticalLayout realUserLayout = new VerticalLayout();
  protected RealUserLimitSettingGrid realUserGrid = new RealUserLimitSettingGrid();
  protected HorizontalLayout realUserButtonLayout = new HorizontalLayout();
  protected Button addRealUserButton = new Button();
  protected Button deleteRealUserButton = new Button();
  protected Panel userPanel = new Panel();
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
    addComponents(createHeaderLayout(), createGridLayout(), backButton);
  }

  private Component createHeaderLayout() {
    titleLabel.setStyleName("h1 no-margin");
    headerLayout.addComponents(titleLabel, createToolsLayout());
    return headerLayout;
  }

  private Component createToolsLayout() {
    editButton.setIcon(VaadinIcons.EDIT);
    editButton.setStyleName("icon-only borderless");
    passwordButton.setIcon(VaadinIcons.LOCK);
    passwordButton.setStyleName("icon-only borderless");
    removeButton.setIcon(VaadinIcons.TRASH);
    removeButton.setStyleName("icon-only borderless");
    toolsLayout.addComponents(editButton, passwordButton, removeButton);
    return toolsLayout;
  }

  private Component createGridLayout() {
    gridLayout.setSizeFull();
    gridLayout.addComponents(createCategoryPanel(), createRealUserPanel(), createUserPanel());
    return gridLayout;
  }

  private Component createCategoryPanel() {
    categoryPanel.setWidth("355px");
    categoryPanel.setHeight("100%");
    categoryLayout.setSizeFull();
    categoryLayout.addComponents(createCategoryGrid(), createCategoryButtonLayout());
    categoryPanel.setContent(categoryLayout);
    return categoryPanel;
  }

  private Component createCategoryGrid() {
    categoryGrid.setSizeFull();
    return categoryGrid;
  }

  private Component createCategoryButtonLayout() {
    categoryButtonLayout.setWidth("100%");
    addCategoryButton.setIcon(VaadinIcons.PLUS_SQUARE_LEFT_O);
    addCategoryButton.setStyleName("icon-only borderless");
    moveUpCategoryButton.setIcon(VaadinIcons.ARROW_UP);
    moveUpCategoryButton.setStyleName("icon-only borderless");
    moveDownCategoryButton.setIcon(VaadinIcons.ARROW_DOWN);
    moveDownCategoryButton.setStyleName("icon-only borderless");
    deleteCategoryButton.setIcon(VaadinIcons.MINUS_SQUARE_LEFT_O);
    deleteCategoryButton.setStyleName("icon-only borderless");
    categoryButtonLayout.addComponents(addCategoryButton, moveUpCategoryButton, moveDownCategoryButton, deleteCategoryButton);
    return categoryButtonLayout;
  }

  private Component createRealUserPanel() {
    realUserPanel.setWidth("355px");
    realUserPanel.setHeight("100%");
    realUserLayout.setSizeFull();
    realUserLayout.addComponents(createRealUserGrid(), createRealUserButtonLayout());
    realUserPanel.setContent(realUserLayout);
    return realUserPanel;
  }

  private Component createRealUserGrid() {
    realUserGrid.setSizeFull();
    return realUserGrid;
  }

  private Component createRealUserButtonLayout() {
    realUserButtonLayout.setWidth("100%");
    addRealUserButton.setIcon(VaadinIcons.PLUS_SQUARE_LEFT_O);
    addRealUserButton.setStyleName("icon-only borderless");
    deleteRealUserButton.setIcon(VaadinIcons.MINUS_SQUARE_LEFT_O);
    deleteRealUserButton.setStyleName("icon-only borderless");
    realUserButtonLayout.addComponents(addRealUserButton, deleteRealUserButton);
    return realUserButtonLayout;
  }

  private Component createUserPanel() {
    userPanel.setWidth("355px");
    userPanel.setHeight("100%");
    userLayout.setSizeFull();
    userLayout.addComponents(createUserGrid(), createUserButtonLayout());
    userPanel.setContent(userLayout);
    return userPanel;
  }

  private Component createUserGrid() {
    userGrid.setSizeFull();
    return userGrid;
  }

  private Component createUserButtonLayout() {
    userButtonLayout.setWidth("100%");
    addUserButton.setIcon(VaadinIcons.PLUS_SQUARE_LEFT_O);
    addUserButton.setStyleName("icon-only borderless");
    deleteUserButton.setIcon(VaadinIcons.MINUS_SQUARE_LEFT_O);
    deleteUserButton.setStyleName("icon-only borderless");
    userButtonLayout.addComponents(addUserButton, deleteUserButton);
    return userButtonLayout;
  }
}
