package pl.kostro.expensesystem.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.ui.event.ExpenseSystemEvent.BrowserResizeEvent;
import pl.kostro.expensesystem.ui.event.ExpenseSystemEventBus;
import pl.kostro.expensesystem.ui.view.LoginView;
import pl.kostro.expensesystem.ui.view.MainView;

import java.util.Locale;
import java.util.TimeZone;

@SpringUI
@Title("Expense System Application")
@Theme("expensesystem")
@Widgetset("pl.kostro.expensesystem.ExpenseSystemWidgetset")
public class ExpenseSystemUI extends UI {

  private static Logger logger = LogManager.getLogger();
  private final ExpenseSystemEventBus expenseEventbus = new ExpenseSystemEventBus();
  private MainView mainView;

  @Override
  protected void init(VaadinRequest vaadinRequest) {
    logger.info("New session start: {}", vaadinRequest.getLocale());
    Locale.setDefault(vaadinRequest.getLocale());
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
    Responsive.makeResponsive(this);
    updateContent();
    
    Page.getCurrent().addBrowserWindowResizeListener(
            (BrowserWindowResizeListener) event -> {
              logger.debug("new width: " + event.getWidth());
              ExpenseSystemEventBus.post(new BrowserResizeEvent());
            });
  }

  public void updateContent() {
    RealUserEntity user = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    if (user != null) {
      setContent(new MainView(this));
      getNavigator().navigateTo(getNavigator().getState());
    } else {
      setContent(new LoginView());
    }
  }

  public static ExpenseSystemEventBus getExpenseEventbus() {
    return ((ExpenseSystemUI) getCurrent()).expenseEventbus;
  }

  public void setMainView(MainView mainView) {
    this.mainView = mainView;
  }

  public MainView getMainView() {
    return mainView;
  }
}
