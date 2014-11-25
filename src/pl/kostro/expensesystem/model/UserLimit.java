package pl.kostro.expensesystem.model;

public class UserLimit {
	private int id;
	private User user;
	private int limit;
	
	public UserLimit(User user, int limit) {
		this.user = user;
		this.limit = limit;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public String toString() {
		return user.getName();
	}
	
	public static void createUserLimit(ExpenseSheet expenseSheet) {
		expenseSheet.getUserLimitList().add(new UserLimit(expenseSheet.getOwner(), 0));
		expenseSheet.getUserLimitList().add(new UserLimit(new User("wspólne"), 0));
	}
	
}
