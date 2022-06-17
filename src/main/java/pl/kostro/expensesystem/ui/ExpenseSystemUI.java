package pl.kostro.expensesystem.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.ui.event.ExpenseSystemEventBus;
import pl.kostro.expensesystem.ui.views.main.MainView;

import java.util.Locale;
import java.util.TimeZone;

public class ExpenseSystemUI extends UI {

  private static Logger logger = LogManager.getLogger();
  private final ExpenseSystemEventBus expenseEventbus = new ExpenseSystemEventBus();
  private MainView mainView;

  @Override
  protected void init(VaadinRequest vaadinRequest) {
    logger.info("New session start: {}", vaadinRequest.getLocale());
    Locale.setDefault(vaadinRequest.getLocale());
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
    /*Responsive.makeResponsive(this);
    updateContent();

    Page.getCurrent().addBrowserWindowResizeListener(
            (BrowserWindowResizeListener) event -> {
              logger.debug("new width: " + event.getWidth());
              ExpenseSystemEventBus.post(new BrowserResizeEvent());
            });*/
  }

  public void updateContent() {
    /*RealUserEntity user = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    if (user != null) {
      setContent(new MainView(this));
      getNavigator().navigateTo(getNavigator().getState());
    } else {
      setContent(new LoginView());
    }*/
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
