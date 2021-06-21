package pl.kostro.expensesystem.utils.transform.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public class UserLimitExpense {
	private UserLimitEntity userLimit;
	private List<ExpenseEntity> expenseList;
	private BigDecimal sum = new BigDecimal(0);
	
	public UserLimitExpense(UserLimitEntity userLimit) {
		this.userLimit = userLimit;
		expenseList = new ArrayList<>();
	}
	public UserLimitEntity getUserLimit() {
		return userLimit;
	}
	public void setUserLimit(UserLimitEntity userLimit) {
		this.userLimit = userLimit;
	}
	
	public List<ExpenseEntity> getExpenseList() {
		return expenseList;
	}
	public BigDecimal getSum() {
		return sum;
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
