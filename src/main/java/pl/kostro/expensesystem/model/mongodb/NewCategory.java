package pl.kostro.expensesystem.model.mongodb;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.utils.Encryption;

@SuppressWarnings("serial")
public class NewCategory extends NewAbstractEntity {
  @Id
  private String id;
  @Transient
  private String name;

  private byte[] name_byte;

  private BigDecimal multiplier;

  private int order;

  public NewCategory() {
    super();
  }

  public NewCategory(String name, int order) {
    super();
    setName(name);
    this.multiplier = new BigDecimal(1);
    this.order = order;
  }

  public NewCategory(Category category) {
    this.name_byte = category.getNameByte();
    this.multiplier = category.getMultiplier();
    this.order = category.getOrder();
  }
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return getName(false);
  }

  public String getName(boolean encrypt) {
    if ((name == null || name.isEmpty()) && name_byte != null && !encrypt) {
      try {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
      name = enc.decryption(name_byte);
      } catch (NullPointerException e) {
        return name_byte.toString();
      }
    }
    return name;
  }
  
  public void setName(String name) {
    setName(name, false);
  }

  public void setName(String name, boolean encrypt) {
    if (name_byte != null && name.equals(this.name) && !encrypt) return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
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
    return getClass().getSimpleName()+"["+getName()+"]";
  }

}
