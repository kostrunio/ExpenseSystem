package pl.kostro.expensesystem.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import pl.kostro.expensesystem.utils.encryption.Encryption;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;

@Entity
@Table(name = "categories")
public class CategoryEntity extends AbstractEntity {
  @Setter
  @Getter
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "c_id")
  private Long id;
  @Transient
  private String name;
  @Column(name = "c_name_byte")
  private byte[] name_byte;
  @Setter
  @Getter
  @Column(name = "c_multiplier")
  private BigDecimal multiplier;
  @Setter
  @Getter
  @Column(name = "c_order")
  private int order;

  public CategoryEntity() {
    super();
  }

  public CategoryEntity(String name, int order) {
    super();
    setName(name);
    this.multiplier = new BigDecimal(1);
    this.order = order;
  }

  public String getName() {
    return getName(false);
  }

  public String getName(boolean encrypt) {
    if ((name == null || name.isEmpty()) && name_byte != null && !encrypt) {
      name = Encryption.decryption(name_byte);
    }
    return name;
  }

  public void setName(String name) {
    setName(name, false);
  }

  public void setName(String name, boolean encrypt) {
    if (name_byte != null && name.equals(this.name) && !encrypt) return;
    this.name_byte = Encryption.encryption(name);
    this.name = name;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getName()+"]";
  }

}
