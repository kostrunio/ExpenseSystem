package pl.kostro.expensesystem.components.mainPageComponents;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.CategoryExpense;
import pl.kostro.expensesystem.utils.DateExpense;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
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
  private HorizontalLayout navigationLayout;

  @AutoGenerated
  private Button nextDayButton;

  @AutoGenerated
  private Button thisDayButton;

  @AutoGenerated
  private Button previousDayButton;

  private static final long serialVersionUID = 1913884300636242936L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private ExpenseSheet expenseSheet;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 * @param basicDateClickHandler 
	 */
	public DayView(final ExpenseSheet expenseSheet, final Calendar calendar) {
		this.expenseSheet = expenseSheet;
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		previousDayButton.addClickListener(new Button.ClickListener() {
      
      private static final long serialVersionUID = -8048895457148394023L;

      @Override
      public void buttonClick(ClickEvent event) {
        calendar.add(java.util.Calendar.DAY_OF_MONTH, -1);
        thisDayButton.setCaption(new SimpleDateFormat("dd MMMM yyyy").format(calendar.getTime()));
        setCompositionRoot(new DayView(expenseSheet, calendar));
      }
    });
    
		thisDayButton.setCaption(new SimpleDateFormat("dd MMMM yyyy").format(calendar.getTime()));
		thisDayButton.addClickListener(new Button.ClickListener() {
      
      private static final long serialVersionUID = -8048895457148394023L;

      @Override
      public void buttonClick(ClickEvent event) {
        setCompositionRoot(new DayView(expenseSheet, calendar));
      }
    });
    
    nextDayButton.addClickListener(new Button.ClickListener() {
      
      private static final long serialVersionUID = -8048895457148394023L;

      @Override
      public void buttonClick(ClickEvent event) {
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
        thisDayButton.setCaption(new SimpleDateFormat("dd MMMM yyyy").format(calendar.getTime()));
        setCompositionRoot(new DayView(expenseSheet, calendar));
      }
    });
		
		prepareExpensesLayout(calendar);
		backButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 9056324173197103796L;

			@Override
			public void buttonClick(ClickEvent event) {
				calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
				setCompositionRoot(new MonthView(expenseSheet, calendar));
			}
			
		});
	}

	private void prepareExpensesLayout(final Calendar calendar) {
		List<Category> categoryList = expenseSheet.getCategoryList();
		expensesGrid.setColumns(5);
		expensesGrid.setRows(categoryList.size()/5+1);
		
		for (int i = 0; i < categoryList.size(); i++) {
			VerticalLayout vertLay = new VerticalLayout();
			expensesGrid.addComponent(vertLay, i%5, i/5);

			Category category = categoryList.get(i);
			Label catLabel = new Label();
			catLabel.setImmediate(false);
			catLabel.setWidth("-1px");
			catLabel.setHeight("-1px");
			catLabel.setValue(category.getName());
			vertLay.addComponent(catLabel);
			
			HorizontalLayout expLay = new HorizontalLayout();
			vertLay.addComponent(expLay);
			Button expButton = new Button();
			DateExpense dateExpenseMap = ExpenseSheetService.getDateExpenseMap(expenseSheet, calendar.getTime());
			if (dateExpenseMap == null
					|| dateExpenseMap.getCategoryExpenseMap().get(category) == null)
				expButton.setCaption("0");
			else {
				CategoryExpense categoryExpenseMap = dateExpenseMap.getCategoryExpenseMap().get(category);
				expButton.setCaption(categoryExpenseMap.getSumString());
			}
			expButton.setImmediate(true);
			expButton.setWidth("-1px");
			expButton.setHeight("-1px");
			expButton.setData(category);
			expButton.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = -465615264102031639L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (event.getButton().getData() instanceof Category) {
						Category category = (Category) event.getButton().getData();
						setCompositionRoot(new CategoryExpenseView(expenseSheet, calendar, category));
					}
				}
				
			});
			expLay.addComponent(expButton);
			
			Button addRowButton = new Button("+");
			addRowButton.setData(category);
			addRowButton.addClickListener(new Button.ClickListener() {
				
				private static final long serialVersionUID = -257222427520041964L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (event.getButton().getData() instanceof Category) {
						Category category = (Category) event.getButton().getData();
						Expense expense = new Expense(calendar.getTime(), "", category, null, "");
						setCompositionRoot(new AddNewExpense(expenseSheet, expense, false, calendar));
					}
				}
			});
			expLay.addComponent(addRowButton);
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
    
    // navigationLayout
    navigationLayout = buildNavigationLayout();
    mainLayout.addComponent(navigationLayout);
    mainLayout.setComponentAlignment(navigationLayout, new Alignment(20));
    
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

  @AutoGenerated
  private HorizontalLayout buildNavigationLayout() {
    // common part: create layout
    navigationLayout = new HorizontalLayout();
    navigationLayout.setImmediate(false);
    navigationLayout.setWidth("100.0%");
    navigationLayout.setHeight("-1px");
    navigationLayout.setMargin(false);
    
    // previousDayButton
    previousDayButton = new Button();
    previousDayButton.setCaption("<");
    previousDayButton.setImmediate(true);
    previousDayButton.setWidth("-1px");
    previousDayButton.setHeight("-1px");
    navigationLayout.addComponent(previousDayButton);
    navigationLayout.setComponentAlignment(previousDayButton, new Alignment(33));
    
    // thisDayButton
    thisDayButton = new Button();
    thisDayButton.setCaption("Button");
    thisDayButton.setImmediate(true);
    thisDayButton.setWidth("-1px");
    thisDayButton.setHeight("-1px");
    navigationLayout.addComponent(thisDayButton);
    navigationLayout.setComponentAlignment(thisDayButton, new Alignment(48));
    
    // nextDayButton
    nextDayButton = new Button();
    nextDayButton.setCaption(">");
    nextDayButton.setImmediate(true);
    nextDayButton.setWidth("-1px");
    nextDayButton.setHeight("-1px");
    navigationLayout.addComponent(nextDayButton);
    navigationLayout.setComponentAlignment(nextDayButton, new Alignment(34));
    
    return navigationLayout;
  }

}
