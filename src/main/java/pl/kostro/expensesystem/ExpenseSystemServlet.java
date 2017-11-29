package pl.kostro.expensesystem;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.VaadinServlet;

import pl.kostro.expensesystem.utils.SendEmail;

@SuppressWarnings("serial")
public class ExpenseSystemServlet extends VaadinServlet {

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