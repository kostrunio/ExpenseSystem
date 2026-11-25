package pl.kostro.expensesystem.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("ExpenseSystem")
public class ExpenseSystemView extends VerticalLayout {

    public ExpenseSystemView() {
        setWidthFull();
        setMaxWidth("900px");
        setPadding(true);
        setSpacing(true);

        add(new H1("ExpenseSystem"));
        add(new Paragraph(
                "The Vaadin Flow 25 migration foundation is in place. The existing Jakarta-compatible "
                        + "domain model, repositories, and services are retained."));

        add(new H2("Feature migration status"));
        add(new Paragraph(
                "The original Vaadin 8 screens are not yet ported: login and registration; daily and "
                        + "monthly expense entry; charts, tables, and user summaries; account management; "
                        + "and expense-sheet, category, user, and limit settings."));
        add(new Paragraph(
                "Expense data is intentionally not exposed until the original authentication and "
                        + "authorization flows are migrated."));
    }
}
