package pl.kostro.expensesystem.utils.transform.model;

import lombok.Getter;
import lombok.Setter;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DateExpense {
  
	@Setter
	private LocalDate date;
	private final Map<CategoryEntity, CategoryExpense> categoryExpenseMap;
	private final Map<UserLimitEntity, UserLimitExpense> userLimitExpenseMap;
	private BigDecimal sum = new BigDecimal(0);
	
	public DateExpense(LocalDate date) {
		this.date = date;
		categoryExpenseMap = new HashMap<>();
		userLimitExpenseMap = new HashMap<>();
	}

	public String toString() {
		return "DateExpense: " + date + ";" + sum;
	}
	
	public void addExpense(ExpenseEntity expense, UserLimitEntity userLimit) {
		sum = sum.add(expense.getValue().multiply(expense.getCategory().getMultiplier()).setScale(2, RoundingMode.HALF_UP));
		CategoryExpense categoryExpense = getCategoryExpenseMap().get(expense.getCategory());
		if (categoryExpense == null) {
			categoryExpense = new CategoryExpense(expense.getCategory());
			getCategoryExpenseMap().put(expense.getCategory(), categoryExpense);
		}
		categoryExpense.addExpense(expense);
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
