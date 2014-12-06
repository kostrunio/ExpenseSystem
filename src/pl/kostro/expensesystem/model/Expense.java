package pl.kostro.expensesystem.model;

import java.util.Date;

import javax.persistence.Column;
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
  @Column(name="ex_date")
  private Date date;
  private String formula;
  private double value;
  @OneToOne
  private Category category;
  @OneToOne
  private User user;
  @Column(name="ex_comment")
  private String comment;

  public Expense() {
    super();
  }

  public Expense(Date date, String formula, Category category, User user, String comment) {
    this.date = date;
    this.formula = formula;
    this.value = Calculator.getResult(formula);
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

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
  
  @Override
  public String toString() {
    return "Expense: " + date + ";" + category + ";" + value;
  }
}
