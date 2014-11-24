package pl.kostro.expensesystem.components.settingsPageComponents;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class NewExpenseSheetView extends CustomComponent {

	private static final long serialVersionUID = 5221862325479800000L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Button saveButton;
	@AutoGenerated
	private GridLayout newSheetGrid;
	@AutoGenerated
	private TextField newSheet;
	@AutoGenerated
	private Label newSheetLabel;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public NewExpenseSheetView(final RealUser loggedUser) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		saveButton.addClickListener(new Button.ClickListener() {
      
			private static final long serialVersionUID = -3336326952937663406L;

			@Override
		      public void buttonClick(ClickEvent event) {
		        ExpenseSheet expenseSheet = new ExpenseSheet();
		        expenseSheet.setName(newSheet.getValue());
		        loggedUser.getExpenseSheetList().add(expenseSheet);
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
    
    // newSheetGrid
    newSheetGrid = buildNewSheetGrid();
    mainLayout.addComponent(newSheetGrid);
    
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
  private GridLayout buildNewSheetGrid() {
    // common part: create layout
    newSheetGrid = new GridLayout();
    newSheetGrid.setImmediate(false);
    newSheetGrid.setWidth("-1px");
    newSheetGrid.setHeight("-1px");
    newSheetGrid.setMargin(true);
    newSheetGrid.setColumns(2);
    
    // newSheetLabel
    newSheetLabel = new Label();
    newSheetLabel.setImmediate(false);
    newSheetLabel.setWidth("-1px");
    newSheetLabel.setHeight("-1px");
    newSheetLabel.setValue("Nazwa arkusza");
    newSheetGrid.addComponent(newSheetLabel, 0, 0);
    
    // newSheet
    newSheet = new TextField();
    newSheet.setImmediate(false);
    newSheet.setWidth("-1px");
    newSheet.setHeight("-1px");
    newSheet.setRequired(true);
    newSheetGrid.addComponent(newSheet, 1, 0);
    
    return newSheetGrid;
  }

}
