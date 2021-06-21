package pl.kostro.expensesystem.view;

import java.text.MessageFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.ExpenseSystemUI;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUserEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.view.design.SettingsDesign;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetEditPasswordWindow;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetEditPasswordWindow.ExpenseSheetPasswordChangeListener;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetEditWindow;
import pl.kostro.expensesystem.views.settingsPage.ExpenseSheetEditWindow.ExpenseSheetEditListener;

@SuppressWarnings("serial")
public class SettingsView extends SettingsDesign implements ExpenseSheetEditListener, ExpenseSheetPasswordChangeListener {

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetService eshs;
  private RealUserService rus;
  private ExpenseSheet expenseSheet;
  private ClickListener editClick = event -> UI.getCurrent().addWindow(new ExpenseSheetEditWindow(SettingsView.this, expenseSheet));
  private ClickListener passwordClick = event -> UI.getCurrent().addWindow(new ExpenseSheetEditPasswordWindow(SettingsView.this));
  private ClickListener removeClick = event -> {
    ConfirmDialog.show(getUI(),
        Msg.get("settingsPage.removeSheetLabel"),
        MessageFormat.format(Msg.get("settingsPage.removeSheetQuestion"), expenseSheet.getName()),
        Msg.get("settingsPage.removeSheetYes"),
        Msg.get("settingsPage.removeSheetNo"),
        dialog -> {
          if (dialog.isConfirmed()) {
            eshs.removeExpenseSheet(expenseSheet);
            RealUserEntity loggedUser = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
            loggedUser = rus.refresh(loggedUser);
            if (loggedUser.getDefaultExpenseSheet() == null && loggedUser.getExpenseSheetList().size() != 0)
              rus.setDefaultExpenseSheet(loggedUser, loggedUser.getExpenseSheetList().get(0));
            VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, null);
            ((ExpenseSystemUI)getUI()).getMainView().refresh();
            UI.getCurrent().getNavigator().navigateTo("");
          }
        });
  };
  private ClickListener backClick = event -> ((ExpenseSystemUI) getUI()).updateContent();

  public SettingsView() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    rus = AppCtxProvider.getBean(RealUserService.class);
    logger.info("create");
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
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
      titleLabel.setValue(expenseSheet.getName());
      categoryGrid.refreshValues();
      realUserGrid.refreshValues();
      userGrid.refreshValues();
    } else {
      headerLayout.setVisible(false);
    }
  }
  
  private void setCaption() {
    editButton.setDescription(Msg.get("settingsPage.changeName"));
    passwordButton.setDescription(Msg.get("settingsPage.changePassword"));
    removeButton.setDescription(Msg.get("settingsPage.removeSheet"));
    categoryPanel.setCaption(Msg.get("settingsPage.categoryLabel"));
    realUserPanel.setCaption(Msg.get("settingsPage.realUserLabel"));
    userPanel.setCaption(Msg.get("settingsPage.userLabel"));
    backButton.setCaption(Msg.get("settingsPage.back"));
  }
  
  private void disableAllButtons() {
    moveUpCategoryButton.setEnabled(false);
    moveDownCategoryButton.setEnabled(false);
    deleteCategoryButton.setEnabled(false);
    deleteRealUserButton.setEnabled(false);
    deleteUserButton.setEnabled(false);
  }

  @Override
  public void expenseSheetNameEdited(TextField nameField) {
    expenseSheet.setName(nameField.getValue());
    eshs.merge(expenseSheet);
    ((ExpenseSystemUI)getUI()).getMainView().refresh();
    titleLabel.setValue(nameField.getValue());
  }

  @Override
  public void expenseSheetPasswordChanged(String newPassword) {
    eshs.decrypt(expenseSheet);
    expenseSheet.setSecretKey(newPassword);
    eshs.encrypt(expenseSheet);
  }

}
