package pl.kostro.expensesystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
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

import pl.kostro.expensesystem.utils.Encryption;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;

@Entity
@Table(name = "expense_sheets")
public class ExpenseSheetEntity extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "es_id")
  private Long id;
  @OneToOne
  @JoinColumn(name = "es_u_id")
  private RealUserEntity owner;
  @Column(name = "es_name")
  private String name;
  @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
  @JoinColumn(name = "c_es_id")
  @OrderBy("order")
  private List<CategoryEntity> categoryList;
  @OneToMany(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "ul_es_id")
  @OrderBy("order")
  private List<UserLimitEntity> userLimitList;
  @OneToMany(mappedBy = "expenseSheet", cascade = CascadeType.REMOVE)
  @OrderBy
  private List<ExpenseEntity> expenseList;
  @Column(name = "es_reload_day")
  private int reloadDay;
  @Column(name = "es_encrypted")
  private boolean encrypted;
  @Transient
  private SecretKeySpec secretKey;
  @Transient
  String key;
  @Transient
  private Map<LocalDate, DateExpense> dateExpenseMap;
  @Transient
  private Map<CategoryEntity, CategoryExpense> categoryExpenseMap;
  @Transient
  private Map<UserLimitEntity, UserLimitExpense> userLimitExpenseMap;
  @Transient
  private Filter filter;
  @Transient
  private LocalDate firstDate;
  @Transient
  private LocalDate lastDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RealUserEntity getOwner() {
    return owner;
  }

  public void setOwner(RealUserEntity owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<CategoryEntity> getCategoryList() {
    if (categoryList == null)
      categoryList = new ArrayList<CategoryEntity>();
    return categoryList;
  }

  public void setCategoryList(List<CategoryEntity> categoryList) {
    this.categoryList = categoryList;
  }

  public List<UserLimitEntity> getUserLimitList() {
    if (userLimitList == null)
      userLimitList = new ArrayList<UserLimitEntity>();
    return userLimitList;
  }

  public void setUserLimitList(List<UserLimitEntity> userLimitList) {
    this.userLimitList = userLimitList;
  }

  public List<ExpenseEntity> getExpenseList() {
    if (expenseList == null)
      expenseList = new ArrayList<ExpenseEntity>();
    return expenseList;
  }

  public void setExpenseList(List<ExpenseEntity> expenseList) {
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

  public SecretKeySpec getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String key) {
    this.key = key;
    if (key == null) secretKey = null;
    this.secretKey = Encryption.getSecretKey(key);
  }

  public String getKey() {
    return key;
  }

  public Map<LocalDate, DateExpense> getDateExpenseMap() {
    if (dateExpenseMap == null)
      dateExpenseMap = new HashMap<LocalDate, DateExpense>();
    return dateExpenseMap;
  }

  public void setDateExpenseMap(Map<LocalDate, DateExpense> dateExpenseMap) {
    this.dateExpenseMap = dateExpenseMap;
  }

  public Map<CategoryEntity, CategoryExpense> getCategoryExpenseMap() {
    if (categoryExpenseMap == null)
      categoryExpenseMap = new HashMap<CategoryEntity, CategoryExpense>();
    return categoryExpenseMap;
  }

  public void setCategoryExpenseMap(Map<CategoryEntity, CategoryExpense> categoryExpenseMap) {
    this.categoryExpenseMap = categoryExpenseMap;
  }

  public Map<UserLimitEntity, UserLimitExpense> getUserLimitExpenseMap() {
    if (userLimitExpenseMap == null)
      userLimitExpenseMap = new HashMap<UserLimitEntity, UserLimitExpense>();
    return userLimitExpenseMap;
  }

  public void setUserLimitExpenseMap(Map<UserLimitEntity, UserLimitExpense> userLimitExpenseMap) {
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
