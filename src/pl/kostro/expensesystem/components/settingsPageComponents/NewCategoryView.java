package pl.kostro.expensesystem.components.settingsPageComponents;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class NewCategoryView extends CustomComponent {

	private static final long serialVersionUID = -1908886165058435579L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Button saveButton;
	@AutoGenerated
	private GridLayout newCategoryGrid;
	@AutoGenerated
	private TextField newCategory;
	@AutoGenerated
	private Label newCategoryLabel;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public NewCategoryView(final ExpenseSheet expenseSheet) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		saveButton.addClickListener(new Button.ClickListener() {
      
			private static final long serialVersionUID = 6169023617465616548L;

			@Override
		      public void buttonClick(ClickEvent event) {
		    	  Category category = new Category(newCategory.getValue(), expenseSheet.getCategoryList().size());
		    	  expenseSheet.getCategoryList().add(category);
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
	
	// newCategoryGrid
	newCategoryGrid = buildNewCategoryGrid();
	mainLayout.addComponent(newCategoryGrid);
	
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
private GridLayout buildNewCategoryGrid() {
	// common part: create layout
	newCategoryGrid = new GridLayout();
	newCategoryGrid.setImmediate(false);
	newCategoryGrid.setWidth("-1px");
	newCategoryGrid.setHeight("-1px");
	newCategoryGrid.setMargin(true);
	newCategoryGrid.setColumns(2);
	
	// newCategoryLabel
	newCategoryLabel = new Label();
	newCategoryLabel.setImmediate(false);
	newCategoryLabel.setWidth("-1px");
	newCategoryLabel.setHeight("-1px");
	newCategoryLabel.setValue("Nazwa kategorii");
	newCategoryGrid.addComponent(newCategoryLabel, 0, 0);
	
	// newCategory
	newCategory = new TextField();
	newCategory.setImmediate(false);
	newCategory.setWidth("-1px");
	newCategory.setHeight("-1px");
	newCategory.setRequired(true);
	newCategoryGrid.addComponent(newCategory, 1, 0);
	
	return newCategoryGrid;
}

}
