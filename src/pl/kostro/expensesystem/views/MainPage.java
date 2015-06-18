package pl.kostro.expensesystem.views;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class MainPage extends HorizontalLayout {

  private static final long serialVersionUID = 1511940780092921916L;
  
  public MainPage() {
    setSizeFull();

    addComponent(new ExpenseMenu());

    ComponentContainer content = new CssLayout();
    content.setSizeFull();
    addComponent(content);
    setExpandRatio(content, 1.0f);
    
    new ExpenseNavigator(content);
  }
}
