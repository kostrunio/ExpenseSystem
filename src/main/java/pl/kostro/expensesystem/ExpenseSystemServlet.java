package pl.kostro.expensesystem;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.vaadin.spring.server.SpringVaadinServlet;

import pl.kostro.expensesystem.utils.SendEmail;

@Component("vaadinServlet")
public class ExpenseSystemServlet extends SpringVaadinServlet {

  private static final long serialVersionUID = 3389588943611093398L;

    @Override
    protected final void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new ExpenseSystemSessionInitListener());
    }

    @Override
    protected void service(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
      try {
        super.service(request, response);
      } catch (Exception | Error e) {
        SendEmail.exception(e.getMessage(), e.getStackTrace());
        throw (e);
      }
    }
}