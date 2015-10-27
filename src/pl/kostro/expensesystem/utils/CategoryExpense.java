package pl.kostro.expensesystem.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;

public class CategoryExpense {
	private Category category;
	private List<Expense> expenseList;
	private BigDecimal sum = new BigDecimal(0);
	
	public CategoryExpense(Category category) {
		this.category = category;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public List<Expense> getExpenseList() {
		if (expenseList == null)
			expenseList = new ArrayList<Expense>();
		return expenseList;
	}
	public void setExpenseList(List<Expense> expenseList) {
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
	
	public void addExpense(Expense expense) {
		setSum(getSum().add(expense.getValue().multiply(expense.getCategory().getMultiplier()).setScale(2)));
		getExpenseList().add(expense);
	}
	public void removeExpense(Expense expense) {
		setSum(getSum().subtract(expense.getValue()));
		getExpenseList().remove(expense);
	}
}
