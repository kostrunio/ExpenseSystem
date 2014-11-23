package pl.kostro.expensesystem.components.mainPageComponents;

import java.util.Date;

import pl.kostro.expensesystem.model.ExpenseSheet;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class CategoryExpenseView extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Button backButton;
	@AutoGenerated
	private HorizontalLayout expenseLayout;
	@AutoGenerated
	private Button addRowButton;
	@AutoGenerated
	private GridLayout expenseGrid;
	@AutoGenerated
	private Button removeButton;
	@AutoGenerated
	private Button valueButton;
	@AutoGenerated
	private Button saveButton;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 * @param parentView 
	 * @param date 
	 * @param expenseSheet 
	 */
	public CategoryExpenseView(final ExpenseSheet expenseSheet, final Date date, final MonthView parentView) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		backButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				setCompositionRoot(new DayView(expenseSheet, date, parentView));
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
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// saveButton
		saveButton = new Button();
		saveButton.setCaption("Zapisz");
		saveButton.setImmediate(true);
		saveButton.setWidth("-1px");
		saveButton.setHeight("-1px");
		mainLayout.addComponent(saveButton);
		
		// expenseLayout
		expenseLayout = buildExpenseLayout();
		mainLayout.addComponent(expenseLayout);
		
		// backButton
		backButton = new Button();
		backButton.setCaption("Powr�t");
		backButton.setImmediate(true);
		backButton.setWidth("-1px");
		backButton.setHeight("-1px");
		mainLayout.addComponent(backButton);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildExpenseLayout() {
		// common part: create layout
		expenseLayout = new HorizontalLayout();
		expenseLayout.setImmediate(false);
		expenseLayout.setWidth("-1px");
		expenseLayout.setHeight("-1px");
		expenseLayout.setMargin(false);
		
		// expenseGrid
		expenseGrid = buildExpenseGrid();
		expenseLayout.addComponent(expenseGrid);
		
		// addRowButton
		addRowButton = new Button();
		addRowButton.setCaption("Dodaj wiersz");
		addRowButton.setImmediate(true);
		addRowButton.setWidth("-1px");
		addRowButton.setHeight("-1px");
		expenseLayout.addComponent(addRowButton);
		
		return expenseLayout;
	}

	@AutoGenerated
	private GridLayout buildExpenseGrid() {
		// common part: create layout
		expenseGrid = new GridLayout();
		expenseGrid.setImmediate(false);
		expenseGrid.setWidth("-1px");
		expenseGrid.setHeight("-1px");
		expenseGrid.setMargin(false);
		expenseGrid.setColumns(5);
		expenseGrid.setRows(5);
		
		// valueButton
		valueButton = new Button();
		valueButton.setCaption("Warto��");
		valueButton.setImmediate(true);
		valueButton.setWidth("-1px");
		valueButton.setHeight("-1px");
		expenseGrid.addComponent(valueButton, 0, 0);
		
		// removeButton
		removeButton = new Button();
		removeButton.setCaption("X");
		removeButton.setImmediate(true);
		removeButton.setWidth("-1px");
		removeButton.setHeight("-1px");
		expenseGrid.addComponent(removeButton, 1, 0);
		
		return expenseGrid;
	}

}