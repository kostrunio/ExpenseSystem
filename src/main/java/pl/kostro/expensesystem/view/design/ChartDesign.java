package pl.kostro.expensesystem.view.design;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ChartDesign extends HorizontalLayout {

  protected VerticalLayout chartLayout;
  protected Panel searchPanel;
  protected VerticalLayout searchLayout;
  protected OptionGroup categoryCombo;
  protected OptionGroup userCombo;
  protected Button searchButton;

  public ChartDesign() {
    setWidth("100%");
    setHeight("200%");
    chartLayout = new VerticalLayout();
    chartLayout.setSizeFull();
    addComponent(chartLayout);
    Component search = buildSearchLayout();
    addComponent(search);
    setExpandRatio(chartLayout, 1);
  }

  private Component buildSearchLayout() {
    searchPanel = new Panel();
    searchPanel.setSizeUndefined();

    searchLayout = new VerticalLayout();
    searchLayout.setSizeUndefined();
    searchLayout.setMargin(true);
    searchLayout.setSpacing(true);
    searchPanel.setContent(searchLayout);

    categoryCombo = new OptionGroup();
    categoryCombo.setMultiSelect(true);
    searchLayout.addComponent(categoryCombo);

    userCombo = new OptionGroup();
    userCombo.setMultiSelect(true);
    searchLayout.addComponent(userCombo);

    searchButton = new Button();
    searchLayout.addComponent(searchButton);

    return searchPanel;
  }
}
