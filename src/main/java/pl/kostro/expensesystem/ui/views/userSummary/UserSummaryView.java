package pl.kostro.expensesystem.ui.views.userSummary;

import com.vaadin.data.Binder;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.EditorOpenListener;
import com.vaadin.ui.components.grid.EditorSaveListener;
import lombok.extern.slf4j.Slf4j;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.math.BigDecimal;

@Slf4j
public class UserSummaryView extends UserSummaryDesign {

  private UserSummaryService uss;
  private ExpenseSheetEntity expenseSheet;

  private final Binder<UserSummaryEntity> binder = new Binder<>();
  private final TextField limitField = new TextField();
  private final TextField sumField = new TextField();

  private final SingleSelectionListener<UserLimitEntity> userChanged = event -> {
    if (event.getSelectedItem().isPresent()) {
      userSummaryGrid.setItems(expenseSheet.getUserLimitList().parallelStream()
          .filter(userLimit -> userLimit.equals(event.getSelectedItem().get()))
          .flatMap(userLimit -> userLimit.getUserSummaryList().stream()));
    } else {
      userSummaryGrid.setItems(expenseSheet.getUserLimitList().parallelStream()
          .flatMap(userLimit -> userLimit.getUserSummaryList().stream()));
    }
  };
  private final EditorOpenListener<UserSummaryEntity> editorOpen = event -> binder.setBean(event.getBean());
  private final EditorSaveListener<UserSummaryEntity> saveUserLimit = event -> uss.merge(event.getBean());

  public UserSummaryView() {
    uss = AppCtxProvider.getBean(UserSummaryService.class);
    log.info("create");
    setCaption();
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addSelectionListener(userChanged);

    Binder.Binding<UserSummaryEntity, String> limitBinder = binder.forField(limitField)
        .bind(userSummary -> userSummary.getLimit().toString(), (userSummary, value) -> userSummary.setLimit(new BigDecimal(value.replaceAll(",", "."))));
    Binder.Binding<UserSummaryEntity, String> sumBinder = binder.forField(sumField)
        .bind(userSummary -> userSummary.getSum()+"", (userLimit, value) -> userLimit.setSum(new BigDecimal(value.replaceAll(",", "."))));

    userSummaryGrid.getColumn("limit").setEditorBinding(limitBinder);
    userSummaryGrid.getColumn("sum").setEditorBinding(sumBinder);
    userSummaryGrid.getEditor().setEnabled(true);
    userSummaryGrid.getEditor().setSaveCaption(Msg.get("userSummary.userSave"));
    userSummaryGrid.getEditor().setCancelCaption(Msg.get("userSummary.userCancel"));
    userSummaryGrid.getEditor().addOpenListener(editorOpen);
    userSummaryGrid.getEditor().addSaveListener(saveUserLimit);
    userSummaryGrid.setItems(expenseSheet.getUserLimitList().stream()
        .flatMap(userLimit -> userLimit.getUserSummaryList().stream()));
  }

  private void setCaption() {
    userBox.setCaption(Msg.get("userSummary.user"));
    userBox.setItemCaptionGenerator(item -> item.getUser().getName());
    userSummaryGrid.getColumn("id").setCaption(Msg.get("userSummary.id"));
    userSummaryGrid.getColumn("date").setCaption(Msg.get("userSummary.date"));
    userSummaryGrid.getColumn("limit").setCaption(Msg.get("userSummary.limit"));
    userSummaryGrid.getColumn("sum").setCaption(Msg.get("userSummary.sum"));
  }
}
