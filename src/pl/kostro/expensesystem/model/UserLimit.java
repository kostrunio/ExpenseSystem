package pl.kostro.expensesystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
  private int limit;
  @ManyToOne
  private ExpenseSheet expenseSheet;
  
  public UserLimit() {
    super();
  }

  public UserLimit(User user, int limit, ExpenseSheet expenseSheet) {
    this.user = user;
    this.limit = limit;
    this.expenseSheet = expenseSheet;
  }

  public User getUser() {
    return user;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  
  public void setUser(User user) {
    this.user = user;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }
  
  public ExpenseSheet getExpenseSheet() {
    return expenseSheet;
  }

  public void setExpenseSheet(ExpenseSheet expenseSheet) {
    this.expenseSheet = expenseSheet;
  }

  public String toString() {
    return user.getName();
  }

}
