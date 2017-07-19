package pl.kostro.expensesystem.utils;

import java.math.BigDecimal;

import pl.kostro.expensesystem.model.UserLimit;

public class UserLimitSumLeft {
  private UserLimit userLimit;
  private BigDecimal sum;
  private BigDecimal left;
  
  public UserLimitSumLeft(UserLimit userLimit, BigDecimal sum, BigDecimal left) {
    super();
    this.userLimit = userLimit;
    this.sum = sum;
    this.left = left;
  }

  public UserLimit getUserLimit() {
    return userLimit;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public BigDecimal getLeft() {
    return left;
  }
}
