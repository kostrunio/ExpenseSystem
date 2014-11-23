package pl.kostro.expensesystem.model;

import java.util.List;

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
	
	public String toString() {
		return getName();
	}

	public static void createCategory(List<Category> categoryList) {
		categoryList.add(new Category("jedzenie", 0));
		categoryList.add(new Category("s³odycze",1));
		categoryList.add(new Category("kosmetyki", 2));
	}
	
}
