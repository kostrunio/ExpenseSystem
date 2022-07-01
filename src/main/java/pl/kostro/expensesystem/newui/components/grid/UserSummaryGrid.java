package pl.kostro.expensesystem.newui.components.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.EditorOpenListener;
import com.vaadin.flow.component.grid.editor.EditorSaveListener;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.math.BigDecimal;

public class UserSummaryGrid extends Grid<UserSummaryEntity> {
    private UserSummaryService uss;

    private Binder<UserSummaryEntity> binder = new Binder<>();
    private TextField limitField = new TextField();
    private TextField sumField = new TextField();

    private EditorOpenListener<UserSummaryEntity> editorOpen = event -> binder.setBean(event.getItem());
    private EditorSaveListener<UserSummaryEntity> saveUserLimit = event -> uss.merge(event.getItem());


    public UserSummaryGrid() {
        uss = AppCtxProvider.getBean(UserSummaryService.class);
        setWidthFull();
        binder.forField(limitField).bind(userSummary -> userSummary.getLimit().toString(), (userSummary, value) -> userSummary.setLimit(new BigDecimal(value.replaceAll(",", "."))));
        binder.forField(sumField).bind(userSummary -> userSummary.getSum()+"", (userLimit, value) -> userLimit.setSum(new BigDecimal(value.replaceAll(",", "."))));

        addColumn(UserSummaryEntity::getId).setHeader(Msg.get("userSummary.id")).setSortable(true);
        addColumn(UserSummaryEntity::getDate).setHeader(Msg.get("userSummary.date")).setSortable(true);
        addColumn(UserSummaryEntity::getLimit).setHeader(Msg.get("userSummary.limit")).setSortable(true).setEditorComponent(limitField);
        addColumn(UserSummaryEntity::getSum).setHeader(Msg.get("userSummary.sum")).setSortable(true).setEditorComponent(sumField);

//    userSummaryGrid.getEditor().setEnabled(true);
//    userSummaryGrid.getEditor().setSaveCaption(Msg.get("userSummary.userSave"));
//    userSummaryGrid.getEditor().setCancelCaption(Msg.get("userSummary.userCancel"));
        getEditor().addOpenListener(editorOpen);
        getEditor().addSaveListener(saveUserLimit);
    }
}
