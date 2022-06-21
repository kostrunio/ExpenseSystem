package pl.kostro.expensesystem.newui.component.grid;

import com.vaadin.flow.component.grid.Grid;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;

public class UserSummaryGrid extends Grid<UserSummaryEntity> {

    public UserSummaryGrid() {
        setSizeFull();
        addColumn(UserSummaryEntity::getId).setHeader("id").setSortable(true);
        addColumn(UserSummaryEntity::getDate).setHeader("date").setSortable(true);
        addColumn(UserSummaryEntity::getLimit).setHeader("limit").setSortable(true);
        addColumn(UserSummaryEntity::getSum).setHeader("sum").setSortable(true);
    }
}
