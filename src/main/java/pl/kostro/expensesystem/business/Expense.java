package pl.kostro.expensesystem.business;

import pl.kostro.expensesystem.utils.Calculator;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
  private Long id;
  private LocalDate date;
  private String formula;
  private BigDecimal value;
  private Category category;
  private User user;
  private String comment;
  private boolean notify;
  private ExpenseSheet expenseSheet;

  public Expense() {
  }

  public Expense(LocalDate date, String formula, Category category, User user, String comment, boolean notify,
                 ExpenseSheet expenseSheet) {
    this.date = date;
    this.formula = formula;
    this.category = category;
    this.user = user;
    this.comment = comment;
    this.notify = notify;
    this.expenseSheet = expenseSheet;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getFormula() {
    return formula;
  }

  public void setFormula(String formula) {
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
    return comment;
  }

  public void setComment(String comment) {
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
    return getClass().getSimpleName()+"[" + getDate() + ";" + getCategory() + ";" + getValue() + "]";
  }

}
