package pl.kostro.expensesystem.components;

import pl.kostro.expensesystem.model.RealUser;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class RemindPasswordPage extends CustomComponent {

	private static final long serialVersionUID = -6197998123860257000L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private TextField email;
	@AutoGenerated
	private Button sendButton;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public RemindPasswordPage(final RealUser loggedUser) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		sendButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 2214296767136186115L;

			@Override
			public void buttonClick(ClickEvent event) {
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
		
		// email
		email = new TextField();
		email.setCaption("Prosz� wpisa� adres e-mail podany przy rejestracji");
		email.setImmediate(false);
		email.setWidth("-1px");
		email.setHeight("-1px");
		email.setRequired(true);
		mainLayout.addComponent(email);
		
		// sendButton
		sendButton = new Button();
		sendButton.setCaption("Wy�lij");
		sendButton.setImmediate(true);
		sendButton.setWidth("-1px");
		sendButton.setHeight("-1px");
		mainLayout.addComponent(sendButton);
		
		return mainLayout;
	}

}
