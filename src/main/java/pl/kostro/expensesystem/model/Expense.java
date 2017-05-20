package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.utils.Calculator;
import pl.kostro.expensesystem.utils.Encryption;

@SuppressWarnings("serial")
@Entity
@Table(name = "expenses")
public class Expense extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "e_id")
  private Long id;
  @Column(name = "e_date")
  private Date date;
  @Transient
  private String formula;
  @Column(name = "e_formula_byte")
  private byte[] formula_byte;
  @Transient
  private BigDecimal value;
  @OneToOne
  @JoinColumn(name = "e_c_id")
  private Category category;
  @OneToOne
  @JoinColumn(name = "e_u_id")
  private User user;
  @Transient
  private String comment;
  @Column(name = "e_comment_byte")
  private byte[] comment_byte;
  @Column(name = "e_notify")
  private boolean notify;
  @ManyToOne
  @JoinColumn(name = "e_es_id")
  private ExpenseSheet expenseSheet;

  public Expense() {
    super();
  }

  public Expense(Date date, String formula, Category category, User user, String comment, boolean notify,
      ExpenseSheet expenseSheet) {
    this.date = date;
    setFormula(formula);
    this.category = category;
    this.user = user;
    setComment(comment);
    this.notify = notify;
    this.expenseSheet = expenseSheet;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getFormula() {
    return getFormula(false);
  }

  public String getFormula(boolean encrypt) {
    if ((formula == null || formula.isEmpty()) && formula_byte != null && !encrypt) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
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
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.formula_byte = enc.encryption(formula);
    this.formula = formula;
  }

  public BigDecimal getValue() {
    try {
    if (getFormula() != null && !formula.isEmpty())
      value = Calculator.getOperationResult(formula);
    return value;
    } catch (NumberFormatException e) {
      return new BigDecimal(-1);
    }
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getComment() {
    return getComment(false);
  }

  public String getComment(boolean encrypt) {
    if ((comment == null || comment.isEmpty()) && comment_byte != null && !encrypt) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
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
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.comment_byte = enc.encryption(comment);
    this.comment = comment;
  }

  public boolean isNotify() {
    return notify;
  }

  public void setNotify(boolean notify) {
    this.notify = notify;
  }

  public ExpenseSheet getExpenseSheet() {
    return expenseSheet;
  }

  public void setExpenseSheet(ExpenseSheet expenseSheet) {
    this.expenseSheet = expenseSheet;
  }

  @Override
  public String toString() {
    return "Expense: " + date + ";" + category + ";" + value;
  }

}
