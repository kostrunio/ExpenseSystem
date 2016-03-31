package pl.kostro.expensesystem.view.design;

import org.vaadin.teemu.VaadinIcons;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.Msg;

@SuppressWarnings("serial")
public class DayDesign extends VerticalLayout {

  protected HorizontalLayout navigationLayout;
  protected Button previousDayButton;
  protected PopupDateField thisDateField;
  protected Button nextDayButton;

  protected HorizontalLayout workingLayout;
  protected VerticalLayout showLayout;
  protected GridLayout expensesGrid;
  protected Button backButton;
  protected VerticalLayout categoryLayout;
  
  public DayDesign() {
    setSizeFull();

    navigationLayout = buildNavigationLayout();
    addComponent(navigationLayout);
    setComponentAlignment(navigationLayout, new Alignment(20));

    workingLayout = new HorizontalLayout();

    showLayout = new VerticalLayout();

    // expensesGrid
    expensesGrid = new GridLayout();
    expensesGrid.setMargin(true);
    expensesGrid.setSpacing(true);
    showLayout.addComponent(expensesGrid);

    // backButton
    backButton = new Button();
    backButton.setCaption(Msg.get("day.back"));
    showLayout.addComponent(backButton);
    workingLayout.addComponent(showLayout);

    // categoryLayout
    categoryLayout = new VerticalLayout();
    workingLayout.addComponent(categoryLayout);
    addComponent(workingLayout);
  }

  private HorizontalLayout buildNavigationLayout() {
    navigationLayout = new HorizontalLayout();
    navigationLayout.setSpacing(true);

    previousDayButton = new Button();
    previousDayButton.setIcon(VaadinIcons.ARROW_LEFT);
    previousDayButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    previousDayButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    navigationLayout.addComponent(previousDayButton);
    navigationLayout.setComponentAlignment(previousDayButton, new Alignment(33));

    // thisDateField
    thisDateField = new PopupDateField();
    thisDateField.setDateFormat("dd-MM-yyyy");
    navigationLayout.addComponent(thisDateField);
    navigationLayout.setComponentAlignment(thisDateField, new Alignment(48));

    // nextDayButton
    nextDayButton = new Button();
    nextDayButton.setIcon(VaadinIcons.ARROW_RIGHT);
    nextDayButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    nextDayButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    navigationLayout.addComponent(nextDayButton);
    navigationLayout.setComponentAlignment(nextDayButton, new Alignment(34));

    return navigationLayout;
  }

}
