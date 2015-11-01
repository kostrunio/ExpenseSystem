package pl.kostro.expensesystem.model;

import java.util.ArrayList;
import java.util.Collections;
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

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.utils.CategoryExpense;
import pl.kostro.expensesystem.utils.DateExpense;
import pl.kostro.expensesystem.utils.Encryption;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.UserLimitExpense;

@Entity
@Table(name="expense_sheets")
public class ExpenseSheet extends AbstractEntity {

  private static final long serialVersionUID = 3614726105787486216L;

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name="es_id")
  private int id;
  @OneToOne
  @JoinColumn(name="es_u_id")
  private RealUser owner;
  @Column(name="es_name")
  private String name;
  @Column(name = "es_name_byte")
  private byte[] name_byte;
  @OneToMany(cascade=CascadeType.REMOVE)
  @JoinColumn(name="c_es_id") 
  @OrderBy("order")
  private List<Category> categoryList;
  @OneToMany(cascade=CascadeType.REMOVE)
  @JoinColumn(name="ul_es_id")
  @OrderBy("order")
  private List<UserLimit> userLimitList;
  @OneToMany(mappedBy="expenseSheet", cascade=CascadeType.REMOVE)
  @OrderBy
  private List<Expense> expenseList;
  @Column(name="es_reload_day")
  private int reloadDay;
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
    if (name_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(RealUser.class).getKeyString());
      name = enc.decryption(name_byte);
    }
    return name;
  }

  public void setName(String name) {
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(RealUser.class).getKeyString());
    this.name_byte = enc.encryption(name);
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
  
  public List<UserLimit> getUserLimitListDesc() {
	  List<UserLimit> returnList = new ArrayList<UserLimit>(getUserLimitList());
	  Collections.reverse(returnList);
	  return returnList;
  }
  
  public List<UserLimit> getUserLimitListRealUser() {
    List<UserLimit> userLimitList = new ArrayList<UserLimit>();
    for (UserLimit userLimit : getUserLimitList())
      if (userLimit.getUser() instanceof RealUser)
        userLimitList.add(userLimit);
    return userLimitList;
  }
  
  public List<UserLimit> getUserLimitListNotRealUser() {
    List<UserLimit> userLimitList = new ArrayList<UserLimit>();
    for (UserLimit userLimit : getUserLimitList())
      if (!(userLimit.getUser() instanceof RealUser))
        userLimitList.add(userLimit);
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
  
  @Override
  public boolean equals(Object o) {
    if(o instanceof ExpenseSheet)
      return getId() == ((ExpenseSheet)o).getId();
    else return this == o;
  }
  
}
