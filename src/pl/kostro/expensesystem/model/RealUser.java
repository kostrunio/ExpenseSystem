package pl.kostro.expensesystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;

@Entity
@DiscriminatorValue(value="2")
@NamedQueries({
  @NamedQuery(
      name = "findUser",
      query = "select u from RealUser u where u.name = :name"
      ),
  @NamedQuery(
      name = "findLoggedUser",
      query = "select u from RealUser u where u.name = :name and u.password = :password"
      )
})
public class RealUser extends User {

  private static final long serialVersionUID = 8197867574179477991L;

  private String password;
  private String email;
  @ManyToMany(fetch=FetchType.EAGER)
  @OrderBy
  private List<ExpenseSheet> expenseSheetList;
  private long defaultExpenseSheetId;
  
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
  
  public long getDefaultExpenseSheetId() {
	  return defaultExpenseSheetId;
  }
  
  public void setDefaultExpenseSheetId(long defaultExpenseSheetId) {
	  this.defaultExpenseSheetId = defaultExpenseSheetId;
  }
  
  public ExpenseSheet getDefaultExpenseSheet() {
	  for (ExpenseSheet expenseSheet : getExpenseSheetList()) {
		  if (expenseSheet.getId() == getDefaultExpenseSheetId())
			  return expenseSheet;
	  }
	  return null;
  }
  
}
