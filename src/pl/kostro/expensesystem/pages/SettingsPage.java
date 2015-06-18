package pl.kostro.expensesystem.pages;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetSettingsView;
import pl.kostro.expensesystem.views.settingsPage.NewExpenseSheetView;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

public class SettingsPage extends CustomComponent implements View {

	private static final long serialVersionUID = 4095840075421339917L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private VerticalLayout mainView;
	@AutoGenerated
	private HorizontalLayout tabLayout;
	@AutoGenerated
	private Button addSheetButton;
	@AutoGenerated
	private MenuBar sheetMenu;
	
	RealUser loggedUser;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public SettingsPage() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		loggedUser = (RealUser) VaadinSession.getCurrent().getAttribute(
        RealUser.class.getName());
		
		MenuBar.Command sheetCommand = new MenuBar.Command() {
      
			private static final long serialVersionUID = -5420888032285767247L;

			@Override
		      public void menuSelected(MenuItem selectedItem) {
		        for (ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
		          if (selectedItem.getText().equals(expenseSheet.getName())) {
		              mainView.removeAllComponents();
		              mainView.addComponent(new ExpenseSheetSettingsView(expenseSheet));
		          }
		        }
		      }
	    };
      
    
    MenuItem sheets = sheetMenu.addItem("Arkusze", null);
    for (ExpenseSheet expenseSheet : loggedUser.getExpenseSheetList()) {
      sheets.addItem(expenseSheet.getName(), sheetCommand);
    }
		
		addSheetButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = -9202021732327761870L;

			@Override
			public void buttonClick(ClickEvent event) {
			  mainView.removeAllComponents();
			  mainView.addComponent(new NewExpenseSheetView(loggedUser));
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
		
		// addSheetButton
		addSheetButton = new Button();
		addSheetButton.setCaption("Dodaj arkusz");
		addSheetButton.setImmediate(false);
		addSheetButton.setWidth("-1px");
		addSheetButton.setHeight("-1px");
		tabLayout.addComponent(addSheetButton);
		tabLayout.setComponentAlignment(addSheetButton, new Alignment(6));
		
		return tabLayout;
	}

  @Override
  public void enter(ViewChangeEvent event) {
    // TODO Auto-generated method stub
    
  }

}
