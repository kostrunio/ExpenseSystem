package pl.kostro.expensesystem.components.settingsPageComponents;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AddUserView extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Button saveButton;
	@AutoGenerated
	private GridLayout newUserGrid;
	@AutoGenerated
	private TextField newUser;
	@AutoGenerated
	private Label newUserLabel;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public AddUserView(final ExpenseSheet expenseSheet) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		saveButton.addClickListener(new Button.ClickListener() {
      
	      @Override
	      public void buttonClick(ClickEvent event) {
	        User user = new User();
	        user.setName(newUser.getValue());
	        expenseSheet.getUserLimitList().add(new UserLimit(user, 0));
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
	
	// newUserGrid
	newUserGrid = buildNewUserGrid();
	mainLayout.addComponent(newUserGrid);
	
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
private GridLayout buildNewUserGrid() {
	// common part: create layout
	newUserGrid = new GridLayout();
	newUserGrid.setImmediate(false);
	newUserGrid.setWidth("-1px");
	newUserGrid.setHeight("-1px");
	newUserGrid.setMargin(true);
	newUserGrid.setColumns(2);
	
	// newUserLabel
	newUserLabel = new Label();
	newUserLabel.setImmediate(false);
	newUserLabel.setWidth("-1px");
	newUserLabel.setHeight("-1px");
	newUserLabel.setValue("Nazwa arkusza");
	newUserGrid.addComponent(newUserLabel, 0, 0);
	
	// newUser
	newUser = new TextField();
	newUser.setImmediate(false);
	newUser.setWidth("-1px");
	newUser.setHeight("-1px");
	newUser.setRequired(true);
	newUserGrid.addComponent(newUser, 1, 0);
	
	return newUserGrid;
}

}
