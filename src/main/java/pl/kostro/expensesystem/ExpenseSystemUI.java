package pl.kostro.expensesystem;

import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

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

import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.view.LoginView;
import pl.kostro.expensesystem.view.MainView;

/**
 *
 */
@SuppressWarnings("serial")
@Title("Expense System Application")
@Theme("expensesystem")
@Widgetset("pl.kostro.expensesystem.ExpenseSystemWidgetset")
public class ExpenseSystemUI extends UI {

  private MainView mainView;

  @Override
  protected void init(VaadinRequest vaadinRequest) {
    Locale.setDefault(vaadinRequest.getLocale());
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
    Responsive.makeResponsive(this);
    updateContent();
  }

  public void updateContent() {
    RealUser user = VaadinSession.getCurrent().getAttribute(RealUser.class);
    if (user != null) {
      setContent(new MainView(this));
      getNavigator().navigateTo(getNavigator().getState());
    } else {
      setContent(new LoginView());
    }
  }

  public void updateContent(Component component) {
    setContent(component);
  }
  
  public void setMainView(MainView mainView) {
    this.mainView = mainView;
  }
  
  public MainView getMainView() {
    return mainView;
  }

  @WebServlet(urlPatterns = "/*", name = "ExpenseSystemUIServlet", asyncSupported = true)
  @VaadinServletConfiguration(ui = ExpenseSystemUI.class, productionMode = false)
  public static class ExpenseSystemUIServlet extends VaadinServlet {
    @Override
    protected final void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new ExpenseSystemSessionInitListener());
    }
  }
}
