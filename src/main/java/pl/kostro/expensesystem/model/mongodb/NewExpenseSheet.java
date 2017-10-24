package pl.kostro.expensesystem.model.mongodb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;

@SuppressWarnings("serial")
public class NewExpenseSheet extends NewAbstractEntity {
  @Id
  private String id;

  private NewRealUser owner;

  private String name;

  private List<NewCategory> categoryList;

  private List<NewUserLimit> userLimitList;

  private List<NewExpense> expenseList;

  private int reloadDay;

  private boolean encrypted;
  @Transient
  private String key;
  @Transient
  private Map<LocalDate, DateExpense> dateExpenseMap;
  @Transient
  private Map<NewCategory, CategoryExpense> categoryExpenseMap;
  @Transient
  private Map<NewUserLimit, UserLimitExpense> userLimitExpenseMap;
  @Transient
  private Filter filter;
  @Transient
  private LocalDate firstDate;
  @Transient
  private LocalDate lastDate;

  public NewExpenseSheet() {
    super();
  }

  public NewExpenseSheet(ExpenseSheet expenseSheet, NewRealUser owner) {
    super();
    this.owner = owner;
    this.name = expenseSheet.getName();
    this.reloadDay = expenseSheet.getReloadDay();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public NewRealUser getOwner() {
    return owner;
  }

  public void setOwner(NewRealUser owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<NewCategory> getCategoryList() {
    if (categoryList == null)
      categoryList = new ArrayList<NewCategory>();
    return categoryList;
  }

  public void setCategoryList(List<NewCategory> categoryList) {
    this.categoryList = categoryList;
  }

  public List<NewUserLimit> getUserLimitList() {
    if (userLimitList == null)
      userLimitList = new ArrayList<NewUserLimit>();
    return userLimitList;
  }

  public void setUserLimitList(List<NewUserLimit> userLimitList) {
    this.userLimitList = userLimitList;
  }

  public List<NewExpense> getExpenseList() {
    if (expenseList == null)
      expenseList = new ArrayList<NewExpense>();
    return expenseList;
  }

  public void setExpenseList(List<NewExpense> expenseList) {
    this.expenseList = expenseList;
  }

  public int getReloadDay() {
    return reloadDay;
  }

  public void setReloadDay(int reloadDay) {
    this.reloadDay = reloadDay;
  }

  public boolean getEncrypted() {
    return encrypted;
  }

  public void setEncrypted(boolean encrypted) {
    this.encrypted = encrypted;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Map<LocalDate, DateExpense> getDateExpenseMap() {
    if (dateExpenseMap == null)
      dateExpenseMap = new HashMap<LocalDate, DateExpense>();
    return dateExpenseMap;
  }

  public void setDateExpenseMap(Map<LocalDate, DateExpense> dateExpenseMap) {
    this.dateExpenseMap = dateExpenseMap;
  }

  public Map<NewCategory, CategoryExpense> getCategoryExpenseMap() {
    if (categoryExpenseMap == null)
      categoryExpenseMap = new HashMap<NewCategory, CategoryExpense>();
    return categoryExpenseMap;
  }

  public void setCategoryExpenseMap(Map<NewCategory, CategoryExpense> categoryExpenseMap) {
    this.categoryExpenseMap = categoryExpenseMap;
  }

  public Map<NewUserLimit, UserLimitExpense> getUserLimitExpenseMap() {
    if (userLimitExpenseMap == null)
      userLimitExpenseMap = new HashMap<NewUserLimit, UserLimitExpense>();
    return userLimitExpenseMap;
  }

  public void setUserLimitExpenseMap(Map<NewUserLimit, UserLimitExpense> userLimitExpenseMap) {
    this.userLimitExpenseMap = userLimitExpenseMap;
  }

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public LocalDate getFirstDate() {
    return firstDate;
  }

  public void setFirstDate(LocalDate firstDate) {
    this.firstDate = firstDate;
  }

  public LocalDate getLastDate() {
    return lastDate;
  }

  public void setLastDate(LocalDate lastDate) {
    this.lastDate = lastDate;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getName()+"]";
  }
}
