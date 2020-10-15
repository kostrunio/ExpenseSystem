package pl.kostro.expensesystem.view;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.Binder;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.EditorOpenListener;
import com.vaadin.ui.components.grid.EditorSaveListener;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.view.design.UserSummaryDesign;

@SuppressWarnings("serial")
public class UserSummaryView extends UserSummaryDesign {

  private Logger logger = LogManager.getLogger();
  private UserSummaryService uss;
  private ExpenseSheet expenseSheet;

  private Binder<UserSummary> binder = new Binder<>();
  private TextField limitField = new TextField();
  private TextField sumField = new TextField();

  private SingleSelectionListener<UserLimit> userChanged = event -> {
    if (event.getSelectedItem().isPresent()) {
      userSummaryGrid.setItems(expenseSheet.getUserLimitList().parallelStream()
          .filter(userLimit -> userLimit.equals(event.getSelectedItem().get()))
          .flatMap(userLimit -> userLimit.getUserSummaryList().stream()));
    } else {
      userSummaryGrid.setItems(expenseSheet.getUserLimitList().parallelStream()
          .flatMap(userLimit -> userLimit.getUserSummaryList().stream()));
    }
  };
  private EditorOpenListener<UserSummary> editorOpen = event -> binder.setBean(event.getBean());
  private EditorSaveListener<UserSummary> saveUserLimit = event -> {
    uss.merge(event.getBean());
  };

  public UserSummaryView() {
    uss = AppCtxProvider.getBean(UserSummaryService.class);
    logger.info("create");
    setCaption();
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addSelectionListener(userChanged);

    Binder.Binding<UserSummary, String> limitBinder = binder.forField(limitField)
        .bind(userSummary -> userSummary.getLimit().toString(), (userSummary, value) -> userSummary.setLimit(new BigDecimal(value.replaceAll(",", "."))));
    Binder.Binding<UserSummary, String> sumBinder = binder.forField(sumField)
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
