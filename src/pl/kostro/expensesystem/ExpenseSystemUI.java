package pl.kostro.expensesystem;

import javax.servlet.annotation.WebServlet;

import pl.kostro.expensesystem.components.LoginPage;
import pl.kostro.expensesystem.components.MainPage;
import pl.kostro.expensesystem.model.RealUser;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("expensesystem")
public class ExpenseSystemUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ExpenseSystemUI.class)
	public static class Servlet extends VaadinServlet {
	}
	
	private RealUser loggedUser = new RealUser();

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		if (loggedUser.getId() == 0) {
			layout.addComponent(new LoginPage(loggedUser));
		} else
			layout.addComponent(new MainPage(loggedUser));
	}

}