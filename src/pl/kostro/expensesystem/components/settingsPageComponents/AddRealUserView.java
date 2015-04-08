package pl.kostro.expensesystem.components.settingsPageComponents;

import java.math.BigDecimal;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.service.RealUserService;
import pl.kostro.expensesystem.service.UserLimitService;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AddRealUserView extends CustomComponent {

  private static final long serialVersionUID = -3905483031176400524L;

  /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

  @AutoGenerated
  private VerticalLayout mainLayout;
  @AutoGenerated
  private Button saveButton;
  @AutoGenerated
  private GridLayout newRealUserGrid;
  @AutoGenerated
  private TextField newRealUser;
  @AutoGenerated
  private Label newRealUserLabel;

  /**
   * The constructor should first build the main layout, set the composition
   * root and then do any custom initialization.
   * 
   * The constructor will not be automatically regenerated by the visual editor.
   */
  public AddRealUserView(final ExpenseSheet expenseSheet) {
    buildMainLayout();
    setCompositionRoot(mainLayout);

    // TODO add user code here
    saveButton.addClickListener(new Button.ClickListener() {

      private static final long serialVersionUID = 8958661760141765780L;

      @Override
      public void buttonClick(ClickEvent event) {
        RealUserService realUserService = new RealUserService();
        UserLimitService userLimitService = new UserLimitService();
        RealUser realUser = realUserService.findRealUser(newRealUser.getValue());
        UserLimit userLimit = new UserLimit(realUser, new BigDecimal(0));
        userLimitService.createUserLimit(expenseSheet, userLimit);
        setCompositionRoot(new ExpenseSheetSettingsView(expenseSheet));
      }
    });
  }

  @AutoGenerated
  private VerticalLayout buildMainLayout() {
    // common part: create layout
    mainLayout = new VerticalLayout();
    mainLayout.setImmediate(false);
    mainLayout.setWidth("100%");
    mainLayout.setHeight("100%");
    mainLayout.setMargin(true);

    // top-level component properties
    setWidth("100.0%");
    setHeight("100.0%");

    // newRealUserGrid
    newRealUserGrid = buildNewRealUserGrid();
    mainLayout.addComponent(newRealUserGrid);

    // saveButton
    saveButton = new Button();
    saveButton.setCaption("Zapisz");
    saveButton.setImmediate(true);
    saveButton.setWidth("-1px");
    saveButton.setHeight("-1px");
    mainLayout.addComponent(saveButton);

    return mainLayout;
  }

  @AutoGenerated
  private GridLayout buildNewRealUserGrid() {
    // common part: create layout
    newRealUserGrid = new GridLayout();
    newRealUserGrid.setImmediate(false);
    newRealUserGrid.setWidth("-1px");
    newRealUserGrid.setHeight("-1px");
    newRealUserGrid.setMargin(true);
    newRealUserGrid.setColumns(2);

    // newRealUserLabel
    newRealUserLabel = new Label();
    newRealUserLabel.setImmediate(false);
    newRealUserLabel.setWidth("-1px");
    newRealUserLabel.setHeight("-1px");
    newRealUserLabel.setValue("Nazwa użytkownika");
    newRealUserGrid.addComponent(newRealUserLabel, 0, 0);

    // newRealUser
    newRealUser = new TextField();
    newRealUser.setImmediate(false);
    newRealUser.setWidth("-1px");
    newRealUser.setHeight("-1px");
    newRealUser.setRequired(true);
    newRealUserGrid.addComponent(newRealUser, 1, 0);

    return newRealUserGrid;
  }

}
