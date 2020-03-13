package pl.kostro.expensesystem.view.design;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import pl.kostro.expensesystem.components.grid.UserSummaryGrid;

public class UserSummaryDesign extends VerticalLayout {
  protected Panel actionPanel = new Panel();
  protected HorizontalLayout actionsLayout = new HorizontalLayout();
  protected ComboBox<pl.kostro.expensesystem.model.UserLimit> userBox = new ComboBox<>();
  protected HorizontalLayout workingLayout = new HorizontalLayout();
  protected UserSummaryGrid userSummaryGrid = new UserSummaryGrid();

  public UserSummaryDesign() {
    setSizeFull();
    setMargin(false);
    addComponents(createActionPanel(), createWorkingLayout());
  }

  private Component createActionPanel() {
    actionPanel.setSizeUndefined();
    actionsLayout.setWidth("100%");
    actionsLayout.setMargin(true);
    actionsLayout.addComponent(userBox);
    actionPanel.setContent(actionsLayout);
    return actionPanel;
  }

  private Component createWorkingLayout() {
    workingLayout.setSizeFull();
    workingLayout.addComponent(userSummaryGrid);
    return workingLayout;
  }
}
