package pl.kostro.expensesystem;

import javax.servlet.annotation.WebServlet;

import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.pages.LoginPage;
import pl.kostro.expensesystem.pages.MainPage;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Title("Expense System Application")
@Theme("expensesystem")
public class ExpenseSystemUI extends UI {

  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = ExpenseSystemUI.class)
  public static class Servlet extends VaadinServlet {
  }

  @Override
  protected void init(VaadinRequest request) {
    Responsive.makeResponsive(this);
    addStyleName(ValoTheme.UI_WITH_MENU);

    updateContent();
  }

  public void updateContent() {
    RealUser user = (RealUser) VaadinSession.getCurrent().getAttribute(RealUser.class.getName());
    if (user != null) {
      // Authenticated user
      setContent(new MainPage());
      removeStyleName("loginview");
      getNavigator().navigateTo(getNavigator().getState());
    } else {
      setContent(new LoginPage());
      addStyleName("loginview");
    }
  }

  public void updateContent(Component component) {
    setContent(component);
  }
}