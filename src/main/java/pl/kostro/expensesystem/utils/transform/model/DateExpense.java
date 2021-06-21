package pl.kostro.expensesystem.utils.transform.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import pl.kostro.expensesystem.AppCtxProvider;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

public class DateExpense {
  
  private ExpenseSheetService eshs;
  private ExpenseSheetTransformService eshts;
  
	private LocalDate date;
	private Map<CategoryEntity, CategoryExpense> categoryExpenseMap;
	private Map<UserLimitEntity, UserLimitExpense> userLimitExpenseMap;
	private BigDecimal sum = new BigDecimal(0);
	
	public DateExpense(LocalDate date) {
		eshs = AppCtxProvider.getBean(ExpenseSheetService.class);
		eshts = AppCtxProvider.getBean(ExpenseSheetTransformService.class);
		this.date = date;
		categoryExpenseMap = new HashMap<>();
		userLimitExpenseMap = new HashMap<>();
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public Map<CategoryEntity, CategoryExpense> getCategoryExpenseMap() {
		return categoryExpenseMap;
	}
	public Map<UserLimitEntity, UserLimitExpense> getUserLimitExpenseMap() {
		return userLimitExpenseMap;
	}
	public BigDecimal getSum() {
		return sum;
	}

	public String toString() {
		return "DateExpense: " + date + ";" + sum;
	}
	
	public void addExpense(ExpenseSheetEntity expenseSheet, ExpenseEntity expense) {
		sum = sum.add(expense.getValue().multiply(expense.getCategory().getMultiplier()).setScale(2, RoundingMode.HALF_UP));
		CategoryExpense categoryExpense = getCategoryExpenseMap().get(expense.getCategory());
		if (categoryExpense == null) {
			categoryExpense = new CategoryExpense(expense.getCategory());
			getCategoryExpenseMap().put(expense.getCategory(), categoryExpense);
		}
		categoryExpense.addExpense(expense);
		UserLimitEntity userLimit = eshts.getUserLimitForUser(expenseSheet, expense.getUser());
		UserLimitExpense userLimitExpense = getUserLimitExpenseMap().get(userLimit);
		if (userLimitExpense == null) {
			userLimitExpense = new UserLimitExpense(userLimit);
			getUserLimitExpenseMap().put(userLimitExpense.getUserLimit(), userLimitExpense);
		}
		userLimitExpense.addExpense(expense);
	}
	public void removeExpense(ExpenseEntity expense) {
		sum = sum.subtract(expense.getValue());
		CategoryExpense categoryExpense = getCategoryExpenseMap().get(expense.getCategory());
		categoryExpense.removeExpense(expense);
	}
}
