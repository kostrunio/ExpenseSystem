package pl.kostro.expensesystem.model.entity;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

  public abstract Long getId();

  public abstract void setId(Long id);

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (this.getId() == null) {
      return false;
    }

    if (obj instanceof AbstractEntity && obj.getClass().equals(getClass())) {
      return this.getId().equals(((AbstractEntity) obj).getId());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
