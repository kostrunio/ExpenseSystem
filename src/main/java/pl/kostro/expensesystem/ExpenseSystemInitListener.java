package pl.kostro.expensesystem;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.newui.login.LoginView;

/**
 * This class is used to listen to BeforeEnter event of all UIs in order to
 * check whether a user is signed in or not before allowing entering any page.
 * It is registered in a file named
 * com.vaadin.flow.server.VaadinServiceInitListener in META-INF/services.
 */
public class ExpenseSystemInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent initEvent) {

        initEvent.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
                if (VaadinSession.getCurrent().getAttribute(RealUserEntity.class) == null)
                    enterEvent.rerouteTo(LoginView.class);
            });
        });
    }
}
