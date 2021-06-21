package pl.kostro.expensesystem.utils.expense;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import pl.kostro.expensesystem.model.ExpenseEntity;
import pl.kostro.expensesystem.model.UserLimit;

public class UserLimitExpense {
	private UserLimit userLimit;
	private List<ExpenseEntity> expenseList;
	private BigDecimal sum = new BigDecimal(0);
	
	public UserLimitExpense(UserLimit userLimit) {
		this.userLimit = userLimit;
	}
	public UserLimit getUserLimit() {
		return userLimit;
	}
	public void setUserLimit(UserLimit userLimit) {
		this.userLimit = userLimit;
	}
	
	public List<ExpenseEntity> getExpenseList() {
		if (expenseList == null)
			expenseList = new ArrayList<ExpenseEntity>();
		return expenseList;
	}
	public void setExpenseList(List<ExpenseEntity> expenseList) {
		this.expenseList = expenseList;
	}
	
	public BigDecimal getSum() {
		return sum;
	}
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	
	public String getSumString() {
		return sum.toString();
	}
	
	public void addExpense(ExpenseEntity expense) {
		setSum(getSum().add(expense.getValue().multiply(expense.getCategory().getMultiplier()).setScale(2, RoundingMode.HALF_UP)));
		getExpenseList().add(expense);
	}
	public void removeExpense(ExpenseEntity expense) {
		setSum(getSum().subtract(expense.getValue()));
		getExpenseList().remove(expense);
	}
}
