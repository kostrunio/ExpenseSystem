package pl.kostro.expensesystem.pages;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.views.mainPage.ExpenseView;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

public class ExpenseNavigator extends Navigator {

  private static final long serialVersionUID = -4570240196212970040L;

  RealUser loggedUser;

  public ExpenseNavigator(ComponentContainer container) {
    super(UI.getCurrent(), container);
    loggedUser = (RealUser) VaadinSession.getCurrent().getAttribute(RealUser.class.getName());

    initViewChangeListener();
    initViewProviders();

    for (final ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
      addView(expenseSheet.getName(), new ExpenseView(expenseSheet));
    }
    addView("settings", new SettingsPage());
  }

  private void initViewChangeListener() {
    addViewChangeListener(new ViewChangeListener() {

      private static final long serialVersionUID = -5886728517257511451L;

      @Override
      public boolean beforeViewChange(final ViewChangeEvent event) {
        // Since there's no conditions in switching between the views
        // we can always return true.
        return true;
      }

      @Override
      public void afterViewChange(final ViewChangeEvent event) {

      }
    });
  }

  private void initViewProviders() {
    // A dedicated view provider is added for each separate view type
    for (final ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
      addView(expenseSheet.getName(), new ExpenseView(expenseSheet));
    }

    setErrorProvider(new ViewProvider() {

      private static final long serialVersionUID = 8120463966489805867L;

      @Override
      public String getViewName(final String viewAndParameters) {
        return loggedUser.getDefaultExpenseSheet().getName();
      }

      @Override
      public View getView(final String viewName) {
        return new ExpenseView(loggedUser.getDefaultExpenseSheet());
      }
    });
  }

}
