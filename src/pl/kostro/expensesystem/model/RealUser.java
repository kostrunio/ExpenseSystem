package pl.kostro.expensesystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

@Entity
@DiscriminatorValue(value="2")
public class RealUser extends User {

  private String password;
  private String email;
  @ManyToMany(fetch=FetchType.EAGER)
  @OrderBy
  private List<ExpenseSheet> expenseSheetList;
  
  public RealUser() {
    super();
  }

  public RealUser(String name) {
    super(name);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<ExpenseSheet> getExpenseSheetList() {
    if (expenseSheetList == null)
      expenseSheetList = new ArrayList<ExpenseSheet>();
    return expenseSheetList;
  }

  public void setExpenseSheets(List<ExpenseSheet> expenseSheetList) {
    this.expenseSheetList = expenseSheetList;
  }

  public static void createUser(RealUser loggedUser) {
    loggedUser.setId(1);
    ExpenseSheet.createExpenseSheet(loggedUser);
  }
}
