package pl.kostro.expensesystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserLimit {

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  @OneToOne
  private User user;
  private int limit;

  public UserLimit(User user, int limit) {
    this.user = user;
    this.limit = limit;
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

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public String toString() {
    return user.getName();
  }

  public static void createUserLimit(ExpenseSheet expenseSheet) {
    expenseSheet.getUserLimitList().add(new UserLimit(expenseSheet.getOwner(), 0));
    expenseSheet.getUserLimitList().add(new UserLimit(new User("wspólne"), 0));
  }

}
