package pl.kostro.expensesystem.newui.main;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.newui.settingsPage.AddSheetWindow;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.util.HashMap;
import java.util.Map;

@Route("main")
@PageTitle("Main")
public class MainView extends MainDesign {

  private RealUserService rus;
  
  private Logger logger = LogManager.getLogger();
  private static final String VALO_MENU_SELECTED = "selected";
  private static final String VALO_MENU_VISIBLE = "valo-menu-visible";

  private Map<String, Button> viewButtons = new HashMap<String, Button>();

  private
  ComponentEventListener<ClickEvent<Button>> showMenuClick = event -> {
    if (menuPart.getClassName().contains(VALO_MENU_VISIBLE)) {
      menuPart.removeClassName(VALO_MENU_VISIBLE);
    } else {
      menuPart.addClassName(VALO_MENU_VISIBLE);
    }
  };
  private
  ComponentEventListener<ClickEvent<Button>> addSheetClick = event -> new AddSheetWindow().open();
  private
  ComponentEventListener<ClickEvent<Button>> accountClick = event -> UI.getCurrent().navigate("account");
  private
  ComponentEventListener<ClickEvent<Button>> logoutClick = event -> {
    VaadinSession.getCurrent().getSession().invalidate();
    UI.getCurrent().getPage().reload();
  };
/*  private ViewProvider errorProvider = new ViewProvider() {
    @Override
    public String getViewName(final String viewAndParameters) {
      return "expenseSheet";
    }

    @Override
    public View getView(final String viewName) {
      return new ExpenseView();
    }
  };*/


  public MainView(/*UI ui*/) {
    rus = AppCtxProvider.getBean(RealUserService.class);
    logger.info("create");
    setCaption();
    showMenuButton.addClickListener(showMenuClick);
    addSheetButton.addClickListener(addSheetClick);
    accountButton.addClickListener(accountClick);
    logoutButton.addClickListener(logoutClick);
    
    buildMenuItems();
    viewButtons.put(addSheetButton.getText(), addSheetButton);
    viewButtons.put(accountButton.getText(), accountButton);
    viewButtons.put(logoutButton.getText(), logoutButton);
    
//    Navigator navigator = new Navigator(ui, content);
//    navigator.addView("expenseSheet", new ExpenseView());
//    navigator.addView("account", new AccountView());
//    navigator.setErrorProvider(errorProvider);
//    ((ExpenseSystemUI) ui).setMainView(this);
  }

  private void setCaption() {
    title.setText(Msg.get("menu.logo"));
//    title.setContentMode(ContentMode.HTML);
    sheetLabel.setText(Msg.get("menu.sheets"));
    settingsLabel.setText(Msg.get("menu.settingsLabel"));
    addSheetButton.setText(Msg.get("menu.addSheet"));
    accountButton.setText(Msg.get("menu.account"));
    logoutButton.setText(Msg.get("menu.logout"));
  }

  public void refresh() {
    logger.info("refresh");
    buildMenuItems();
  }

  private void buildMenuItems() {
    sheetLayout.removeAll();
    viewButtons.clear();
    RealUserEntity loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    rus.fetchExpenseSheetList(loggedUser);
    for (final ExpenseSheetEntity esh : loggedUser.getExpenseSheetList())
      createButton("expenseSheet/" + esh.getId(), esh.getName(), esh.equals(loggedUser.getDefaultExpenseSheet()));
  }

  private void createButton(final String name, String caption, boolean defaultExpense) {
    Button button = new Button(caption, event -> UI.getCurrent().navigate(name));
    button.setClassName(ValoTheme.MENU_ITEM);
    if (defaultExpense)
      button.setIcon(VaadinIcon.HOME.create());
    else
      button.setIcon(VaadinIcon.FILE_O.create());
    sheetLayout.add(button);
    viewButtons.put(name, button);
  }

  public void setActiveView(String viewName) {
    for (Button button : viewButtons.values()) {
      button.removeClassName(VALO_MENU_SELECTED);
    }
    Button selected = viewButtons.get(viewName);
    if (selected != null) {
      selected.addClassName(VALO_MENU_SELECTED);
    }
    menuPart.removeClassName(VALO_MENU_VISIBLE);
  }

}
