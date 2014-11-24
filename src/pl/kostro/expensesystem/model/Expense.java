package pl.kostro.expensesystem.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.kostro.expensesystem.utils.Calculator;

public class Expense {
	private int id;
	private Date date;
	private String formula;
	private double value;
	private Category category;
	private User user;
	
	public Expense(Date date, String formula, Category category, User user) {
		super();
		this.date = date;
		this.formula = formula;
		this.category = category;
		this.user = user;
		this.value = Calculator.getResult(formula);
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
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
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
	
	public String getValueString() {
		return "" + value;
	}
	
	public String toString() {
		return "Expense: " + date + ";" + category + ";" + value;
	}

	public static void createExpense(ExpenseSheet expenseSheet) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {
			expenseSheet.getExpenseList().add(
					new Expense(
							df.parse(df.format(new Date())),
							"1+2",
							expenseSheet.getCategoryList().get(0),
							expenseSheet.getOwner()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
