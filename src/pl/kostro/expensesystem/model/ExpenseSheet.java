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

import pl.kostro.expensesystem.utils.DateExpense;

@Entity
public class ExpenseSheet {

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  @OneToOne
  private RealUser owner;
  private String name;
  @OneToMany(fetch=FetchType.EAGER)
  @OrderBy("orderId")
  private List<Category> categoryList;
  @OneToMany(fetch=FetchType.EAGER)
  @OrderBy
  private List<UserLimit> userLimitList;
  @OneToMany(fetch=FetchType.EAGER)
  @OrderBy
  private List<Expense> expenseList;
  private int reloadeDay;
  private int mainLimit;
  @OneToOne
  private UserLimit defaultUserLimit;
  @Transient
  private Map<Date, DateExpense> dateExpenseMap;

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
  
}
