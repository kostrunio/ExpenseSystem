package pl.kostro.expensesystem.utils.expense;

import java.math.BigDecimal;

import pl.kostro.expensesystem.model.entity.CategoryEntity;

public class CategorySum {
  private CategoryEntity category;
  private BigDecimal sum;
  
  public CategorySum(CategoryEntity category, BigDecimal sum) {
    super();
    this.category = category;
    this.sum = sum;
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public BigDecimal getSum() {
    return sum;
  }
}
