package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.utils.Calculator;
import pl.kostro.expensesystem.utils.Encryption;

@Entity
@Table(name="expenses")
@NamedQueries({
  @NamedQuery(
      name = "findAllExpense",
      query = "select e from ExpenseSheet es join es.expenseList e where es = :expenseSheet order by e.date desc"
      ),
  @NamedQuery(
      name = "findFirstExpense",
      query = "select e from ExpenseSheet es join es.expenseList e where es = :expenseSheet and e.date = (select min(e1.date) from Expense e1 where e1.expenseSheet = e.expenseSheet)"
      ),
  @NamedQuery(
      name = "findExpenseByDates",
      query = "select e from ExpenseSheet es join es.expenseList e where es.id = :expenseSheet and e.date between :startDate and :endDate"
      ),
  @NamedQuery(
      name = "findExpenseByCategory",
      query = "select e from ExpenseSheet es join es.expenseList e where es.id = :expenseSheet and e.category = :category"
      )
})
public class Expense extends AbstractEntity {

  private static final long serialVersionUID = 3149559648002478493L;

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name="e_id")
  private int id;
  @Column(name="e_date")
  private Date date;
  @Column(name="e_formula")
  private String formula;
  @Column(name="e_formula_byte")
  private byte[] formula_byte;
  @Column(name="e_value")
  private BigDecimal value;
  @Column(name="e_value_byte")
  private byte[] value_byte;
  @OneToOne
  @JoinColumn(name="e_c_id")
  private Category category;
  @OneToOne
  @JoinColumn(name="e_u_id")
  private User user;
  @Column(name="e_comment")
  private String comment;
  @Column(name="e_comment_byte")
  private byte[] comment_byte;
  @ManyToOne
  @JoinColumn(name="e_es_id")
  private ExpenseSheet expenseSheet;

  public Expense() {
    super();
  }

  public Expense(Date date, String formula, Category category, User user, String comment, ExpenseSheet expenseSheet) {
    this.date = date;
    setFormula(formula);
    this.category = category;
    this.user = user;
    setComment(comment);
    this.expenseSheet = expenseSheet;
  }
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getFormula() {
    if (formula_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      formula = enc.decryption(formula_byte);
    }
    return formula;
  }

  public void setFormula(String formula) {
    if (formula_byte != null && formula.equals(this.formula)) return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.formula_byte = enc.encryption(formula);
    this.formula = formula;
    setValue(Calculator.getResult(formula));
  }

  public BigDecimal getValue() {
    if (value_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      value = new BigDecimal(enc.decryption(value_byte));
    }
    return value;
  }

  public void setValue(BigDecimal value) {
    if (value_byte != null && value.equals(this.value)) return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.value_byte = enc.encryption(value.toString());
    this.value = value;
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
    if (comment_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      comment = enc.decryption(comment_byte);
    }
    return comment;
  }

  public void setComment(String comment) {
    if (comment_byte != null && comment.equals(this.comment)) return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.comment_byte = enc.encryption(comment);
    this.comment = comment;
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
  
  @Override
  public boolean equals(Object o) {
    if(o instanceof Expense)
      return getId() == ((Expense)o).getId();
    else return this == o;
  }
}
