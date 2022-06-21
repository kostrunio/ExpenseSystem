package pl.kostro.expensesystem.newui.component.grid;

import com.vaadin.flow.component.grid.Grid;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;
import pl.kostro.expensesystem.utils.msg.Msg;

public class UserSummaryGrid extends Grid<UserSummaryEntity> {

    public UserSummaryGrid() {
        setSizeFull();
        addColumn(UserSummaryEntity::getId).setHeader(Msg.get("userSummary.id")).setSortable(true);
        addColumn(UserSummaryEntity::getDate).setHeader(Msg.get("userSummary.date")).setSortable(true);
        addColumn(UserSummaryEntity::getLimit).setHeader(Msg.get("userSummary.limit")).setSortable(true);
        addColumn(UserSummaryEntity::getSum).setHeader(Msg.get("userSummary.sum")).setSortable(true);
    }
}
