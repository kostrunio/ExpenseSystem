package pl.kostro.expensesystem.ui.views.main;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.ui.ExpenseSystemUI;
import pl.kostro.expensesystem.ui.views.account.AccountView;
import pl.kostro.expensesystem.ui.views.expense.ExpenseView;
import pl.kostro.expensesystem.ui.views.settingsPage.AddSheetWindow;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MainView extends MainDesign {

  private final RealUserService rus;
  
  private static final String VALO_MENU_SELECTED = "selected";
  private static final String VALO_MENU_VISIBLE = "valo-menu-visible";

  private final Map<String, Button> viewButtons = new HashMap<>();

  private final ClickListener showMenuClick = event -> {
    if (menuPart.getStyleName().contains(VALO_MENU_VISIBLE)) {
      menuPart.removeStyleName(VALO_MENU_VISIBLE);
    } else {
      menuPart.addStyleName(VALO_MENU_VISIBLE);
    }
  };
  private final ClickListener addSheetClick = event -> UI.getCurrent().addWindow(new AddSheetWindow());
  private final ClickListener accountClick = event -> UI.getCurrent().getNavigator().navigateTo("account");
  private final ClickListener logoutClick = event -> {
    VaadinSession.getCurrent().getSession().invalidate();
    Page.getCurrent().reload();
  };
  private final ViewProvider errorProvider = new ViewProvider() {
    @Override
    public String getViewName(final String viewAndParameters) {
      return "expenseSheet";
    }

    @Override
    public View getView(final String viewName) {
      return new ExpenseView();
    }
  };


  public MainView(UI ui) {
    rus = AppCtxProvider.getBean(RealUserService.class);
    log.info("create");
    setCaption();
    showMenuButton.addClickListener(showMenuClick);
    addSheetButton.addClickListener(addSheetClick);
    accountButton.addClickListener(accountClick);
    logoutButton.addClickListener(logoutClick);
    
    buildMenuItems();
    viewButtons.put(addSheetButton.getCaption(), addSheetButton);
    viewButtons.put(accountButton.getCaption(), accountButton);
    viewButtons.put(logoutButton.getCaption(), logoutButton);
    
    Navigator navigator = new Navigator(ui, content);
    navigator.addView("expenseSheet", new ExpenseView());
    navigator.addView("account", new AccountView());
    navigator.setErrorProvider(errorProvider);
    ((ExpenseSystemUI) ui).setMainView(this);
  }

  private void setCaption() {
    title.setValue(Msg.get("menu.logo"));
    title.setContentMode(ContentMode.HTML);
    sheetLabel.setValue(Msg.get("menu.sheets"));
    settingsLabel.setValue(Msg.get("menu.settingsLabel"));
    addSheetButton.setCaption(Msg.get("menu.addSheet"));
    accountButton.setCaption(Msg.get("menu.account"));
    logoutButton.setCaption(Msg.get("menu.logout"));
  }

  public void refresh() {
    log.info("refresh");
    buildMenuItems();
  }

  private void buildMenuItems() {
    sheetLayout.removeAllComponents();
    viewButtons.clear();
    RealUserEntity loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    rus.fetchExpenseSheetList(loggedUser);
    for (final ExpenseSheetEntity esh : loggedUser.getExpenseSheetList())
      createButton("expenseSheet/" + esh.getId(), esh.getName(), esh.equals(loggedUser.getDefaultExpenseSheet()));
  }

  private void createButton(final String name, String caption, boolean defaultExpense) {
    Button button = new Button(caption, event -> getUI().getNavigator().navigateTo(name));
    button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
    if (defaultExpense)
      button.setIcon(VaadinIcons.HOME);
    else
      button.setIcon(VaadinIcons.FILE_O);
    sheetLayout.addComponent(button);
    viewButtons.put(name, button);
  }

  public void setActiveView(String viewName) {
    for (Button button : viewButtons.values()) {
      button.removeStyleName(VALO_MENU_SELECTED);
    }
    Button selected = viewButtons.get(viewName);
    if (selected != null) {
      selected.addStyleName(VALO_MENU_SELECTED);
    }
    menuPart.removeStyleName(VALO_MENU_VISIBLE);
  }

}
