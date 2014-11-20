package pl.kostro.expensesystem.components.mainPageComponents;

import pl.kostro.expensesystem.model.ExpenseSheet;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;

public class ExpenseView extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private VerticalLayout mainView;
	@AutoGenerated
	private MenuBar monthMenu;
	@AutoGenerated
	private HorizontalLayout menuLayout;
	@AutoGenerated
	private Button cleanFilterButton;
	@AutoGenerated
	private Button filterButton;
	@AutoGenerated
	private MenuBar yearMenu;
	private String year;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ExpenseView(final ExpenseSheet expenseSheet) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		MenuBar.Command yearCommand = new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				// TODO Auto-generated method stub
				year = selectedItem.getText();
				monthMenu.setVisible(true);
			}
		};
		
		MenuItem sheets = yearMenu.addItem("2014", yearCommand);
		
		MenuBar.Command monthCommand = new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				// TODO Auto-generated method stub
				mainView.removeAllComponents();
				mainView.addComponent(new MonthView(expenseSheet, selectedItem.getText(), year));
			}
		};
		
		MenuItem yanuary = monthMenu.addItem("stycze�", monthCommand);
		MenuItem february = monthMenu.addItem("luty", monthCommand);
		MenuItem march = monthMenu.addItem("marzec", monthCommand);
		MenuItem april = monthMenu.addItem("kwiecie�", monthCommand);
		MenuItem may = monthMenu.addItem("maj", monthCommand);
		MenuItem june = monthMenu.addItem("czerwiec", monthCommand);
		MenuItem july = monthMenu.addItem("lipiec", monthCommand);
		MenuItem august = monthMenu.addItem("sierpie�", monthCommand);
		MenuItem september = monthMenu.addItem("wrzesie�", monthCommand);
		MenuItem october = monthMenu.addItem("pa�dziernik", monthCommand);
		MenuItem november = monthMenu.addItem("listopad", monthCommand);
		MenuItem december = monthMenu.addItem("grudzie�", monthCommand);
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
		
		// menuLayout
		menuLayout = buildMenuLayout();
		mainLayout.addComponent(menuLayout);
		
		// monthMenu
		monthMenu = new MenuBar();
		monthMenu.setImmediate(false);
		monthMenu.setVisible(false);
		monthMenu.setWidth("100.0%");
		monthMenu.setHeight("-1px");
		mainLayout.addComponent(monthMenu);
		
		// mainView
		mainView = new VerticalLayout();
		mainView.setImmediate(false);
		mainView.setWidth("-1px");
		mainView.setHeight("-1px");
		mainView.setMargin(false);
		mainLayout.addComponent(mainView);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildMenuLayout() {
		// common part: create layout
		menuLayout = new HorizontalLayout();
		menuLayout.setImmediate(false);
		menuLayout.setWidth("100.0%");
		menuLayout.setHeight("-1px");
		menuLayout.setMargin(false);
		menuLayout.setSpacing(true);
		
		// yearMenu
		yearMenu = new MenuBar();
		yearMenu.setImmediate(false);
		yearMenu.setWidth("-1px");
		yearMenu.setHeight("-1px");
		menuLayout.addComponent(yearMenu);
		
		// filterButton
		filterButton = new Button();
		filterButton.setCaption("Filtruj");
		filterButton.setImmediate(true);
		filterButton.setWidth("-1px");
		filterButton.setHeight("-1px");
		menuLayout.addComponent(filterButton);
		menuLayout.setComponentAlignment(filterButton, new Alignment(6));
		
		// cleanFilterButton
		cleanFilterButton = new Button();
		cleanFilterButton.setCaption("Wyczy��");
		cleanFilterButton.setImmediate(true);
		cleanFilterButton.setWidth("-1px");
		cleanFilterButton.setHeight("-1px");
		menuLayout.addComponent(cleanFilterButton);
		
		return menuLayout;
	}

}
