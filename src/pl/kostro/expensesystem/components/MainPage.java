package pl.kostro.expensesystem.components;

import pl.kostro.expensesystem.components.mainPageComponents.ExpenseView;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class MainPage extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private HorizontalLayout tabLayout;
	@AutoGenerated
	private MenuBar sheetMenu;
	@AutoGenerated
	private Button settingButton;
	@AutoGenerated
	private Button logoutButton;
	@AutoGenerated
	private VerticalLayout mainView;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public MainPage(final RealUser loggedUser) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		MenuBar.Command sheetCommand = new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				// TODO Auto-generated method stub
				for (ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
					if (selectedItem.getText().equals(expenseSheet.getName())) {
							mainView.removeAllComponents();
							mainView.addComponent(new ExpenseView(expenseSheet));
					}
				}
			}
		};
			
		
		MenuItem sheets = sheetMenu.addItem("Arkusze", null);
		for (ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
			MenuItem newSheet = sheets.addItem(expenseSheet.getName(), sheetCommand);
		}
		
		settingButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				setCompositionRoot(new SettingsPage(loggedUser));
			}
		});
		
		logoutButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				loggedUser.removeData();
				setCompositionRoot(new LoginPage(loggedUser));
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
		
		// tabLayout
		tabLayout = buildTabLayout();
		mainLayout.addComponent(tabLayout);
		
		// mainView
		mainView = new VerticalLayout();
		mainView.setImmediate(false);
		mainView.setWidth("-1px");
		mainView.setHeight("-1px");
		mainView.setMargin(true);
		mainLayout.addComponent(mainView);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildTabLayout() {
		// common part: create layout
		tabLayout = new HorizontalLayout();
		tabLayout.setImmediate(false);
		tabLayout.setWidth("100%");
		tabLayout.setHeight("-1px");
		tabLayout.setMargin(false);
		tabLayout.setSpacing(true);
		
		// sheetMenu
		sheetMenu = new MenuBar();
		sheetMenu.setImmediate(false);
		sheetMenu.setWidth("-1px");
		sheetMenu.setHeight("-1px");
		tabLayout.addComponent(sheetMenu);
		
		// settingButton
		settingButton = new Button();
		settingButton.setCaption("Ustawienia");
		settingButton.setImmediate(false);
		settingButton.setWidth("-1px");
		settingButton.setHeight("-1px");
		tabLayout.addComponent(settingButton);
		tabLayout.setComponentAlignment(settingButton, new Alignment(6));
		
		// logoutButton
		logoutButton = new Button();
		logoutButton.setCaption("Wyloguj");
		logoutButton.setImmediate(true);
		logoutButton.setWidth("-1px");
		logoutButton.setHeight("-1px");
		tabLayout.addComponent(logoutButton);
		
		return tabLayout;
	}

}
