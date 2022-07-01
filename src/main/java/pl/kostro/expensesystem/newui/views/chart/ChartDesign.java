package pl.kostro.expensesystem.newui.views.chart;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class ChartDesign extends VerticalLayout {
  protected VerticalLayout chartLayout = new VerticalLayout();
  protected VerticalLayout chartLayout2 = new VerticalLayout();
  protected CheckboxGroup<CategoryEntity> categoryCombo = new CheckboxGroup<>();
  protected CheckboxGroup<UserLimitEntity> userCombo = new CheckboxGroup<>();

  public ChartDesign() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeFull();
    chartLayout.setSizeFull();
    chartLayout.setSpacing(true);
    chartLayout.setMargin(false);
    layout.add(chartLayout, createSearchPanel());
    chartLayout2.setSizeFull();
    chartLayout2.setSpacing(true);
    chartLayout2.setMargin(false);
    add(layout, chartLayout2);
  }

  private Component createSearchPanel() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeUndefined();
    layout.setSpacing(false);
    layout.setMargin(true);
    layout.add(categoryCombo, createUserComboLayout());
    return layout;
  }

  private Component createUserComboLayout() {
    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setMargin(false);
    layout.add(userCombo);
    return layout;
  }
}
