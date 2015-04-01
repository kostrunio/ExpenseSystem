package pl.kostro.expensesystem.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.CategoryExpense;
import pl.kostro.expensesystem.utils.DateExpense;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.UserLimitExpense;

@Entity
public class ExpenseSheet extends AbstractEntity {

  private static final long serialVersionUID = 3614726105787486216L;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  @OneToOne
  private RealUser owner;
  private String name;
  @OneToMany(mappedBy="expenseSheet", fetch=FetchType.EAGER)
  @OrderBy("orderId")
  private List<Category> categoryList;
  @OneToMany(mappedBy="expenseSheet", fetch=FetchType.EAGER)
  @OrderBy
  private List<UserLimit> userLimitList;
  @OneToMany(mappedBy="expenseSheet")
  @OrderBy
  private List<Expense> expenseList;
  private int reloadeDay;
  private int mainLimit;
  @OneToOne
  private UserLimit defaultUserLimit;
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

  public int getId() {
    return id;
  }
  public void setId(int id) {
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

  public int getReloadeDay() {
    return reloadeDay;
  }

  public void setReloadeDay(int reloadeDay) {
    this.reloadeDay = reloadeDay;
  }

  public int getMainLimit() {
    return mainLimit;
  }

  public void setMainLimit(int mainLimit) {
    this.mainLimit = mainLimit;
  }

  public UserLimit getDefaultUserLimit() {
    return defaultUserLimit;
  }

  public void setDefaultUserLimit(UserLimit defaultUserLimit) {
    this.defaultUserLimit = defaultUserLimit;
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
