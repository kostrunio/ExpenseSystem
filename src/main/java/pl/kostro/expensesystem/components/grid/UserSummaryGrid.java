package pl.kostro.expensesystem.components.grid;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import pl.kostro.expensesystem.model.UserSummary;

public class UserSummaryGrid extends Grid<UserSummary> {

    public UserSummaryGrid() {
        setSizeFull();
        setColumnId("id", addColumn(UserSummary::getId).setSortable(true));
        setColumnId("date", addColumn(UserSummary::getDate).setSortable(true));
        setColumnId("limit", addColumn(UserSummary::getLimit).setSortable(true));
        setColumnId("sum", addColumn(UserSummary::getSum).setSortable(true));
    }
}
