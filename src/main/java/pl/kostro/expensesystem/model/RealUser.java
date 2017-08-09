package pl.kostro.expensesystem.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import pl.kostro.expensesystem.utils.LocalDateTimePersistenceConverter;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue(value="2")
public class RealUser extends User {
  @Column(name = "u_password")
  private String password;
  @Column(name = "u_password_byte")
  private byte[] password_byte;
  @Column(name = "u_email")
  private String email;
  @Column(name = "u_log_date")
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  private LocalDateTime logDate;
  @ManyToMany
  @JoinTable(name="user_expense_sheet",
  joinColumns=
      @JoinColumn(name="ues_u_id", referencedColumnName="u_id"),
  inverseJoinColumns=
      @JoinColumn(name="ues_es_id", referencedColumnName="es_id")
  )
  @OrderBy
  private List<ExpenseSheet> expenseSheetList;
  @OneToOne
  @JoinColumn(name = "u_default_es_id")
  private ExpenseSheet defaultExpenseSheet;
  @Transient
  String clearPassword;

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

  public byte[] getPasswordByte() {
    return password_byte;
  }

  public void setPasswordByte(byte[] password_byte) {
    this.password_byte = password_byte;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getLogDate() {
    return logDate;
  }

  public void setLogDate(LocalDateTime logDate) {
    this.logDate = logDate;
  }

  public List<ExpenseSheet> getExpenseSheetList() {
    if (expenseSheetList == null)
      expenseSheetList = new ArrayList<ExpenseSheet>();
    return expenseSheetList;
  }

  public void setExpenseSheetList(List<ExpenseSheet> expenseSheetList) {
    this.expenseSheetList = expenseSheetList;
  }

  public ExpenseSheet getDefaultExpenseSheet() {
    return defaultExpenseSheet;
  }

  public void setDefaultExpenseSheet(ExpenseSheet defaultExpenseSheet) {
    this.defaultExpenseSheet = defaultExpenseSheet;
  }

  public String getClearPassword() {
    return clearPassword;
  }

  public void setClearPassword(String clearPassword) {
    this.clearPassword = clearPassword;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getName()+"]";
  }
}
