package pl.kostro.expensesystem.model.mongodb;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.utils.Calculator;
import pl.kostro.expensesystem.utils.Encryption;

@SuppressWarnings("serial")
public class NewExpense extends NewAbstractEntity {
  @Id
  private String id;

  private LocalDate date;
  @Transient
  private String formula;

  private byte[] formula_byte;
  @Transient
  private BigDecimal value;

  private NewCategory category;

  private NewUser user;
  @Transient
  private String comment;

  private byte[] comment_byte;

  private boolean notify;

  private NewExpenseSheet expenseSheet;

  public NewExpense() {
    super();
  }

  public NewExpense(LocalDate date, String formula, NewCategory category, NewUser user, String comment, boolean notify,
      NewExpenseSheet expenseSheet) {
    this.date = date;
    setFormula(formula);
    this.category = category;
    this.user = user;
    setComment(comment);
    this.notify = notify;
    this.expenseSheet = expenseSheet;
  }

  public NewExpense(Expense expense, NewCategory newCategory, NewUser newUser, NewExpenseSheet newExpenseSheet) {
    this.date = expense.getDate();
    this.formula_byte = expense.getFormulaByte();
    this.category = newCategory;
    this.user = newUser;
    this.comment_byte = expense.getCommentByte();
    this.notify = expense.isNotify();
    this.expenseSheet = newExpenseSheet;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getFormula() {
    return getFormula(false);
  }

  public String getFormula(boolean encrypt) {
    if ((formula == null || formula.isEmpty()) && formula_byte != null && !encrypt) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
      formula = enc.decryption(formula_byte);
    }
    return formula;
  }

  public void setFormula(String formula) {
    setFormula(formula, false);
  }

  public void setFormula(String formula, boolean encrypt) {
    if (formula_byte != null && formula.equals(this.formula) && !encrypt)
      return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
    this.formula_byte = enc.encryption(formula);
    this.formula = formula;
  }

  public BigDecimal getValue() {
    try {
    if (getFormula() != null && !formula.isEmpty())
      value = Calculator.getOperationResult(formula);
    return value;
    } catch (NullPointerException e) {
      return new BigDecimal(-1);
    }
  }

  public NewCategory getCategory() {
    return category;
  }

  public void setCategory(NewCategory category) {
    this.category = category;
  }

  public NewUser getUser() {
    return user;
  }

  public void setUser(NewUser user) {
    this.user = user;
  }

  public String getComment() {
    return getComment(false);
  }

  public String getComment(boolean encrypt) {
    if ((comment == null || comment.isEmpty()) && comment_byte != null && !encrypt) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
      comment = enc.decryption(comment_byte);
    }
    return comment;
  }

  public void setComment(String comment) {
    setComment(comment, false);
  }

  public void setComment(String comment, boolean encrypt) {
    if (comment_byte != null && comment.equals(this.comment) && !encrypt)
      return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
    this.comment_byte = enc.encryption(comment);
    this.comment = comment;
  }

  public boolean isNotify() {
    return notify;
  }

  public void setNotify(boolean notify) {
    this.notify = notify;
  }

  public NewExpenseSheet getExpenseSheet() {
    return expenseSheet;
  }

  public void setExpenseSheet(NewExpenseSheet expenseSheet) {
    this.expenseSheet = expenseSheet;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"[" + getDate() + ";" + getCategory() + ";" + getValue() + "]";
  }

}
