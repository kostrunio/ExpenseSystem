package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.Calculator;

@Entity
@NamedQueries({
  @NamedQuery(
      name = "findAllExpense",
      query = "select e from ExpenseSheet es join es.expenseList e where es = :expenseSheet"
      ),
  @NamedQuery(
      name = "findFirstExpense",
      query = "select e from ExpenseSheet es join es.expenseList e where es = :expenseSheet and e.date = (select min(e1.date) from Expense e1 where e1.expenseSheet = e.expenseSheet) and rownum = 1"
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
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  @Column(name="ex_date")
  private Date date;
  private String formula;
  private BigDecimal value;
  @OneToOne
  private Category category;
  @OneToOne
  private User user;
  @Column(name="ex_comment")
  private String comment;
  @ManyToOne
  private ExpenseSheet expenseSheet;

  public Expense() {
    super();
  }

  public Expense(Date date, String formula, Category category, User user, String comment) {
    this.date = date;
    setFormula(formula);
    this.category = category;
    this.user = user;
    this.comment = comment;
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
    return formula;
  }

  public void setFormula(String formula) {
    this.formula = formula;
    this.value = Calculator.getResult(formula);
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
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
    return comment;
  }

  public void setComment(String comment) {
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
}
