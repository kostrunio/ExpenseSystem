package pl.kostro.expensesystem.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractEntity implements Serializable {
  public abstract int getId();
  public abstract void setId(int id);
  
}
