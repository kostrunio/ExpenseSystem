package pl.kostro.expensesystem.components.mainPageComponents;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class DayView extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Button button_2;
	@AutoGenerated
	private GridLayout gridLayout_1;
	@AutoGenerated
	private Label label_1;
	@AutoGenerated
	private Button button_1;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public DayView() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
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
		
		// gridLayout_1
		gridLayout_1 = buildGridLayout_1();
		mainLayout.addComponent(gridLayout_1);
		
		// button_2
		button_2 = new Button();
		button_2.setCaption("Powr�t");
		button_2.setImmediate(false);
		button_2.setWidth("-1px");
		button_2.setHeight("-1px");
		mainLayout.addComponent(button_2);
		
		return mainLayout;
	}

	@AutoGenerated
	private GridLayout buildGridLayout_1() {
		// common part: create layout
		gridLayout_1 = new GridLayout();
		gridLayout_1.setImmediate(false);
		gridLayout_1.setWidth("-1px");
		gridLayout_1.setHeight("-1px");
		gridLayout_1.setMargin(true);
		gridLayout_1.setSpacing(true);
		gridLayout_1.setColumns(2);
		gridLayout_1.setRows(8);
		
		// button_1
		button_1 = new Button();
		button_1.setCaption("Button");
		button_1.setImmediate(false);
		button_1.setWidth("-1px");
		button_1.setHeight("-1px");
		gridLayout_1.addComponent(button_1, 1, 0);
		
		// label_1
		label_1 = new Label();
		label_1.setImmediate(false);
		label_1.setWidth("-1px");
		label_1.setHeight("-1px");
		label_1.setValue("jedzenie");
		gridLayout_1.addComponent(label_1, 0, 0);
		gridLayout_1.setComponentAlignment(label_1, new Alignment(48));
		
		return gridLayout_1;
	}

}
