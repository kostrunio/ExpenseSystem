package pl.kostro.expensesystem.newui.settings;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.newui.dialog.ConfirmDialog;
import pl.kostro.expensesystem.newui.main.MainView;
import pl.kostro.expensesystem.newui.settingsPage.ExpenseSheetEditListener;
import pl.kostro.expensesystem.newui.settingsPage.ExpenseSheetEditPasswordWindow;
import pl.kostro.expensesystem.newui.settingsPage.ExpenseSheetEditWindow;
import pl.kostro.expensesystem.newui.settingsPage.ExpenseSheetPasswordChangeListener;
import pl.kostro.expensesystem.utils.msg.Msg;

import java.text.MessageFormat;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings")
public class SettingsView extends SettingsDesign implements ExpenseSheetEditListener, ExpenseSheetPasswordChangeListener {

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetService eshs;
  private RealUserService rus;
  private ExpenseSheetEntity expenseSheet;
  private ComponentEventListener<ClickEvent<Button>> editClick = event -> new ExpenseSheetEditWindow(SettingsView.this, expenseSheet).open();
  private ComponentEventListener<ClickEvent<Button>> passwordClick = event -> new ExpenseSheetEditPasswordWindow(SettingsView.this).open();
  private ComponentEventListener<ClickEvent<Button>> removeClick = event -> {
    ConfirmDialog.show(Msg.get("settingsPage.removeSheetLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeSheetQuestion"), expenseSheet.getName()),
        Msg.get("settingsPage.removeSheetYes"),
        Msg.get("settingsPage.removeSheetNo"),
        dialog -> {
          if (dialog.isConfirmed()) {
            rus.removeExpenseSheetFromUsers(expenseSheet);
            eshs.removeExpenseSheet(expenseSheet);
            RealUserEntity loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
            loggedUser = rus.refresh(loggedUser);
            if (loggedUser.getDefaultExpenseSheet() == null && loggedUser.getExpenseSheetList().size() != 0)
              rus.setDefaultExpenseSheet(loggedUser, loggedUser.getExpenseSheetList().get(0));
            VaadinSession.getCurrent().setAttribute(ExpenseSheetEntity.class, null);
//            ((ExpenseSystemUI)getUI()).getMainView().refresh();
            UI.getCurrent().navigate("");
          }
        });
  };
  private ComponentEventListener<ClickEvent<Button>> backClick = event -> UI.getCurrent().getPage().reload();

  public SettingsView() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    rus = AppCtxProvider.getBean(RealUserService.class);
    logger.info("create");
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    setCaption();
    editButton.addClickListener(editClick);
    passwordButton.addClickListener(passwordClick);
    removeButton.addClickListener(removeClick);
    categoryGrid.setAddCategoryButton(addCategoryButton);
    categoryGrid.setMoveUpCategoryButton(moveUpCategoryButton);
    categoryGrid.setMoveDownCategoryButton(moveDownCategoryButton);
    categoryGrid.setDeleteCategoryButton(deleteCategoryButton);
    realUserGrid.setAddUserLimitButton(addRealUserButton);
    realUserGrid.setDeleteUserLimitButton(deleteRealUserButton);
    userGrid.setAddUserLimitButton(addUserButton);
    userGrid.setDeleteUserLimitButton(deleteUserButton);
    backButton.addClickListener(backClick);
    disableAllButtons();
    
    if (expenseSheet != null) {
      titleLabel.setText(expenseSheet.getName());
      categoryGrid.refreshValues();
      realUserGrid.refreshValues();
      userGrid.refreshValues();
    } else {
      headerLayout.setVisible(false);
    }
  }
  
  private void setCaption() {
//    editButton.setText(Msg.get("settingsPage.changeName"));
//    passwordButton.setText(Msg.get("settingsPage.changePassword"));
//    removeButton.setText(Msg.get("settingsPage.removeSheet"));
    categoryLabel.setText(Msg.get("settingsPage.categoryLabel"));
    realUserLabel.setText(Msg.get("settingsPage.realUserLabel"));
    userLabel.setText(Msg.get("settingsPage.userLabel"));
    backButton.setText(Msg.get("settingsPage.back"));
  }
  
  private void disableAllButtons() {
    moveUpCategoryButton.setEnabled(false);
    moveDownCategoryButton.setEnabled(false);
    deleteCategoryButton.setEnabled(false);
    deleteRealUserButton.setEnabled(false);
    deleteUserButton.setEnabled(false);
  }

  @Override
  public void expenseSheetNameEdited(String nameName) {
    expenseSheet.setName(nameName);
    eshs.merge(expenseSheet);
    UI.getCurrent().getPage().reload();
    titleLabel.setText(nameName);
  }

  @Override
  public void expenseSheetPasswordChanged(String newPassword) {
    eshs.decrypt(expenseSheet);
    expenseSheet.setSecretKey(newPassword);
    eshs.encrypt(expenseSheet);
  }

}
