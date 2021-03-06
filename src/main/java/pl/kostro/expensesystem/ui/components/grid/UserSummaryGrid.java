package pl.kostro.expensesystem.ui.components.grid;

import com.vaadin.ui.Grid;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;

public class UserSummaryGrid extends Grid<UserSummaryEntity> {

    public UserSummaryGrid() {
        setSizeFull();
        setColumnId("id", addColumn(UserSummaryEntity::getId).setSortable(true));
        setColumnId("date", addColumn(UserSummaryEntity::getDate).setSortable(true));
        setColumnId("limit", addColumn(UserSummaryEntity::getLimit).setSortable(true));
        setColumnId("sum", addColumn(UserSummaryEntity::getSum).setSortable(true));
    }
}
