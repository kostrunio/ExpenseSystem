package pl.kostro.expensesystem.newui.views.userSummary;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.newui.views.main.MainView;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.util.stream.Collectors;

@Route(value = "summary", layout = MainView.class)
@PageTitle("Summary")
public class UserSummaryView extends UserSummaryDesign {

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetEntity expenseSheet;

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

  public UserSummaryView() {
    logger.info("create");
    setCaption();
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(userChanged);

    userSummaryGrid.setItems(expenseSheet.getUserLimitList().stream()
            .flatMap(userLimit -> userLimit.getUserSummaryList().stream())
            .collect(Collectors.toList()));
  }

  private void setCaption() {
    userBox.setLabel(Msg.get("userSummary.user"));
    userBox.setItemLabelGenerator(item -> item.getUser().getName());
  }
}
