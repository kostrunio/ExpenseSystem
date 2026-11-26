package pl.kostro.expensesystem.ui.views.main;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class MainDesign extends HorizontalLayout {
  protected CssLayout menuPartLayout = new CssLayout();
  protected HorizontalLayout menuTitleLayout = new HorizontalLayout();
  protected Label titleLabel = new Label();
  protected Button showMenuButton = new Button();
  protected CssLayout menuItemsLayout = new CssLayout();
  protected Label sheetLabel = new Label();
  protected VerticalLayout sheetLayout = new VerticalLayout();
  protected Label settingsLabel = new Label();
  protected Button addSheetButton = new Button();
  protected Button accountButton = new Button();
  protected Button logoutButton = new Button();
  protected VerticalLayout contentLayout = new VerticalLayout();

  public MainDesign() {
    setStyleName("valo-menu-responsive");
    setSpacing(false);
    setResponsive(true);
    setSizeFull();
    contentLayout.setSizeFull();
    contentLayout.setSpacing(false);
    contentLayout.setMargin(false);
    addComponents(createCssLayout(), contentLayout);
    setExpandRatio(contentLayout, 1);
  }

  private Component createCssLayout() {
    CssLayout layout = new CssLayout();
    layout.setStyleName("valo-menu");
    layout.addComponent(createMenuPart());
    return layout;
  }

  private Component createMenuPart() {
    menuPartLayout.setStyleName("valo-menu-part");
    showMenuButton.setIcon(VaadinIcons.MENU);
    showMenuButton.setStyleName("primary small valo-menu-toggle");
    menuPartLayout.addComponents(createMenuTitleLayout(), showMenuButton, createMenuItemsLayout());
    return menuPartLayout;
  }

  private Component createMenuTitleLayout() {
    menuTitleLayout.setStyleName("valo-menu-title");
    menuTitleLayout.setWidth("100%");
    titleLabel.setContentMode(ContentMode.HTML);
    titleLabel.setValue("<strong>Expense</strong> Application");
    menuTitleLayout.addComponent(titleLabel);
    menuTitleLayout.setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);
    return menuTitleLayout;
  }

  private Component createMenuItemsLayout() {
    menuItemsLayout.setStyleName("valo-menuitems");
    sheetLabel.setStyleName("valo-menu-subtitle");
    sheetLayout.setSpacing(false);
    sheetLayout.setMargin(false);
    settingsLabel.setStyleName("valo-menu-subtitle");
    addSheetButton.setIcon(VaadinIcons.FILE_ADD);
    addSheetButton.setPrimaryStyleName("valo-menu-item");
    accountButton.setIcon(VaadinIcons.AUTOMATION);
    accountButton.setPrimaryStyleName("valo-menu-item");
    logoutButton.setIcon(VaadinIcons.SIGN_OUT);
    logoutButton.setPrimaryStyleName("valo-menu-item");
    menuItemsLayout.addComponents(sheetLabel, sheetLayout, settingsLabel, addSheetButton, accountButton, logoutButton);
    return menuItemsLayout;
  }
}
