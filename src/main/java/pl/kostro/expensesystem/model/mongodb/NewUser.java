package pl.kostro.expensesystem.model.mongodb;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import pl.kostro.expensesystem.model.User;

@SuppressWarnings("serial")
public class NewUser extends NewAbstractEntity {
  @Id
  private String id;

  private String name;

  private LocalDateTime creationDate = LocalDateTime.now();

  public NewUser() {
    super();
  }

  public NewUser(String name) {
    setName(name);
  }

  public NewUser(User user) {
    this.name = user.getName();
    this.creationDate = user.getCreationDate();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getName()+"]";
  }

}
