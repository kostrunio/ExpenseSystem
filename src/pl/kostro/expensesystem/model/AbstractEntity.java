package pl.kostro.expensesystem.model;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {

  private static final long serialVersionUID = -1283402772887956244L;

  public abstract int getId();
  public abstract void setId(int id);
  
}
