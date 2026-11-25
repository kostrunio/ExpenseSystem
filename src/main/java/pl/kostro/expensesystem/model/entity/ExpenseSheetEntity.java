package pl.kostro.expensesystem.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import pl.kostro.expensesystem.utils.encryption.Encryption;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.transform.model.CategoryExpense;
import pl.kostro.expensesystem.utils.transform.model.DateExpense;
import pl.kostro.expensesystem.utils.transform.model.UserLimitExpense;

import javax.crypto.spec.SecretKeySpec;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "expense_sheets")
public class ExpenseSheetEntity extends AbstractEntity {
  @Setter
  @Getter
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "es_id")
  private Long id;
  @Setter
  @Getter
  @OneToOne
  @JoinColumn(name = "es_u_id")
  private RealUserEntity owner;
  @Setter
  @Getter
  @Column(name = "es_name")
  private String name;
  @Setter
  @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
  @JoinColumn(name = "c_es_id")
  @OrderBy("order")
  private List<CategoryEntity> categoryList;
  @Setter
  @OneToMany(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "ul_es_id")
  @OrderBy("order")
  private List<UserLimitEntity> userLimitList;
  @Setter
  @OneToMany(mappedBy = "expenseSheet", cascade = CascadeType.REMOVE)
  @OrderBy
  private List<ExpenseEntity> expenseList;
  @Setter
  @Getter
  @Column(name = "es_reload_day")
  private int reloadDay;
  @Setter
  @Column(name = "es_encrypted")
  private boolean encrypted;
  @Getter
  @Transient
  private SecretKeySpec secretKey;
  @Getter
  @Transient
  String key;
  @Setter
  @Transient
  private Map<LocalDate, DateExpense> dateExpenseMap;
  @Setter
  @Transient
  private Map<CategoryEntity, CategoryExpense> categoryExpenseMap;
  @Setter
  @Transient
  private Map<UserLimitEntity, UserLimitExpense> userLimitExpenseMap;
  @Setter
  @Getter
  @Transient
  private Filter filter;
  @Setter
  @Getter
  @Transient
  private LocalDate firstDate;
  @Setter
  @Getter
  @Transient
  private LocalDate lastDate;

  public List<CategoryEntity> getCategoryList() {
    if (categoryList == null)
      categoryList = new ArrayList<>();
    return categoryList;
  }

  public List<UserLimitEntity> getUserLimitList() {
    if (userLimitList == null)
      userLimitList = new ArrayList<>();
    return userLimitList;
  }

  public List<ExpenseEntity> getExpenseList() {
    if (expenseList == null)
      expenseList = new ArrayList<>();
    return expenseList;
  }

  public boolean getEncrypted() {
    return encrypted;
  }

  public void setSecretKey(String key) {
    this.key = key;
    if (key == null) secretKey = null;
    this.secretKey = Encryption.getSecretKey(key);
  }

  public Map<LocalDate, DateExpense> getDateExpenseMap() {
    if (dateExpenseMap == null)
      dateExpenseMap = new HashMap<>();
    return dateExpenseMap;
  }

  public Map<CategoryEntity, CategoryExpense> getCategoryExpenseMap() {
    if (categoryExpenseMap == null)
      categoryExpenseMap = new HashMap<>();
    return categoryExpenseMap;
  }

  public Map<UserLimitEntity, UserLimitExpense> getUserLimitExpenseMap() {
    if (userLimitExpenseMap == null)
      userLimitExpenseMap = new HashMap<>();
    return userLimitExpenseMap;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getName()+"]";
  }

}
