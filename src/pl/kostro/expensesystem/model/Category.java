package pl.kostro.expensesystem.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "categories")
public class Category extends AbstractEntity {

  private static final long serialVersionUID = 4537772469034122927L;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "c_id")
  private int id;
  @Column(name = "c_name")
  private String name;
  @Column(name="c_multiplier")
  private BigDecimal multiplier;
  @Column(name = "c_order")
  private int order;

  public Category() {
    super();
  }

  public Category(String name, int order) {
    super();
    this.name = name;
    this.multiplier = new BigDecimal(1);
    this.order = order;
  }
  
  public Category(String name, BigDecimal multiplier, int order) {
    super();
    this.name = name;
    this.multiplier = multiplier;
    this.order = order;
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
  
  public BigDecimal getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(BigDecimal multiplier) {
    this.multiplier = multiplier;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
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
    if (o instanceof Category)
      return getId() == ((Category) o).getId();
    else
      return this == o;
  }

}
