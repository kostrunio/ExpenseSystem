package pl.kostro.expensesystem.components.settingsPageComponents;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class ExpenseSheetSettingsView extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_2;
	@AutoGenerated
	private VerticalLayout verticalLayout_4;
	@AutoGenerated
	private Button button_7;
	@AutoGenerated
	private Table table_2;
	@AutoGenerated
	private VerticalLayout verticalLayout_3;
	@AutoGenerated
	private Button button_6;
	@AutoGenerated
	private Table table_1;
	@AutoGenerated
	private VerticalLayout verticalLayout_2;
	@AutoGenerated
	private Button button_5;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private GridLayout gridLayout_1;
	@AutoGenerated
	private Button button_4;
	@AutoGenerated
	private Button button_3;
	@AutoGenerated
	private Button button_2;
	@AutoGenerated
	private Button button_1;
	@AutoGenerated
	private Tree tree_1;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ExpenseSheetSettingsView() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
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
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		mainLayout.addComponent(horizontalLayout_2);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setImmediate(false);
		horizontalLayout_2.setWidth("-1px");
		horizontalLayout_2.setHeight("-1px");
		horizontalLayout_2.setMargin(false);
		
		// verticalLayout_2
		verticalLayout_2 = buildVerticalLayout_2();
		horizontalLayout_2.addComponent(verticalLayout_2);
		
		// verticalLayout_3
		verticalLayout_3 = buildVerticalLayout_3();
		horizontalLayout_2.addComponent(verticalLayout_3);
		
		// verticalLayout_4
		verticalLayout_4 = buildVerticalLayout_4();
		horizontalLayout_2.addComponent(verticalLayout_4);
		
		return horizontalLayout_2;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_2() {
		// common part: create layout
		verticalLayout_2 = new VerticalLayout();
		verticalLayout_2.setCaption("Kategorie");
		verticalLayout_2.setImmediate(false);
		verticalLayout_2.setWidth("-1px");
		verticalLayout_2.setHeight("-1px");
		verticalLayout_2.setMargin(true);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		verticalLayout_2.addComponent(horizontalLayout_1);
		
		// button_5
		button_5 = new Button();
		button_5.setCaption("Nowa kategoria");
		button_5.setImmediate(false);
		button_5.setWidth("-1px");
		button_5.setHeight("-1px");
		verticalLayout_2.addComponent(button_5);
		
		return verticalLayout_2;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		
		// tree_1
		tree_1 = new Tree();
		tree_1.setImmediate(false);
		tree_1.setWidth("-1px");
		tree_1.setHeight("-1px");
		horizontalLayout_1.addComponent(tree_1);
		
		// gridLayout_1
		gridLayout_1 = buildGridLayout_1();
		horizontalLayout_1.addComponent(gridLayout_1);
		horizontalLayout_1.setExpandRatio(gridLayout_1, 1.0f);
		
		return horizontalLayout_1;
	}

	@AutoGenerated
	private GridLayout buildGridLayout_1() {
		// common part: create layout
		gridLayout_1 = new GridLayout();
		gridLayout_1.setImmediate(false);
		gridLayout_1.setWidth("-1px");
		gridLayout_1.setHeight("-1px");
		gridLayout_1.setMargin(false);
		gridLayout_1.setColumns(3);
		gridLayout_1.setRows(3);
		
		// button_1
		button_1 = new Button();
		button_1.setCaption("Button");
		button_1.setImmediate(false);
		button_1.setWidth("-1px");
		button_1.setHeight("-1px");
		gridLayout_1.addComponent(button_1, 1, 0);
		gridLayout_1.setComponentAlignment(button_1, new Alignment(24));
		
		// button_2
		button_2 = new Button();
		button_2.setCaption("Lewo");
		button_2.setImmediate(false);
		button_2.setWidth("-1px");
		button_2.setHeight("-1px");
		gridLayout_1.addComponent(button_2, 0, 1);
		gridLayout_1.setComponentAlignment(button_2, new Alignment(34));
		
		// button_3
		button_3 = new Button();
		button_3.setCaption("Prawo");
		button_3.setImmediate(false);
		button_3.setWidth("-1px");
		button_3.setHeight("-1px");
		gridLayout_1.addComponent(button_3, 2, 1);
		gridLayout_1.setComponentAlignment(button_3, new Alignment(33));
		
		// button_4
		button_4 = new Button();
		button_4.setCaption("D�");
		button_4.setImmediate(false);
		button_4.setWidth("-1px");
		button_4.setHeight("-1px");
		gridLayout_1.addComponent(button_4, 1, 2);
		gridLayout_1.setComponentAlignment(button_4, new Alignment(20));
		
		return gridLayout_1;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_3() {
		// common part: create layout
		verticalLayout_3 = new VerticalLayout();
		verticalLayout_3.setCaption("Dost�py");
		verticalLayout_3.setImmediate(false);
		verticalLayout_3.setWidth("-1px");
		verticalLayout_3.setHeight("-1px");
		verticalLayout_3.setMargin(true);
		
		// table_1
		table_1 = new Table();
		table_1.setImmediate(false);
		table_1.setWidth("-1px");
		table_1.setHeight("-1px");
		verticalLayout_3.addComponent(table_1);
		
		// button_6
		button_6 = new Button();
		button_6.setCaption("Dodaj dost�p");
		button_6.setImmediate(false);
		button_6.setWidth("-1px");
		button_6.setHeight("-1px");
		verticalLayout_3.addComponent(button_6);
		
		return verticalLayout_3;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_4() {
		// common part: create layout
		verticalLayout_4 = new VerticalLayout();
		verticalLayout_4.setCaption("Dodatkowi u�ytkownicy");
		verticalLayout_4.setImmediate(false);
		verticalLayout_4.setWidth("-1px");
		verticalLayout_4.setHeight("-1px");
		verticalLayout_4.setMargin(true);
		
		// table_2
		table_2 = new Table();
		table_2.setImmediate(false);
		table_2.setWidth("-1px");
		table_2.setHeight("-1px");
		verticalLayout_4.addComponent(table_2);
		
		// button_7
		button_7 = new Button();
		button_7.setCaption("Dodaj nowego");
		button_7.setImmediate(false);
		button_7.setWidth("-1px");
		button_7.setHeight("-1px");
		verticalLayout_4.addComponent(button_7);
		
		return verticalLayout_4;
	}

}
