package pl.kostro.expensesystem.dao.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "categories")
public class CategoryEntity extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "c_id")
  private Long id;
  @Column(name = "c_name_byte")
  private byte[] name_byte;
  @Column(name = "c_multiplier")
  private BigDecimal multiplier;
  @Column(name = "c_order")
  private int order;

  public CategoryEntity() {
    super();
  }

  public CategoryEntity(byte[] name_byte, int order) {
    super();
    this.name_byte = name_byte;
    this.multiplier = new BigDecimal(1);
    this.order = order;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public byte[] getNameByte() {
    return name_byte;
  }

  public void setNameByte(byte[] name_byte) {
    this.name_byte = name_byte;
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
    return getClass().getSimpleName()+"["+getNameByte()+"]";
  }

}
