package pl.kostro.expensesystem.view.design;

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
  protected CssLayout menuPart = new CssLayout();
  protected HorizontalLayout menuTitle = new HorizontalLayout();
  protected Label title = new Label();
  protected Button showMenuButton = new Button();
  protected CssLayout menuItems = new CssLayout();
  protected Label sheetLabel = new Label();
  protected VerticalLayout sheetLayout = new VerticalLayout();
  protected Label settingsLabel = new Label();
  protected Button addSheetButton = new Button();
  protected Button accountButton = new Button();
  protected Button logoutButton = new Button();
  protected VerticalLayout content = new VerticalLayout();

  public MainDesign() {
    setStyleName("valo-menu-responsive");
    setSpacing(false);
    setResponsive(true);
    setSizeFull();
    content.setSpacing(false);
    content.setSizeFull();
    content.setMargin(false);
    addComponents(createCssLayout(), content);
    setExpandRatio(content, 1);
  }

  private Component createCssLayout() {
    CssLayout layout = new CssLayout();
    layout.setStyleName("valo-menu");
    layout.addComponent(createMenuPart());
    return layout;
  }

  private Component createMenuPart() {
    menuPart.setStyleName("valo-menu-part");
    showMenuButton.setIcon(VaadinIcons.MENU);
    showMenuButton.setStyleName("primary small valo-menu-toggle");
    menuPart.addComponents(createMenuTitle(), showMenuButton, createMenuItems());
    return menuPart;
  }

  private Component createMenuTitle() {
    menuTitle.setStyleName("valo-menu-title");
    menuTitle.setWidth("100%");
    title.setContentMode(ContentMode.HTML);
    title.setValue("<strong>Expense</strong> Application");
    menuTitle.addComponent(title);
    menuTitle.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
    return menuTitle;
  }

  private Component createMenuItems() {
    menuItems.setStyleName("valo-menuitems");
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
    menuItems.addComponents(sheetLabel, sheetLayout, settingsLabel, addSheetButton, accountButton, logoutButton);
    return menuItems;
  }
}
