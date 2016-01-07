package pl.kostro.expensesystem.views.settingsPage;


import java.text.MessageFormat;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.components.grid.CategoryGrid;
import pl.kostro.expensesystem.components.grid.RealUserLimitGrid;
import pl.kostro.expensesystem.components.grid.UserLimitGrid;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.RealUserService;
import pl.kostro.expensesystem.views.ExpenseMenu;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetEditPasswordWindow.ExpenseSheetPasswordChangeListener;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetEditWindow.ExpenseSheetEditListener;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class ExpenseSheetSettingsView extends CustomComponent implements ExpenseSheetSettingsChangeListener, ExpenseSheetEditListener, ExpenseSheetPasswordChangeListener {

  @AutoGenerated
  private VerticalLayout mainLayout;

  @AutoGenerated
  private HorizontalLayout gridLayout;
  
  @AutoGenerated
  private HorizontalLayout buttonLayout;

  @AutoGenerated
  private Button delSheetButton;

  @AutoGenerated
  private Panel userPanel;

  @AutoGenerated
  private VerticalLayout userLayout;

  @AutoGenerated
  private HorizontalLayout userButtonLayout;

  @AutoGenerated
  private Button deleteUserButton;

  @AutoGenerated
  private Button addUserButton;

  @AutoGenerated
  private UserLimitGrid userGrid;

  @AutoGenerated
  private Panel realUserPanel;

  @AutoGenerated
  private VerticalLayout realUserLayout;

  @AutoGenerated
  private HorizontalLayout realUserButtonLayout;

  @AutoGenerated
  private Button deleteRealUserButton;

  @AutoGenerated
  private Button addRealUserButton;

  @AutoGenerated
  private RealUserLimitGrid realUserGrid;

  @AutoGenerated
  private Panel categoryPanel;

  @AutoGenerated
  private VerticalLayout categoryLayout;

  @AutoGenerated
  private HorizontalLayout categoryButtonLayout;

  @AutoGenerated
  private Button deleteCategoryButton;

  @AutoGenerated
  private Button moveDownCategoryButton;

  @AutoGenerated
  private Button moveUpCategoryButton;

  @AutoGenerated
  private Button addCategoryButton;

  @AutoGenerated
  private CategoryGrid categoryGrid;

  private static final long serialVersionUID = -4661152658922847492L;

  /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

  private ExpenseSheet expenseSheet;
  private Label titleLabel;

  public ExpenseSheetSettingsView() {
    prepareView();
  }
  
  private void prepareView() {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    buildMainLayout();
    setCompositionRoot(mainLayout);

    // TODO add user code here
    
    disableAllButtons();
    
    delSheetButton.addClickListener(new Button.ClickListener() {
      private static final long serialVersionUID = 8619201598088375093L;

      @Override
      public void buttonClick(ClickEvent event) {
        ConfirmDialog.show(getUI(),
            Msg.get("settingsPage.removeSheetLabel"),
            MessageFormat.format(Msg.get("settingsPage.removeSheetQuestion"), expenseSheet.getName()),
            Msg.get("settingsPage.removeSheetYes"),
            Msg.get("settingsPage.removeSheetNo"),
            new ConfirmDialog.Listener() {

          private static final long serialVersionUID = 3844318339125611876L;

          @Override
          public void onClose(ConfirmDialog dialog) {
            if (dialog.isConfirmed()) {
              ExpenseSheetService.removeExpenseSheet(expenseSheet);
              RealUser loggedUser = VaadinSession.getCurrent().getAttribute(RealUser.class);
              RealUserService.refresh(loggedUser);
              if (loggedUser.getDefaultExpenseSheet() == null && loggedUser.getExpenseSheetList().size() != 0)
                RealUserService.setDefaultExpenseSheet(loggedUser, loggedUser.getExpenseSheetList().get(0));
              VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, null);
              VaadinSession.getCurrent().getAttribute(ExpenseMenu.class).refresh();
              UI.getCurrent().getNavigator().navigateTo("");
            }
          }
        });
      }
    });
    
    categoryGrid.setAddCategoryButton(addCategoryButton);
    categoryGrid.setMoveUpCategoryButton(moveUpCategoryButton);
    categoryGrid.setMoveDownCategoryButton(moveDownCategoryButton);
    categoryGrid.setDeleteCategoryButton(deleteCategoryButton);
    
    realUserGrid.setAddUserLimitButton(addRealUserButton);
    realUserGrid.setDeleteUserLimitButton(deleteRealUserButton);
    
    userGrid.setAddUserLimitButton(addUserButton);
    userGrid.setDeleteUserLimitButton(deleteUserButton);
    
    if (expenseSheet != null) {
      categoryGrid.filCategoryGrid();
      realUserGrid.filUserLimitGrid(expenseSheet.getUserLimitListRealUser());
      userGrid.filUserLimitGrid(expenseSheet.getUserLimitListNotRealUser());
    }
  }
  
  private void disableAllButtons() {
    moveUpCategoryButton.setEnabled(false);
    moveDownCategoryButton.setEnabled(false);
    deleteCategoryButton.setEnabled(false);
    deleteRealUserButton.setEnabled(false);
    deleteUserButton.setEnabled(false);
  }

  @AutoGenerated
  private VerticalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setImmediate(false);
    mainLayout.setWidth("100%");
    mainLayout.setHeight("-1px");
    mainLayout.setMargin(true);
    
    if (expenseSheet != null)
      mainLayout.addComponent(buildHeader());
    
    gridLayout = new HorizontalLayout();
    gridLayout.setImmediate(false);
    gridLayout.setWidth("100%");
    gridLayout.setHeight("80%");
    gridLayout.setSpacing(true);
    mainLayout.addComponent(gridLayout);
    
    // top-level component properties
    setWidth("100.0%");
    setHeight("100.0%");
    
    // categoryPanel
    categoryPanel = buildCategoryPanel();
    gridLayout.addComponent(categoryPanel);
    
    // realUserPanel
    realUserPanel = buildRealUserPanel();
    gridLayout.addComponent(realUserPanel);
    
    // userPanel
    userPanel = buildUserPanel();
    gridLayout.addComponent(userPanel);
    
    // buttonLayout
    buttonLayout = buildButtonLayout();
    mainLayout.addComponent(buttonLayout);
    
    return mainLayout;
  }
  
  private Component buildHeader() {
    HorizontalLayout header = new HorizontalLayout();
    header.addStyleName("viewheader");
    header.setSpacing(true);

    
    titleLabel = new Label(expenseSheet.getName());
    titleLabel.setSizeUndefined();
    titleLabel.addStyleName(ValoTheme.LABEL_H1);
    titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
    header.addComponent(titleLabel);

    HorizontalLayout tools = new HorizontalLayout(buildEditButton(), buildPasswordButton());
    tools.setSpacing(true);
    tools.addStyleName("toolbar");
    header.addComponent(tools);

    return header;
  }
  
  private Component buildEditButton() {
    Button result = new Button();
    result.setIcon(FontAwesome.EDIT);
    result.addStyleName("icon-edit");
    result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    result.setDescription(Msg.get("expenseSheet.changeName"));
    result.addClickListener(new ClickListener() {
      private static final long serialVersionUID = 1792451562271503948L;

      @Override
      public void buttonClick(final ClickEvent event) {
        UI.getCurrent().addWindow(new ExpenseSheetEditWindow(ExpenseSheetSettingsView.this, expenseSheet));
      }
    });
    return result;
  }
  
  private Component buildPasswordButton() {
    Button result = new Button();
    result.setIcon(FontAwesome.LOCK);
    result.addStyleName("icon-edit");
    result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    result.setDescription(Msg.get("expenseSheet.changePassword"));
    result.addClickListener(new ClickListener() {
      private static final long serialVersionUID = 1792451562271503948L;

      @Override
      public void buttonClick(final ClickEvent event) {
        UI.getCurrent().addWindow(new ExpenseSheetEditPasswordWindow(ExpenseSheetSettingsView.this));
      }
  });
    return result;
  }

  @AutoGenerated
  private Panel buildCategoryPanel() {
    // common part: create layout
    categoryPanel = new Panel();
    categoryPanel.setCaption(Msg.get("settingsPage.categoryLabel"));
    categoryPanel.setImmediate(false);
    categoryPanel.setWidth("100.0%");
    categoryPanel.setHeight("-1px");
    
    // categoryLayout
    categoryLayout = buildCategoryLayout();
    categoryPanel.setContent(categoryLayout);
    
    return categoryPanel;
  }

  @AutoGenerated
  private VerticalLayout buildCategoryLayout() {
    // common part: create layout
    categoryLayout = new VerticalLayout();
    categoryLayout.setImmediate(false);
    categoryLayout.setWidth("100.0%");
    categoryLayout.setHeight("-1px");
    categoryLayout.setMargin(true);
    
    // categoryGrid
    categoryGrid = new CategoryGrid(this);
    categoryGrid.setImmediate(false);
    categoryGrid.setWidth("100%");
    categoryGrid.setHeight("100%");
    categoryLayout.addComponent(categoryGrid);
    
    // categoryButtonLayout
    categoryButtonLayout = buildCategoryButtonLayout();
    categoryLayout.addComponent(categoryButtonLayout);
    
    return categoryLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildCategoryButtonLayout() {
    // common part: create layout
    categoryButtonLayout = new HorizontalLayout();
    categoryButtonLayout.setImmediate(false);
    categoryButtonLayout.setWidth("-1px");
    categoryButtonLayout.setHeight("-1px");
    categoryButtonLayout.setMargin(true);
    categoryButtonLayout.setSpacing(true);
    
    // addCategoryButton
    addCategoryButton = new Button();
    addCategoryButton.setIcon(FontAwesome.PLUS_SQUARE);
    addCategoryButton.setImmediate(true);
    addCategoryButton.setWidth("-1px");
    addCategoryButton.setHeight("-1px");
    categoryButtonLayout.addComponent(addCategoryButton);
    
    // moveUpCategoryButton
    moveUpCategoryButton = new Button();
    moveUpCategoryButton.setIcon(FontAwesome.ANGLE_UP);
    moveUpCategoryButton.setImmediate(true);
    moveUpCategoryButton.setWidth("-1px");
    moveUpCategoryButton.setHeight("-1px");
    categoryButtonLayout.addComponent(moveUpCategoryButton);
    
    // moveDownCategoryButton
    moveDownCategoryButton = new Button();
    moveDownCategoryButton.setIcon(FontAwesome.ANGLE_DOWN);
    moveDownCategoryButton.setImmediate(true);
    moveDownCategoryButton.setWidth("-1px");
    moveDownCategoryButton.setHeight("-1px");
    categoryButtonLayout.addComponent(moveDownCategoryButton);
    
    // deleteCategoryButton
    deleteCategoryButton = new Button();
    deleteCategoryButton.setIcon(FontAwesome.MINUS_SQUARE);
    deleteCategoryButton.setImmediate(true);
    deleteCategoryButton.setWidth("-1px");
    deleteCategoryButton.setHeight("-1px");
    categoryButtonLayout.addComponent(deleteCategoryButton);
    
    return categoryButtonLayout;
  }

  @AutoGenerated
  private Panel buildRealUserPanel() {
    // common part: create layout
    realUserPanel = new Panel();
    realUserPanel.setCaption(Msg.get("settingsPage.realUserLabel"));
    realUserPanel.setImmediate(false);
    realUserPanel.setWidth("100.0%");
    realUserPanel.setHeight("-1px");
    
    // realUserLayout
    realUserLayout = buildRealUserLayout();
    realUserPanel.setContent(realUserLayout);
    
    return realUserPanel;
  }

  @AutoGenerated
  private VerticalLayout buildRealUserLayout() {
    // common part: create layout
    realUserLayout = new VerticalLayout();
    realUserLayout.setImmediate(false);
    realUserLayout.setWidth("100.0%");
    realUserLayout.setHeight("-1px");
    realUserLayout.setMargin(true);
    
    // realUserGrid
    realUserGrid = new RealUserLimitGrid(this);
    realUserGrid.setImmediate(false);
    realUserGrid.setWidth("100%");
    realUserGrid.setHeight("100%");
    realUserLayout.addComponent(realUserGrid);
    
    // realUserButtonLayout
    realUserButtonLayout = buildRealUserButtonLayout();
    realUserLayout.addComponent(realUserButtonLayout);
    
    return realUserLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildRealUserButtonLayout() {
    // common part: create layout
    realUserButtonLayout = new HorizontalLayout();
    realUserButtonLayout.setImmediate(false);
    realUserButtonLayout.setWidth("-1px");
    realUserButtonLayout.setHeight("-1px");
    realUserButtonLayout.setMargin(true);
    realUserButtonLayout.setSpacing(true);
    
    // addRealUserButton
    addRealUserButton = new Button();
    addRealUserButton.setIcon(FontAwesome.PLUS_SQUARE);
    addRealUserButton.setImmediate(true);
    addRealUserButton.setWidth("-1px");
    addRealUserButton.setHeight("-1px");
    realUserButtonLayout.addComponent(addRealUserButton);
    
    // deleteRealUserButton
    deleteRealUserButton = new Button();
    deleteRealUserButton.setIcon(FontAwesome.MINUS_SQUARE);
    deleteRealUserButton.setImmediate(true);
    deleteRealUserButton.setWidth("-1px");
    deleteRealUserButton.setHeight("-1px");
    realUserButtonLayout.addComponent(deleteRealUserButton);
    
    return realUserButtonLayout;
  }

  @AutoGenerated
  private Panel buildUserPanel() {
    // common part: create layout
    userPanel = new Panel();
    userPanel.setCaption(Msg.get("settingsPage.userLabel"));
    userPanel.setImmediate(false);
    userPanel.setWidth("100.0%");
    userPanel.setHeight("-1px");
    
    // userLayout
    userLayout = buildUserLayout();
    userPanel.setContent(userLayout);
    
    return userPanel;
  }

  @AutoGenerated
  private VerticalLayout buildUserLayout() {
    // common part: create layout
    userLayout = new VerticalLayout();
    userLayout.setImmediate(false);
    userLayout.setWidth("100.0%");
    userLayout.setHeight("-1px");
    userLayout.setMargin(true);
    
    // userGrid
    userGrid = new UserLimitGrid(this);
    userGrid.setImmediate(false);
    userGrid.setWidth("100%");
    userGrid.setHeight("100%");
    userLayout.addComponent(userGrid);
    
    // userButtonLayout
    userButtonLayout = buildUserButtonLayout();
    userLayout.addComponent(userButtonLayout);
    
    return userLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildUserButtonLayout() {
    // common part: create layout
    userButtonLayout = new HorizontalLayout();
    userButtonLayout.setImmediate(false);
    userButtonLayout.setWidth("-1px");
    userButtonLayout.setHeight("-1px");
    userButtonLayout.setMargin(true);
    userButtonLayout.setSpacing(true);
    
    // addUserButton
    addUserButton = new Button();
    addUserButton.setIcon(FontAwesome.PLUS_SQUARE);
    addUserButton.setImmediate(true);
    addUserButton.setWidth("-1px");
    addUserButton.setHeight("-1px");
    userButtonLayout.addComponent(addUserButton);
    
    // deleteUserButton
    deleteUserButton = new Button();
    deleteUserButton.setIcon(FontAwesome.MINUS_SQUARE);
    deleteUserButton.setImmediate(true);
    deleteUserButton.setWidth("-1px");
    deleteUserButton.setHeight("-1px");
    userButtonLayout.addComponent(deleteUserButton);
    
    return userButtonLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildButtonLayout() {
    // common part: create layout
    buttonLayout = new HorizontalLayout();
    buttonLayout.setImmediate(false);
    buttonLayout.setWidth("-1px");
    buttonLayout.setHeight("-1px");
    buttonLayout.setMargin(true);
    buttonLayout.setSpacing(true);
    
    // delSheetButton
    delSheetButton = new Button();
    delSheetButton.setCaption(Msg.get("settingsPage.removeSheet"));
    delSheetButton.setIcon(FontAwesome.MINUS_SQUARE);
    delSheetButton.setImmediate(true);
    delSheetButton.setWidth("-1px");
    delSheetButton.setHeight("-1px");
    buttonLayout.addComponent(delSheetButton);
    
    return buttonLayout;
  }

  @Override
  public void expenseSheetSettingsChange() {
    mainLayout.removeAllComponents();
    prepareView();
  }

  @Override
  public void expenseSheetNameEdited(TextField nameField) {
    expenseSheet.setName(nameField.getValue());
    ExpenseSheetService.merge(expenseSheet);
    VaadinSession.getCurrent().getAttribute(ExpenseMenu.class).refresh();
    titleLabel.setValue(nameField.getValue());
  }

  @Override
  public void expenseSheetPasswordChanged(String newPassword) {
    ExpenseSheetService.decrypt(expenseSheet);
    expenseSheet.setKey(newPassword);
    ExpenseSheetService.encrypt(expenseSheet);
  }
  
  

}
