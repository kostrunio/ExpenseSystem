package pl.kostro.expensesystem.ui.views.table;

import com.vaadin.data.HasValue;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox.NewItemProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.components.grid.FooterRow;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.haijian.Exporter;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.*;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TableView extends TableDesign {
  
  private final ExpenseSheetTransformService eshts;
  private final LocalDate date;
  private ExpenseSheetEntity expenseSheet;
  private final FooterRow footer = expenseGrid.prependFooterRow();
  private Column<ExpenseEntity, LocalDate> dateColumn;
  private Column<ExpenseEntity, String> categoryColumn;
  private Column<ExpenseEntity, String> formulaColumn;
  private Column<ExpenseEntity, BigDecimal> valueColumn;
  private Column<ExpenseEntity, LocalDate> updateDateColumn;
  
  private final StreamResource exportData = new StreamResource((StreamResource.StreamSource) () -> Exporter.exportAsExcel(expenseGrid), "export.xls");
  private final FileDownloader excelFileDownloader = new FileDownloader(exportData);
  
  private final HasValue.ValueChangeListener filterChanged = event -> {
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
        formulaField.getValue().replace(',','.'),
        commentBox.getValue()));
    refreshExpenses();
  };
  private final ClickListener newClicked = event -> expenseForm.edit(new ExpenseEntity());
  private final SelectionListener<ExpenseEntity> itemClicked = event -> {
    if (!expenseGrid.getSelectedItems().isEmpty())
      expenseForm.edit(expenseGrid.getSelectedItems().iterator().next());
    else
      expenseForm.setVisible(false);
  };
  private final NewItemProvider addComment = Optional::of;
  
  public TableView() {
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    log.info("create");
    expenseSheet = VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class);
    date = VaadinSession.getCurrent().getAttribute(LocalDate.class);
    
    excelFileDownloader.extend(exportButton);
    
    setCaption();
    fromDateField.setDateFormat("yyyy-MM-dd");
    toDateField.setDateFormat("yyyy-MM-dd");
    expenseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

    expenseForm.prepare(expenseSheet, this);
    
    fromDateField.setValue(date.withDayOfMonth(1));
    fromDateField.addValueChangeListener(filterChanged);
    toDateField.setValue(date.withDayOfMonth(date.lengthOfMonth()));
    toDateField.addValueChangeListener(filterChanged);
    categoryBox.setItemCaptionGenerator(CategoryEntity::getName);
    categoryBox.setItems(expenseSheet.getCategoryList());
    categoryBox.addValueChangeListener(filterChanged);
    userBox.setItemCaptionGenerator(item -> item.getUser().getName());
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(filterChanged);
    formulaField.addValueChangeListener(filterChanged);
    commentBox.setNewItemProvider(addComment);
    commentBox.setItems(String::contains, eshts.getAllComments(expenseSheet));
    commentBox.addValueChangeListener(filterChanged);
    newExpenseButton.addClickListener(newClicked);
    expenseGrid.addSelectionListener(itemClicked);
    
    if (expenseSheet.getFilter() != null) {
      if (expenseSheet.getFilter().getCategories() != null
          && !expenseSheet.getFilter().getCategories().isEmpty())
        categoryBox.setSelectedItem(expenseSheet.getFilter().getCategories().get(0));
      if (expenseSheet.getFilter().getUsers() != null
          && !expenseSheet.getFilter().getUsers().isEmpty())
        userBox.setSelectedItem(eshts.getUserLimitForUser(expenseSheet.getFilter().getUsers().get(0), expenseSheet));
      if (expenseSheet.getFilter().getFormula() != null
          && !expenseSheet.getFilter().getFormula().isEmpty())
        formulaField.setValue(expenseSheet.getFilter().getFormula());
      if (expenseSheet.getFilter().getComment() != null
          && !expenseSheet.getFilter().getComment().isEmpty())
        commentBox.setSelectedItem(expenseSheet.getFilter().getComment());
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
    fromDateField.setCaption(Msg.get("findPage.dateFrom"));
    toDateField.setCaption(Msg.get("findPage.dateTo"));
    categoryBox.setCaption(Msg.get("findPage.category"));
    userBox.setCaption(Msg.get("findPage.user"));
    formulaField.setCaption(Msg.get("findPage.formula"));
    commentBox.setCaption(Msg.get("findPage.comment"));
    newExpenseButton.setCaption(Msg.get("findPage.add"));
    dateColumn = expenseGrid.addColumn(ExpenseEntity::getDate).setCaption(Msg.get("findPage.date")).setId("date");
    categoryColumn = expenseGrid.addColumn(item -> item.getCategory().getName()).setCaption(Msg.get("findPage.category")).setId("category");
    expenseGrid.addColumn(item -> item.getUser().getName()).setCaption(Msg.get("findPage.user")).setId("user");
    formulaColumn = expenseGrid.addColumn(ExpenseEntity::getFormula).setCaption(Msg.get("findPage.formula")).setId("formula");
    valueColumn = expenseGrid.addColumn(ExpenseEntity::getValue).setCaption(Msg.get("findPage.value")).setId("value");
    expenseGrid.addColumn(ExpenseEntity::getComment).setCaption(Msg.get("findPage.comment")).setId("comment");
    updateDateColumn = expenseGrid.addColumn(ExpenseEntity::getUpdateDate).setCaption(Msg.get("findPage.updateDate")).setId("updateDate");
    footer.getCell(dateColumn).setText(Msg.get("findPage.rows"));
    footer.getCell(formulaColumn).setText(Msg.get("findPage.sum"));
    exportButton.setCaption(Msg.get("findPage.export"));
  }
  
  public void refreshExpenses() {
    List<ExpenseEntity> expensesList = eshts.findAllExpenses(expenseSheet);
    expenseGrid.setItems(expensesList);
    footer.getCell(categoryColumn).setText(""+expensesList.size());
    footer.getCell(valueColumn).setHtml("<b>" + calcualteSum(expensesList) + "</b>");
    expenseGrid.sort(expenseGrid.getColumns().get(0), SortDirection.DESCENDING);
    expenseForm.setVisible(false);
  }

  private BigDecimal calcualteSum(List<ExpenseEntity> expensesList) {
    BigDecimal result = new BigDecimal(0);
    for(ExpenseEntity exp : expensesList) {
      try {
        result = result.add(exp.getValue());
      } catch (Exception e) {
        log.error("calcualteSum: Problem "+ e.getMessage() + " with " + exp.getId());
      }
    }
    return result;
  }
}
