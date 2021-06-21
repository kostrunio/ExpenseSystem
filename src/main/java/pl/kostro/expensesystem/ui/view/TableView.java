package pl.kostro.expensesystem.ui.view;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.haijian.Exporter;
import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.ui.view.design.TableDesign;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.msg.Msg;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableView extends TableDesign {
  
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;

  private Logger logger = LogManager.getLogger();
  private LocalDate date;
  private ExpenseSheetEntity expenseSheet;
  private FooterRow footer = expenseGrid.prependFooterRow();
  private Column<ExpenseEntity, LocalDate> dateColumn;
  private Column<ExpenseEntity, String> categoryColumn;
  private Column<ExpenseEntity, String> formulaColumn;
  private Column<ExpenseEntity, BigDecimal> valueColumn;
  
  private StreamResource exportData = new StreamResource((StreamResource.StreamSource) () -> Exporter.exportAsExcel(expenseGrid), "export.xls");
  private FileDownloader excelFileDownloader = new FileDownloader(exportData);
  
  private HasValue.ValueChangeListener filterChanged = event -> {
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
        (String)commentBox.getValue()));
    refreshExpenses();
  };
  private ClickListener newClicked = event -> expenseForm.edit(new ExpenseEntity());
  private SelectionListener<ExpenseEntity> itemClicked = event -> {
    if (expenseGrid.getSelectedItems().size() != 0)
      expenseForm.edit(expenseGrid.getSelectedItems().iterator().next());
    else
      expenseForm.setVisible(false);
  };
  private NewItemProvider addComment = event -> Optional.of(event);
  
  public TableView() {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
    eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
    logger.info("create");
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
    categoryBox.setItemCaptionGenerator(item -> item.getName());
    categoryBox.setItems(expenseSheet.getCategoryList());
    categoryBox.addValueChangeListener(filterChanged);
    userBox.setItemCaptionGenerator(item -> item.getUser().getName());
    userBox.setItems(expenseSheet.getUserLimitList());
    userBox.addValueChangeListener(filterChanged);
    formulaField.addValueChangeListener(filterChanged);
    commentBox.setNewItemProvider(addComment);
    commentBox.setItems((itemCaption, filterText) -> itemCaption.contains(filterText), eshts.getAllComments(expenseSheet));
    commentBox.addValueChangeListener(filterChanged);
    newExpenseButton.addClickListener(newClicked);
    expenseGrid.addSelectionListener(itemClicked);
    
    if (expenseSheet.getFilter() != null) {
      if (expenseSheet.getFilter().getCategories() != null
          && expenseSheet.getFilter().getCategories().size() > 0)
        categoryBox.setSelectedItem(expenseSheet.getFilter().getCategories().get(0));
      if (expenseSheet.getFilter().getUsers() != null
          && expenseSheet.getFilter().getUsers().size() > 0)
        userBox.setSelectedItem(eshts.getUserLimitForUser(expenseSheet, expenseSheet.getFilter().getUsers().get(0)));
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
        logger.error("calcualteSum: Problem "+ e.getMessage() + " with " + exp.getId());
      }
    }
    return result;
  }
}
