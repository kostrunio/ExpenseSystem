package pl.kostro.expensesystem.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox.NewItemHandler;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.components.dialog.ConfirmDialog;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.Calculator;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.view.design.DayDesign;

@SuppressWarnings("serial")
public class DayView extends DayDesign {
  
  private ExpenseService es;
  private ExpenseSheetService eshs;

  private Logger logger = LogManager.getLogger();
  private ExpenseSheet expenseSheet;
  private LocalDate date;
  private Category category;
  private Expense expense;
  private boolean modify;

  private Button.ClickListener prevClick = event -> {
    date = date.minusDays(1);
    removeAllComponents();
    addComponent(new DayView());
  };
  private Button.ClickListener nextClick = event -> {
    date = date.plusDays(1);
    removeAllComponents();
    addComponent(new DayView());
  };
  private Button.ClickListener backClick = event -> {
    removeAllComponents();
    addComponent(new MonthView());
  };
  private Button.ClickListener categoryClick = event -> {
    if (event.getButton().getData() instanceof Category) {
      category = (Category) event.getButton().getData();
      prepareExpenseListLayout();
    }
  };
  private Button.ClickListener valueClick = event -> {
    if (event.getButton().getData() instanceof Expense) {
      buildAddNewExpense((Expense) event.getButton().getData(), true);
    }
  };
  private Button.ClickListener removeClick = event -> {
    if (event.getButton().getData() instanceof Expense) {
      final Expense expense = (Expense) event.getButton().getData();
      ConfirmDialog.show(getUI(), Msg.get("category.removeLabel"), Msg.get("category.removeQuestion"),
          Msg.get("category.removeYes"), Msg.get("category.removeNo"), dialog -> {
            if (dialog.isConfirmed()) {
              es.removeExpense(expenseSheet, expense);
              prepareCategoryListLayout();
              prepareExpenseListLayout();
            }
          });
    }
  };
  private Button.ClickListener saveClick = event -> {
    if (userBox.getValue() instanceof UserLimit) {
      es.saveExpense(expenseSheet, expense, userBox.getValue(), formulaField.getValue(),
          commentBox.getValue(), notifyBox.getValue(), modify);
      prepareCategoryListLayout();
      prepareExpenseListLayout();
    }
  };
  private ValueChangeListener<LocalDate> dateChanged = event -> {
    date = event.getValue();
    removeAllComponents();
    addComponent(new DayView());
  };
  @SuppressWarnings("rawtypes")
  private ValueChangeListener verifyFormula = event -> verifyFormula(formulaField.getValue());
  private NewItemHandler addComment = event -> {};

  @SuppressWarnings("unchecked")
  public DayView() {
    es = AppCtxProvider.getBean(ExpenseService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    logger.info("create");
    this.expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheet.class);
    this.category = expenseSheet.getCategoryList().get(0);
    this.date = VaadinSession.getCurrent().getAttribute(LocalDate.class);

    setCaption();
    thisDateField.setDateFormat("yyyy-MM-dd");
    thisDateField.setValue(date);
    previousDayButton.addClickListener(prevClick);
    thisDateField.addValueChangeListener(dateChanged);
    nextDayButton.addClickListener(nextClick);

    prepareCategoryListLayout();
    backButton.addClickListener(backClick);

    userBox.setEmptySelectionAllowed(false);
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(verifyFormula);
    formulaField.addValueChangeListener(verifyFormula);
    commentBox.setNewItemHandler(addComment);
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
    List<Category> categoryList = expenseSheet.getCategoryList();

    for (int i = 0; i < categoryList.size(); i++) {
      VerticalLayout vertLay = new VerticalLayout();
      vertLay.setMargin(false);
      vertLay.setSpacing(false);
      vertLay.addStyleName("category-button");
      categoryGrid.addComponent(vertLay);

      Category category = categoryList.get(i);
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
    List<Expense> expenseList;
    DateExpense dateExpenseMap = eshs.getDateExpenseMap(expenseSheet, date);
    if (dateExpenseMap == null || dateExpenseMap.getCategoryExpenseMap().get(category) == null)
      expenseList = new ArrayList<Expense>();
    else {
      expenseList = dateExpenseMap.getCategoryExpenseMap().get(category).getExpenseList();
    }
    expenseGrid.setColumns(5);
    expenseGrid.setRows(expenseList.size() == 0 ? 1 : expenseList.size());

    for (int i = 0; i < expenseList.size(); i++) {
      Expense expense = expenseList.get(i);
      Label user = new Label();
      user.setEnabled(false);
      user.setValue(expense.getUser().getName());
      expenseGrid.addComponent(user, 0, i);

      Button valueButton = new Button();
      valueButton.setCaption(es.getValueString(expense));
      valueButton.setData(expense);
      expenseGrid.addComponent(valueButton, 1, i);

      valueButton.addClickListener(valueClick);

      Label comment = new Label();
      comment.setEnabled(false);
//      comment.setNullRepresentation("");
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
    buildAddNewExpense(es.prepareNewExpense(expenseSheet, date, category,
        expenseSheet.getUserLimitList().get(0).getUser()), false);
  }

  private void verifyFormula(Object formula) {
    if (formula != null && !formula.toString().equals("") && Calculator.verifyAllowed(formula.toString()))
      saveButton.setEnabled(true);
    else
      saveButton.setEnabled(false);
  }

  public void buildAddNewExpense(Expense expense, Boolean modify) {
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
