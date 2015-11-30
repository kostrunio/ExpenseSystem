package pl.kostro.expensesystem.views;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class MainPage extends HorizontalLayout {

  private static final long serialVersionUID = 1511940780092921916L;
  
  public MainPage() {
    setSizeFull();
    addStyleName("mainview");
    
    ExpenseMenu expenseMenu = new ExpenseMenu();
    VaadinSession.getCurrent().setAttribute(ExpenseMenu.class, expenseMenu);
    
    addComponent(expenseMenu);

    ComponentContainer content = new CssLayout();
    content.setPrimaryStyleName("valo-content");
    content.setSizeFull();
    addComponent(content);
    setExpandRatio(content, 1.0f);
    
    new ExpenseNavigator(content);
  }
}
