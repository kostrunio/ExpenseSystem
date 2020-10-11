package pl.kostro.expensesystem.dao.model;

import java.util.ArrayList;
import java.util.List;

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

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
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

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getName()+"]";
  }
}
