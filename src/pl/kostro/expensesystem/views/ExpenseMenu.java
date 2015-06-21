package pl.kostro.expensesystem.views;

import pl.kostro.expensesystem.ExpenseSystemUI;
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

  CssLayout menuItemsLayout = new CssLayout();

  RealUser loggedUser;

  public ExpenseMenu() {
    loggedUser = (RealUser) VaadinSession.getCurrent().getAttribute(RealUser.class.getName());

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
    menuLayout.addComponent(buildMenuItems());
    return menuLayout;
  }

  private Component buildTitle() {
    Label logo = new Label("<strong>Expense Application</strong>", ContentMode.HTML);
    logo.setSizeUndefined();
    HorizontalLayout logoWrapper = new HorizontalLayout(logo);
    logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
    logoWrapper.setStyleName(ValoTheme.MENU_TITLE);
    return logoWrapper;
  }

  private Component buildMenuItems() {
    menuItemsLayout.setHeight("100%");
    menuItemsLayout.setStyleName("valo-menuitems");
    
    Label sheetLabel = new Label("Zdefiniowane arkusze", ContentMode.HTML);
    sheetLabel.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
    sheetLabel.addStyleName(ValoTheme.LABEL_H4);
    sheetLabel.setHeight("20px");
    menuItemsLayout.addComponent(sheetLabel);

    for (final ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
      final Button b = new Button(expenseSheet.getName());
      b.addStyleName(ValoTheme.MENU_ITEM);
      b.addStyleName(ValoTheme.BUTTON_BORDERLESS);
      if (expenseSheet.equals(loggedUser.getDefaultExpenseSheet()))
          b.setIcon(FontAwesome.HOME);
      else
        b.setIcon(FontAwesome.FILE_O);
      b.addClickListener(new ClickListener() {
        private static final long serialVersionUID = 6367146207004338123L;

        @Override
        public void buttonClick(final ClickEvent event) {
          UI.getCurrent().getNavigator().navigateTo(event.getButton().getCaption());
        }
      });

      menuItemsLayout.addComponent(b);
    }
    
    Label settingsLabel = new Label("Zarz¹dzanie", ContentMode.HTML);
    settingsLabel.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
    settingsLabel.addStyleName(ValoTheme.LABEL_H4);
    settingsLabel.setHeight("20px");
    menuItemsLayout.addComponent(settingsLabel);

    final Button settingButton = new Button("Ustawienia");
    settingButton.addStyleName(ValoTheme.MENU_ITEM);
    settingButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    settingButton.setIcon(FontAwesome.FILES_O);
    settingButton.addClickListener(new ClickListener() {
      
      private static final long serialVersionUID = -722003682240462618L;

      @Override
      public void buttonClick(ClickEvent event) {
        UI.getCurrent().getNavigator().navigateTo("settings");
      }
    });
    menuItemsLayout.addComponent(settingButton);
    
    final Button logoutButton = new Button("Wyloguj");
    logoutButton.addStyleName(ValoTheme.MENU_ITEM);
    logoutButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    logoutButton.setIcon(FontAwesome.EJECT);
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
