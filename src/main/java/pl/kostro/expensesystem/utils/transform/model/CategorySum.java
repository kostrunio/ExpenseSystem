package pl.kostro.expensesystem.utils.transform.model;

import lombok.Getter;
import pl.kostro.expensesystem.model.entity.CategoryEntity;

import java.math.BigDecimal;

public class CategorySum {
  @Getter
  private final CategoryEntity category;
  private final BigDecimal sum;
  
  public CategorySum(CategoryEntity category, BigDecimal sum) {
    super();
    this.category = category;
    this.sum = sum;
  }

  public String getSumString() {
    return sum.toString().replace('.',',');
  }
}
