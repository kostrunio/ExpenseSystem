package pl.kostro.expensesystem.newui.views.day;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.newui.views.month.MonthView;
import pl.kostro.expensesystem.utils.calculator.Calculator;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.model.CategoryExpense;
import pl.kostro.expensesystem.utils.transform.model.DateExpense;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = "day")
@PageTitle("Day")
public class DayView extends DayDesign {
  
  private ExpenseService es;
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;

  private Logger logger = LogManager.getLogger();
  private ExpenseSheetEntity expenseSheet;
  private LocalDate date;
  private CategoryEntity category;
  private ExpenseEntity expense;
  private boolean modify;

  private ComponentEventListener<ClickEvent<Button>> prevClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.minusDays(1));
    removeAll();
    add(new DayView());
  };
  private ComponentEventListener<ClickEvent<Button>> nextClick = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, date.plusDays(1));
    removeAll();
    add(new DayView());
  };
  private ComponentEventListener<ClickEvent<Button>> backClick = event -> {
    removeAll();
    add(new MonthView());
  };
  private ComponentEventListener<ClickEvent<Button>> categoryClick = event -> {
    /*if (event.getSource().getData() instanceof CategoryEntity) {
      category = (CategoryEntity) event.getButton().getData();
      prepareExpenseListLayout();
    }*/
  };
  private ComponentEventListener<ClickEvent<Button>> valueClick = event -> {
    /*if (event.getButton().getData() instanceof ExpenseEntity) {
      buildAddNewExpense((ExpenseEntity) event.getButton().getData(), true);
    }*/
  };
  private ComponentEventListener<ClickEvent<Button>> removeClick = event -> {
    /*if (event.getButton().getData() instanceof ExpenseEntity) {
      final ExpenseEntity expense = (ExpenseEntity) event.getButton().getData();
      ConfirmDialog.show(
          Msg.get("category.removeLabel"),
          Msg.get("category.removeQuestion"),
          Msg.get("category.removeYes"),
          Msg.get("category.removeNo"),
          dialog -> {
            if (dialog.isConfirmed()) {
              eshts.removeExpense(expense, expenseSheet);
              es.remove(expense);
              prepareCategoryListLayout();
              prepareExpenseListLayout();
            }
          });
    }*/
  };
  private ComponentEventListener<ClickEvent<Button>> saveClick = event -> {
    if (userBox.getValue() instanceof UserLimitEntity) {
      if (modify) {
        eshts.removeExpense(expense, expenseSheet);
        es.remove(expense);
      }
      expense.setUser(userBox.getValue().getUser());
      expense.setFormula(formulaField.getValue().startsWith("=") ? formulaField.getValue().substring(1) : formulaField.getValue());
      if (commentBox.getValue() != null)
        expense.setComment(commentBox.getValue().toString());
      expense.setNotify(notifyBox.getValue());
      expense.setExpenseSheet(expenseSheet);
      es.save(expense);
      eshts.addExpense(expense, expenseSheet);
      prepareCategoryListLayout();
      prepareExpenseListLayout();
    }
  };
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<DatePicker, LocalDate>> dateChanged = event -> {
    VaadinSession.getCurrent().setAttribute(LocalDate.class, event.getValue());
    removeAll();
    add(new DayView());
  };
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<UserLimitEntity>, UserLimitEntity>> verifyUserFormula = event -> verifyFormula(formulaField.getValue());
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<TextField, String>> verifyFormula = event -> verifyFormula(formulaField.getValue());
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>> verifyCommentFormula = event -> verifyFormula(formulaField.getValue());
//  private NewItemProvider addComment = event -> Optional.of(event);

  public DayView() {
    es = AppCtxProvider.getBean(ExpenseService.class);
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    logger.info("create");
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    category = expenseSheet.getCategoryList().get(0);
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class);

    setCaption();
    DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
    singleFormatI18n.setDateFormat("yyyy-MM-dd");
    thisDateField.setI18n(singleFormatI18n);
    thisDateField.setValue(date);
    previousDayButton.addClickListener(prevClick);
    thisDateField.addValueChangeListener(dateChanged);
    nextDayButton.addClickListener(nextClick);

    prepareCategoryListLayout();
    backButton.addClickListener(backClick);

//    userBox.setEmptySelectionAllowed(false);
    userBox.setItemLabelGenerator(item -> item.getUser().getName());
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(verifyUserFormula);
    formulaField.addValueChangeListener(verifyFormula);
//    commentBox.setNewItemProvider(addComment);
//    commentBox.setEmptySelectionAllowed(true);
    commentBox.addValueChangeListener(verifyCommentFormula);
    saveButton.addClickShortcut(Key.ENTER);
    saveButton.addClickListener(saveClick);
    prepareExpenseListLayout();
  }

  private void setCaption() {
    backButton.setText(Msg.get("day.back"));
    userBox.setLabel(Msg.get("newExpense.user"));
    formulaField.setLabel(Msg.get("newExpense.formula"));
    commentBox.setLabel(Msg.get("newExpense.comment"));
    notifyBox.setLabel(Msg.get("newExpense.notify"));
    saveButton.setText(Msg.get("newExpense.save"));
    
  }

  private void prepareCategoryListLayout() {
    categoryGrid.removeAll();
    List<CategoryEntity> categoryList = expenseSheet.getCategoryList();

    for (int i = 0; i < categoryList.size(); i++) {
      VerticalLayout vertLay = new VerticalLayout();
      vertLay.setMargin(false);
      vertLay.setSpacing(false);
      vertLay.setClassName("category-button");
      categoryGrid.add(vertLay);

      CategoryEntity category = categoryList.get(i);
      Label catLabel = new Label();
      catLabel.setText(category.getName());
      vertLay.add(catLabel);
      vertLay.setAlignItems(Alignment.CENTER);

      com.vaadin.flow.component.button.Button expButton = new Button();
      vertLay.add(expButton);
      vertLay.setAlignItems(Alignment.CENTER);
      DateExpense dateExpenseMap = eshts.getDateExpenseMap(expenseSheet, date);
      if (dateExpenseMap == null || dateExpenseMap.getCategoryExpenseMap().get(category) == null)
        expButton.setText("0");
      else {
        CategoryExpense categoryExpenseMap = dateExpenseMap.getCategoryExpenseMap().get(category);
        expButton.setText(categoryExpenseMap.getSumString());
      }
//      expButton.setData(category);
      expButton.addClickListener(categoryClick);
    }
  }

  private void prepareExpenseListLayout() {
//    expenseGrid.removeAllComponents();
    categoryLabel.setText(category.getName());
    List<ExpenseEntity> expenseList;
    DateExpense dateExpenseMap = eshts.getDateExpenseMap(expenseSheet, date);
    if (dateExpenseMap == null || dateExpenseMap.getCategoryExpenseMap().get(category) == null)
      expenseList = new ArrayList<ExpenseEntity>();
    else {
      expenseList = dateExpenseMap.getCategoryExpenseMap().get(category).getExpenseList();
    }
//    expenseGrid.setColumns(5);
//    expenseGrid.setRows(expenseList.size() == 0 ? 1 : expenseList.size());
    expenseGrid.setColumns(5);
    expenseGrid.setRows(expenseList.size() == 0 ? 1 : expenseList.size());

    for (int i = 0; i < expenseList.size(); i++) {
      ExpenseEntity expense = expenseList.get(i);
      Label user = new Label();
      user.setEnabled(false);
      user.setText(expense.getUser().getName());
      expenseGrid.addComponent(user, 0, i);

      Button valueButton = new Button();
      valueButton.setText(expense.getValue().toString());
//      valueButton.setData(expense);
      expenseGrid.addComponent(valueButton, 1, i);

      valueButton.addClickListener(valueClick);

      Label comment = new Label();
      comment.setEnabled(false);
      comment.setText(expense.getComment());
      expenseGrid.addComponent(comment, 2, i);

      Button removeButton = new Button();
      removeButton.setIcon(VaadinIcon.TRASH.create());
      removeButton.addThemeVariants(ButtonVariant.LUMO_ICON);
//      removeButton.setData(expense);
      removeButton.addClickListener(removeClick);
      expenseGrid.addComponent(removeButton, 3, i);
      
      if (expense.isNotify()) {
        Label notifyLabel = new Label();
//        notifyLabel.setIcon(VaadinIcon.ENVELOPE.create());
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
      userBox.setValue(eshts.getUserLimitForUser(expense.getUser(), expenseSheet));
    else
      userBox.setValue(expenseSheet.getUserLimitList().get(0));

    formulaField.focus();
    formulaField.setValue(expense.getFormula());

    commentBox.setItems((itemCaption, filterText) -> itemCaption.contains(filterText), eshts.getCommentForCategory(expenseSheet, expense.getCategory()));
    commentBox.setValue(expense.getComment());
    
    if (expense.isNotify() || expense.getDate().isAfter(LocalDate.now())) {
      notifyBox.setValue(expense.isNotify());
      notifyBox.setVisible(true);
    }
  }
}
