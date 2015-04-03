package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserLimit extends AbstractEntity {

  private static final long serialVersionUID = 3781249751926840458L;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  @OneToOne
  private User user;
  private BigDecimal limit;
  private int orderId;
  @ManyToOne
  private ExpenseSheet expenseSheet;
  @OneToMany(mappedBy="userLimit", fetch=FetchType.EAGER)
  @OrderBy(value="date")
  private List<UserSummary> userSummaryList;
  private boolean continuousSummary;
  
  public UserLimit() {
    super();
  }

  public UserLimit(User user, BigDecimal limit, ExpenseSheet expenseSheet) {
    this.user = user;
    this.limit = limit;
    this.expenseSheet = expenseSheet;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }

  public BigDecimal getLimit() {
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }
  
  public int getOrderId() {
    return orderId;
  }
  
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }
  
  public ExpenseSheet getExpenseSheet() {
    return expenseSheet;
  }

  public void setExpenseSheet(ExpenseSheet expenseSheet) {
    this.expenseSheet = expenseSheet;
  }
  
  public List<UserSummary> getUserSummaryList() {
    if (userSummaryList == null)
      userSummaryList = new ArrayList<UserSummary>();
    return userSummaryList;
  }

  public void setUserSummaryList(List<UserSummary> userSummaryList) {
    this.userSummaryList = userSummaryList;
  }
  
  public boolean isContinuousSummary() {
    return continuousSummary;
  }
  
  public void setContinuousSummary(boolean continuousSummary) {
    this.continuousSummary = continuousSummary;
  }

  public String toString() {
    return user.getName();
  }

}
