package pl.kostro.expensesystem.model;

import java.util.ArrayList;
import java.util.List;

public class RealUser extends User {
	private String password;
	private String email;
	private List<ExpenseSheet> expenseSheetList;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public List<ExpenseSheet> getExpenseSheetList() {
		if (expenseSheetList == null)
			expenseSheetList = new ArrayList<ExpenseSheet>();
		return expenseSheetList;
	}
	public void setExpenseSheets(List<ExpenseSheet> expenseSheetList) {
		this.expenseSheetList = expenseSheetList;
	}
	
	public void removeData() {
		setId(0);
		setName(null);
		setEmail(null);
		setExpenseSheets(null);
	}
	
	public static void createUser(RealUser loggedUser) {
		loggedUser.setId(1);
		ExpenseSheet.createExpenseSheet(loggedUser);
	}
}
