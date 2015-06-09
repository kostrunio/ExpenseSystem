package pl.kostro.expensesystem.components.settingsPageComponents;

import java.math.BigDecimal;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.UserLimit;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class ExpenseSheetSettingsView extends CustomComponent {

	private static final long serialVersionUID = -4661152658922847492L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private HorizontalLayout mainLayout;
	@AutoGenerated
	private VerticalLayout userLayout;
	@AutoGenerated
	private Button addUserButton;
	@AutoGenerated
	private Table userTable;
	@AutoGenerated
	private VerticalLayout realUserLayout;
	@AutoGenerated
	private Button addRealUserButton;
	@AutoGenerated
	private Table realUserTable;
	@AutoGenerated
	private VerticalLayout categoryLayout;
	@AutoGenerated
	private Button newCategoryButton;
	@AutoGenerated
	private HorizontalLayout modifyCategoryLayout;
	@AutoGenerated
	private GridLayout buttoGrid;
	@AutoGenerated
	private Button downButton;
	@AutoGenerated
	private Button rightButton;
	@AutoGenerated
	private Button leftButton;
	@AutoGenerated
	private Button upButton;
	@AutoGenerated
	private Tree categoryTree;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 * @param expenseSheet 
	 */
	@SuppressWarnings("unchecked")
  public ExpenseSheetSettingsView(final ExpenseSheet expenseSheet) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		categoryTree.addItems(expenseSheet.getCategoryList());
		categoryTree.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 6185034786110604775L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() == null) {
					upButton.setEnabled(false);
					downButton.setEnabled(false);
					return;
				}
				if (event.getProperty().getValue() instanceof Category) {
					Category category = (Category) event.getProperty().getValue();
					if (category.getOrder() == 0) {
						upButton.setEnabled(false);
						downButton.setEnabled(true);
					} else if (category.getOrder() == expenseSheet.getCategoryList().size()-1) {
						upButton.setEnabled(true);
						downButton.setEnabled(false);
					} else {
						upButton.setEnabled(true);
						downButton.setEnabled(true);
					}
				}
			}
		});
		
		upButton.setEnabled(false);
		leftButton.setEnabled(false);
		rightButton.setEnabled(false);
		downButton.setEnabled(false);
		
		newCategoryButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = -3215244626255112805L;

			@Override
			public void buttonClick(ClickEvent event) {
				setCompositionRoot(new NewCategoryView(expenseSheet));
			}
		});
		
		realUserTable.setPageLength(5);
		realUserTable.addContainerProperty("Nazwa", String.class, null);
		realUserTable.addContainerProperty("Limit", BigDecimal.class, null);
		realUserTable.setSelectable(true);
		realUserTable.setImmediate(true);
		
		userTable.setPageLength(5);
		userTable.addContainerProperty("Nazwa", String.class, null);
		userTable.addContainerProperty("Limit", BigDecimal.class, null);
		userTable.setSelectable(true);
		userTable.setImmediate(true);
		for (UserLimit userLimit : expenseSheet.getUserLimitList()) {
			if (userLimit.getUser() instanceof RealUser) {
				Object newItemId = realUserTable.addItem();
				Item row = realUserTable.getItem(newItemId);
				row.getItemProperty("Nazwa").setValue(userLimit.getUser().getName());
				row.getItemProperty("Limit").setValue(userLimit.getLimit());
			} else {
				Object newItemId = userTable.addItem();
				Item row = userTable.getItem(newItemId);
				row.getItemProperty("Nazwa").setValue(userLimit.getUser().getName());
				row.getItemProperty("Limit").setValue(userLimit.getLimit());
			}
		}
		
		addRealUserButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = -4826331461965840632L;

			@Override
			public void buttonClick(ClickEvent event) {
				setCompositionRoot(new AddRealUserView(expenseSheet, null));
			}
		});
		
		addUserButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = -8899926296443637490L;

			@Override
			public void buttonClick(ClickEvent event) {
				setCompositionRoot(new AddUserView(expenseSheet, null));
			}
		});
	}

	@AutoGenerated
private HorizontalLayout buildMainLayout() {
	// common part: create layout
	mainLayout = new HorizontalLayout();
	mainLayout.setImmediate(false);
	mainLayout.setWidth("100%");
	mainLayout.setHeight("100%");
	mainLayout.setMargin(false);
	
	// top-level component properties
	setWidth("100.0%");
	setHeight("100.0%");
	
	// categoryLayout
	categoryLayout = buildCategoryLayout();
	mainLayout.addComponent(categoryLayout);
	
	// realUserLayout
	realUserLayout = buildRealUserLayout();
	mainLayout.addComponent(realUserLayout);
	
	// userLayout
	userLayout = buildUserLayout();
	mainLayout.addComponent(userLayout);
	
	return mainLayout;
}

@AutoGenerated
private VerticalLayout buildCategoryLayout() {
	// common part: create layout
	categoryLayout = new VerticalLayout();
	categoryLayout.setCaption("Kategorie");
	categoryLayout.setImmediate(false);
	categoryLayout.setWidth("-1px");
	categoryLayout.setHeight("-1px");
	categoryLayout.setMargin(true);
	
	// modifyCategoryLayout
	modifyCategoryLayout = buildModifyCategoryLayout();
	categoryLayout.addComponent(modifyCategoryLayout);
	
	// newCategoryButton
	newCategoryButton = new Button();
	newCategoryButton.setCaption("Nowa kategoria");
	newCategoryButton.setImmediate(true);
	newCategoryButton.setWidth("-1px");
	newCategoryButton.setHeight("-1px");
	categoryLayout.addComponent(newCategoryButton);
	
	return categoryLayout;
}

@AutoGenerated
private HorizontalLayout buildModifyCategoryLayout() {
	// common part: create layout
	modifyCategoryLayout = new HorizontalLayout();
	modifyCategoryLayout.setImmediate(false);
	modifyCategoryLayout.setWidth("-1px");
	modifyCategoryLayout.setHeight("-1px");
	modifyCategoryLayout.setMargin(false);
	
	// categoryTree
	categoryTree = new Tree();
	categoryTree.setImmediate(true);
	categoryTree.setWidth("-1px");
	categoryTree.setHeight("-1px");
	modifyCategoryLayout.addComponent(categoryTree);
	
	// buttoGrid
	buttoGrid = buildButtoGrid();
	modifyCategoryLayout.addComponent(buttoGrid);
	modifyCategoryLayout.setExpandRatio(buttoGrid, 1.0f);
	
	return modifyCategoryLayout;
}

@AutoGenerated
private GridLayout buildButtoGrid() {
	// common part: create layout
	buttoGrid = new GridLayout();
	buttoGrid.setImmediate(false);
	buttoGrid.setWidth("-1px");
	buttoGrid.setHeight("-1px");
	buttoGrid.setMargin(false);
	buttoGrid.setColumns(3);
	buttoGrid.setRows(3);
	
	// upButton
	upButton = new Button();
	upButton.setCaption("G�ra");
	upButton.setImmediate(true);
	upButton.setWidth("-1px");
	upButton.setHeight("-1px");
	buttoGrid.addComponent(upButton, 1, 0);
	buttoGrid.setComponentAlignment(upButton, new Alignment(24));
	
	// leftButton
	leftButton = new Button();
	leftButton.setCaption("Lewo");
	leftButton.setImmediate(true);
	leftButton.setWidth("-1px");
	leftButton.setHeight("-1px");
	buttoGrid.addComponent(leftButton, 0, 1);
	buttoGrid.setComponentAlignment(leftButton, new Alignment(34));
	
	// rightButton
	rightButton = new Button();
	rightButton.setCaption("Prawo");
	rightButton.setImmediate(true);
	rightButton.setWidth("-1px");
	rightButton.setHeight("-1px");
	buttoGrid.addComponent(rightButton, 2, 1);
	buttoGrid.setComponentAlignment(rightButton, new Alignment(33));
	
	// downButton
	downButton = new Button();
	downButton.setCaption("D�");
	downButton.setImmediate(true);
	downButton.setWidth("-1px");
	downButton.setHeight("-1px");
	buttoGrid.addComponent(downButton, 1, 2);
	buttoGrid.setComponentAlignment(downButton, new Alignment(20));
	
	return buttoGrid;
}

@AutoGenerated
private VerticalLayout buildRealUserLayout() {
	// common part: create layout
	realUserLayout = new VerticalLayout();
	realUserLayout.setCaption("Dost�py");
	realUserLayout.setImmediate(false);
	realUserLayout.setWidth("-1px");
	realUserLayout.setHeight("-1px");
	realUserLayout.setMargin(true);
	
	// realUserTable
	realUserTable = new Table();
	realUserTable.setImmediate(false);
	realUserTable.setWidth("-1px");
	realUserTable.setHeight("-1px");
	realUserLayout.addComponent(realUserTable);
	
	// addRealUserButton
	addRealUserButton = new Button();
	addRealUserButton.setCaption("Dodaj dost�p");
	addRealUserButton.setImmediate(true);
	addRealUserButton.setWidth("-1px");
	addRealUserButton.setHeight("-1px");
	realUserLayout.addComponent(addRealUserButton);
	
	return realUserLayout;
}

@AutoGenerated
private VerticalLayout buildUserLayout() {
	// common part: create layout
	userLayout = new VerticalLayout();
	userLayout.setCaption("Dodatkowi u�ytkownicy");
	userLayout.setImmediate(false);
	userLayout.setWidth("-1px");
	userLayout.setHeight("-1px");
	userLayout.setMargin(true);
	
	// userTable
	userTable = new Table();
	userTable.setImmediate(false);
	userTable.setWidth("-1px");
	userTable.setHeight("-1px");
	userLayout.addComponent(userTable);
	
	// addUserButton
	addUserButton = new Button();
	addUserButton.setCaption("Dodaj nowego");
	addUserButton.setImmediate(true);
	addUserButton.setWidth("-1px");
	addUserButton.setHeight("-1px");
	userLayout.addComponent(addUserButton);
	
	return userLayout;
}

}
