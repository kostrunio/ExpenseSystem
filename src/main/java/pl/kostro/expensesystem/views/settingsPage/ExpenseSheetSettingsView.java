package pl.kostro.expensesystem.views.settingsPage;

import java.text.MessageFormat;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.components.grid.CategoryGrid;
import pl.kostro.expensesystem.components.grid.RealUserLimitGrid;
import pl.kostro.expensesystem.components.grid.UserLimitGrid;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.service.RealUserService;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetEditPasswordWindow.ExpenseSheetPasswordChangeListener;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetEditWindow.ExpenseSheetEditListener;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ExpenseSheetSettingsView extends Panel implements ExpenseSheetEditListener, ExpenseSheetPasswordChangeListener {

  private VerticalLayout mainLayout;
  private Label titleLabel;

  private HorizontalLayout gridLayout;

  private Panel categoryPanel;
  private VerticalLayout categoryLayout;
  private CategoryGrid categoryGrid;
  private HorizontalLayout categoryButtonLayout;
  private Button addCategoryButton;
  private Button moveUpCategoryButton;
  private Button moveDownCategoryButton;
  private Button deleteCategoryButton;
  
  private Panel realUserPanel;
  private VerticalLayout realUserLayout;
  private RealUserLimitGrid realUserGrid;
  private HorizontalLayout realUserButtonLayout;
  private Button addRealUserButton;
  private Button deleteRealUserButton;
  
  private Panel userPanel;
  private VerticalLayout userLayout;
  private UserLimitGrid userGrid;
  private HorizontalLayout userButtonLayout;
  private Button addUserButton;
  private Button deleteUserButton;
  private ExpenseSheet expenseSheet;

  public ExpenseSheetSettingsView() {
    prepareView();
  }
  
  private void prepareView() {
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    setContent(buildMainLayout());

    disableAllButtons();
    
    if (expenseSheet != null) {
      categoryGrid.refreshValues();
      realUserGrid.refreshValues();
      userGrid.refreshValues();
    }
  }
  
  private void disableAllButtons() {
    moveUpCategoryButton.setEnabled(false);
    moveDownCategoryButton.setEnabled(false);
    deleteCategoryButton.setEnabled(false);
    deleteRealUserButton.setEnabled(false);
    deleteUserButton.setEnabled(false);
  }

  private VerticalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setWidth("100%");
    mainLayout.setHeightUndefined();
    
    if (expenseSheet != null)
      mainLayout.addComponent(buildHeader());
    
    gridLayout = new HorizontalLayout();
    gridLayout.setSizeFull();
    gridLayout.setSpacing(true);
    mainLayout.addComponent(gridLayout);
    
    // top-level component properties
    addStyleName(ValoTheme.PANEL_BORDERLESS);
    setSizeFull();
    
    // categoryPanel
    categoryPanel = buildCategoryPanel();
    gridLayout.addComponent(categoryPanel);
    
    // realUserPanel
    realUserPanel = buildRealUserPanel();
    gridLayout.addComponent(realUserPanel);
    
    // userPanel
    userPanel = buildUserPanel();
    gridLayout.addComponent(userPanel);
    
    return mainLayout;
  }
  
  private Component buildHeader() {
    HorizontalLayout header = new HorizontalLayout();
    header.setSpacing(true);

    titleLabel = new Label(expenseSheet.getName());
    titleLabel.setSizeUndefined();
    titleLabel.addStyleName(ValoTheme.LABEL_H1);
    titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
    header.addComponent(titleLabel);

    HorizontalLayout tools = new HorizontalLayout(buildEditButton(), buildPasswordButton(), buildDelButton());
    tools.setSpacing(true);
    header.addComponent(tools);

    return header;
  }
  
  private Component buildEditButton() {
    Button edit = new Button();
    edit.setIcon(FontAwesome.EDIT);
    edit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    edit.setDescription(Msg.get("settingsPage.changeName"));
    edit.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        UI.getCurrent().addWindow(new ExpenseSheetEditWindow(ExpenseSheetSettingsView.this, expenseSheet));
      }
    });
    return edit;
  }
  
  private Component buildPasswordButton() {
    Button password = new Button();
    password.setIcon(FontAwesome.LOCK);
    password.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    password.setDescription(Msg.get("settingsPage.changePassword"));
    password.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        UI.getCurrent().addWindow(new ExpenseSheetEditPasswordWindow(ExpenseSheetSettingsView.this));
      }
    });
    return password;
  }

  private Component buildDelButton() {
    Button delete = new Button();
    delete.setIcon(FontAwesome.TRASH_O);
    delete.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    delete.setDescription(Msg.get("settingsPage.removeSheet"));
    delete.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        ConfirmDialog.show(getUI(),
            Msg.get("settingsPage.removeSheetLabel"),
            MessageFormat.format(Msg.get("settingsPage.removeSheetQuestion"), expenseSheet.getName()),
            Msg.get("settingsPage.removeSheetYes"),
            Msg.get("settingsPage.removeSheetNo"),
            new ConfirmDialog.Listener() {
          @Override
          public void onClose(ConfirmDialog dialog) {
            if (dialog.isConfirmed()) {
              ExpenseSheetService.removeExpenseSheet(expenseSheet);
              RealUser loggedUser = VaadinSession.getCurrent().getAttribute(RealUser.class);
              RealUserService.refresh(loggedUser);
              if (loggedUser.getDefaultExpenseSheet() == null && loggedUser.getExpenseSheetList().size() != 0)
                RealUserService.setDefaultExpenseSheet(loggedUser, loggedUser.getExpenseSheetList().get(0));
              VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, null);
              ((ExpenseSystemUI)getUI()).getMainView().refresh();
              UI.getCurrent().getNavigator().navigateTo("");
            }
          }
        });
      }
    });
    return delete;
  }

  private Panel buildCategoryPanel() {
    // common part: create layout
    categoryPanel = new Panel(Msg.get("settingsPage.categoryLabel"));
    categoryPanel.setImmediate(false);
    categoryPanel.setWidth("100.0%");
    categoryPanel.setHeight("30%");
    
    // categoryLayout
    categoryLayout = buildCategoryLayout();
    categoryPanel.setContent(categoryLayout);
    
    return categoryPanel;
  }

  private VerticalLayout buildCategoryLayout() {
    // common part: create layout
    categoryLayout = new VerticalLayout();
    categoryLayout.setSizeFull();
    categoryLayout.setMargin(true);
    
    // categoryGrid
    categoryGrid = new CategoryGrid();
    categoryGrid.setSizeFull();
    categoryLayout.addComponent(categoryGrid);
    
    // categoryButtonLayout
    categoryButtonLayout = buildCategoryButtonLayout();
    categoryLayout.addComponent(categoryButtonLayout);
    
    return categoryLayout;
  }

  private HorizontalLayout buildCategoryButtonLayout() {
    // common part: create layout
    categoryButtonLayout = new HorizontalLayout();
    categoryButtonLayout.setMargin(true);
    categoryButtonLayout.setSpacing(true);
    
    // addCategoryButton
    addCategoryButton = new Button();
    addCategoryButton.setIcon(FontAwesome.PLUS_SQUARE);
    addCategoryButton.setImmediate(true);
    addCategoryButton.setSizeUndefined();
    categoryButtonLayout.addComponent(addCategoryButton);
    
    // moveUpCategoryButton
    moveUpCategoryButton = new Button();
    moveUpCategoryButton.setIcon(FontAwesome.ANGLE_UP);
    moveUpCategoryButton.setImmediate(true);
    categoryButtonLayout.addComponent(moveUpCategoryButton);
    
    // moveDownCategoryButton
    moveDownCategoryButton = new Button();
    moveDownCategoryButton.setIcon(FontAwesome.ANGLE_DOWN);
    moveDownCategoryButton.setImmediate(true);
    categoryButtonLayout.addComponent(moveDownCategoryButton);
    
    // deleteCategoryButton
    deleteCategoryButton = new Button();
    deleteCategoryButton.setIcon(FontAwesome.MINUS_SQUARE);
    deleteCategoryButton.setImmediate(true);
    categoryButtonLayout.addComponent(deleteCategoryButton);
    
    categoryGrid.setAddCategoryButton(addCategoryButton);
    categoryGrid.setMoveUpCategoryButton(moveUpCategoryButton);
    categoryGrid.setMoveDownCategoryButton(moveDownCategoryButton);
    categoryGrid.setDeleteCategoryButton(deleteCategoryButton);
    
    return categoryButtonLayout;
  }

  private Panel buildRealUserPanel() {
    // common part: create layout
    realUserPanel = new Panel(Msg.get("settingsPage.realUserLabel"));
    realUserPanel.setImmediate(false);
    realUserPanel.setWidth("100.0%");
    realUserPanel.setHeight("30%");
    
    // realUserLayout
    realUserLayout = buildRealUserLayout();
    realUserPanel.setContent(realUserLayout);
    
    return realUserPanel;
  }

  private VerticalLayout buildRealUserLayout() {
    // common part: create layout
    realUserLayout = new VerticalLayout();
    realUserLayout.setSizeFull();
    realUserLayout.setMargin(true);
    
    // realUserGrid
    realUserGrid = new RealUserLimitGrid();
    realUserGrid.setSizeFull();
    realUserLayout.addComponent(realUserGrid);
    
    // realUserButtonLayout
    realUserButtonLayout = buildRealUserButtonLayout();
    realUserLayout.addComponent(realUserButtonLayout);
    
    return realUserLayout;
  }

  private HorizontalLayout buildRealUserButtonLayout() {
    // common part: create layout
    realUserButtonLayout = new HorizontalLayout();
    realUserButtonLayout.setMargin(true);
    realUserButtonLayout.setSpacing(true);
    
    // addRealUserButton
    addRealUserButton = new Button();
    addRealUserButton.setIcon(FontAwesome.PLUS_SQUARE);
    addRealUserButton.setImmediate(true);
    realUserButtonLayout.addComponent(addRealUserButton);
    
    // deleteRealUserButton
    deleteRealUserButton = new Button();
    deleteRealUserButton.setIcon(FontAwesome.MINUS_SQUARE);
    deleteRealUserButton.setImmediate(true);
    realUserButtonLayout.addComponent(deleteRealUserButton);
    
    realUserGrid.setAddUserLimitButton(addRealUserButton);
    realUserGrid.setDeleteUserLimitButton(deleteRealUserButton);
    
    return realUserButtonLayout;
  }

  private Panel buildUserPanel() {
    // common part: create layout
    userPanel = new Panel(Msg.get("settingsPage.userLabel"));
    userPanel.setWidth("100.0%");
    userPanel.setHeight("30%");
    
    // userLayout
    userLayout = buildUserLayout();
    userPanel.setContent(userLayout);
    
    return userPanel;
  }

  private VerticalLayout buildUserLayout() {
    // common part: create layout
    userLayout = new VerticalLayout();
    userLayout.setSizeFull();
    userLayout.setMargin(true);
    
    // userGrid
    userGrid = new UserLimitGrid();
    userGrid.setSizeFull();
    userLayout.addComponent(userGrid);
    
    // userButtonLayout
    userButtonLayout = buildUserButtonLayout();
    userLayout.addComponent(userButtonLayout);
    
    return userLayout;
  }

  private HorizontalLayout buildUserButtonLayout() {
    // common part: create layout
    userButtonLayout = new HorizontalLayout();
    userButtonLayout.setMargin(true);
    userButtonLayout.setSpacing(true);
    
    // addUserButton
    addUserButton = new Button();
    addUserButton.setIcon(FontAwesome.PLUS_SQUARE);
    addUserButton.setImmediate(true);
    userButtonLayout.addComponent(addUserButton);
    
    // deleteUserButton
    deleteUserButton = new Button();
    deleteUserButton.setIcon(FontAwesome.MINUS_SQUARE);
    deleteUserButton.setImmediate(true);
    userButtonLayout.addComponent(deleteUserButton);
    
    userGrid.setAddUserLimitButton(addUserButton);
    userGrid.setDeleteUserLimitButton(deleteUserButton);
    
    return userButtonLayout;
  }

  @Override
  public void expenseSheetNameEdited(TextField nameField) {
    expenseSheet.setName(nameField.getValue());
    ExpenseSheetService.merge(expenseSheet);
    ((ExpenseSystemUI)getUI()).getMainView().refresh();
    titleLabel.setValue(nameField.getValue());
  }

  @Override
  public void expenseSheetPasswordChanged(String newPassword) {
    ExpenseSheetService.decrypt(expenseSheet);
    expenseSheet.setKey(newPassword);
    ExpenseSheetService.encrypt(expenseSheet);
  }

}
