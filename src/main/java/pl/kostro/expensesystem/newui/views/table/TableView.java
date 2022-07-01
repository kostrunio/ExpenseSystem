package pl.kostro.expensesystem.newui.views.table;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.*;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.newui.views.main.MainView;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = "table", layout = MainView.class)
@PageTitle("Table")
public class TableView extends TableDesign {
  
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;

  private Logger logger = LogManager.getLogger();
  private LocalDate date;
  private ExpenseSheetEntity expenseSheet;
  private FooterRow footer = expenseGrid.prependFooterRow();
  private Grid.Column<ExpenseEntity> dateColumn;
  private Grid.Column<ExpenseEntity> categoryColumn;
  private Grid.Column<ExpenseEntity> formulaColumn;
  private Grid.Column<ExpenseEntity> valueColumn;
  
//  private StreamResource exportData = new StreamResource((StreamResource.StreamSource) () -> Exporter.exportAsExcel(expenseGrid), "export.xls");
//  private FileDownloader excelFileDownloader = new FileDownloader(exportData);
  
  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<DatePicker, LocalDate>> filterDateChanged = event -> {
    UserEntity filterUser = null;
    if (userBox.getValue() instanceof UserLimitEntity) {
      filterUser = userBox.getValue().getUser();
    }
    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(categoryBox.getValue());
    List<UserEntity> users = new ArrayList<>();
    users.add(filterUser);
    expenseSheet.setFilter(new Filter(
        fromDateField.getValue(),
        toDateField.getValue(),
        categories,
        users,
        formulaField.getValue(),
        commentBox.getValue()));
    refreshExpenses();
  };

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<CategoryEntity>, CategoryEntity>> filterCategoryChanged = event -> {
    UserEntity filterUser = null;
    if (userBox.getValue() instanceof UserLimitEntity) {
      filterUser = userBox.getValue().getUser();
    }
    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(categoryBox.getValue());
    List<UserEntity> users = new ArrayList<>();
    users.add(filterUser);
    expenseSheet.setFilter(new Filter(
            fromDateField.getValue(),
            toDateField.getValue(),
            categories,
            users,
            formulaField.getValue(),
            commentBox.getValue()));
    refreshExpenses();
  };

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<UserLimitEntity>, UserLimitEntity>> filterUserChanged = event -> {
    UserEntity filterUser = null;
    if (userBox.getValue() instanceof UserLimitEntity) {
      filterUser = userBox.getValue().getUser();
    }
    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(categoryBox.getValue());
    List<UserEntity> users = new ArrayList<>();
    users.add(filterUser);
    expenseSheet.setFilter(new Filter(
            fromDateField.getValue(),
            toDateField.getValue(),
            categories,
            users,
            formulaField.getValue(),
            commentBox.getValue()));
    refreshExpenses();
  };

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<TextField, String>> filterFormulaChanged = event -> {
    UserEntity filterUser = null;
    if (userBox.getValue() instanceof UserLimitEntity) {
      filterUser = userBox.getValue().getUser();
    }
    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(categoryBox.getValue());
    List<UserEntity> users = new ArrayList<>();
    users.add(filterUser);
    expenseSheet.setFilter(new Filter(
            fromDateField.getValue(),
            toDateField.getValue(),
            categories,
            users,
            formulaField.getValue(),
            commentBox.getValue()));
    refreshExpenses();
  };

  private HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>> filterCommentChanged = event -> {
    UserEntity filterUser = null;
    if (userBox.getValue() instanceof UserLimitEntity) {
      filterUser = userBox.getValue().getUser();
    }
    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(categoryBox.getValue());
    List<UserEntity> users = new ArrayList<>();
    users.add(filterUser);
    expenseSheet.setFilter(new Filter(
            fromDateField.getValue(),
            toDateField.getValue(),
            categories,
            users,
            formulaField.getValue(),
            commentBox.getValue()));
    refreshExpenses();
  };

  private
  ComponentEventListener<ClickEvent<Button>> newClicked = event -> expenseForm.edit(new ExpenseEntity());
  private SelectionListener<Grid<ExpenseEntity>, ExpenseEntity> itemClicked = event -> {
    if (expenseGrid.getSelectedItems().size() != 0)
      expenseForm.edit(expenseGrid.getSelectedItems().iterator().next());
    else
      expenseForm.setVisible(false);
  };
//  private NewItemProvider addComment = event -> Optional.of(event);
  
  public TableView() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    logger.info("create");
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class);
    
//    excelFileDownloader.extend(exportButton);
    
    setCaption();
    DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
    singleFormatI18n.setDateFormat("yyyy-MM-dd");
    fromDateField.setI18n(singleFormatI18n);
    toDateField.setI18n(singleFormatI18n);
    expenseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

    expenseForm.prepare(expenseSheet, this);
    
    fromDateField.setValue(date.withDayOfMonth(1));
    fromDateField.addValueChangeListener(filterDateChanged);
    toDateField.setValue(date.withDayOfMonth(date.lengthOfMonth()));
    toDateField.addValueChangeListener(filterDateChanged);
    categoryBox.setItemLabelGenerator(item -> item.getName());
    categoryBox.setItems(expenseSheet.getCategoryList());
    categoryBox.addValueChangeListener(filterCategoryChanged);
    userBox.setItemLabelGenerator(item -> item.getUser().getName());
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(filterUserChanged);
    formulaField.addValueChangeListener(filterFormulaChanged);
//    commentBox.setNewItemProvider(addComment);
    commentBox.setItems((itemCaption, filterText) -> itemCaption.contains(filterText), eshts.getAllComments(expenseSheet));
    commentBox.addValueChangeListener(filterCommentChanged);
    newExpenseButton.addClickListener(newClicked);
    expenseGrid.addSelectionListener(itemClicked);
    
    if (expenseSheet.getFilter() != null) {
      if (expenseSheet.getFilter().getCategories() != null
          && expenseSheet.getFilter().getCategories().size() > 0)
        categoryBox.setValue(expenseSheet.getFilter().getCategories().get(0));
      if (expenseSheet.getFilter().getUsers() != null
          && expenseSheet.getFilter().getUsers().size() > 0)
        userBox.setValue(eshts.getUserLimitForUser(expenseSheet.getFilter().getUsers().get(0), expenseSheet));
      if (expenseSheet.getFilter().getFormula() != null
          && !expenseSheet.getFilter().getFormula().isEmpty())
        formulaField.setValue(expenseSheet.getFilter().getFormula());
      if (expenseSheet.getFilter().getComment() != null
          && !expenseSheet.getFilter().getComment().isEmpty())
        commentBox.setValue(expenseSheet.getFilter().getComment());
      expenseSheet.getFilter().setDateFrom(fromDateField.getValue());
      expenseSheet.getFilter().setDateTo(toDateField.getValue());
    } else
      expenseSheet.setFilter(new Filter(
        fromDateField.getValue(),
        toDateField.getValue(),
        null,
        null,
        null,
        null));

    refreshExpenses();
  }
  
  private void setCaption() {
    fromDateField.setLabel(Msg.get("findPage.dateFrom"));
    toDateField.setLabel(Msg.get("findPage.dateTo"));
    categoryBox.setLabel(Msg.get("findPage.category"));
    userBox.setLabel(Msg.get("findPage.user"));
    formulaField.setLabel(Msg.get("findPage.formula"));
    commentBox.setLabel(Msg.get("findPage.comment"));
    newExpenseButton.setText(Msg.get("findPage.add"));
    dateColumn = expenseGrid.addColumn(ExpenseEntity::getDate).setHeader(Msg.get("findPage.date"));
    dateColumn.setId("date");
    categoryColumn = expenseGrid.addColumn(item -> item.getCategory().getName()).setHeader(Msg.get("findPage.category"));
    categoryColumn.setId("category");
    expenseGrid.addColumn(item -> item.getUser().getName()).setHeader(Msg.get("findPage.user")).setId("user");
    formulaColumn = expenseGrid.addColumn(ExpenseEntity::getFormula).setHeader(Msg.get("findPage.formula"));
    formulaColumn.setId("formula");
    valueColumn = expenseGrid.addColumn(ExpenseEntity::getValue).setHeader(Msg.get("findPage.value"));
    valueColumn.setId("value");
    expenseGrid.addColumn(ExpenseEntity::getComment).setHeader(Msg.get("findPage.comment")).setId("comment");
    footer.getCell(dateColumn).setText(Msg.get("findPage.rows"));
    footer.getCell(formulaColumn).setText(Msg.get("findPage.sum"));
    exportButton.setText(Msg.get("findPage.export"));
  }
  
  public void refreshExpenses() {
    List<ExpenseEntity> expensesList = eshts.findAllExpenses(expenseSheet);
    expenseGrid.setItems(expensesList);
    footer.getCell(categoryColumn).setText(""+expensesList.size());
    footer.getCell(valueColumn).setText(calcualteSum(expensesList).toString());
    GridSortOrder sortOrder = new GridSortOrder(expenseGrid.getColumns().get(0), SortDirection.DESCENDING);
    expenseGrid.sort(List.of(sortOrder));
    expenseForm.setVisible(false);
  }

  private BigDecimal calcualteSum(List<ExpenseEntity> expensesList) {
    BigDecimal result = new BigDecimal(0);
    for(ExpenseEntity exp : expensesList) {
      try {
        result = result.add(exp.getValue());
      } catch (Exception e) {
        logger.error("calcualteSum: Problem "+ e.getMessage() + " with " + exp.getId());
      }
    }
    return result;
  }
}
