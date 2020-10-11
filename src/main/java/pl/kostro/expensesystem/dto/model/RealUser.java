package pl.kostro.expensesystem.dto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RealUser extends User {
  private String password;
  private byte[] password_byte;
  private String email;
  private LocalDateTime logDate;
  private List<ExpenseSheet> expenseSheetList;
  private ExpenseSheet defaultExpenseSheet;
  String clearPassword;

  public RealUser() {
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
