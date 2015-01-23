package pl.kostro.expensesystem.utils;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.User;

public class Filter {
	private Category category;
	private User user;
	private String formula;
	private String comment;
	
	public Filter(Category category, User user, String formula, String comment) {
		super();
		this.category = category;
		this.user = user;
		this.formula = formula;
		this.comment = comment;
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

	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
