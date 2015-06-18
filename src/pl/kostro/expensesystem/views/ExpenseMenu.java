package pl.kostro.expensesystem.views;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

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

  CssLayout menuItemsLayout = new CssLayout();

  RealUser loggedUser;

  public ExpenseMenu() {
    loggedUser = (RealUser) VaadinSession.getCurrent().getAttribute(RealUser.class.getName());

    setPrimaryStyleName("valo-menu");
    setSizeUndefined();

    setCompositionRoot(buildContent());
  }

  private Component buildContent() {
    final CssLayout menuLayout = new CssLayout();
    menuLayout.addStyleName("sidebar");
    menuLayout.addStyleName(ValoTheme.MENU_PART);
    menuLayout.addStyleName("no-vertical-drag-hints");
    menuLayout.addStyleName("no-horizontal-drag-hints");
    menuLayout.setWidth(null);
    menuLayout.setHeight("100%");

    menuLayout.addComponent(buildTitle());
    menuLayout.addComponent(buildMenuItems());
    return menuLayout;
  }

  private Component buildTitle() {
    Label logo = new Label("Arkusze wydatków", ContentMode.HTML);
    logo.setSizeUndefined();
    HorizontalLayout logoWrapper = new HorizontalLayout(logo);
    logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
    logoWrapper.addStyleName("valo-menu-title");
    return logoWrapper;
  }

  private Component buildMenuItems() {
    menuItemsLayout.addStyleName("valo-menuitems");
    menuItemsLayout.setHeight("100%");

    for (final ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
      final Button b = new Button(expenseSheet.getName());
      b.setStyleName("valo-menu-item");
      b.addClickListener(new ClickListener() {
        private static final long serialVersionUID = 6367146207004338123L;

        @Override
        public void buttonClick(final ClickEvent event) {
          UI.getCurrent().getNavigator().navigateTo(event.getButton().getCaption());
        }
      });

      menuItemsLayout.addComponent(b);
    }

    final Button settingButton = new Button("Ustawienia");
    settingButton.setStyleName("valo-menu-item");
    settingButton.addClickListener(new ClickListener() {
      
      private static final long serialVersionUID = -722003682240462618L;

      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().getNavigator().navigateTo("settings");
      }
    });
    menuItemsLayout.addComponent(settingButton);
    
    final Button logoutButton = new Button("Wyloguj");
    logoutButton.setStyleName("valo-menu-item");
    logoutButton.addClickListener(new ClickListener() {
      
      private static final long serialVersionUID = -1813471548646140303L;

      @Override
      public void buttonClick(ClickEvent event) {
        VaadinSession.getCurrent().setAttribute(RealUser.class.getName(), null);
        ((ExpenseSystemUI)getUI()).updateContent();
      }
    });
    menuItemsLayout.addComponent(logoutButton);
    
    return menuItemsLayout;

  }

}
