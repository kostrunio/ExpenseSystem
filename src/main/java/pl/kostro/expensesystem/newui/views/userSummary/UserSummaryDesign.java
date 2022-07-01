package pl.kostro.expensesystem.newui.views.userSummary;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.newui.components.grid.UserSummaryGrid;

public class UserSummaryDesign extends VerticalLayout {
  protected Div actionPanel = new Div();
  protected HorizontalLayout actionsLayout = new HorizontalLayout();
  protected ComboBox<UserLimitEntity> userBox = new ComboBox<>();
  protected HorizontalLayout workingLayout = new HorizontalLayout();
  protected UserSummaryGrid userSummaryGrid = new UserSummaryGrid();

  public UserSummaryDesign() {
    setSizeFull();
    setMargin(false);
    add(createActionPanel(), createWorkingLayout());
  }

  private Component createActionPanel() {
    actionPanel.setSizeUndefined();
    actionsLayout.setWidth("100%");
    actionsLayout.setMargin(true);
    actionsLayout.add(userBox);
    actionPanel.add(actionsLayout);
    return actionPanel;
  }

  private Component createWorkingLayout() {
    workingLayout.setSizeFull();
    workingLayout.add(userSummaryGrid);
    return workingLayout;
  }
}
