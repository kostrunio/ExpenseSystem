package pl.kostro.expensesystem.utils.expense;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;

public class DateExpense {
  
  private ExpenseSheetService eshs;
  
	private LocalDate date;
	private Map<Category, CategoryExpense> categoryExpenseMap;
	private Map<UserLimit, UserLimitExpense> userLimitExpenseMap;
	private BigDecimal sum = new BigDecimal(0);
	
	public DateExpense(LocalDate date) {
    eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
		this.date = date;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
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
	
	public Map<UserLimit, UserLimitExpense> getUserLimitExpenseMap() {
		if (userLimitExpenseMap == null)
			userLimitExpenseMap = new HashMap<UserLimit, UserLimitExpense>();
		return userLimitExpenseMap;
	}
	public void setUserLimitExpenseMap(Map<UserLimit, UserLimitExpense> userLimitExpenseMap) {
		this.userLimitExpenseMap = userLimitExpenseMap;
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
	
	public void addExpense(ExpenseSheet expenseSheet, Expense expense) {
	  if (expense.getValue() == null) {
	    return;
	  }
		setSum(getSum().add(expense.getValue().multiply(expense.getCategory().getMultiplier()).setScale(2, RoundingMode.HALF_UP)));
		CategoryExpense categoryExpense = getCategoryExpenseMap().get(expense.getCategory());
		if (categoryExpense == null) {
			categoryExpense = new CategoryExpense(expense.getCategory());
			getCategoryExpenseMap().put(expense.getCategory(), categoryExpense);
		}
		categoryExpense.addExpense(expense);
		UserLimit userLimit = eshs.getUserLimitForUser(expenseSheet, expense.getUser());
		UserLimitExpense userLimitExpense = getUserLimitExpenseMap().get(userLimit);
		if (userLimitExpense == null) {
			userLimitExpense = new UserLimitExpense(userLimit);
			getUserLimitExpenseMap().put(userLimitExpense.getUserLimit(), userLimitExpense);
		}
		userLimitExpense.addExpense(expense);
	}
	public void removeExpense(Expense expense) {
		setSum(getSum().subtract(expense.getValue()));
		CategoryExpense categoryExpense = getCategoryExpenseMap().get(expense.getCategory());
		categoryExpense.removeExpense(expense);
	}
}
