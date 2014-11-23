package pl.kostro.expensesystem.model;

import java.util.Date;

public class Expense {
	private int id;
	private Date date;
	private String formula;
	private Category category;
	private User user;
	
	public Expense(Date date, String formula, Category category, User user) {
		super();
		this.date = date;
		this.formula = formula;
		this.category = category;
		this.user = user;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public static void createExpense(ExpenseSheet expenseSheet) {
		expenseSheet.getExpenseList().add(
				new Expense(
						new Date(),
						"=1+2",
						expenseSheet.getCategoryList().get(0),
						expenseSheet.getOwner()));
	}
}