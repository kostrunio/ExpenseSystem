package pl.kostro.expensesystem.utils.transform.model;

import pl.kostro.expensesystem.model.entity.UserLimitEntity;

import java.math.BigDecimal;

public class UserLimitSumLeft {
  private final UserLimitEntity userLimit;
  private final BigDecimal sum;
  private final BigDecimal left;
  
  public UserLimitSumLeft(UserLimitEntity userLimit, BigDecimal sum, BigDecimal left) {
    this.userLimit = userLimit;
    this.sum = sum;
    this.left = left;
  }

  public UserLimitEntity getUserLimit() {
    return userLimit;
  }

  public String getSumString() {
    return sum.toString().replace('.',',');
  }

  public String getLeftString() {
    return left.toString().replace('.',',');
  }
}
