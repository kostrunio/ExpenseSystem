package pl.kostro.expensesystem.utils;

import java.math.BigDecimal;

import pl.kostro.expensesystem.model.Category;

public class CategorySum {
  private Category category;
  private BigDecimal sum;
  
  public CategorySum(Category category, BigDecimal sum) {
    super();
    this.category = category;
    this.sum = sum;
  }

  public Category getCategory() {
    return category;
  }

  public BigDecimal getSum() {
    return sum;
  }
}
