package pl.kostro.expensesystem.ui.views.chart;

import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class ChartDesign extends VerticalLayout {
  protected VerticalLayout chartLayout = new VerticalLayout();
  protected VerticalLayout chartLayout2 = new VerticalLayout();
  protected CheckBoxGroup<CategoryEntity> categoryCombo = new CheckBoxGroup<>();
  protected CheckBoxGroup<UserLimitEntity> userCombo = new CheckBoxGroup<>();

  public ChartDesign() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeFull();
    chartLayout.setSizeFull();
    chartLayout.setSpacing(true);
    chartLayout.setMargin(false);
    layout.addComponents(chartLayout, createSearchPanel());
    chartLayout2.setSizeFull();
    chartLayout2.setSpacing(true);
    chartLayout2.setMargin(false);
    addComponents(layout, chartLayout2);
  }

  private Component createSearchPanel() {
    Panel panel = new Panel();
    panel.setSizeUndefined();
    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeUndefined();
    layout.setSpacing(false);
    layout.setMargin(true);
    layout.addComponents(categoryCombo, createUserComboLayout());
    panel.setContent(layout);
    return panel;
  }

  private Component createUserComboLayout() {
    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setMargin(false);
    layout.addComponent(userCombo);
    return layout;
  }
}
