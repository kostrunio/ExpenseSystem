package pl.kostro.expensesystem.utils.transform.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;

public class CategoryExpense implements Serializable {
  private CategoryEntity category;
  private List<ExpenseEntity> expenseList;
  private BigDecimal sum = new BigDecimal(0);

  public CategoryExpense(CategoryEntity category) {
    this.category = category;
    expenseList = new ArrayList<>();
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public List<ExpenseEntity> getExpenseList() {
    return expenseList;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public String getSumString() {
    return sum.toString();
  }

  public void addExpense(ExpenseEntity expense) {
    sum = sum.add(expense.getValue().multiply(expense.getCategory().getMultiplier()).setScale(2, RoundingMode.HALF_UP));
    getExpenseList().add(expense);
  }

  public void removeExpense(ExpenseEntity expense) {
    sum = sum.subtract(expense.getValue());
    getExpenseList().remove(expense);
  }

  public void setSum(BigDecimal sum) {
    this.sum = sum;
  }
}
