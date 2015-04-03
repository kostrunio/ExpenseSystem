package pl.kostro.expensesystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Category extends AbstractEntity {
  
  private static final long serialVersionUID = 4537772469034122927L;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  private String name;
	private int orderId;
	@ManyToOne
  private ExpenseSheet expenseSheet;
	
	public Category() {
	  super();
	}
	
	public Category (String name, int orderId, ExpenseSheet expenseSheet) {
	  super();
	  this.name = name;
	  this.orderId = orderId;
	  this.expenseSheet = expenseSheet;
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
	
  public ExpenseSheet getExpenseSheet() {
    return expenseSheet;
  }

  public void setExpenseSheet(ExpenseSheet expenseSheet) {
    this.expenseSheet = expenseSheet;
  }
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
	  return getId();
	}
	
	@Override
	public boolean equals(Object o) {
	  if(o instanceof Category)
	    return getId() == ((Category)o).getId();
	  else return this == o;
	}
	
}
