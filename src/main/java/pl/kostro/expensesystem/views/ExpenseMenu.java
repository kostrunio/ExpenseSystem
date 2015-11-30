package pl.kostro.expensesystem.views;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class ExpenseMenu extends CustomComponent {

  private static final long serialVersionUID = -2718336134775274630L;

  private CssLayout menuItemsLayout = new CssLayout();

  public ExpenseMenu() {
    setPrimaryStyleName(ValoTheme.MENU_ROOT);
    setSizeUndefined();

    setCompositionRoot(buildMenu());
  }

  private Component buildMenu() {
    final CssLayout menuLayout = new CssLayout();
    menuLayout.addStyleName("sidebar");
    menuLayout.addStyleName(ValoTheme.MENU_PART);
    menuLayout.addStyleName("no-vertical-drag-hints");
    menuLayout.addStyleName("no-horizontal-drag-hints");
    menuLayout.setWidth(null);
    menuLayout.setHeight("100%");

    menuLayout.addComponent(buildTitle());
    menuLayout.addComponent(buildToggleButton());
    menuLayout.addComponent(buildMenuItems());
    return menuLayout;
  }

  private Component buildTitle() {
    Label logo = new Label(Msg.get("menu.logo"), ContentMode.HTML);
    logo.setSizeUndefined();
    HorizontalLayout logoWrapper = new HorizontalLayout(logo);
    logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
    logoWrapper.setStyleName(ValoTheme.MENU_TITLE);
    return logoWrapper;
  }

  private Component buildToggleButton() {
    Button valoMenuToggleButton = new Button(Msg.get("menu"), new ClickListener() {
      private static final long serialVersionUID = -2516134629337123428L;

      @Override
      public void buttonClick(final ClickEvent event) {
        if (getCompositionRoot().getStyleName().contains("menu-valo-visible")) {
          getCompositionRoot().removeStyleName("menu-valo-visible");
        } else {
          getCompositionRoot().addStyleName("menu-valo-visible");
        }
      }
    });
    valoMenuToggleButton.setIcon(FontAwesome.LIST);
    valoMenuToggleButton.addStyleName("valo-menu-toggle");
    valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
    return valoMenuToggleButton;
  }

  private Component buildMenuItems() {
    RealUser loggedUser = VaadinSession.getCurrent().getAttribute(RealUser.class);

    menuItemsLayout.setStyleName("valo-menuitems");

    Label sheetLabel = new Label(Msg.get("menu.sheets"));
    sheetLabel.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
    menuItemsLayout.addComponent(sheetLabel);

    for (final ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
      final Button b = new Button(expenseSheet.getName());
      b.setPrimaryStyleName(ValoTheme.MENU_ITEM);
      if (expenseSheet.equals(loggedUser.getDefaultExpenseSheet()))
        b.setIcon(FontAwesome.HOME);
      else
        b.setIcon(FontAwesome.FILE_O);
      b.addClickListener(new ClickListener() {
        private static final long serialVersionUID = 6367146207004338123L;

        @Override
        public void buttonClick(final ClickEvent event) {
          UI.getCurrent().getNavigator().navigateTo("expenseSheet/" + expenseSheet.getId());
        }
      });

      menuItemsLayout.addComponent(b);
    }

    Label settingsLabel = new Label(Msg.get("menu.settingsLabel"));
    settingsLabel.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
    menuItemsLayout.addComponent(settingsLabel);

    final Button settingButton = new Button(Msg.get("menu.settingsButton"));
    settingButton.setPrimaryStyleName(ValoTheme.MENU_ITEM);
    settingButton.setIcon(FontAwesome.FILES_O);
    settingButton.addClickListener(new ClickListener() {

      private static final long serialVersionUID = -722003682240462618L;

      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().getNavigator().navigateTo("settings");
      }
    });
    menuItemsLayout.addComponent(settingButton);

    final Button logoutButton = new Button(Msg.get("menu.logout"));
    logoutButton.setPrimaryStyleName(ValoTheme.MENU_ITEM);
    logoutButton.setIcon(FontAwesome.EJECT);
    logoutButton.addClickListener(new ClickListener() {

      private static final long serialVersionUID = -1813471548646140303L;

      @Override
      public void buttonClick(ClickEvent event) {
        VaadinSession.getCurrent().setAttribute(RealUser.class, null);
        VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, null);
        ExpenseEntityDao.close();
        ((ExpenseSystemUI) getUI()).updateContent();
      }
    });
    menuItemsLayout.addComponent(logoutButton);

    return menuItemsLayout;

  }

  public void refresh() {
    menuItemsLayout.removeAllComponents();
    buildMenuItems();
  }

}
