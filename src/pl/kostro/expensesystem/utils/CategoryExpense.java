package pl.kostro.expensesystem.utils;

import java.util.ArrayList;
import java.util.List;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;

public class CategoryExpense {
	private Category category;
	private List<Expense> expenseList;
	private double sum;
	
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
	
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	
	public String getSumString() {
		return Double.toString(sum);
	}
	
	public void addExpense(Expense expense) {
		setSum(getSum() + expense.getValue());
		getExpenseList().add(expense);
	}
	public void removeExpense(Expense expense) {
		setSum(getSum() - expense.getValue());
		getExpenseList().remove(expense);
	}
}
