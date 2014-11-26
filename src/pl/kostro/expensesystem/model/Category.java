package pl.kostro.expensesystem.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Category {
  
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String name;
	private int orderId;
	
	public Category() {
	  super();
	}
	
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
