package pl.kostro.expensesystem.utils.transform.model;

import lombok.Getter;
import lombok.Setter;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CategoryExpense implements Serializable {
  private final CategoryEntity category;
  private final List<ExpenseEntity> expenseList;
  @Setter
  private BigDecimal sum = new BigDecimal(0);

  public CategoryExpense(CategoryEntity category) {
    this.category = category;
    expenseList = new ArrayList<>();
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

}
