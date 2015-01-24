package pl.kostro.expensesystem.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;

public class DateExpense {
	private Date date;
	private Map<Category, CategoryExpense> categoryExpenseMap;
	private BigDecimal sum = new BigDecimal(0);
	
	public DateExpense(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Map<Category, CategoryExpense> getCategoryExpenseMap() {
		if (categoryExpenseMap == null)
			categoryExpenseMap = new HashMap<Category, CategoryExpense>();
		return categoryExpenseMap;
	}
	public void setCategoryExpenseMap(Map<Category, CategoryExpense> categoryExpenseMap) {
		this.categoryExpenseMap = categoryExpenseMap;
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
	
	public String toString() {
		return "DateExpense: " + date + ";" + sum;
	}
	
	public void addExpense(Expense expense) {
		setSum(getSum().add(expense.getValue()));
		CategoryExpense categoryExpense = getCategoryExpenseMap().get(expense.getCategory());
		if (categoryExpense == null) {
			categoryExpense = new CategoryExpense(expense.getCategory());
			getCategoryExpenseMap().put(expense.getCategory(), categoryExpense);
		}
		categoryExpense.addExpense(expense);
	}
	public void removeExpense(Expense expense) {
		setSum(getSum().subtract(expense.getValue()));
		CategoryExpense categoryExpense = getCategoryExpenseMap().get(expense.getCategory());
		categoryExpense.removeExpense(expense);
	}
}
