package pl.kostro.expensesystem.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.utils.Encryption;

@Entity
@Table(name = "categories")
public class Category extends AbstractEntity {

  private static final long serialVersionUID = 4537772469034122927L;

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "c_id")
  private int id;
  @Column(name = "c_name")
  private String name;
  @Column(name = "c_name_byte")
  private byte[] name_byte;
  @Column(name = "c_multiplier")
  private BigDecimal multiplier;
  @Column(name = "c_order")
  private int order;

  public Category() {
    super();
  }

  public Category(String name, int order) {
    super();
    setName(name);
    this.multiplier = new BigDecimal(1);
    this.order = order;
  }
  
  public Category(String name, BigDecimal multiplier, int order) {
    super();
    setName(name);
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
    if ((name == null || name.isEmpty()) && name_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      name = enc.decryption(name_byte);
    }
    if (name != null && name_byte == null)
      setName(name);
    return name;
  }

  public void setName(String name) {
    if (name_byte != null && name.equals(this.name)) return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.name_byte = enc.encryption(name);
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
