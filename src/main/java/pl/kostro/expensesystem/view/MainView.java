package pl.kostro.expensesystem.view;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.view.design.MainDesign;
import pl.kostro.expensesystem.views.settingsPage.AddSheetWindow;

@SuppressWarnings("serial")
public class MainView extends MainDesign {

  private static final String VALO_MENU_SELECTED = "selected";
  private static final String VALO_MENU_VISIBLE = "valo-menu-visible";

  private Map<String, Button> viewButtons = new HashMap<String, Button>();

  private ClickListener showMenuClick = new ClickListener() {
    @Override
    public void buttonClick(final ClickEvent event) {
      if (menuPart.getStyleName().contains(VALO_MENU_VISIBLE)) {
        menuPart.removeStyleName(VALO_MENU_VISIBLE);
      } else {
        menuPart.addStyleName(VALO_MENU_VISIBLE);
      }
    }
  };
  private ClickListener addSheetClick = new ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      UI.getCurrent().addWindow(new AddSheetWindow());
    }
  };
  private ClickListener accountClick = new ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      UI.getCurrent().getNavigator().navigateTo("account");
    }
  };
  private ClickListener logoutClick = new ClickListener() {
    @Override
    public void buttonClick(ClickEvent event) {
      ExpenseEntityDao.close();
      VaadinSession.getCurrent().getSession().invalidate();
      Page.getCurrent().reload();
    }

  };
  private ViewProvider errorProvider = new ViewProvider() {
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
    buildMenuItems();
  }

  private void buildMenuItems() {
    sheetLayout.removeAllComponents();
    viewButtons.clear();
    RealUser loggedUser = VaadinSession.getCurrent().getAttribute(RealUser.class);
    for (final ExpenseSheet esh : loggedUser.getExpenseSheetList())
      createButton("expenseSheet/" + esh.getId(), esh.getName(), esh.equals(loggedUser.getDefaultExpenseSheet()));
  }

  private void createButton(final String name, String caption, boolean defaultExpense) {
    Button button = new Button(caption, new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        getUI().getNavigator().navigateTo(name);
      }
    });
    button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
    if (defaultExpense)
      button.setIcon(FontAwesome.HOME);
    else
      button.setIcon(FontAwesome.FILE_O);
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
