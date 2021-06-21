package pl.kostro.expensesystem.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox.NewItemProvider;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.CategoryEntity;
import pl.kostro.expensesystem.model.ExpenseEntity;
import pl.kostro.expensesystem.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Calculator;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.view.design.DayDesign;

public class DayView extends DayDesign {
  
  private ExpenseService es;
  private ExpenseSheetService eshs;

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetEntity expenseSheet;
  private LocalDate date;
  private CategoryEntity category;
  private ExpenseEntity expense;
  private boolean modify;

  private Button.ClickListener prevClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.minusDays(1));
    removeAllComponents();
    addComponent(new DayView());
  };
  private Button.ClickListener nextClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.plusDays(1));
    removeAllComponents();
    addComponent(new DayView());
  };
  private Button.ClickListener backClick = event -> {
    removeAllComponents();
    addComponent(new MonthView());
  };
  private Button.ClickListener categoryClick = event -> {
    if (event.getButton().getData() instanceof CategoryEntity) {
      category = (CategoryEntity) event.getButton().getData();
      prepareExpenseListLayout();
    }
  };
  private Button.ClickListener valueClick = event -> {
    if (event.getButton().getData() instanceof ExpenseEntity) {
      buildAddNewExpense((ExpenseEntity) event.getButton().getData(), true);
    }
  };
  private Button.ClickListener removeClick = event -> {
    if (event.getButton().getData() instanceof ExpenseEntity) {
      final ExpenseEntity expense = (ExpenseEntity) event.getButton().getData();
      ConfirmDialog.show(getUI(), Msg.get("category.removeLabel"), Msg.get("category.removeQuestion"),
          Msg.get("category.removeYes"), Msg.get("category.removeNo"), dialog -> {
            if (dialog.isConfirmed()) {
              eshs.removeExpense(expense, expenseSheet);
              es.remove(expense);
              prepareCategoryListLayout();
              prepareExpenseListLayout();
            }
          });
    }
  };
  private Button.ClickListener saveClick = event -> {
    if (userBox.getValue() instanceof UserLimitEntity) {
      if (modify) {
        eshs.removeExpense(expense, expenseSheet);
        es.remove(expense);
      }
      expense.setUser(userBox.getValue().getUser());
      expense.setFormula(formulaField.getValue().startsWith("=") ? formulaField.getValue().substring(1) : formulaField.getValue());
      if (commentBox.getValue() != null)
        expense.setComment(commentBox.getValue().toString());
      expense.setNotify(notifyBox.getValue());
      expense.setExpenseSheet(expenseSheet);
      es.save(expense);
      eshs.addExpense(expense, expenseSheet);
      prepareCategoryListLayout();
      prepareExpenseListLayout();
    }
  };
  private ValueChangeListener<LocalDate> dateChanged = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getValue());
    removeAllComponents();
    addComponent(new DayView());
  };
  private ValueChangeListener verifyFormula = event -> verifyFormula(formulaField.getValue());
  private NewItemProvider addComment = event -> Optional.of(event);

  public DayView() {
    es = AppCtxProvider.getBean(ExpenseService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    logger.info("create");
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    category = expenseSheet.getCategoryList().get(0);
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class);

    setCaption();
    thisDateField.setDateFormat("yyyy-MM-dd");
    thisDateField.setValue(date);
    previousDayButton.addClickListener(prevClick);
    thisDateField.addValueChangeListener(dateChanged);
    nextDayButton.addClickListener(nextClick);

    prepareCategoryListLayout();
    backButton.addClickListener(backClick);

    userBox.setEmptySelectionAllowed(false);
    userBox.setItemCaptionGenerator(item -> item.getUser().getName());
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(verifyFormula);
    formulaField.addValueChangeListener(verifyFormula);
    commentBox.setNewItemProvider(addComment);
    commentBox.setEmptySelectionAllowed(true);
    commentBox.addValueChangeListener(verifyFormula);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(saveClick);
    prepareExpenseListLayout();
  }

  private void setCaption() {
    backButton.setCaption(Msg.get("day.back"));
    userBox.setCaption(Msg.get("newExpense.user"));
    formulaField.setCaption(Msg.get("newExpense.formula"));
    commentBox.setCaption(Msg.get("newExpense.comment"));
    notifyBox.setCaption(Msg.get("newExpense.notify"));
    saveButton.setCaption(Msg.get("newExpense.save"));
    
  }

  private void prepareCategoryListLayout() {
    categoryGrid.removeAllComponents();
    List<CategoryEntity> categoryList = expenseSheet.getCategoryList();

    for (int i = 0; i < categoryList.size(); i++) {
      VerticalLayout vertLay = new VerticalLayout();
      vertLay.setMargin(false);
      vertLay.setSpacing(false);
      vertLay.addStyleName("category-button");
      categoryGrid.addComponent(vertLay);

      CategoryEntity category = categoryList.get(i);
      Label catLabel = new Label();
      catLabel.setValue(category.getName());
      vertLay.addComponent(catLabel);
      vertLay.setComponentAlignment(catLabel, Alignment.TOP_CENTER);

      Button expButton = new Button();
      vertLay.addComponent(expButton);
      vertLay.setComponentAlignment(expButton, Alignment.TOP_CENTER);
      DateExpense dateExpenseMap = eshs.getDateExpenseMap(expenseSheet, date);
      if (dateExpenseMap == null || dateExpenseMap.getCategoryExpenseMap().get(category) == null)
        expButton.setCaption("0");
      else {
        CategoryExpense categoryExpenseMap = dateExpenseMap.getCategoryExpenseMap().get(category);
        expButton.setCaption(categoryExpenseMap.getSumString());
      }
      expButton.setData(category);
      expButton.addClickListener(categoryClick);
    }
  }

  private void prepareExpenseListLayout() {
    expenseGrid.removeAllComponents();
    categoryLabel.setValue(category.getName());
    List<ExpenseEntity> expenseList;
    DateExpense dateExpenseMap = eshs.getDateExpenseMap(expenseSheet, date);
    if (dateExpenseMap == null || dateExpenseMap.getCategoryExpenseMap().get(category) == null)
      expenseList = new ArrayList<ExpenseEntity>();
    else {
      expenseList = dateExpenseMap.getCategoryExpenseMap().get(category).getExpenseList();
    }
    expenseGrid.setColumns(5);
    expenseGrid.setRows(expenseList.size() == 0 ? 1 : expenseList.size());

    for (int i = 0; i < expenseList.size(); i++) {
      ExpenseEntity expense = expenseList.get(i);
      Label user = new Label();
      user.setEnabled(false);
      user.setValue(expense.getUser().getName());
      expenseGrid.addComponent(user, 0, i);

      Button valueButton = new Button();
      valueButton.setCaption(expense.getValue().toString());
      valueButton.setData(expense);
      expenseGrid.addComponent(valueButton, 1, i);

      valueButton.addClickListener(valueClick);

      Label comment = new Label();
      comment.setEnabled(false);
      comment.setValue(expense.getComment());
      expenseGrid.addComponent(comment, 2, i);

      Button removeButton = new Button();
      removeButton.setIcon(VaadinIcons.TRASH);
      removeButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
      removeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
      removeButton.setData(expense);
      removeButton.addClickListener(removeClick);
      expenseGrid.addComponent(removeButton, 3, i);
      
      if (expense.isNotify()) {
        Label notifyLabel = new Label();
        notifyLabel.setIcon(VaadinIcons.ENVELOPE);
        expenseGrid.addComponent(notifyLabel, 4, i);
      }
    }
    buildAddNewExpense(new ExpenseEntity(date, "", category, expenseSheet.getUserLimitList().get(0).getUser(), "", date.isAfter(LocalDate.now()) ? true : false, expenseSheet), false);
  }

  private void verifyFormula(Object formula) {
    if (formula != null && !formula.toString().equals("") && Calculator.verifyAllowed(formula.toString()))
      saveButton.setEnabled(true);
    else
      saveButton.setEnabled(false);
  }

  public void buildAddNewExpense(ExpenseEntity expense, Boolean modify) {
    this.expense = expense;
    this.modify = modify;
    
    if (expense.getUser() != null)
      userBox.setSelectedItem(eshs.getUserLimitForUser(expenseSheet, expense.getUser()));
    else
      userBox.setSelectedItem(expenseSheet.getUserLimitList().get(0));

    formulaField.focus();
    formulaField.setValue(expense.getFormula());

    commentBox.setItems((itemCaption, filterText) -> itemCaption.contains(filterText), eshs.getCommentForCategory(expenseSheet, expense.getCategory()));
    commentBox.setSelectedItem(expense.getComment());
    
    if (expense.isNotify() || expense.getDate().isAfter(LocalDate.now())) {
      notifyBox.setValue(expense.isNotify());
      notifyBox.setVisible(true);
    }
  }
}
