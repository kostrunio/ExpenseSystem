package pl.kostro.expensesystem.utils.transform.model;

import pl.kostro.expensesystem.model.entity.UserLimitEntity;

import java.math.BigDecimal;

public class UserLimitSumLeft {
  private UserLimitEntity userLimit;
  private BigDecimal sum;
  private BigDecimal left;
  
  public UserLimitSumLeft(UserLimitEntity userLimit, BigDecimal sum, BigDecimal left) {
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
