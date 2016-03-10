package pl.kostro.expensesystem.view;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.views.settingsPage.AddSheetWindow;

/**
 * Responsive navigation menu presenting a list of available views to the user.
 */
@SuppressWarnings("serial")
public class MenuView extends CssLayout {

  private static final String VALO_MENUITEMS = "valo-menuitems";
  private static final String VALO_MENU_TOGGLE = "valo-menu-toggle";
  private static final String VALO_MENU_VISIBLE = "valo-menu-visible";
  private Navigator navigator;
  private Map<String, Button> viewButtons = new HashMap<String, Button>();

  private CssLayout menuItemsLayout;
  private CssLayout menuPart;

  public MenuView(Navigator navigator) {
    this.navigator = navigator;
    setPrimaryStyleName(ValoTheme.MENU_ROOT);
    menuPart = new CssLayout();
    menuPart.addStyleName(ValoTheme.MENU_PART);
    addComponent(menuPart);

    // header of the menu
    final HorizontalLayout top = buildTopLayout();
    menuPart.addComponent(top);

    // logout menu item
    menuPart.addComponent(buildLogoutMenu());

    // button for toggling the visibility of the menu when on a small screen
    menuPart.addComponent(buildShowMenu());

    // container for the navigation buttons, which are added by addView()
    menuItemsLayout = new CssLayout();
    buildMenuItems();
    menuPart.addComponent(menuItemsLayout);
  }

  private HorizontalLayout buildTopLayout() {
    HorizontalLayout top = new HorizontalLayout();
    top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
    top.addStyleName(ValoTheme.MENU_TITLE);
    top.setSpacing(true);
    Label title = new Label(Msg.get("menu.logo"), ContentMode.HTML);
    title.addStyleName(ValoTheme.LABEL_H3);
    title.setSizeUndefined();
    top.addComponent(title);
    return top;
  }

  private Component buildLogoutMenu() {
    MenuBar logoutMenu = new MenuBar();
    logoutMenu.addStyleName("user-menu");
    logoutMenu.addItem(Msg.get("menu.logout"), FontAwesome.EJECT, new Command() {
      @Override
      public void menuSelected(MenuItem selectedItem) {
        ExpenseEntityDao.close();
        VaadinSession.getCurrent().getSession().invalidate();
        Page.getCurrent().reload();
      }
    });
    return logoutMenu;
  }

  private Component buildShowMenu() {
    final Button showMenu = new Button("Menu", new ClickListener() {
      @Override
      public void buttonClick(final ClickEvent event) {
        if (menuPart.getStyleName().contains(VALO_MENU_VISIBLE)) {
          menuPart.removeStyleName(VALO_MENU_VISIBLE);
        } else {
          menuPart.addStyleName(VALO_MENU_VISIBLE);
        }
      }
    });
    showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
    showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
    showMenu.addStyleName(VALO_MENU_TOGGLE);
    showMenu.setIcon(FontAwesome.NAVICON);
    return showMenu;
  }

  private Component buildMenuItems() {
    RealUser loggedUser = VaadinSession.getCurrent().getAttribute(RealUser.class);

    menuItemsLayout.setPrimaryStyleName(VALO_MENUITEMS);

    Label sheetLabel = new Label(Msg.get("menu.sheets"));
    sheetLabel.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
    menuItemsLayout.addComponent(sheetLabel);

    for (final ExpenseSheet esh : loggedUser.getExpenseSheetList())
      createViewButton("expenseSheet/"+esh.getId(), esh.getName(), esh.equals(loggedUser.getDefaultExpenseSheet()));

    Label settingsLabel = new Label(Msg.get("menu.settingsLabel"));
    settingsLabel.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
    menuItemsLayout.addComponent(settingsLabel);

    final Button addSheetButton = new Button(Msg.get("menu.addSheet"));
    addSheetButton.setPrimaryStyleName(ValoTheme.MENU_ITEM);
    addSheetButton.setIcon(FontAwesome.PLUS_SQUARE);
    addSheetButton.addClickListener(new Button.ClickListener() {

      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().addWindow(new AddSheetWindow());
      }
    });
    menuItemsLayout.addComponent(addSheetButton);

    return menuItemsLayout;

  }

  
  public void refresh() {
    menuItemsLayout.removeAllComponents();
    buildMenuItems();
  }

  private void createViewButton(final String name, String caption, boolean defaultExpense) {
    Button button = new Button(caption, new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        navigator.navigateTo(name);

      }
    });
    button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
    if (defaultExpense)
      button.setIcon(FontAwesome.HOME);
    else
      button.setIcon(FontAwesome.FILE_O);
    menuItemsLayout.addComponent(button);
    viewButtons.put(name, button);
  }

  /**
   * Highlights a view navigation button as the currently active view in the
   * menu. This method does not perform the actual navigation.
   *
   * @param viewName
   *          the name of the view to show as active
   */
  public void setActiveView(String viewName) {
    for (Button button : viewButtons.values()) {
      button.removeStyleName("selected");
    }
    Button selected = viewButtons.get(viewName);
    if (selected != null) {
      selected.addStyleName("selected");
    }
    menuPart.removeStyleName(VALO_MENU_VISIBLE);
  }

}
