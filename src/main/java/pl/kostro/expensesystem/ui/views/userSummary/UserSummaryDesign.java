package pl.kostro.expensesystem.ui.views.userSummary;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import pl.kostro.expensesystem.ui.components.grid.UserSummaryGrid;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class UserSummaryDesign extends VerticalLayout {
  protected Panel actionPanel = new Panel();
  protected HorizontalLayout actionsLayout = new HorizontalLayout();
  protected ComboBox<UserLimitEntity> userBox = new ComboBox<>();
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
