package pl.kostro.expensesystem.dto.model;

import java.math.BigDecimal;

public class Category {
  private Long id;
  private String name;
  private BigDecimal multiplier;
  private int order;

  public Category(String name, int order) {
    this.name = name;
    this.multiplier = new BigDecimal(1);
    this.order = order;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
    return getClass().getSimpleName()+"["+getName()+"]";
  }

}
