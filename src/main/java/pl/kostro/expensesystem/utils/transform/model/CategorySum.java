package pl.kostro.expensesystem.utils.transform.model;

import pl.kostro.expensesystem.model.entity.CategoryEntity;

import java.math.BigDecimal;

public class CategorySum {
  private final CategoryEntity category;
  private final BigDecimal sum;
  
  public CategorySum(CategoryEntity category, BigDecimal sum) {
    super();
    this.category = category;
    this.sum = sum;
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public String getSumString() {
    return sum.toString().replace('.',',');
  }
}
