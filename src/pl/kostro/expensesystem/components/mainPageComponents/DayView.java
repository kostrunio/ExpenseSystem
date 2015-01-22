package pl.kostro.expensesystem.components.mainPageComponents;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.utils.CategoryExpense;
import pl.kostro.expensesystem.utils.DateExpense;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class DayView extends CustomComponent {

	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private Button backButton;

	@AutoGenerated
	private GridLayout expensesGrid;

	@AutoGenerated
	private Label dateLabel;

	private static final long serialVersionUID = 1913884300636242936L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private Date date;
	private ExpenseSheet expenseSheet;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 * @param basicDateClickHandler 
	 */
	public DayView(final ExpenseSheet expenseSheet, final Date date) {
		this.date = date;
		this.expenseSheet = expenseSheet;
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		dateLabel.setValue(new SimpleDateFormat("dd").format(date) + " "
		+ new SimpleDateFormat("MMMM").format(date) + " "
				+ new SimpleDateFormat("yyyy").format(date));
		
		prepareExpensesLayout();
		backButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 9056324173197103796L;

			@Override
			public void buttonClick(ClickEvent event) {
				setCompositionRoot(new MonthView(expenseSheet,
						new SimpleDateFormat("MMMM").format(date),
						new SimpleDateFormat("yyyy").format(date)));
			}
			
		});
	}

	private void prepareExpensesLayout() {
		List<Category> categoryList = expenseSheet.getCategoryList();
		expensesGrid.setColumns(3);
		expensesGrid.setRows(categoryList.size());
		
		for (int i = 0; i < categoryList.size(); i++) {
			Category category = categoryList.get(i);
			Label catLabel = new Label();
			catLabel.setImmediate(false);
			catLabel.setWidth("-1px");
			catLabel.setHeight("-1px");
			catLabel.setValue(category.getName());
			expensesGrid.addComponent(catLabel, 0, i);
			expensesGrid.setComponentAlignment(catLabel, new Alignment(48));
			
			Button catButton = new Button();
			DateExpense dateExpenseMap = expenseSheet.getDateExpenseMap().get(date);
			if (dateExpenseMap == null
					|| dateExpenseMap.getCategoryExpenseMap().get(category) == null)
				catButton.setCaption("0");
			else {
				CategoryExpense categoryExpenseMap = dateExpenseMap.getCategoryExpenseMap().get(category);
				catButton.setCaption(categoryExpenseMap.getSumString());
			}
			catButton.setImmediate(true);
			catButton.setWidth("-1px");
			catButton.setHeight("-1px");
			catButton.setData(category);
			catButton.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = -465615264102031639L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (event.getButton().getData() instanceof Category) {
						Category category = (Category) event.getButton().getData();
						setCompositionRoot(new CategoryExpenseView(expenseSheet, date, category));
					}
				}
				
			});
			expensesGrid.addComponent(catButton, 1, i);
			
			Button addRowButton = new Button("+");
			addRowButton.setData(category);
			addRowButton.addClickListener(new Button.ClickListener() {
				
				private static final long serialVersionUID = -257222427520041964L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (event.getButton().getData() instanceof Category) {
						Category category = (Category) event.getButton().getData();
						Expense expense = new Expense(date, "", category, null, "");
						setCompositionRoot(new AddNewExpense(expenseSheet, expense, false));
					}
				}
			});
			expensesGrid.addComponent(addRowButton, 2, i);
		}
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setCaption("kwota");
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// dateLabel
		dateLabel = new Label();
		dateLabel.setImmediate(false);
		dateLabel.setWidth("-1px");
		dateLabel.setHeight("-1px");
		dateLabel.setValue("Label");
		mainLayout.addComponent(dateLabel);
		mainLayout.setComponentAlignment(dateLabel, new Alignment(48));
		
		// expensesGrid
		expensesGrid = new GridLayout();
		expensesGrid.setImmediate(false);
		expensesGrid.setWidth("-1px");
		expensesGrid.setHeight("-1px");
		expensesGrid.setMargin(true);
		expensesGrid.setSpacing(true);
		mainLayout.addComponent(expensesGrid);
		
		// backButton
		backButton = new Button();
		backButton.setCaption("Powr�t");
		backButton.setImmediate(true);
		backButton.setWidth("-1px");
		backButton.setHeight("-1px");
		mainLayout.addComponent(backButton);
		
		return mainLayout;
	}

}
