package pl.kostro.expensesystem.newui.userSummary;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.editor.EditorOpenListener;
import com.vaadin.flow.component.grid.editor.EditorSaveListener;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.newui.main.MainView;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Route(value = "summary", layout = MainView.class)
@PageTitle("Summary")
public class UserSummaryView extends UserSummaryDesign {

  private Logger logger = LogManager.getLogger();
  private UserSummaryService uss;
  private ExpenseSheetEntity expenseSheet;

  private Binder<UserSummaryEntity> binder = new Binder<>();
  private TextField limitField = new TextField();
  private TextField sumField = new TextField();

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<UserLimitEntity>, UserLimitEntity>> userChanged = event -> {
    if (!event.getHasValue().isEmpty()) {
      userSummaryGrid.setItems(expenseSheet.getUserLimitList().parallelStream()
          .filter(userLimit -> userLimit.equals(event.getValue()))
          .flatMap(userLimit -> userLimit.getUserSummaryList().stream())
          .collect(Collectors.toList()));
    } else {
      userSummaryGrid.setItems(expenseSheet.getUserLimitList().parallelStream()
          .flatMap(userLimit -> userLimit.getUserSummaryList().stream())
          .collect(Collectors.toList()));
    }
  };
  private EditorOpenListener<UserSummaryEntity> editorOpen = event -> binder.setBean(event.getItem());
  private EditorSaveListener<UserSummaryEntity> saveUserLimit = event -> uss.merge(event.getItem());

  public UserSummaryView() {
    uss = AppCtxProvider.getBean(UserSummaryService.class);
    logger.info("create");
    setCaption();
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(userChanged);

    binder.forField(limitField).bind(userSummary -> userSummary.getLimit().toString(), (userSummary, value) -> userSummary.setLimit(new BigDecimal(value.replaceAll(",", "."))));
    binder.forField(sumField).bind(userSummary -> userSummary.getSum()+"", (userLimit, value) -> userLimit.setSum(new BigDecimal(value.replaceAll(",", "."))));

    userSummaryGrid.getColumnByKey("limit").setEditorComponent(limitField);
    userSummaryGrid.getColumnByKey("sum").setEditorComponent(sumField);
//    userSummaryGrid.getEditor().setEnabled(true);
//    userSummaryGrid.getEditor().setSaveCaption(Msg.get("userSummary.userSave"));
//    userSummaryGrid.getEditor().setCancelCaption(Msg.get("userSummary.userCancel"));
    userSummaryGrid.getEditor().addOpenListener(editorOpen);
    userSummaryGrid.getEditor().addSaveListener(saveUserLimit);
    userSummaryGrid.setItems(expenseSheet.getUserLimitList().stream()
        .flatMap(userLimit -> userLimit.getUserSummaryList().stream())
        .collect(Collectors.toList()));
  }

  private void setCaption() {
    userBox.setLabel(Msg.get("userSummary.user"));
    userBox.setItemLabelGenerator(item -> item.getUser().getName());
  }
}
