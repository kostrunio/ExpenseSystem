package pl.kostro.expensesystem.model;

public class Category {
	private int id;
	private String name;
	private int orderId;
	
	public Category(String name, int orderId) {
		this.name = name;
		this.orderId = orderId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
}
