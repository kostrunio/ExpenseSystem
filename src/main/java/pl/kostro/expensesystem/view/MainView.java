package pl.kostro.expensesystem.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.views.mainPage.ExpenseView;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {
  private MenuView menu;

  public MainView(ExpenseSystemUI ui) {
    setStyleName("main-screen");

    CssLayout viewContainer = new CssLayout();
    viewContainer.addStyleName("valo-content");
    viewContainer.setSizeFull();

    final Navigator navigator = new Navigator(ui, viewContainer);
    navigator.addView("expenseSheet", new ExpenseView());
    navigator.setErrorProvider(new ViewProvider() {
      @Override
      public String getViewName(final String viewAndParameters) {
        return "expenseSheet";
      }

      @Override
      public View getView(final String viewName) {
        return new ExpenseView();
      }
    });
    menu = new MenuView(navigator);
    VaadinSession.getCurrent().setAttribute(MenuView.class, menu);

    addComponent(menu);

    addComponent(viewContainer);
    setExpandRatio(viewContainer, 1);
    setSizeFull();
  }
}
