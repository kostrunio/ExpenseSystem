package pl.kostro.expensesystem.views;

import pl.kostro.expensesystem.views.mainPage.ExpenseView;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

public class ExpenseNavigator extends Navigator {

  private static final long serialVersionUID = -4570240196212970040L;

  public ExpenseNavigator(ComponentContainer container) {
    super(UI.getCurrent(), container);

    addView("expenseSheet", new ExpenseView());
    
    setErrorProvider(new ViewProvider() {
      private static final long serialVersionUID = 8120463966489805867L;

      @Override
      public String getViewName(final String viewAndParameters) {
        return "expenseSheet";
      }

      @Override
      public View getView(final String viewName) {
        return new ExpenseView();
      }
    });
  }

}
