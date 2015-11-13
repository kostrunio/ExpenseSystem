package pl.kostro.expensesystem;

import java.util.TimeZone;

import javax.servlet.annotation.WebServlet;

import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.views.LoginPage;
import pl.kostro.expensesystem.views.MainPage;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
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
@Widgetset("pl.kostro.expensesystem.widgetset.ExpensesystemWidgetset")
public class ExpenseSystemUI extends UI {

  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = ExpenseSystemUI.class)
  public static class Servlet extends VaadinServlet {
  }

  @Override
  protected void init(VaadinRequest request) {
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
    Responsive.makeResponsive(this);
    addStyleName(ValoTheme.UI_WITH_MENU);

    updateContent();
  }

  public void updateContent() {
    RealUser user = VaadinSession.getCurrent().getAttribute(RealUser.class);
    if (user != null) {
      setContent(new MainPage());
      getNavigator().navigateTo(getNavigator().getState());
    } else {
      setContent(new LoginPage());
    }
  }

  public void updateContent(Component component) {
    setContent(component);
  }
}