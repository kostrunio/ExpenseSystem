package pl.kostro.expensesystem.model.mongodb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

@SuppressWarnings("serial")
public class NewRealUser extends NewUser {

  private String password;

  private byte[] password_byte;

  private String email;

  private LocalDateTime logDate;

  private List<NewExpenseSheet> expenseSheetList;

  private NewExpenseSheet defaultExpenseSheet;
  @Transient
  String clearPassword;

  public NewRealUser() {
    super();
  }

  public NewRealUser(String name) {
    super(name);
  }

  public NewRealUser(pl.kostro.expensesystem.model.RealUser realUser) {
    setName(realUser.getName());
    setCreationDate(realUser.getCreationDate());
    this.password = realUser.getPassword();
    this.password_byte = realUser.getPasswordByte();
    this.email = realUser.getEmail();
    this.logDate = realUser.getLogDate();
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

  public List<NewExpenseSheet> getExpenseSheetList() {
    if (expenseSheetList == null)
      expenseSheetList = new ArrayList<NewExpenseSheet>();
    return expenseSheetList;
  }

  public void setExpenseSheetList(List<NewExpenseSheet> expenseSheetList) {
    this.expenseSheetList = expenseSheetList;
  }

  public NewExpenseSheet getDefaultExpenseSheet() {
    return defaultExpenseSheet;
  }

  public void setDefaultExpenseSheet(NewExpenseSheet defaultExpenseSheet) {
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
    return getClass().getSimpleName()+"["+getName()+", " + getLogDate() + "]";
  }
}
