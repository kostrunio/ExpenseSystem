package pl.kostro.expensesystem.utils.expense;

import java.math.BigDecimal;

import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class UserLimitSumLeft {
  private UserLimitEntity userLimit;
  private BigDecimal sum;
  private BigDecimal left;
  
  public UserLimitSumLeft(UserLimitEntity userLimit, BigDecimal sum, BigDecimal left) {
    super();
    this.userLimit = userLimit;
    this.sum = sum;
    this.left = left;
  }

  public UserLimitEntity getUserLimit() {
    return userLimit;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public BigDecimal getLeft() {
    return left;
  }
}
