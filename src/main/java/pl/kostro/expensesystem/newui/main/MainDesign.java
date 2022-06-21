package pl.kostro.expensesystem.newui.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
public class MainDesign extends AppLayout {
  protected FlexLayout menuPart = new FlexLayout();
  protected HorizontalLayout menuTitle = new HorizontalLayout();
  protected Label title = new Label();
  protected Button showMenuButton = new Button();
  protected FlexLayout menuItems = new FlexLayout();
  protected Span sheetLabel = new Span();
  protected VerticalLayout sheetLayout = new VerticalLayout();
  protected Span settingsLabel = new Span();
  protected Button addSheetButton = new Button();
  protected Button accountButton = new Button();
  protected Button logoutButton = new Button();
  protected VerticalLayout content = new VerticalLayout();

  public MainDesign() {
//    setStyleName("valo-menu-responsive");
//    setSpacing(false);
//    setResponsive(true);
//    setSizeFull();
//    content.setSpacing(false);
//    content.setSizeFull();
//    content.setMargin(false);
//    add(createCssLayout(), content);
//    setFlexGrow(1, content);

    createDrawer();
    addToNavbar(createMenuTitle());
  }

/*  private Component createCssLayout() {
    FlexLayout layout = new FlexLayout();
    layout.setClassName("valo-menu");
    layout.add(createMenuPart());
    return layout;
  }*/

/*  private Component createMenuPart() {
    menuPart.setClassName("valo-menu-part");
    showMenuButton.setIcon(VaadinIcon.MENU.create());
    showMenuButton.setClassName("primary small valo-menu-toggle");
    menuPart.add(createMenuTitle(), showMenuButton, createMenuItems());
    return menuPart;
  }*/

  private void createDrawer() {
    final DrawerToggle drawerToggle = new DrawerToggle();
    drawerToggle.addClassName("menu-toggle");
    addToNavbar(drawerToggle);

    addSheetButton = createMenuButton(addSheetButton, VaadinIcon.FILE_ADD.create());
    accountButton = createMenuButton(accountButton, VaadinIcon.AUTOMATION.create());
    logoutButton = createMenuButton(logoutButton, VaadinIcon.SIGN_OUT.create());
    VerticalLayout buttonLayout = new VerticalLayout();
    buttonLayout.setSpacing(false);
    buttonLayout.setMargin(false);
    buttonLayout.add(addSheetButton, accountButton, logoutButton);
    addToDrawer(new VerticalLayout(sheetLabel, sheetLayout, settingsLabel, buttonLayout));
  }

  private Component createMenuTitle() {
    menuTitle.setClassName("menu-header");/*was valo-menu-title*/
    menuTitle.setWidthFull();
//    title.setContentMode(ContentMode.HTML);
//    title.setText("Expense Application");
    menuTitle.add(title);
    menuTitle.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    return menuTitle;
  }

  protected Button createMenuButton(Button routerButton, Icon icon) {
    routerButton.setClassName("menu-button");
    routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
    routerButton.setIcon(icon);
    icon.setSize("24px");
    return routerButton;
  }
}
