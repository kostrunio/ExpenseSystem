package pl.kostro.expensesystem.utils.transform.model;

import java.math.BigDecimal;

public class UserSumChange {
    private String userName;
    private BigDecimal sum;
    private BigDecimal prevSum;

    public UserSumChange(String userLimit, BigDecimal sum, BigDecimal prevSum) {
        this.userName = userLimit;
        this.sum = sum;
        this.prevSum = prevSum;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getPrevSum() {
        return prevSum;
    }
}
