package pl.kostro.expensesystem.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;

@SuppressWarnings("serial")
@Entity
@Table(name = "expense_sheets")
public class ExpenseSheet extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "es_id")
  private Long id;
  @OneToOne
  @JoinColumn(name = "es_u_id")
  private RealUser owner;
  @Column(name = "es_name")
  private String name;
  @OneToMany(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "c_es_id")
  @OrderBy("order")
  private List<Category> categoryList;
  @OneToMany(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "ul_es_id")
  @OrderBy("order")
  private List<UserLimit> userLimitList;
  @OneToMany(mappedBy = "expenseSheet", cascade = CascadeType.REMOVE)
  @OrderBy
  private List<Expense> expenseList;
  @Column(name = "es_reload_day")
  private int reloadDay;
  @Column(name = "es_encrypted")
  private boolean encrypted;
  @Transient
  private String key;
  @Transient
  private Map<Date, DateExpense> dateExpenseMap;
  @Transient
  private Map<Category, CategoryExpense> categoryExpenseMap;
  @Transient
  private Map<UserLimit, UserLimitExpense> userLimitExpenseMap;
  @Transient
  private Filter filter;
  @Transient
  private Date firstDate;
  @Transient
  private Date lastDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RealUser getOwner() {
    return owner;
  }

  public void setOwner(RealUser owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Category> getCategoryList() {
    if (categoryList == null)
      categoryList = new ArrayList<Category>();
    return categoryList;
  }

  public void setCategoryList(List<Category> categoryList) {
    this.categoryList = categoryList;
  }

  public List<UserLimit> getUserLimitList() {
    if (userLimitList == null)
      userLimitList = new ArrayList<UserLimit>();
    return userLimitList;
  }

  public void setUserLimitList(List<UserLimit> userLimitList) {
    this.userLimitList = userLimitList;
  }

  public List<Expense> getExpenseList() {
    if (expenseList == null)
      expenseList = new ArrayList<Expense>();
    return expenseList;
  }

  public void setExpenseList(List<Expense> expenseList) {
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

  public Map<Date, DateExpense> getDateExpenseMap() {
    if (dateExpenseMap == null)
      dateExpenseMap = new HashMap<Date, DateExpense>();
    return dateExpenseMap;
  }

  public void setDateExpenseMap(Map<Date, DateExpense> dateExpenseMap) {
    this.dateExpenseMap = dateExpenseMap;
  }

  public Map<Category, CategoryExpense> getCategoryExpenseMap() {
    if (categoryExpenseMap == null)
      categoryExpenseMap = new HashMap<Category, CategoryExpense>();
    return categoryExpenseMap;
  }

  public void setCategoryExpenseMap(Map<Category, CategoryExpense> categoryExpenseMap) {
    this.categoryExpenseMap = categoryExpenseMap;
  }

  public Map<UserLimit, UserLimitExpense> getUserLimitExpenseMap() {
    if (userLimitExpenseMap == null)
      userLimitExpenseMap = new HashMap<UserLimit, UserLimitExpense>();
    return userLimitExpenseMap;
  }

  public void setUserLimitExpenseMap(Map<UserLimit, UserLimitExpense> userLimitExpenseMap) {
    this.userLimitExpenseMap = userLimitExpenseMap;
  }

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public Date getFirstDate() {
    return firstDate;
  }

  public void setFirstDate(Date firstDate) {
    this.firstDate = firstDate;
  }

  public Date getLastDate() {
    return lastDate;
  }

  public void setLastDate(Date lastDate) {
    this.lastDate = lastDate;
  }

}
