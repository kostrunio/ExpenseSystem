 package pl.kostro.expensesystem.components;

import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.service.RealUserService;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class LoginPage extends CustomComponent {

	@AutoGenerated
  private VerticalLayout mainLayout;
  @AutoGenerated
  private HorizontalLayout buttonsLayout;
  @AutoGenerated
  private Button registerButton;
  @AutoGenerated
  private Button remindPasswordButton;
  @AutoGenerated
  private HorizontalLayout loginLayout;
  @AutoGenerated
  private Button loginButton;
  @AutoGenerated
  private GridLayout loginGrid;
  @AutoGenerated
  private PasswordField password;
  @AutoGenerated
  private TextField userName;

  private static final long serialVersionUID = 73520020905697667L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public LoginPage() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		userName.focus();
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addStyleName(Reindeer.BUTTON_DEFAULT);
		loginButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = -4843102675674916019L;

			@Override
			public void buttonClick(ClickEvent event) {
			  RealUserService realUserService = new RealUserService();
			  RealUser loggedUser = realUserService.getUserData(userName.getValue(), password.getValue());
			  if (loggedUser == null)
			    new Notification("B��d logowania", "B��dna nazwa u�ytkownika lub has�o", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
			  else
			    setCompositionRoot(new MainPage(loggedUser));
			}
		});
		
		remindPasswordButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 2365069575006210471L;

			@Override
			public void buttonClick(ClickEvent event) {
				setCompositionRoot(new RemindPasswordPage());
			}
		});
		
		registerButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 8249193265154727363L;

			@Override
			public void buttonClick(ClickEvent event) {
				setCompositionRoot(new RegisterPage());
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
    mainLayout.setSpacing(true);
    
    // top-level component properties
    setWidth("100.0%");
    setHeight("100.0%");
    
    // loginLayout
    loginLayout = buildLoginLayout();
    mainLayout.addComponent(loginLayout);
    
    // buttonsLayout
    buttonsLayout = buildButtonsLayout();
    mainLayout.addComponent(buttonsLayout);
    
    return mainLayout;
  }

  @AutoGenerated
  private HorizontalLayout buildLoginLayout() {
    // common part: create layout
    loginLayout = new HorizontalLayout();
    loginLayout.setImmediate(false);
    loginLayout.setWidth("-1px");
    loginLayout.setHeight("-1px");
    loginLayout.setMargin(false);
    
    // loginGrid
    loginGrid = buildLoginGrid();
    loginLayout.addComponent(loginGrid);
    
    // loginButton
    loginButton = new Button();
    loginButton.setCaption("Zaloguj");
    loginButton.setImmediate(true);
    loginButton.setWidth("-1px");
    loginButton.setHeight("-1px");
    loginLayout.addComponent(loginButton);
    loginLayout.setComponentAlignment(loginButton, new Alignment(9));
    
    return loginLayout;
  }

  @AutoGenerated
  private GridLayout buildLoginGrid() {
    // common part: create layout
    loginGrid = new GridLayout();
    loginGrid.setCaption("Logowanie do aplikacji");
    loginGrid.setImmediate(false);
    loginGrid.setWidth("-1px");
    loginGrid.setHeight("-1px");
    loginGrid.setMargin(true);
    loginGrid.setColumns(2);
    loginGrid.setRows(2);
    
    // userName
    userName = new TextField();
    userName.setCaption("nazwa u�ytkownika");
    userName.setImmediate(false);
    userName.setWidth("-1px");
    userName.setHeight("-1px");
    loginGrid.addComponent(userName, 0, 0);
    
    // password
    password = new PasswordField();
    password.setCaption("has�o");
    password.setImmediate(false);
    password.setWidth("-1px");
    password.setHeight("-1px");
    loginGrid.addComponent(password, 0, 1);
    
    return loginGrid;
  }

  @AutoGenerated
  private HorizontalLayout buildButtonsLayout() {
    // common part: create layout
    buttonsLayout = new HorizontalLayout();
    buttonsLayout.setImmediate(false);
    buttonsLayout.setWidth("-1px");
    buttonsLayout.setHeight("-1px");
    buttonsLayout.setMargin(false);
    buttonsLayout.setSpacing(true);
    
    // remindPasswordButton
    remindPasswordButton = new Button();
    remindPasswordButton.setCaption("Przypomnij has�o");
    remindPasswordButton.setImmediate(true);
    remindPasswordButton.setWidth("-1px");
    remindPasswordButton.setHeight("-1px");
    buttonsLayout.addComponent(remindPasswordButton);
    
    // registerButton
    registerButton = new Button();
    registerButton.setCaption("Rejestracja");
    registerButton.setImmediate(true);
    registerButton.setWidth("-1px");
    registerButton.setHeight("-1px");
    buttonsLayout.addComponent(registerButton);
    
    return buttonsLayout;
  }

}
