package pl.kostro.expensesystem.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.Calculator;

@Entity
public class Expense {

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  private Date date;
  private String formula;
  private double value;
  @OneToOne
  private Category category;
  @OneToOne
  private User user;
  private String comment;

  public Expense() {
    super();
  }

  public Expense(Date date, String formula, Category category, User user, String comment) {
    super();
    this.date = date;
    this.formula = formula;
    this.category = category;
    this.user = user;
    this.value = Calculator.getResult(formula);
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

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
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

  public String getValueString() {
    return new String() + value;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String toString() {
    return "Expense: " + date + ";" + category + ";" + value;
  }

  public static void createExpense(ExpenseSheet expenseSheet) {
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    try {
      expenseSheet.getExpenseList().add(new Expense(df.parse(df.format(new Date())), "1+2", expenseSheet.getCategoryList().get(0), expenseSheet.getOwner(), new String()));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
