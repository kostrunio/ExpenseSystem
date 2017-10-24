package pl.kostro.expensesystem.model.mongodb;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class NewAbstractEntity implements Serializable, Cloneable {

  public abstract String getId();

  public abstract void setId(String id);

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (this.getId() == null) {
      return false;
    }

    if (obj instanceof NewAbstractEntity && obj.getClass().equals(getClass())) {
      return this.getId().equals(((NewAbstractEntity) obj).getId());
    }

    return false;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 43 * hash + Objects.hashCode(this.getId());
    return hash;
  }
}
