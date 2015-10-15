package pl.kostro.expensesystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
      ),
  @NamedQuery(
      name = "findUsersWithExpenseSheet",
      query = "select u from RealUser u join u.expenseSheetList es where es = :expenseSheet "
      )
})
public class RealUser extends User {

  private static final long serialVersionUID = 8197867574179477991L;

  @Column(name="u_password")
  private String password;
  @Column(name="u_email")
  private String email;
  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(name="user_expense_sheet",
  joinColumns=
      @JoinColumn(name="ues_u_id", referencedColumnName="u_id"),
  inverseJoinColumns=
      @JoinColumn(name="ues_es_id", referencedColumnName="es_id")
  )
  @OrderBy
  private List<ExpenseSheet> expenseSheetList;
  @OneToOne
  @JoinColumn(name="u_default_es_id")
  private ExpenseSheet defaultExpenseSheet;
  
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
  
  public ExpenseSheet getDefaultExpenseSheet() {
	  return defaultExpenseSheet;
  }
  
  public void setDefaultExpenseSheet(ExpenseSheet defaultExpenseSheet) {
	  this.defaultExpenseSheet = defaultExpenseSheet;
  }
  
}
